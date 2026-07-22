package com.hireflow.entity;

import com.hireflow.entity.enums.InterviewStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interviews")
public class Interview extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(nullable = false)
    private LocalDate interviewDate;

    @Column(nullable = false)
    private LocalTime interviewTime;

    @Column(length = 255)
    private String meetingLink;

    @Column(length = 150)
    private String interviewer;

    @Column(length = 150)
    private String round;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private InterviewStatus status = InterviewStatus.SCHEDULED;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(length = 20)
    private String outcome;
}