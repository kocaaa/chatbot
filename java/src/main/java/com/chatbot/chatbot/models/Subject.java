package com.chatbot.chatbot.models;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    String name;
    String professor;
    List<String> assistants;
}
