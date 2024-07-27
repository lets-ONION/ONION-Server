package site.lets_onion.lets_onionApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.lets_onion.lets_onionApp.domain.friendship.FriendshipStatus;
import site.lets_onion.lets_onionApp.dto.friendship.FriendDTO;
import site.lets_onion.lets_onionApp.dto.friendship.FriendshipDTO;
import site.lets_onion.lets_onionApp.dto.friendship.PendingFriendRequestDTO;
import site.lets_onion.lets_onionApp.service.friendship.FriendshipService;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendshipController {

  private final FriendshipService friendshipService;
  private final JwtProvider jwtProvider;


  @PostMapping("/request/create")
  @Operation(summary = "친구 요청", description = """
      친구 요청을 보내는 API입니다.<br>
      **target_id**는 친구 요청을 보낼 유저의 ID입니다.
      """)
  @ApiResponse(responseCode = "200")
  public ResponseEntity<ResponseDTO<FriendshipDTO>> requestFriendship(
      HttpServletRequest request,
      @RequestParam(name = "target_id") Long targetId
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    ResponseDTO<FriendshipDTO> response = friendshipService.sendFriendRequest(
        memberId, targetId);

    return ResponseEntity.ok(response);
  }


  @PatchMapping("/request/response")
  @Operation(summary = "친구 요청 응답", description = """
      친구 요청에 응답하는 API입니다.<br><br>
      **request_id**는 요청의 ID입니다.<br>
      **response**는 응답의 유형입니다.<br><br>
      응답은 boolean 타입이며, 다음과 같습니다..<br>
      **ACCEPT** : 수락<br>
      **REJECT** : 거절<br><br>
      """)
  @ApiResponse(responseCode = "200")
  public ResponseEntity<ResponseDTO<Boolean>> requestFriendshipResponse(
      HttpServletRequest request,
      @RequestParam(name = "target_id") Long targetId,
      @Parameter(schema = @Schema(allowableValues = {"ACCEPT", "REJECT"}))
      @RequestParam(name = "response") FriendshipStatus status
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    ResponseDTO<Boolean> response = friendshipService.updateFriendStatus(
        memberId, targetId, status
    );
    return ResponseEntity.ok(response);
  }


  @DeleteMapping("/delete")
  @Operation(summary = "친구 삭제", description = """
      친구를 목록에서 삭제하는 API입니다.<br>
      **friend_id**는 삭제할 친구의 ID입니다.
      """)
  @ApiResponse(responseCode = "200")
  public ResponseEntity<ResponseDTO<Boolean>> deleteFriendship(
      HttpServletRequest request,
      @RequestParam(name = "friend_id") Long friendId
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    ResponseDTO<Boolean> response = friendshipService.deleteFriend(
        memberId, friendId
    );

    return ResponseEntity.ok(response);
  }


  @GetMapping("/list/get")
  @Operation(summary = "친구 목록 조회", description = """
      친구 목록을 조회하는 API입니다.
      """)
  @ApiResponse(responseCode = "200")
  public ResponseEntity<ResponseDTO<List<FriendDTO>>> getFriendList(
      HttpServletRequest request
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    ResponseDTO<List<FriendDTO>> response =
        friendshipService.getFriendList(memberId);

    return ResponseEntity.ok(response);
  }


  @GetMapping("/request/get")
  @Operation(summary = "받은 친구 신청 목록 조회", description = """
      다른 유저에게서 받은 친구 요청 중<br>
      현재 대기 상태에 있는 목록을 조회합니다.<br>
      /request/response 를 통해 응답할 수 있습니다. 
      """)
  @ApiResponse(responseCode = "200")
  public ResponseEntity<ResponseDTO<List<PendingFriendRequestDTO>>> getRequestList(
      HttpServletRequest request
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    ResponseDTO<List<PendingFriendRequestDTO>> response =
        friendshipService.getReceivedFriendRequestList(memberId);

    return ResponseEntity.ok(response);
  }
}
