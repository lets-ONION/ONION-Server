package site.lets_onion.lets_onionApp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import site.lets_onion.lets_onionApp.domain.member.Member;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class DeviceToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String deviceToken;

    @CreatedDate
    private LocalDateTime createdAT;

    @Builder
    public DeviceToken(Long id, Member member, String deviceToken) {
        this.id = id;
        this.member = member;
        this.deviceToken = deviceToken;
        this.member.getDeviceTokens().add(this);
    }
}
