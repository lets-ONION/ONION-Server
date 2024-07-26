package site.lets_onion.lets_onionApp.util.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.dto.integration.FriendRequestDTO;
import site.lets_onion.lets_onionApp.dto.integration.KakaoMemberInfoDTO;
import site.lets_onion.lets_onionApp.dto.integration.KakaoScopesDTO;
import site.lets_onion.lets_onionApp.dto.integration.KakaoTokenResponseDTO;
import site.lets_onion.lets_onionApp.service.member.Redirection;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;
import site.lets_onion.lets_onionApp.util.redis.KakaoRedisConnector;
import site.lets_onion.lets_onionApp.util.redis.KakaoTokens;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoRequest {

    @Value("${kakao.apiKey}")
    private String clientId;
    private WebClient tokenWebClient = WebClient.create("https://kauth.kakao.com/oauth/token");
    private WebClient memberInfoWebClient = WebClient.create("https://kapi.kakao.com/v2/user/me");
    private WebClient logoutWebClient = WebClient.create("https://kapi.kakao.com/v1/user/logout");
    private WebClient scopeWebClient = WebClient.create("https://kapi.kakao.com/v2/user/scopes");
    private WebClient kakaoTokenRefreshWebClient = WebClient.create("https://kauth.kakao.com/oauth/token");
    private WebClient friendsWebClient = WebClient.create("https://kapi.kakao.com/v1/api/talk/friends");
    private final KakaoRedisConnector kakaoRedisConnector;


    /**
     * 카카오 인증서버와 통신하여 토큰을 발급받습니다.
     * @param code
     * @param redirection
     * @return
     */
    public KakaoTokenResponseDTO requestKakaoAuthToken(String code, Redirection redirection) {
        return tokenWebClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("redirect_uri", redirection.getRedirectUri())
                        .with("code", code))
                .retrieve().bodyToMono(KakaoTokenResponseDTO.class)
                .block();
    }


    /**
     * 카카오 인증 서버에 유저 정보를 요청합니다.
     * @param token
     * @return
     */
    public KakaoMemberInfoDTO requestKakaoMemberInfo(String token) {
        return memberInfoWebClient.get()
                .header("Authorization", "Bearer " + token)
                .retrieve().bodyToMono(KakaoMemberInfoDTO.class)
                .block();
    }


    /**
     *
     * 카카오 인증 서버에 로그아웃을 요청합니다.
     * @param member
     * @param token
     */
    public void requestKakaoLogout(Member member, String token) {
        try {
            logoutWebClient.post().contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .header("Authorization", "Bearer " + token)
                    .body(BodyInserters
                            .fromFormData("target_id_type", "user_id")
                            .with("target_id", member.getKakaoId().toString()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
                requestKakaoLogout(member, kakaoTokenRefresh(member));
            } else {
                log.error(e.getMessage());
                throw new CustomException(Exceptions.KAKAO_LOGOUT_FAILED);
            }
        }
    }


    /**
     * 카카오 인증 서버에 유저 동의 정보를 요청합니다.
     * @param member
     * @param token
     * @return
     */
    public KakaoScopesDTO requestKakaoScopes(Member member, String token) {

        try {
            return scopeWebClient
                .get().uri(URIBuilder -> URIBuilder
                    .queryParam("target_id_type", "user_id")
                    .queryParam("target_id", member.getKakaoId().toString())
                    .build())
                .header("Authorization", "Bearer " + token)
                .retrieve().bodyToMono(KakaoScopesDTO.class).block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
                return requestKakaoScopes(member, kakaoTokenRefresh(member));
            } else {
                log.error(e.getMessage());
                throw new CustomException(Exceptions.INTERNAL_SERVER_ERROR);
            }
        }

    }


    /**
     * 카카오 서버에서 유저의 카카오 친구 중<br>
     * 앱 사용자 리스트를 조회합니다.
     * @param member
     * @param token
     * @param offset
     * @return
     */
    public FriendRequestDTO kakaoRequestFriends(Member member, String token, int offset) {
        try {
            return friendsWebClient.get()
                    .uri(URIBuilder -> URIBuilder
                            .queryParam("limit", "100")
                            .queryParam("offset", String.valueOf(offset))
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .retrieve().bodyToMono(FriendRequestDTO.class).block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
                return kakaoRequestFriends(member, kakaoTokenRefresh(member), offset);
            } else {
                log.error(e.getMessage());
                throw new RuntimeException(e.getMessage() + e.getResponseBodyAsString());
            }
        }
    }


    /**
     * 카카오 인증 서버에 로그아웃을 요청합니다
     * @param member
     * @return
     */
    private String kakaoTokenRefresh(Member member) {
        String refreshToken = kakaoRedisConnector.get(member.getId()).getRefreshToken();
        KakaoTokenResponseDTO response = kakaoTokenRefreshWebClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("refresh_token", refreshToken))
                .retrieve().bodyToMono(KakaoTokenResponseDTO.class)
                .block();

        kakaoRedisConnector.setWithTtl(
                member.getId(),
                new KakaoTokens(response.getAccessToken(), response.getRefreshToken()),
                (long) response.getRefreshExpiresIn()
        );

        return response.getAccessToken();
    }
}
