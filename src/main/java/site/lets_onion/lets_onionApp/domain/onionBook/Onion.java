package site.lets_onion.lets_onionApp.domain.onionBook;

import jakarta.persistence.*;
import lombok.Getter;

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
            throw new NotEnoughQuantityException("더이상 삭제할 수 없습니다.");
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
