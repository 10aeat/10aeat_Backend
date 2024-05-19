package com.final_10aeat.domain.member.controller;

import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberRegisterRequestDto;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.service.MemberService;
import com.final_10aeat.global.security.principal.MemberPrincipal;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseDTO<Void> register(
            @RequestBody MemberRegisterRequestDto request
    ) {
        memberService.register(request);
        return ResponseDTO.ok();
    }

    @PostMapping("/login")
    public ResponseDTO<Void> login(
            HttpServletResponse response,
            @RequestBody MemberLoginRequestDto request) {
        String token = memberService.login(request);
        response.setHeader("accessToken", token);
        return ResponseDTO.ok();
    }

    @GetMapping("/test")
    public ResponseDTO<?> test(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
            ){
        Member member = (Member) memberPrincipal.getMember();
        /*
        Security에서 Loginable을 사용해 추상화 하려는 시도를 했으나
        컨트롤러단에선 오히려 회원 정보를 이용 할 때 Loginable에서 없는 정보가 필요하면
        null로 입력되는 문제가 발생함
        */
        return ResponseDTO.okWithData(member.getName());
    }
}
