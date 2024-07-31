package site.lets_onion.lets_onionApp.service.onion;

import site.lets_onion.lets_onionApp.dto.calendar.PosNoteDTO;
import site.lets_onion.lets_onionApp.dto.onion.*;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

public interface OnionService {

    /*메인페이지 조회*/
    ResponseDTO<BabyOnionsDTO> getMainPage(Long memberId);

    /*양파 이름 지정*/
    ResponseDTO<BabyOnionsDTO> saveOnionName(Long memberId, NamingOnionsDTO namingOnionsDTO);

    /*긍정 일기 저장*/
    ResponseDTO<Boolean> savePosNote(Long memberId, PosNoteDTO posNoteDTO);

    /*긍정, 부정 양파 물 주기*/
    ResponseDTO<PosOnionDTO> waterPosOnion(Long memberId);
    ResponseDTO<NegOnionDTO> waterNegOnion(Long memberId);

    /*양파 진화*/
    ResponseDTO<EvolvedOnionDTO> evolveOnion(Long memberId, boolean isPos);

}
