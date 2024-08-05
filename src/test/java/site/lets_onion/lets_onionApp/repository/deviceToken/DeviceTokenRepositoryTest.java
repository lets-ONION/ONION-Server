package site.lets_onion.lets_onionApp.repository.deviceToken;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.TestGenerator;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.service.member.MemberService;
import site.lets_onion.lets_onionApp.util.push.PushType;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DeviceTokenRepositoryTest {

  @Autowired
  private DeviceTokenRepository deviceTokenRepository;
  @Autowired
  private TestGenerator generator;
  @Autowired
  private MemberService memberService;

  @Test
  @Transactional
  public void 물주기_디바이스토큰_조회() throws Exception {
    /*given*/
    int accepted = 100;

    for (int i = 0; i < 100; i++) {
      Member testMember = generator.createMember();
      String token = generator.generateRandomString(10);
      memberService.saveDeviceToken(testMember.getId(), token);
      if (generator.generateRandomBoolean()) {
            memberService.modifyPushSetting(testMember.getId(), PushType.WATERING_TIME);
            accepted--;
      }
    }

    /*when*/
    List<String> tokens = deviceTokenRepository.findAllByWateringTime();

    /*then*/
    assertEquals(accepted, tokens.size());
  }
}
