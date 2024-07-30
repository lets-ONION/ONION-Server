package site.lets_onion.lets_onionApp.domain.onion;

import lombok.Getter;

@Getter
public enum OnionLevel {

    ZERO(0, "https://i.imgur.com/wFI6Whf.png", "https://i.imgur.com/422VRBF.png"),
    ONE(1, "https://i.imgur.com/oDoQjha.png", "https://i.imgur.com/8IctF19.png"),
    TWO(2, "https://i.imgur.com/lnjlMGv.png", "https://i.imgur.com/QbYAJSN.png"),
    THREE(3, "https://i.imgur.com/KYx9dzH.png", "https://i.imgur.com/pryC0Q4.png"),
    FOUR(4, "https://i.imgur.com/wc62zxv.png", "https://i.imgur.com/CO4R0yb.png"),
    FIVE(5, "https://i.imgur.com/u3vGcfo.png", "https://i.imgur.com/Vc1mRVo.png"),
    SIX(6, "https://i.imgur.com/IZn4IFy.png", "https://i.imgur.com/EHFWhL5.png"),
    SEVEN(7, "https://i.imgur.com/paae1bc.gifv", "https://i.imgur.com/SC2KAFa.gifv");

    private final int value;
    private final String posImageUrl;
    private final String negImageUrl;

    OnionLevel(int value, String posImageUrl, String negImageUrl) {
        this.value = value;
        this.posImageUrl = posImageUrl;
        this.negImageUrl = negImageUrl;
    }
}