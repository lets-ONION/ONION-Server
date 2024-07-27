package site.lets_onion.lets_onionApp.repository.friendship;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.friendship.Friendship;
import site.lets_onion.lets_onionApp.domain.friendship.FriendshipStatus;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;

@Repository
public class BaseFriendshipRepositoryImpl implements BaseFriendshipRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Friendship findByFriendshipId(Long friendshipId) {
    return Optional.ofNullable(em.find(
            Friendship.class, friendshipId
        )
    ).orElseThrow(() ->
        new CustomException(Exceptions.FRIENDSHIP_NOT_EXIST));
  }

  @Override
  public List<Friendship> findFriendsByMemberId(Long memberId) {
    return em.createQuery("select f from Friendship f"
                + " left join fetch f.fromMember"
                + " left join fetch f.toMember"
                + " where f.status =:ACCEPTED"
                + " and (f.fromMember.id =:memberId"
                + " or f.toMember.id =:memberId)",
            Friendship.class)
        .setParameter("memberId", memberId)
        .setParameter("ACCEPTED", FriendshipStatus.ACCEPTED)
        .getResultList();
  }

  @Override
  public List<Friendship> findSentFriendRequestsByMemberId(Long memberId) {
    return em.createQuery("select f from Friendship f"
                + " left join fetch f.fromMember"
                + " left join fetch f.toMember"
                + " where f.status =:PENDING"
                + " and f.fromMember.id =:memberId",
            Friendship.class)
        .setParameter("memberId", memberId)
        .getResultList();
  }

  @Override
  public List<Friendship> findReceivedFriendRequestsByMemberId(Long memberId) {
    return em.createQuery("select f from Friendship f"
                + " left join fetch f.fromMember"
                + " left join fetch f.toMember"
                + " where f.status =:PENDING"
                + " and f.toMember.id =:memberId",
            Friendship.class)
        .setParameter("memberId", memberId)
        .setParameter("PENDING", FriendshipStatus.PENDING)
        .getResultList();
  }

  @Override
  public Friendship findByMemberIdAndFriendId(Long memberId, Long friendId) {
    return em.createQuery("select f from Friendship f"
                + " left join fetch f.fromMember"
                + " left join fetch f.toMember"
                + " where (f.toMember.id =:memberId"
                + " and f.fromMember.id =:friendId)"
                + " or (f.toMember.id =:friendId"
                + " and f.fromMember.id =:memberId)",
            Friendship.class)
        .setParameter("memberId", memberId)
        .setParameter("friendId", friendId)
        .getSingleResult();
  }

  @Override
  public long countFriendByMemberId(Long memberId) {
    return (Long) em.createQuery("select count(f) from Friendship f"
            + " where f.status =:ACCEPTED"
            + " and (f.fromMember.id =:memberId"
            + " or f.toMember.id =:memberId)")
        .setParameter("memberId", memberId)
        .setParameter("ACCEPTED", FriendshipStatus.ACCEPTED)
        .getSingleResult();
  }

  @Override
  public long countReceivedFriendRequestsByMemberId(Long memberId) {
    return (Long) em.createQuery("select count(f) from Friendship f"
            + " where f.status =:PENDING"
            + " and f.toMember.id =:memberId")
        .setParameter("memberId", memberId)
        .setParameter("PENDING", FriendshipStatus.PENDING)
        .getSingleResult();
  }
}
