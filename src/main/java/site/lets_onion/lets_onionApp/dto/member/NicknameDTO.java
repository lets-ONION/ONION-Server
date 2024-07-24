package site.lets_onion.lets_onionApp.dto.member;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

@Data
public class NicknameDTO {

    private String nickname;

    @JsonCreator
    public NicknameDTO(String nickname) {
        this.nickname = nickname;
    }
}
