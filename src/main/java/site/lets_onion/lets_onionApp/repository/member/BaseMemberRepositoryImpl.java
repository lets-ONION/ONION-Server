package site.lets_onion.lets_onionApp.repository.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
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
    public Member findByKakaoId(Long kakaoId) {
        List<Member> result = em.createQuery("select m from Member m " +
                " where m.kakaoId =:kakaoId", Member.class)
            .setParameter("kakaoId", kakaoId)
            .setMaxResults(1)
            .getResultList();

        return result.isEmpty() ? null : result.get(0);
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
