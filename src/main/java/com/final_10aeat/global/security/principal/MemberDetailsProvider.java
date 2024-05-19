package com.final_10aeat.global.security.principal;

import com.final_10aeat.domain.member.exception.MemberNotExistException;
import com.final_10aeat.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberDetailsProvider {

    private final MemberRepository memberRepository;

    public UserDetails loadMemberByEmail(String email) {
        return new MemberPrincipal(memberRepository.findByEmail(email)
                .orElseThrow(MemberNotExistException::new));
    }

    public UserDetails loadAdminByEmail(String email){
        return null;// TODO Admin 엔티티 작성 이후 추가
    }
}
