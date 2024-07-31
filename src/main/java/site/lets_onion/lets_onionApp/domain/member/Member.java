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

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private GrowingOnion onions;

    @Builder
    public Member(Long id, @NonNull Long kakaoId, String nickname) {
        this.id = id;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        pushNotification = new PushNotification();
        this.onions = GrowingOnion.builder().member(this).build();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

}
