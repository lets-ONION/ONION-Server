package site.lets_onion.lets_onionApp.repository.friendship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.TestGenerator;
import site.lets_onion.lets_onionApp.domain.friendship.Friendship;
import site.lets_onion.lets_onionApp.domain.friendship.FriendshipStatus;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.repository.member.MemberRepository;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class FriendshipRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private FriendshipRepository friendshipRepository;
  @Autowired
  private TestGenerator generator;
  private Member testMember;

  @BeforeEach
  void setUp() {
    testMember = Member.builder()
        .id(1L).kakaoId(1L)
        .nickname("test_member")
        .build();

    testMember = memberRepository.save(testMember);
  }


  @Test
  @Transactional
  public void 친구_요청이_존재하지_않을_때() throws Exception {
    /*given*/

    /*when*/
    Exception e = assertThrows(
        CustomException.class,
        () -> friendshipRepository.findByFriendshipId(1L)
    );
    /*then*/
    assertEquals(
        Exceptions.FRIENDSHIP_NOT_EXIST.getMsg(),
        e.getMessage());
  }


  @Test
  @Transactional
  public void 친구_요청_단건_조회() throws Exception {
    /*given*/
    Member friend = generator.createMember();
    Friendship friendship = generator.createFriendship(testMember, friend);

    /*when*/
    Friendship searchedFriendship =
        friendshipRepository.findByFriendshipId(friendship.getId());

    /*then*/
    assertEquals(friendship, searchedFriendship);
  }


  @Test
  @Transactional
  public void 보낸_친구_요청_조회() throws Exception {
    /*given*/
    int requested = generator.generateRandomInt(100, 50);
    List<Friendship> friendships = new ArrayList<>();
    for (int i = 0; i < requested; i++) {
      friendships.add(
          generator.createFriendship(
              testMember, generator.createMember()
          )
      );
    }

    /*when*/
    List<Friendship> searched =
        friendshipRepository.findSentFriendRequestsByMemberId(
            testMember.getId()
        );

    /*then*/
    assertEquals(requested, searched.size());
    for (Friendship friendship : searched) {
      assertEquals(testMember, friendship.getFromMember());
    }
  }


  @Test
  @Transactional
  public void 받은_친구_요청_조회() throws Exception {
    /*given*/
    int requested = generator.generateRandomInt(100, 50);
    List<Friendship> friendships = new ArrayList<>();
    for (int i = 0; i < requested; i++) {
      friendships.add(
          generator.createFriendship(
              generator.createMember(), testMember
          )
      );
    }

    /*when*/
    List<Friendship> searched =
        friendshipRepository.findReceivedFriendRequestsByMemberId(
            testMember.getId()
        );

    /*then*/
    assertEquals(requested, searched.size());
    for (Friendship friendship : searched) {
      assertEquals(testMember, friendship.getToMember());
    }
  }


  @Test
  @Transactional
  public void 받은_친구_요청_개수_조회() throws Exception {
    /*given*/
    int requested = generator.generateRandomInt(100, 50);
    for (int i = 0; i < requested; i++) {
      generator.createFriendship(generator.createMember(), testMember);
    }

    /*when*/
    int result = (int) friendshipRepository
        .countReceivedFriendRequestsByMemberId(
            testMember.getId()
        );

    /*then*/
    assertEquals(requested, result);
  }


  @Test
  @Transactional
  public void 두_유저의_Friendship_엔티티_조회() throws Exception {
    /*given*/
    Member friend = generator.createMember();
    Friendship friendship = generator.createFriendship(testMember, friend);

    /*when*/
    Friendship searched = friendshipRepository.findByMemberIdAndFriendId(
        testMember.getId(), friend.getId()
    );

    /*then*/
    assertEquals(friendship, searched);
  }


  @Test
  @Transactional
  public void 친구_수_조회() throws Exception {
    /*given*/
    int friends = 0;

    /*when*/
    for (int i = 0; i < 100; i++) {
      if (generator.generateRandomBoolean()) {
        /*내가 친구 요청을 보낸 경우*/
        Friendship request = generator
            .createFriendship(testMember, generator.createMember());
        if (generator.generateRandomBoolean()) {
          /*친구가 된 경우*/
          request.updateStatus(FriendshipStatus.ACCEPTED);
          friends++;
        }
      } else {
        /*내가 친구 요청을 받은 경우*/
        Friendship request = generator
            .createFriendship(generator.createMember(), testMember);
        if (generator.generateRandomBoolean()) {
          /*친구가 된 경우*/
          request.updateStatus(FriendshipStatus.ACCEPTED);
          friends++;
        }
      }
    }

    int searched = (int) friendshipRepository
        .countFriendByMemberId(testMember.getId());

    /*then*/
    assertEquals(friends, searched);
  }


  @Test
  @Transactional
  public void 친구목록_조회() throws Exception {
    /*given*/
    List<Friendship> friendships = new ArrayList<>();

    /*when*/
    for (int i = 0; i < 100; i++) {
      if (generator.generateRandomBoolean()) {
        /*내가 친구 요청을 보낸 경우*/
        Friendship request = generator
            .createFriendship(testMember, generator.createMember());
        if (generator.generateRandomBoolean()) {
          /*친구가 된 경우*/
          request.updateStatus(FriendshipStatus.ACCEPTED);
          friendships.add(request);
        }
      } else {
        /*내가 친구 요청을 받은 경우*/
        Friendship request = generator
            .createFriendship(generator.createMember(), testMember);
        if (generator.generateRandomBoolean()) {
          /*친구가 된 경우*/
          request.updateStatus(FriendshipStatus.ACCEPTED);
          friendships.add(request);
        }
      }
    }

    List<Friendship> searched = friendshipRepository
        .findFriendsByMemberId(testMember.getId());

    /*then*/
    assertEquals(friendships.size(), searched.size());
    for (Friendship friendship : searched) {
      if (!friendship.getFromMember().equals(testMember)
          && !friendship.getToMember().equals(testMember)) {
        fail();
      }
    }
  }
}