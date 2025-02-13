package com.example.schedulemanagerplus.jwt.repository;

import com.example.schedulemanagerplus.jwt.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByMemberId(String memberId);

}
