package com.final_10aeat.global.entity;

import com.final_10aeat.domain.member.entity.MemberRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class Loginable extends SoftDeletableBaseTimeEntity {
    @Column(unique = true)
    protected String email;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    protected Loginable(String email, String password, MemberRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    protected Loginable() {
    }
}
