package com.final_10aeat.domain.admin.entity;

import com.final_10aeat.domain.member.entity.MemberRole;
import com.final_10aeat.global.entity.Loginable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "admin")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends Loginable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @Column(name = "lunch_start")
    private LocalDateTime lunchBreakStart;

    @Column(name = "lunch_end")
    private LocalDateTime lunchBreakEnd;

    @Column(name = "admin_office")
    private String adminOffice;

    private String affiliation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_id")
    private Office office;

    @Builder
    private Admin(String email, String password, Long id, String name, String phoneNumber, LocalDateTime lunchBreakStart, LocalDateTime lunchBreakEnd, String adminOffice, String affiliation, Office office) {
        super(email, password, MemberRole.ADMIN);
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.lunchBreakStart = lunchBreakStart;
        this.lunchBreakEnd = lunchBreakEnd;
        this.adminOffice = adminOffice;
        this.affiliation = affiliation;
        this.office = office;
    }
}
