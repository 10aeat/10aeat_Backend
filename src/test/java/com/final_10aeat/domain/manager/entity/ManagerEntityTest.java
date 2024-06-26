package com.final_10aeat.domain.manager.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.final_10aeat.common.config.TestConfig;
import com.final_10aeat.domain.manager.repository.ManagerRepository;
import com.final_10aeat.domain.office.repository.OfficeRepository;
import com.final_10aeat.common.enumclass.MemberRole;
import com.final_10aeat.domain.office.entity.Office;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Transactional
@Import(TestConfig.class)
class ManagerEntityTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private OfficeRepository officeRepository;

    private Office office;

    @BeforeEach
    void setUp() {
        office = Office.builder()
            .officeName("미왕 빌딩")
            .address("123 Gangnam St.")
            .mapX(35.6895)
            .mapY(139.6917)
            .build();

        officeRepository.save(office);
    }

    @Test
    @DisplayName("관리자 엔티티 저장 및 조회 테스트")
    void testSaveAndFindManager() {
        Manager manager = Manager.builder()
            .email("manager@example.com")
            .password("securePassword")
            .name("김관리")
            .phoneNumber("010-1234-5678")
            .lunchBreakStart(LocalDateTime.of(2024, 1, 1, 12, 0))
            .lunchBreakEnd(LocalDateTime.of(2024, 1, 1, 13, 0))
            .managerOffice("중앙 현관 1층 관리자 사무소")
            .affiliation("김씨 관리 협회")
            .office(office)
            .role(MemberRole.MANAGER)
            .build();

        managerRepository.save(manager);

        Manager foundManager = managerRepository.findById(manager.getId()).orElse(null);

        assertThat(foundManager).isNotNull();
        assertThat(foundManager.getEmail()).isEqualTo("manager@example.com");
        assertThat(foundManager.getName()).isEqualTo("김관리");
        assertThat(foundManager.getOffice().getOfficeName()).isEqualTo("미왕 빌딩");
    }

    @Test
    @DisplayName("사무소 엔티티 저장 및 조회 테스트")
    void testSaveAndFindOffice() {
        Office foundOffice = officeRepository.findById(office.getId()).orElse(null);

        assertThat(foundOffice).isNotNull();
        assertThat(foundOffice.getOfficeName()).isEqualTo("미왕 빌딩");
        assertThat(foundOffice.getAddress()).isEqualTo("123 Gangnam St.");
    }
}
