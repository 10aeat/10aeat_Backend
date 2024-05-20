package com.final_10aeat.domain.member.controller;

import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberRegisterRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberWithdrawRequestDto;
import com.final_10aeat.domain.member.service.MemberService;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping()
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

    @DeleteMapping()
    public ResponseDTO<Void> withdraw(
            @RequestBody @Valid MemberWithdrawRequestDto request
    ){
        memberService.withdraw(request);
        return ResponseDTO.ok();
    }
}
