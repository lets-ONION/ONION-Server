package site.lets_onion.lets_onionApp.domain.onionBook;

import jakarta.persistence.*;
import lombok.Getter;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;

@Embeddable
@Getter
public class Onion {

    @Enumerated(EnumType.STRING)
    private OnionType onionType;

    private int collectedQuantity;

    // 비즈니스 로직
    /**
     * 개수 감소
     */
    public void decreaseQuantity() {
        int restQuantity = this.collectedQuantity - 1;
        if (restQuantity < 1) { //1개 미만으로는 제거하지 않음.
            throw new CustomException(Exceptions.NOT_ENOUGH_QUANTITY);
        }
        this.collectedQuantity = restQuantity;
    }

    /**
     * 개수 증가
     */
    public void increaseQuantity() {
        this.collectedQuantity += 1;
    }

}
