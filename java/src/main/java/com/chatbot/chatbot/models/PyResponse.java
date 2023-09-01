package com.chatbot.chatbot.models;

import com.chatbot.chatbot.enums.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONException;
import org.json.JSONObject;

@Data
@AllArgsConstructor
public class PyResponse {
    Question question;
    Double probability;

    public PyResponse(JSONObject jsonObject) throws JSONException {
        this(Question.parse(jsonObject.get("question").toString()), (Double) jsonObject.get("probability"));
    }
}
