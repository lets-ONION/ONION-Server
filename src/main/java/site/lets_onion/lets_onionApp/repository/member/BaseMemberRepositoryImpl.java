package site.lets_onion.lets_onionApp.repository.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.friendship.FriendshipStatus;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;

@Repository
public class BaseMemberRepositoryImpl implements BaseMemberRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Member findByMemberId(Long memberId) {
    return Optional.ofNullable(em.find(
            Member.class, memberId
        )
    ).orElseThrow(() ->
        new CustomException(Exceptions.MEMBER_NOT_EXIST));
  }


  @Override
  public Optional<Member> findByKakaoId(Long kakaoId) {
    try {
      return Optional.ofNullable(em.createQuery("select m from Member m"
              + " where m.kakaoId =:kakaoId", Member.class)
          .setParameter("kakaoId", kakaoId)
          .getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Member> findAllByKakaoId(Long memberId, List<Long> kakaoIds) {
    return em.createQuery("select m from Member m"
            + " where m.kakaoId in :kakaoIds", Member.class)
        .setParameter("kakaoIds", kakaoIds)
        .getResultList();
  }

  @Override
  public List<Member> findAllNotFriend(Long memberId, List<Long> memberIds) {
    return em.createQuery("select m from Member m"
        + " left join Friendship f on"
        + " (m.id = f.toMember.id or m.id = f.toMember.id)"
        + " and (f.fromMember.id =:memberId or f.toMember.id =:memberId)"
        + " and f.status !=:REJECT"
        + " where m.id in :memberIds"
        + " and (f.id is null or f.status =:REJECT)", Member.class)
        .setParameter("memberId", memberId)
        .setParameter("memberIds", memberIds)
        .setParameter("REJECT", FriendshipStatus.REJECT)
        .getResultList();
  }


  @Override
  public Member findByNickname(String nickname) {
    return em.createQuery("select m from Member m " +
            " where m.nickname =:nickname", Member.class)
        .setParameter("nickname", nickname)
        .getSingleResult();
  }

  @Override
  public Member findWithDeviceTokens(Long memberId) {
    return em.createQuery("select m from Member m" +
            " left join fetch m.deviceTokens" +
            " where m.id =:memberId", Member.class)
        .setParameter("memberId", memberId)
        .getSingleResult();
  }
}
