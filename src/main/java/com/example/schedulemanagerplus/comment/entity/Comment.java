package com.example.schedulemanagerplus.comment.entity;

import com.example.schedulemanagerplus.common.TimeStamp;
import com.example.schedulemanagerplus.member.entity.Member;
import com.example.schedulemanagerplus.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "comment")
public class Comment extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String content;

    public Comment() {}
    public Comment(Schedule schedule, Member member, String content) {
        this.schedule = schedule;
        this.member = member;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }

}