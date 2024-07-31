package site.lets_onion.lets_onionApp.domain.onionBook;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.lets_onion.lets_onionApp.domain.member.Member;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnionBook {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "onions_book_id")
    private Long id;

    @OneToOne(mappedBy = "onionBook", fetch = LAZY)
    private Member member;

    /**
     * 양파 도감들
     */
    @Embedded
    private Onion onionGgang;

    @Embedded
    private Onion onionRing;

    @Embedded
    private Onion onionRaw;

    @Embedded
    private Onion onionPilled;

    @Embedded
    private Onion onionFried;

    @Embedded
    private Onion onionPickle;

    @Embedded
    private Onion onionSushi;

    @Embedded
    private Onion onionKimchi;

    @Embedded
    private Onion onionSoup;

    @Embedded
    private Onion onionGrilled;


    //조회 메서드
    /**
     * onionType에 맞는 필드 조회
     */
    public Onion getOnion(OnionType onionType) {
        switch (onionType) {
            case ONION_GGANG:
                return this.onionGgang;
            case ONION_RING:
                return this.onionRing;
            case ONION_RAW:
                return this.onionRaw;
            case ONION_PILLED:
                return this.onionPilled;
            case ONION_FRIED:
                return this.onionFried;
            case ONION_PICKLE:
                return this.onionPickle;
            case ONION_SUSHI:
                return this.onionSushi;
            case ONION_KIMCHI:
                return this.onionKimchi;
            case ONION_SOUP:
                return this.onionSoup;
            case ONION_GRILLED:
                return this.onionGrilled;
        }
    }

}
