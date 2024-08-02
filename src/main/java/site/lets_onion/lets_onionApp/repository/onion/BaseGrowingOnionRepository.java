package site.lets_onion.lets_onionApp.repository.onion;

import site.lets_onion.lets_onionApp.domain.onion.GrowingOnion;

public interface BaseGrowingOnionRepository {

    /*pk로 조회*/
    GrowingOnion findByMemberId(Long memberId);

}
