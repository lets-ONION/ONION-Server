package site.lets_onion.lets_onionApp.domain.onionBook;


import jakarta.persistence.*;
import lombok.Getter;
import site.lets_onion.lets_onionApp.domain.member.Member;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class OnionBook {

    @Id @GeneratedValue
    @Column(name = "onions_book_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 양파 도감들
     */
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "onion_ggang")
    private Onion onionGgang;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "onion_ring")
    private Onion onionRing;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "onion_raw")
    private Onion onionRaw;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "onion_pilled")
    private Onion onionPilled;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "onion_fried")
    private Onion onionFried;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "onion_pickle")
    private Onion onionPickle;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "onion_sushi")
    private Onion onionSushi;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "onion_kimchi")
    private Onion onionKimchi;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "onion_soup")
    private Onion onionSoup;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "onion_grilled")
    private Onion onionGrilled;


    //조회 메서드
    //이거로 하려니 가져오는 로직이 너무 복잡해서 주석처리함
//    Onion getOnion(OnionType onionType) {
//        switch (onionType) {
//            case OnionType.ONION_GGANG:
//                return this.onionGgang;
//            case OnionType.ONION_RING:
//                return this.onionRing;
//            case OnionType.ONION_RAW:
//                return this.onionRaw;
//            case OnionType.ONION_PILLED:
//                return this.onionPilled;
//            case OnionType.ONION_FRIED:
//                return this.onionFried;
//            case OnionType.ONION_PICKLE:
//                return this.onionPickle;
//            case OnionType.ONION_SUSHI:
//                return this.onionSushi;
//            case OnionType.ONION_KIMCHI:
//                return this.onionKimchi;
//            case OnionType.ONION_SOUP:
//                return this.onionSoup;
//            case OnionType.ONION_GRILLED:
//                return this.onionGrilled;
//        }
//    }

}
