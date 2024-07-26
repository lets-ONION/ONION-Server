package site.lets_onion.lets_onionApp.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class KakaoScopesDTO {

    @JsonProperty("id")
    private Long kakaoId;
    private List<Scope> scopes;

    @Data
    static public class Scope {
        private String id;
        @JsonProperty("display_name")
        private String displayName;
        private String type;
        private boolean using;
        private boolean agree;
        private boolean revocable;
    }
}
