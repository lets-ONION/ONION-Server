package site.lets_onion.lets_onionApp.repository.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.Member;

@Repository
public class BaseMemberRepositoryImpl implements BaseMemberRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Member findByKakaoId(String kakaoId) {
        return em.createQuery("select m from Member m " +
                " where m.kakaoId =:kakaoId", Member.class)
                .setParameter("kakaoId", kakaoId)
                .getSingleResult();
    }

    @Override
    public Member findByNickname(String nickname) {
        return em.createQuery("select m from Member m " +
                        " where m.nickname =:nickname", Member.class)
                .setParameter("nickname", nickname)
                .getSingleResult();
    }

    @Override
    public Member updateNickname(Long memberId, String nickname) {
        Member member = em.find(Member.class, memberId);
        member.setNickname(nickname);
        return member;
    }
}
