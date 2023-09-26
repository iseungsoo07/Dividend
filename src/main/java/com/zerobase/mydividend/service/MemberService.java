package com.zerobase.mydividend.service;

import com.zerobase.mydividend.exception.impl.AlreadyUsingIdException;
import com.zerobase.mydividend.exception.impl.DoesNotExistIdException;
import com.zerobase.mydividend.exception.impl.PasswordUnmatchException;
import com.zerobase.mydividend.model.Auth;
import com.zerobase.mydividend.persist.MemberRepository;
import com.zerobase.mydividend.persist.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }

    // 회원 가입
    public MemberEntity register(Auth.SignUp member) {
        boolean exists = memberRepository.existsByUsername(member.getUsername());

        if (exists) {
            throw new AlreadyUsingIdException();
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));

        return memberRepository.save(member.toEntity());
    }

    // 로그인 검증
    public MemberEntity authenticate(Auth.SignIn member) {
        MemberEntity user = memberRepository.findByUsername(member.getUsername())
                .orElseThrow(DoesNotExistIdException::new);

        if (!passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new PasswordUnmatchException();
        }

        return user;
    }
}
