package com.chatbot.chatbot.models;

import lombok.*;

import javax.persistence.*;

@Table(name = "exam_registration")
@Entity
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExamRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String month;
    private String date;
}
