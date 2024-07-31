package site.lets_onion.lets_onionApp.repository.onion;

import org.springframework.data.jpa.repository.JpaRepository;
import site.lets_onion.lets_onionApp.domain.onion.GrowingOnion;

public interface GrowingOnionRepository extends JpaRepository<GrowingOnion, Long>, BaseGrowingOnionRepository {
}
