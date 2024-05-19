package com.final_10aeat.domain.member.entity;

import com.final_10aeat.domain.admin.entity.Office;
import com.final_10aeat.global.entity.Loginable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter
@Table(name = "member", indexes = @Index(
        name = "idx_email_deletedAt", columnList = "email, deletedAt", unique = true
))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Loginable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToMany
    @JoinTable(
            name = "member_building",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "building_info_id")
    )
    private Set<BuildingInfo> buildingInfos;

    @ManyToMany
    @JoinTable(
            name = "member_office",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "office_id")
    )
    private Set<Office> offices;

    @Builder
    private Member(String email, String password, Long id, String name, Set<BuildingInfo> buildingInfos, Set<Office> offices) {
        super(email, password, MemberRole.OWNER);//현재 프로젝트에는 가정하고 있기 때문에 OWENER로 초기화
        this.id = id;
        this.name = name;
        this.buildingInfos = buildingInfos;
        this.offices = offices;
    }
}
