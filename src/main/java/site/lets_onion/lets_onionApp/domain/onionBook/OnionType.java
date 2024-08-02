package site.lets_onion.lets_onionApp.domain.onionBook;

import lombok.Getter;

import java.util.Random;

@Getter
public enum OnionType {

    ONION_GGANG("양파깡", "https://imgur.com/s8D57zC"),
    ONION_RING("양파링", "https://imgur.com/vfcZzfs"),
    ONION_RAW("생양파", "https://imgur.com/cpH5jvA"),
    ONION_PILLED("깐양파", "https://imgur.com/dNv03Iq"),
    ONION_FRIED("양파튀김", "https://imgur.com/NNg1jfT"),
    ONION_PICKLE("양파피클", "https://imgur.com/4mjrwWX"),
    ONION_SUSHI("양파가 누운 연어초밥", "https://imgur.com/rHv8v3c"),
    ONION_KIMCHI("양파김치", "https://imgur.com/0TlJGFL"),
    ONION_SOUP("양파스프", "https://imgur.com/ffjeUVx"),
    ONION_GRILLED("구운 양파", "https://imgur.com/duzJbpC");


    private final String onionName;
    private final String imageUrl;

    OnionType(String onionName, String imageUrl) {
        this.onionName = onionName;
        this.imageUrl = imageUrl;
    }

    private static final Random idx = new Random();

    public static OnionType randomType(){
        OnionType[] onionTypes = values();
        return onionTypes[idx.nextInt(onionTypes.length)];
    }
}
