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
