package site.lets_onion.lets_onionApp;

import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.friendship.Friendship;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.repository.friendship.FriendshipRepository;
import site.lets_onion.lets_onionApp.repository.member.MemberRepository;

/**
 * 테스트에 사용할 객체 생성기입니다..
 */
@Component
public class TestGenerator {

  private final SecureRandom secureRandom = new SecureRandom();
  private static final String CHARACTERS =
      "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private FriendshipRepository friendshipRepository;

  /**
   * 랜덤 문자열 생성기
   *
   * @param length 길이
   * @return
   */
  public String generateRandomString(int length) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int randomIndex = secureRandom.nextInt(CHARACTERS.length());
      char randomChar = CHARACTERS.charAt(randomIndex);
      sb.append(randomChar);
    }
    return sb.toString();
  }

  /**
   * 랜덤 long 정수 생성기
   *
   * @return
   */
  public long generateRandomLong() {
    return secureRandom.nextLong();
  }

  /**
   * 랜덤 long 정수 생성기
   *
   * @param max 최대값
   * @param min 최소값
   * @return
   */
  public long generateRandomLong(long max, long min) {
    return secureRandom.nextLong(max - min) + min;
  }

  /**
   * 랜덤 int 정수 생성기
   *
   * @return
   */
  public int generateRandomInt() {
    return secureRandom.nextInt();
  }

  /**
   * 랜덤 int 정수 생성기
   *
   * @param max 최대값
   * @param min 최소값
   * @return
   */
  public int generateRandomInt(int max, int min) {
    return secureRandom.nextInt(max - min) + min;
  }

  /**
   * 랜덤 boolean 생성기
   *
   * @return
   */
  public boolean generateRandomBoolean() {
    return secureRandom.nextBoolean();
  }

  @Transactional
  public Member createMember() {
    return memberRepository.save(Member.builder()
        .kakaoId(generateRandomLong())
        .nickname(generateRandomString(10))
        .build());
  }

  @Transactional
  public Friendship createFriendship(Member fromMember, Member toMember) {
    return friendshipRepository.save(Friendship.builder()
        .fromMember(fromMember)
        .toMember(toMember)
        .build());
  }

}
