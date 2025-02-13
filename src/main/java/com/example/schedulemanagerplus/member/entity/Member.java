package com.example.schedulemanagerplus.member.entity;

import com.example.schedulemanagerplus.common.TimeStamp;
import de.huxhorn.sulky.ulid.ULID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Member")
public class Member extends TimeStamp {
    @Id
    @Column(updatable = false, nullable = false, length = 26)
    private String id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;


    public Member(String email, String name, String password) {
        this.id = generateUlid();
        this.email = email;
        this.name = name;
        this.password = password;
    }

    private String generateUlid() {
        return new ULID().nextULID();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

}
