package site.lets_onion.lets_onionApp.domain.onionBook;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectedOnion {

    @Enumerated(EnumType.STRING)
    private OnionType onionType;

    private int collectedQuantity;

    @Builder
    public CollectedOnion(OnionType onionType) {
        this.onionType = onionType;
        this.collectedQuantity = 0;
    }

    //테스트용
    @Builder(builderClassName = "TestCollectedOnionBuilder", builderMethodName = "testBuilder")
    public CollectedOnion(OnionType onionType, int collectedQuantity) {
        this.onionType = onionType;
        this.collectedQuantity = collectedQuantity;
    }


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
