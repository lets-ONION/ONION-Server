package site.lets_onion.lets_onionApp.repository.deviceToken;

import org.springframework.data.jpa.repository.JpaRepository;
import site.lets_onion.lets_onionApp.domain.member.DeviceToken;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long>, BaseDeviceTokenRepository {
}
