package site.lets_onion.lets_onionApp.domain.member;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import site.lets_onion.lets_onionApp.domain.DeviceToken;
import site.lets_onion.lets_onionApp.domain.onion.GrowingOnion;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<DeviceToken> deviceTokens = new ArrayList<>();

    private Long kakaoId;
    private String nickname;

    @Embedded
    private PushNotification pushNotification;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member")
    private GrowingOnion posOnion;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member")
    private GrowingOnion negOnion;

    @Builder
    public Member(Long id, @NonNull Long kakaoId, String nickname, GrowingOnion posOnion, GrowingOnion negOnion) {
        this.id = id;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.posOnion = posOnion;
        this.negOnion = negOnion;
        pushNotification = new PushNotification();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
