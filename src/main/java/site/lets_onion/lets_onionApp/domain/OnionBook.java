package site.lets_onion.lets_onionApp.domain;


import jakarta.persistence.*;
import lombok.Getter;

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

    //양파 도감들
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

}
