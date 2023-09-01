package com.chatbot.chatbot.models;

import com.chatbot.chatbot.enums.Questions;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONException;
import org.json.JSONObject;

@Data
@AllArgsConstructor
public class PyResponse {
    Questions question;
    Double probability;

    public PyResponse(JSONObject jsonObject) throws JSONException {
        this(Questions.parse(jsonObject.get("question").toString()), (Double) jsonObject.get("probability"));
    }
}
