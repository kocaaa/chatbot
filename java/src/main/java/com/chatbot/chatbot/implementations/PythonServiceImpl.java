package com.chatbot.chatbot.implementations;

import com.chatbot.chatbot.models.PyMessage;
import com.chatbot.chatbot.models.PyResponse;
import com.chatbot.chatbot.services.PythonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class PythonServiceImpl implements PythonService {
    private final String fastApiEndpoint;
    private static final String CHAT_BOT_PY_ENDPOINT = "/question";

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
            HttpResponse response = getResponse(httpClient, json);
            JSONObject responseJSON = getResponseJSON(response.getEntity());
            pyResponse = new PyResponse(responseJSON);
        } catch (IOException e) {
            log.error("Error occurred while sending POST request to FastAPI");
            e.printStackTrace();
        }

        return pyResponse;
    }

    private HttpResponse getResponse(CloseableHttpClient httpClient, JSONObject requestBody) throws IOException {
        HttpPost request = new HttpPost(fastApiEndpoint + CHAT_BOT_PY_ENDPOINT);
        request.addHeader("content-type", "application/json");
        request.setEntity(new StringEntity(requestBody.toString()));
        return httpClient.execute(request);
    }

    private JSONObject getResponseJSON(HttpEntity httpEntity) throws IOException, JSONException {
        String responseString = EntityUtils.toString(httpEntity, "UTF-8");
        return new JSONObject(responseString);
    }
}
