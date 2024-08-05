package site.lets_onion.lets_onionApp.util.push;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.dto.push.PushTestRequestDTO;
import site.lets_onion.lets_onionApp.dto.push.PushTestResponseDTO;
import site.lets_onion.lets_onionApp.repository.deviceToken.DeviceTokenRepository;
import site.lets_onion.lets_onionApp.repository.member.MemberRepository;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;
import site.lets_onion.lets_onionApp.util.response.Responses;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PushProvider {

    private final MemberRepository memberRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    /*물주기 푸시 알림*/
    public void sendPushWateringTime(List<String> deviceTokens) {
        Notification notification = Notification.builder()
                .setTitle(PushType.WATERING_TIME.getMessage())
                .setBody(PushType.WATERING_TIME.getMessage())
                .build();

        sendPushByFcm(notification, deviceTokens);
    }


    public void sendPushToOne(Member fromMember, Member toMember, PushType pushType) {
        String message = fromMember.getNickname() + pushType.getMessage();

        Notification notification = Notification.builder()
                .setTitle(message).setBody(message)
                .build();

        List<String> deviceTokens = deviceTokenRepository
                .findAllDeviceTokensByMemberId(toMember.getId());

        sendPushByFcm(notification, deviceTokens);

        /*단건 발송 로직은 사용하지 않으나 임시로 주석처리 했습니다.*/
//        Message push = Message.builder()
//                .setToken(//==받는 사람의 토큰==//)
//                .setNotification(notification)
//                .build();

//        try {
//            FirebaseMessaging.getInstance().send(push);
//        } catch (FirebaseMessagingException e) {
//            log.error("푸시 단건 발송 에러 발생: {}", e.getMessage(), e);
//            throw new CustomException(Exceptions.FAILED_TO_SEND_PUSH_MESSAGE);
//        } catch (Exception e) {
//            log.error("푸시 로직 에러 발생: {}", e.getMessage(), e);
//            throw new RuntimeException(e);
//        }
    }

    public void sendPushToAll(PushType pushType) {
        List<String> allDeviceTokens = deviceTokenRepository.findAllDeviceTokens();

        Notification notification = Notification.builder()
                .setTitle(pushType.getMessage())
                .setBody(pushType.getMessage())
                .build();

        sendPushByFcm(notification, allDeviceTokens);
    }

    /*FCM을 통해 푸시 알림 전송*/
    private PushTestResponseDTO sendPushByFcm(Notification notification, List<String> deviceTokens) {
        if (deviceTokens.isEmpty()) {return null;}
        MulticastMessage pushes = MulticastMessage.builder()
                .setNotification(notification)
                .addAllTokens(deviceTokens)
                .build();

        try {
          return new PushTestResponseDTO(FirebaseMessaging.getInstance().sendEachForMulticast(pushes));
        } catch (FirebaseMessagingException e) {
            log.error("푸시 전체 발송 에러 발생: {}", e.getMessage(), e);
            throw new CustomException(Exceptions.FAILED_TO_SEND_PUSH_MESSAGE);
        } catch (Exception e) {
            log.error("푸시 로직 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    /*
    * 아래는 테스트용 메소드입니다.
    * 차후 삭제 예정입니다.
    * */
    public ResponseDTO<PushTestResponseDTO> pushTest(Long memberId, PushTestRequestDTO dto) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new NullPointerException("User Not Exists"));

        Notification notification = Notification.builder()
                .setTitle(dto.getTitle()).setBody(dto.getBody())
                .build();

        List<String> deviceTokens = deviceTokenRepository
                .findAllDeviceTokensByMemberId(memberId);

        MulticastMessage pushes = MulticastMessage.builder()
                .setNotification(notification)
                .addAllTokens(deviceTokens)
                .build();
        try {
             PushTestResponseDTO responseDTO = new PushTestResponseDTO(FirebaseMessaging.getInstance().sendEachForMulticast(pushes));
             return new ResponseDTO<>(responseDTO, Responses.OK);
        } catch (FirebaseMessagingException e) {
            log.error("푸시 전체 발송 에러 발생: {}", e.getMessage(), e);
            throw new CustomException(Exceptions.FAILED_TO_SEND_PUSH_MESSAGE);
        } catch (Exception e) {
            log.error("푸시 로직 에러 발생: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
