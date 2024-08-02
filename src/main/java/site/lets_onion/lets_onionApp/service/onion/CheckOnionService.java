package site.lets_onion.lets_onionApp.service.onion;

import site.lets_onion.lets_onionApp.domain.onion.NegCheckResult;
import site.lets_onion.lets_onionApp.domain.onion.PosCheckResult;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

public interface CheckOnionService {

    //긍정양파 일기 검토
    ResponseDTO<PosCheckResult> checkPosNote(String posNote);

    //부정양파 일기 검토
    ResponseDTO<NegCheckResult> checkNegNote(String negNote);

    //테스트
    ResponseDTO<String> test(String note);
}
