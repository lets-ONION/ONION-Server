package site.lets_onion.lets_onionApp.util.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.lets_onion.lets_onionApp.repository.deviceToken.DeviceTokenRepository;
import site.lets_onion.lets_onionApp.util.push.PushProvider;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

  private final PushProvider pushProvider;
  private final DeviceTokenRepository deviceTokenRepository;

//  @Scheduled(cron = "0 25 2 * * ?")
//  public void sendPushWateringTime() {
//    List<String> pushTokens = deviceTokenRepository.findAllByWateringTime();
//    pushProvider.sendPushWateringTime(pushTokens);
//  }

}
