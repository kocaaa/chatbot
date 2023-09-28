package com.chatbot.chatbot.models;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class YearExams {
    String year;
    List<Month> months;
    List<Exam> exams;
}
