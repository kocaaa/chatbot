package com.chatbot.chatbot.models;

import lombok.*;

import javax.persistence.*;

@Table(name = "courses")
@Entity
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private String professor;
}
