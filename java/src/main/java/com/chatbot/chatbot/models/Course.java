package com.chatbot.chatbot.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Course {
    private String name;
    private String url;
    private String professor;
}
