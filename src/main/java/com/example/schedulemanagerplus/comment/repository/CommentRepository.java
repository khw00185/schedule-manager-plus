package com.example.schedulemanagerplus.comment.repository;

import com.example.schedulemanagerplus.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByScheduleIdOrderByModifiedAtDesc(Long scheduleId, Pageable pageable);
    int countByScheduleId(Long scheduleId);
}
