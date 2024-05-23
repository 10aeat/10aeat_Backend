package com.final_10aeat.domain.manager.controller;

import com.final_10aeat.domain.manager.dto.request.CreateManagerRequestDto;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.manager.service.ManagerService;
import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.global.util.ResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> register(
        @RequestBody @Valid CreateManagerRequestDto request) {
        managerService.register(request);
        return ResponseEntity.ok(ResponseDTO.ok());
    }

    @PostMapping("/login")
    public ResponseDTO<Void> login(
        HttpServletResponse response,
        @RequestBody @Valid MemberLoginRequestDto request) {
        String token = managerService.login(request);
        response.setHeader("accessToken", token);
        return ResponseDTO.ok();
    }
}