package com.chatbot.chatbot.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Employee {
    private String name;
    private String title;
    private String cabinet;
    private String locale;
    private String email;
    private String consultation;
}
