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

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 양파 도감들
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "collectedQuantity", column = @Column(name = "onion_ggang_quantity")),
            @AttributeOverride(name = "onionType", column = @Column(name = "onion_ggang_type")),
    })
    private CollectedOnion onionGgang;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "collectedQuantity", column = @Column(name = "onion_ring_quantity")),
            @AttributeOverride(name = "onionType", column = @Column(name = "onion_ring_type")),
    })
    private CollectedOnion onionRing;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "collectedQuantity", column = @Column(name = "onion_raw_quantity")),
            @AttributeOverride(name = "onionType", column = @Column(name = "onion_raw_type")),
    })
    private CollectedOnion onionRaw;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "collectedQuantity", column = @Column(name = "onion_pilled_quantity")),
            @AttributeOverride(name = "onionType", column = @Column(name = "onion_pilled_type")),
    })
    private CollectedOnion onionPilled;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "collectedQuantity", column = @Column(name = "onion_fried_quantity")),
            @AttributeOverride(name = "onionType", column = @Column(name = "onion_fried_type")),
    })
    private CollectedOnion onionFried;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "collectedQuantity", column = @Column(name = "onion_pickle_quantity")),
            @AttributeOverride(name = "onionType", column = @Column(name = "onion_pickle_type")),
    })
    private CollectedOnion onionPickle;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "collectedQuantity", column = @Column(name = "onion_sushi_quantity")),
            @AttributeOverride(name = "onionType", column = @Column(name = "onion_sushi_type")),
    })
    private CollectedOnion onionSushi;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "collectedQuantity", column = @Column(name = "onion_kimchi_quantity")),
            @AttributeOverride(name = "onionType", column = @Column(name = "onion_kimchi_type")),
    })
    private CollectedOnion onionKimchi;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "collectedQuantity", column = @Column(name = "onion_soup_quantity")),
            @AttributeOverride(name = "onionType", column = @Column(name = "onion_soup_type")),
    })
    private CollectedOnion onionSoup;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "collectedQuantity", column = @Column(name = "onion_grilled_quantity")),
            @AttributeOverride(name = "onionType", column = @Column(name = "onion_grilled_type")),
    })
    private CollectedOnion onionGrilled;


    //조회 메서드
    /**
     * onionType에 맞는 필드 조회
     */
    public CollectedOnion getOnion(OnionType onionType) {
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
            default:
                throw new IllegalArgumentException("Unknown OnionType");
        }
    }

}
