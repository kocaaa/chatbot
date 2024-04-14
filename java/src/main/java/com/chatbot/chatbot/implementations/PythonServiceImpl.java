package com.chatbot.chatbot.implementations;

import com.chatbot.chatbot.models.PyMessage;
import com.chatbot.chatbot.models.PyResponse;
import com.chatbot.chatbot.models.Subject;
import com.chatbot.chatbot.models.YearExams;
import com.chatbot.chatbot.services.PythonService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.chatbot.chatbot.constants.Constants.*;

@Slf4j
@Service
public class PythonServiceImpl implements PythonService {
    private final String fastApiEndpoint;

    @Autowired
    public PythonServiceImpl(@Value("${python.fastapi.chatbot.endpoint}") String fastApiEndpoint) {
        this.fastApiEndpoint = fastApiEndpoint;
    }

    @Override
    public PyResponse getChatBotResponse(PyMessage message) throws JSONException {
        PyResponse pyResponse = null;

        JSONObject json = new JSONObject();
        json.put("question", message.getQuestion());

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = postResponse(httpClient, json);
            JSONObject responseJSON = getResponseJSON(response.getEntity());
            pyResponse = new PyResponse(responseJSON);
        } catch (IOException e) {
            log.error("Error occurred while sending POST request to FastAPI.", e);
        }

        return pyResponse;
    }

    @Cacheable("subjects")
    @Override
    public List<Subject> getAllSubjects() throws JSONException {
        List<Subject> subjects = new ArrayList<>();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = getResponse(httpClient, SUBJECTS_ENDPOINT);
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            subjects = parseToSubjects(new JSONArray(responseString));
        } catch (IOException e) {
            log.error("Error occurred while sending POST request to FastAPI", e);
        }

        return subjects;
    }

    @Cacheable("schedule")
    @Override
    public List<YearExams> getExamSchedule() throws JSONException {
        List<YearExams> examSchedule = new ArrayList<>();

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpResponse response = getResponse(httpClient, EXAM_SCHEDULE_ENDPOINT);
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            examSchedule = parseExamSchedule(new JSONArray(responseString));
        } catch (IOException e) {
            log.error("Error occurred while sending POST request to FastAPI", e);
        }

        return examSchedule;
    }

    private List<YearExams> parseExamSchedule(JSONArray jsonArray) throws JSONException {
        Gson gson = new Gson();
        List<YearExams> examSchedule = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            examSchedule.add(gson.fromJson(jsonObject.toString(), YearExams.class));
        }

        return examSchedule;
    }

    private HttpResponse getResponse(CloseableHttpClient httpClient, String endpoint) throws IOException {
        HttpGet request = new HttpGet(fastApiEndpoint + endpoint);
        request.addHeader("content-type", "application/json");
        return httpClient.execute(request);
    }

    private HttpResponse postResponse(CloseableHttpClient httpClient, JSONObject requestBody) throws IOException {
        HttpPost request = new HttpPost(fastApiEndpoint + CHAT_BOT_ENDPOINT);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(requestBody.toString()));
        return httpClient.execute(request);
    }

    private JSONObject getResponseJSON(HttpEntity httpEntity) throws IOException, JSONException {
        String responseString = EntityUtils.toString(httpEntity, "UTF-8");
        return new JSONObject(responseString);
    }

    private List<Subject> parseToSubjects(JSONArray jsonArray) throws JSONException {
        List<Subject> subjects = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            List<String> assistants = new ArrayList<>();

            JSONArray assistantsJson = jsonObject.getJSONArray("assistants");

            for (int j = 0; j < assistantsJson.length(); j++) {
                assistants.add(assistantsJson.get(j).toString());
            }

            subjects.add(
                    Subject.builder()
                            .name(jsonObject.getString("name"))
                            .initials(jsonObject.getString("initials"))
                            .professor(jsonObject.getString("professor"))
                            .assistants(assistants)
                            .build()
            );
        }

        return subjects;
    }
}
