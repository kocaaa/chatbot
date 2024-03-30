package com.chatbot.chatbot.models;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationPeriod {
    private String month;
    private String date;
}
