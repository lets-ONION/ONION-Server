package site.lets_onion.lets_onionApp.domain.onion;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.lets_onion.lets_onionApp.domain.member.Member;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GrowingOnion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "growing_onion_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "pos_onion_name")),
            @AttributeOverride(name = "isPos", column = @Column(name = "pos_onion_is_pos")),
            @AttributeOverride(name = "growthStage", column = @Column(name = "pos_onion_growth_stage")),
            @AttributeOverride(name = "onionLevel", column = @Column(name = "pos_onion_level")),
            @AttributeOverride(name = "generation", column = @Column(name = "pos_onion_generation"))
    })
    private Onion posOnion;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "neg_onion_name")),
            @AttributeOverride(name = "isPos", column = @Column(name = "neg_onion_is_pos")),
            @AttributeOverride(name = "growthStage", column = @Column(name = "neg_onion_growth_stage")),
            @AttributeOverride(name = "onionLevel", column = @Column(name = "neg_onion_level")),
            @AttributeOverride(name = "generation", column = @Column(name = "neg_onion_generation"))
    })
    private Onion negOnion;

    @Builder
    public GrowingOnion(Member member) {
        this.member = member;
        this.posOnion = Onion.builder()
                .isPos(true)
                .build();
        this.negOnion = Onion.builder()
                .isPos(false)
                .build();
    }

}
