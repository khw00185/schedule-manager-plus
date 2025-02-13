package com.example.schedulemanagerplus.schedule.entity;

import com.example.schedulemanagerplus.common.TimeStamp;
import com.example.schedulemanagerplus.member.entity.Member;
import com.example.schedulemanagerplus.schedule.dto.request.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "schedule")
public class Schedule extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "longtext")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    public Schedule() {}

    public Schedule(ScheduleRequestDto request, Member member) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.member = member;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
