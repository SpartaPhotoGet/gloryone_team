package com.example.loginlivesession2.service;

import com.example.loginlivesession2.dto.LoginReqDto;
import com.example.loginlivesession2.dto.MemberReqDto;
import com.example.loginlivesession2.entity.Member;
import com.example.loginlivesession2.entity.RefreshToken;
import com.example.loginlivesession2.exception.ErrorCode;
import com.example.loginlivesession2.exception.RequestException;
import com.example.loginlivesession2.global.ResponseDto;
import com.example.loginlivesession2.jwt.dto.TokenDto;
import com.example.loginlivesession2.jwt.util.JwtUtil;
import com.example.loginlivesession2.repository.MemberRepository;
import com.example.loginlivesession2.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public String signup(MemberReqDto memberReqDto) {
        // userId 중복 검사
        if(memberRepository.findByUserId(memberReqDto.getUserId()).isPresent()){
            throw new RequestException(ErrorCode.USERID_DUPLICATION_409);
        }

        memberReqDto.setEncodePwd(passwordEncoder.encode(memberReqDto.getPassword()));
        Member Member = new Member(memberReqDto);

        memberRepository.save(Member);
        return "회원가입완료";
    }

    @Transactional
    public String login(LoginReqDto loginReqDto, HttpServletResponse response) {

        // Member 저장소 내 userId가 없음
        Member account = memberRepository.findByUserId(loginReqDto.getUserId()).orElseThrow(
                () -> new RequestException(ErrorCode.USER_NOT_FOUND_404)
        );

        TokenDto tokenDto = jwtUtil.createAllToken(loginReqDto.getUserId());

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(loginReqDto.getUserId());

        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefresh_Token()));
        }else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefresh_Token(), loginReqDto.getUserId());
            refreshTokenRepository.save(newToken);
        }

        setHeader(response, tokenDto);

        return "로그인 완료!";

    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_TOKEN, tokenDto.getAuthorization());
        response.addHeader(JwtUtil.REFRESH_TOKEN, tokenDto.getAuthorization());
    }
}
