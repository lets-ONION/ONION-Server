package site.lets_onion.lets_onionApp.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import site.lets_onion.lets_onionApp.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, BaseMemberRepository {

}
