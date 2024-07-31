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

    private String name;

    private boolean posOrNeg;

    private int growthStage;

    private OnionLevel onionLevel;

    private int generation;

    @Builder
    public GrowingOnion(Member member, String name, boolean posOrNeg) {
        this.member = member;
        this.name = name;
        this.posOrNeg = posOrNeg;
        this.growthStage = 0;
        this.onionLevel = OnionLevel.ZERO;
        this.generation = 1;
    }

    public void waterOnion(Long id){
        ++this.growthStage;
        if (this.growthStage == 7){
            this.growthStage = 0;

        }
    }

}
