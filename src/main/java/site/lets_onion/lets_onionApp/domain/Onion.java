package site.lets_onion.lets_onionApp.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Onion {

    @Id @GeneratedValue
    @Column(name = "onion_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OnionType onionType;

    private int collectedQuantity;
}
