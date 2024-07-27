package site.lets_onion.lets_onionApp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import site.lets_onion.lets_onionApp.dto.friendship.FriendDTO;
import site.lets_onion.lets_onionApp.dto.friendship.FriendshipDTO;
import site.lets_onion.lets_onionApp.dto.friendship.PendingFriendRequestDTO;
import site.lets_onion.lets_onionApp.repository.friendship.FriendshipRepository;
import site.lets_onion.lets_onionApp.repository.member.MemberRepository;
import site.lets_onion.lets_onionApp.service.friendship.FriendshipService;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class FriendshipServiceTest {

  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private FriendshipRepository friendshipRepository;
  @Autowired
  private FriendshipService friendshipService;
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
  public void 친구요청_보내기() throws Exception {
    /*given*/
    Member friend = generator.createMember();

    /*when*/
    FriendshipDTO response = friendshipService
        .sendFriendRequest(testMember.getId(), friend.getId())
        .getData();

    /*then*/
    assertEquals(
        response.getFromMember().getId(),
        testMember.getId()
    );
    assertEquals(
        response.getToMember().getId(),
        friend.getId()
    );
  }


  @Test
  @Transactional
  public void 친구_요청에_응답() throws Exception {
    /*given*/
    long expectedAccept = 0;
    long expectedRejected = 0;
    List<Friendship> requests = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      requests.add(
          generator.createFriendship(
              generator.createMember(),
              testMember
          )
      );
    }

    /*when*/
    for (Friendship request : requests) {
      if (generator.generateRandomBoolean()) {
        /*수락한 경우*/
        friendshipService.updateFriendStatus(
            testMember.getId(),
            request.getId(),
            FriendshipStatus.ACCEPT
        );
        expectedAccept++;
      } else {
        friendshipService.updateFriendStatus(
            testMember.getId(),
            request.getId(),
            FriendshipStatus.REJECT
        );
        expectedRejected++;
      }
    }

    long resultAccept = 0;
    long resultRejected = 0;

    for (Friendship request : requests) {
      if (request.getStatus().equals(FriendshipStatus.ACCEPT)) {
        resultAccept++;
      } else {
        resultRejected++;
      }
    }

    /*then*/
    assertEquals(expectedAccept, resultAccept);
    assertEquals(expectedRejected, resultRejected);
  }


  @Test
  @Transactional
  public void 삭제_테스트_케이스() throws Exception {
    /*given*/
    Member friend = generator.createMember();
    Friendship friendship =
        generator.createFriendship(
            testMember, friend
        );

    /*when*/
    Exception exception = assertThrows(
        CustomException.class, () -> friendshipService.deleteFriend(
            testMember.getId(), friend.getId()
        )
    );

    /*then*/
    assertEquals(
        Exceptions.NOT_FRIENDS_ALREADY.getMsg(),
        exception.getMessage()
    );
  }


  @Test
  @Transactional
  public void 친구_삭제() throws Exception {
    /*given*/
    Member friend = generator.createMember();
    Friendship friendship =
        generator.createFriendship(
            testMember, friend
        );
    friendshipService.updateFriendStatus(
        friend.getId(),
        friendship.getId(),
        FriendshipStatus.ACCEPT
    );

    /*when*/
    friendshipService.deleteFriend(
        testMember.getId(),
        friend.getId()
    );

    Optional<Friendship> result =
        friendshipRepository.findById(friendship.getId());

    /*then*/
    assertNull(result.orElse(null));
  }


  @Test
  @Transactional
  public void 친구목록_조회() throws Exception {
    /*given*/
    for (int i = 0; i < 100; i++) {
      if (generator.generateRandomBoolean()) {
        /*내가 친구 요청을 보낸 경우*/
        Friendship request = generator
            .createFriendship(testMember, generator.createMember());
        if (generator.generateRandomBoolean()) {
          /*친구가 된 경우*/
          request.updateStatus(FriendshipStatus.ACCEPT);
        }
      } else {
        /*내가 친구 요청을 받은 경우*/
        Friendship request = generator
            .createFriendship(generator.createMember(), testMember);
        if (generator.generateRandomBoolean()) {
          /*친구가 된 경우*/
          request.updateStatus(FriendshipStatus.ACCEPT);
        }
      }
    }

    /*when*/
    List<Friendship> expected = friendshipRepository
        .findFriendsByMemberId(testMember.getId());

    List<FriendDTO> result = friendshipService
        .getFriendList(testMember.getId()).getData();

    /*then*/
    assertEquals(expected.size(), result.size());
  }


  @Test
  @Transactional
  public void 친구_목록_조회() throws Exception {
    /*given*/
    long friends = 0;

    /*when*/
    for (int i = 0; i < 100; i++) {
      if (generator.generateRandomBoolean()) {
        /*요청을 받은 경우*/
        Long friendshipId = generator.createFriendship(
            generator.createMember(),
            testMember
        ).getId();
        if (generator.generateRandomBoolean()) {
          /*요청을 수락한 경우*/
          friendshipService.updateFriendStatus(
              testMember.getId(),
              friendshipId,
              FriendshipStatus.ACCEPT
          );
          friends++;
        } else {
          if (generator.generateRandomBoolean()) {
            /*응답하지 않은 경우*/
            continue;
          }
          /*요청을 거절한 경우*/
          friendshipService.updateFriendStatus(
              testMember.getId(),
              friendshipId,
              FriendshipStatus.REJECT
          );
        }
      } else {
        Member friend = generator.createMember();
        /*요청을 보낸 경우*/
        Long friendshipId = generator.createFriendship(
            testMember,
            friend
        ).getId();
        if (generator.generateRandomBoolean()) {
          /*요청을 수락한 경우*/
          friendshipService.updateFriendStatus(
              friend.getId(),
              friendshipId,
              FriendshipStatus.ACCEPT
          );
          friends++;
        } else {
          if (generator.generateRandomBoolean()) {
            /*응답하지 않은 경우*/
            continue;
          }
          /*요청을 거절한 경우*/
          friendshipService.updateFriendStatus(
              friend.getId(),
              friendshipId,
              FriendshipStatus.REJECT
          );
        }
      }
    }

    List<FriendDTO> result = friendshipService
        .getFriendList(testMember.getId())
        .getData();

    /*then*/
    assertEquals(friends, result.size());
  }


  @Test
  @Transactional
  public void 받은_친구_신청_중_대기_조회() throws Exception {
    /*given*/
    long pending = 0;

    /*when*/
    for (int i = 0; i < 100; i++) {
      if (generator.generateRandomBoolean()) {
        /*요청을 받은 경우*/
        Long friendshipId = generator.createFriendship(
            generator.createMember(),
            testMember
        ).getId();
        if (generator.generateRandomBoolean()) {
          /*요청을 수락한 경우*/
          friendshipService.updateFriendStatus(
              testMember.getId(),
              friendshipId,
              FriendshipStatus.ACCEPT
          );
        } else {
          if (generator.generateRandomBoolean()) {
            /*응답하지 않은 경우*/
            pending++;
          } else {
            /*요청을 거절한 경우*/
            friendshipService.updateFriendStatus(
                testMember.getId(),
                friendshipId,
                FriendshipStatus.REJECT
            );
          }
        }
      } else {
        Member friend = generator.createMember();
        /*요청을 보낸 경우*/
        Long friendshipId = generator.createFriendship(
            testMember,
            friend
        ).getId();
        if (generator.generateRandomBoolean()) {
          /*요청을 수락한 경우*/
          friendshipService.updateFriendStatus(
              friend.getId(),
              friendshipId,
              FriendshipStatus.ACCEPT
          );
        } else {
          if (generator.generateRandomBoolean()) {
            /*응답하지 않은 경우*/
            continue;
          }
          /*요청을 거절한 경우*/
          friendshipService.updateFriendStatus(
              friend.getId(),
              friendshipId,
              FriendshipStatus.REJECT
          );
        }
      }
    }

    List<PendingFriendRequestDTO> response = friendshipService
        .getReceivedFriendRequestList(testMember.getId()).getData();

    /*then*/
    assertEquals(pending, response.size());
  }
}