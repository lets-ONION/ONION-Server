package site.lets_onion.lets_onionApp.repository.deviceToken;

import java.util.List;

public interface BaseDeviceTokenRepository {

    /*특정 유저의 디바이스 토큰 모두 조회*/
    List<String> findAllDeviceTokensByMemberId(Long memberId);

    /*DB상의 모든 디바이스 토큰 조회*/
    List<String> findAllDeviceTokens();

    /*특정 디바이스 토큰 삭제*/
    int deleteDeviceToken(Long memberId, String deviceToken);
}
