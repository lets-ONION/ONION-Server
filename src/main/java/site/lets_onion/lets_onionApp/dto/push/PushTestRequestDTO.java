package site.lets_onion.lets_onionApp.dto.push;

import lombok.Data;

/**
 * 푸시를 테스트해보기 위한 DTO입니다.
 */
@Data
public class PushTestRequestDTO {
    private String title;
    private String body;
}
