package site.lets_onion.lets_onionApp.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import site.lets_onion.lets_onionApp.domain.Member;
import site.lets_onion.lets_onionApp.dto.jwt.TokenDTO;
import site.lets_onion.lets_onionApp.dto.member.*;
import site.lets_onion.lets_onionApp.repository.member.MemberRepository;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;
import site.lets_onion.lets_onionApp.util.redis.RedisConnector;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;
import site.lets_onion.lets_onionApp.util.response.Responses;

import java.util.List;

@Service @Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final RedisConnector redisConnector;

    @Value("${kakao.apiKey}")
    private String clientId;
    private String kakaoCodeUri = "https://kauth.kakao.com/oauth/authorize?response_type=code";
    private WebClient tokenWebClient = WebClient.create("https://kauth.kakao.com/oauth/token");
    private WebClient memberInfoWebClient = WebClient.create("https://kapi.kakao.com/v2/user/me");

    @Override
    public String getRedirectUri(Redirection redirection) {
        return kakaoCodeUri + "&client_id=" + clientId +
                "&redirect_uri=" + redirection.getRedirectUri();
    }

    @Override
    @Transactional
    public ResponseDTO<LoginDTO> login(String code, Redirection redirection) {
        String token = requestKakaoToken(code, redirection);
        Long kakaoId = requestMemberInfo(token);

        List<Member> searchedResult = memberRepository.findByKakaoId(kakaoId);
        Member member;
        boolean existMember;
        if (searchedResult.isEmpty()) {
             member = createMember(kakaoId);
             existMember = false;
        } else {
            member = searchedResult.get(0);
            existMember = true;
        }
        LoginDTO loginDTO = new LoginDTO(member,
                jwtProvider.createAccessToken(member.getId()),
                jwtProvider.createRefreshToken(member.getId()),
                existMember
        );

        if (existMember) {
            return new ResponseDTO<>(loginDTO, Responses.OK);
        } else {
            return new ResponseDTO<>(loginDTO, Responses.CREATED);
        }
    }

    @Override
    public ResponseDTO logout(Long memberId, String accessToken, String refreshToken) {
        jwtProvider.logout(accessToken, refreshToken);
        return new ResponseDTO(null, Responses.OK);
    }

    @Override
    public ResponseDTO<TokenDTO> tokenReissue(String refreshToken) {
        TokenDTO tokens = jwtProvider.refreshAccessToken(refreshToken);
        return new ResponseDTO<>(tokens, Responses.OK);
    }

    @Override
    @Transactional
    public ResponseDTO<StatusMessageDTO> updateStatusMessage(Long memberId, String message) {
        redisConnector.setWithTtl(memberId.toString(), message, 86400000L); // 24시간 유지
        return new ResponseDTO<>(new StatusMessageDTO(message),
                Responses.CREATED);
    }

    @Override
    @Transactional
    public ResponseDTO<StatusMessageDTO> getStatusMessage(Long memberId) {
        String message = redisConnector.get(memberId.toString());
        return new ResponseDTO<>(new StatusMessageDTO(message),
                Responses.OK);
    }

    @Override
    @Transactional
    public ResponseDTO<MemberInfoDTO> updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId).get();
        member.setNickname(nickname);
        return new ResponseDTO<>(new MemberInfoDTO(member),
                Responses.OK);
    }


    @Override
    public ResponseDTO<MemberInfoDTO> getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        return new ResponseDTO<>(new MemberInfoDTO(member),
                Responses.OK);
    }


    /*카카오 인증 서버에 액세스 토큰 요청*/
    private String requestKakaoToken(String code, Redirection redirection) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirection.getRedirectUri());
        formData.add("code", code);

        TokenResponseDTO response = tokenWebClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve().bodyToMono(TokenResponseDTO.class)
                .block();

        return response.getAccessToken().toString();
    }


    /*카카오 인증 서버에 유저 정보 요청*/
    private Long requestMemberInfo(String token) {
        KakaoMemberInfoDTO response = memberInfoWebClient.get()
                .header("Authorization", "Bearer " + token)
                .retrieve().bodyToMono(KakaoMemberInfoDTO.class)
                .block();

        return response.getKakaoId();
    }


    /*새로운 유저 데이터 생성*/
    private Member createMember(Long kakaoId) {
        Member member = Member.builder().kakaoId(kakaoId).build();
        return memberRepository.save(member);
    }

}
