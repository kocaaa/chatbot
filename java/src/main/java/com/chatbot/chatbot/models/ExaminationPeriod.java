package com.chatbot.chatbot.models;

import lombok.*;

import javax.persistence.*;

@Table(name = "examination_periods")
@Entity
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String month;
    private String date;
}
