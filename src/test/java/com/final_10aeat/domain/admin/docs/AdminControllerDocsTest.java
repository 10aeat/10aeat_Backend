package com.final_10aeat.domain.admin.docs;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_10aeat.docs.RestDocsSupport;
import com.final_10aeat.domain.admin.controller.AdminController;
import com.final_10aeat.domain.admin.dto.request.CreateAdminRequestDto;
import com.final_10aeat.domain.admin.entity.Admin;
import com.final_10aeat.domain.admin.entity.Office;
import com.final_10aeat.domain.admin.repository.AdminRepository;
import com.final_10aeat.domain.admin.repository.OfficeRepository;
import com.final_10aeat.domain.admin.service.AdminService;
import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.global.security.jwt.JwtTokenGenerator;
import com.final_10aeat.domain.member.entity.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

public class AdminControllerDocsTest extends RestDocsSupport {

    private AdminService adminService;
    private AdminRepository adminRepository;
    private OfficeRepository officeRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenGenerator jwtTokenGenerator;
    private ObjectMapper objectMapper;

    @Override
    public Object initController() {
        adminRepository = Mockito.mock(AdminRepository.class);
        officeRepository = Mockito.mock(OfficeRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        jwtTokenGenerator = Mockito.mock(JwtTokenGenerator.class);
        adminService = new AdminService(adminRepository, officeRepository, passwordEncoder,
            jwtTokenGenerator);
        objectMapper = new ObjectMapper();
        return new AdminController(adminService);
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(initController())
            .apply(documentationConfiguration(restDocumentation))
            .build();

        Office office = Office.builder()
            .id(1L)
            .officeName("미왕 빌딩")
            .address("123 Gangnam St.")
            .mapX("35.6895")
            .mapY("139.6917")
            .build();
        when(officeRepository.findById(1L)).thenReturn(Optional.of(office));

        Admin admin = Admin.builder()
            .email("admin@example.com")
            .password("adminPassword")
            .name("김관리")
            .phoneNumber("010-1234-5678")
            .lunchBreakStart(null)
            .lunchBreakEnd(null)
            .adminOffice("중앙 현관 1층 관리자 사무실")
            .affiliation("김씨 관리 협회")
            .office(office)
            .role(MemberRole.ADMIN)
            .build();
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("adminPassword", "adminPassword")).thenReturn(true);
    }

    @DisplayName("관리자 등록 API 문서화")
    @Test
    void testRegisterAdmin() throws Exception {
        // Given
        CreateAdminRequestDto registerRequest = new CreateAdminRequestDto(
            "admin@naver.com", "adminPassword", "김관리", "010-1234-5678", null, null,
            "중앙 현관 1층 관리자 사무실", "김씨 관리 협회", 1L
        );

        // When
        when(adminService.registerAdmin(registerRequest)).thenReturn(null);

        // Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isOk())
            .andDo(document("admin-register",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("name").description("이름"),
                    fieldWithPath("phoneNumber").description("전화번호"),
                    fieldWithPath("lunchBreakStart").optional().description("점심시간 시작"),
                    fieldWithPath("lunchBreakEnd").optional().description("점심시간 끝"),
                    fieldWithPath("adminOffice").description("관리 사무소 위치"),
                    fieldWithPath("affiliation").description("소속 정보"),
                    fieldWithPath("officeId").description("관리하고 있는 건물 ID")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }

    @DisplayName("관리자 로그인 API 문서화")
    @Test
    void testLoginAdmin() throws Exception {
        // Given
        MemberLoginRequestDto loginRequest = new MemberLoginRequestDto(
            "admin@example.com", "adminPassword"
        );

        // When
        String token = "mockToken";
        when(adminService.login(loginRequest)).thenReturn(token);

        // Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andDo(document("admin-login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("이메일"),
                    fieldWithPath("password").description("비밀번호")
                ),
                responseHeaders(
                    headerWithName("accessToken").description("발급된 액세스 토큰")
                ),
                responseFields(
                    fieldWithPath("code").description("응답 상태 코드")
                )
            ));
    }
}