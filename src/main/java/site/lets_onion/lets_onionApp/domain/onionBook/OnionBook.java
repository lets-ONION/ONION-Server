package site.lets_onion.lets_onionApp.domain.onionBook;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;

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


    @Builder
    public OnionBook(Member member) {
        this.member = member;
        this.onionGgang = CollectedOnion.builder().onionType(OnionType.ONION_GGANG).build();
        this.onionRing = CollectedOnion.builder().onionType(OnionType.ONION_RING).build();
        this.onionRaw = CollectedOnion.builder().onionType(OnionType.ONION_RAW).build();
        this.onionPilled = CollectedOnion.builder().onionType(OnionType.ONION_PILLED).build();
        this.onionFried = CollectedOnion.builder().onionType(OnionType.ONION_FRIED).build();
        this.onionPickle = CollectedOnion.builder().onionType(OnionType.ONION_PICKLE).build();
        this.onionSushi = CollectedOnion.builder().onionType(OnionType.ONION_SUSHI).build();
        this.onionKimchi = CollectedOnion.builder().onionType(OnionType.ONION_KIMCHI).build();
        this.onionSoup = CollectedOnion.builder().onionType(OnionType.ONION_SOUP).build();
        this.onionGrilled = CollectedOnion.builder().onionType(OnionType.ONION_GRILLED).build();
    }

    //테스트용
    @Builder(builderClassName = "TestOnionBookBuilder", builderMethodName = "testBuilder")
    public OnionBook(Member member, int ggang, int ring, int raw, int pilled, int fried, int pickle, int sushi, int kimchi, int soup, int grilled) {
        this.member = member;
        this.onionGgang = CollectedOnion.testBuilder().onionType(OnionType.ONION_GGANG).collectedQuantity(ggang).build();
        this.onionRing = CollectedOnion.testBuilder().onionType(OnionType.ONION_RING).collectedQuantity(ring).build();
        this.onionRaw = CollectedOnion.testBuilder().onionType(OnionType.ONION_RAW).collectedQuantity(raw).build();
        this.onionPilled = CollectedOnion.testBuilder().onionType(OnionType.ONION_PILLED).collectedQuantity(pilled).build();
        this.onionFried = CollectedOnion.testBuilder().onionType(OnionType.ONION_FRIED).collectedQuantity(fried).build();
        this.onionPickle = CollectedOnion.testBuilder().onionType(OnionType.ONION_PICKLE).collectedQuantity(pickle).build();
        this.onionSushi = CollectedOnion.testBuilder().onionType(OnionType.ONION_SUSHI).collectedQuantity(sushi).build();
        this.onionKimchi = CollectedOnion.testBuilder().onionType(OnionType.ONION_KIMCHI).collectedQuantity(kimchi).build();
        this.onionSoup = CollectedOnion.testBuilder().onionType(OnionType.ONION_SOUP).collectedQuantity(soup).build();
        this.onionGrilled = CollectedOnion.testBuilder().onionType(OnionType.ONION_GRILLED).collectedQuantity(grilled).build();
    }


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
                throw new CustomException(Exceptions.ONION_TYPE_NOT_EXIST);
        }
    }

}
