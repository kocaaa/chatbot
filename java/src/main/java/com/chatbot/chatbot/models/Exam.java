package com.chatbot.chatbot.models;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    String name;
    List<String> dates;
    String initials;
}
