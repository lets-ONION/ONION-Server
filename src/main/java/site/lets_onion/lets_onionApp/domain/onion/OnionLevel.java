package site.lets_onion.lets_onionApp.domain.onion;

import lombok.Getter;

@Getter
public enum OnionLevel {

    ZERO("https://i.imgur.com/wFI6Whf.png", "https://i.imgur.com/422VRBF.png"),
    ONE("https://i.imgur.com/oDoQjha.png", "https://i.imgur.com/8IctF19.png"),
    TWO("https://i.imgur.com/lnjlMGv.png", "https://i.imgur.com/QbYAJSN.png"),
    THREE("https://i.imgur.com/KYx9dzH.png", "https://i.imgur.com/pryC0Q4.png"),
    FOUR("https://i.imgur.com/wc62zxv.png", "https://i.imgur.com/CO4R0yb.png"),
    FIVE("https://i.imgur.com/u3vGcfo.png", "https://i.imgur.com/Vc1mRVo.png"),
    SIX("https://i.imgur.com/IZn4IFy.png", "https://i.imgur.com/EHFWhL5.png"),
    SEVEN_1("", ""),
    SEVEN_2("", "");

    private final String posImageUrl;
    private final String negImageUrl;

    OnionLevel(String posImageUrl, String negImageUrl) {
        this.posImageUrl = posImageUrl;
        this.negImageUrl = negImageUrl;
    }
}