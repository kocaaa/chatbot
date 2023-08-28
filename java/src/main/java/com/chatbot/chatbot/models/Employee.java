package com.chatbot.chatbot.models;

import lombok.*;

import javax.persistence.*;

@Table(name = "employees")
@Entity
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    private String cabinet;
    private String locale;
    private String email;
    private String consultation;
}
