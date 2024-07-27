package site.lets_onion.lets_onionApp.repository.friendship;

import org.springframework.data.jpa.repository.JpaRepository;
import site.lets_onion.lets_onionApp.domain.friendship.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long>,
    BaseFriendshipRepository {

}
