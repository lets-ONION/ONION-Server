package site.lets_onion.lets_onionApp.domain.onionBook;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Getter
public enum OnionType {

    ONION_GGANG("양파깡", "https://imgur.com/s8D57zC.png"),
    ONION_RING("양파링", "https://imgur.com/vfcZzfs.png"),
    ONION_RAW("생양파", "https://imgur.com/cpH5jvA.png"),
    ONION_PILLED("깐양파", "https://imgur.com/dNv03Iq.png"),
    ONION_FRIED("양파튀김", "https://imgur.com/NNg1jfT.png"),
    ONION_PICKLE("양파피클", "https://imgur.com/4mjrwWX.png"),
    ONION_SUSHI("양파가 누운 연어초밥", "https://imgur.com/rHv8v3c.png"),
    ONION_KIMCHI("양파김치", "https://imgur.com/0TlJGFL.png"),
    ONION_SOUP("양파스프", "https://imgur.com/ffjeUVx.png"),
    ONION_GRILLED("구운 양파", "https://imgur.com/duzJbpC.png");


    private final String onionName;
    private final String imageUrl;

    OnionType(String onionName, String imageUrl) {
        this.onionName = onionName;
        this.imageUrl = imageUrl;
    }

    /**
     * onionName으로 OnionType 조회
     */
    private static final Map<String, OnionType> ONION_NAME_MAP = new HashMap<>();
    static {
        for (OnionType onionType : values()) {
            ONION_NAME_MAP.put(onionType.onionName, onionType);
        }
    }
    public static OnionType getByOnionName(String onionName) {
        return ONION_NAME_MAP.get(onionName);
    }

    private static final Random idx = new Random();

    public static OnionType randomType(){
        OnionType[] onionTypes = values();
        return onionTypes[idx.nextInt(onionTypes.length)];
    }
}
