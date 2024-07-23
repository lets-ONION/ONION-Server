package site.lets_onion.lets_onionApp.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.lets_onion.lets_onionApp.dto.jwt.TokenDTO;
import site.lets_onion.lets_onionApp.dto.member.LoginDTO;
import site.lets_onion.lets_onionApp.service.member.MemberService;
import site.lets_onion.lets_onionApp.service.member.Redirection;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;


    @GetMapping("/oauth/kakao/login")
    public ResponseEntity<?> getRedirect(HttpServletRequest request) {
        String addr = request.getRemoteAddr();
        Redirection redirection;
        if ("127.0.0.1".equals(addr) || "0:0:0:0:0:0:0:1".equals(addr)) {
            redirection = Redirection.LOCAL;
        } else {
            redirection = Redirection.SERVER;
        }
        String redirectUri = memberService.getRedirectUri(redirection);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUri));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }


    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<ResponseDTO<LoginDTO>> localLogin(HttpServletRequest request, @RequestParam String code) {
        String addr = request.getRemoteAddr();
        Redirection redirection;
        if ("127.0.0.1".equals(addr) || "0:0:0:0:0:0:0:1".equals(addr)) {
            redirection = Redirection.LOCAL;
        } else {
            redirection = Redirection.SERVER;
        }
        ResponseDTO<LoginDTO> response = memberService.login(code, redirection);
        if (!response.getData().isExistUser()) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }


    @PostMapping("/token/refresh")
    public ResponseEntity<ResponseDTO<TokenDTO>> tokenReissue(
            @RequestBody Map<String, String> request) {
        return new ResponseEntity<>(
                memberService.tokenReissue(request.get("refresh_token")),
                HttpStatus.OK);
    }


    @PostMapping("/auth/logout")
    public ResponseEntity<ResponseDTO> logout(HttpServletRequest request
    , @RequestBody TokenDTO dto) {
        Long memberId = jwtProvider.getMemberId(request);
        return new ResponseEntity<>(
                memberService.logout(memberId, dto.getAccessToken(),
                        dto.getRefreshToken()), HttpStatus.OK);
    }
}
