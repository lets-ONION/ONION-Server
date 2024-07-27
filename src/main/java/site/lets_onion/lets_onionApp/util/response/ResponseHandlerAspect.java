package site.lets_onion.lets_onionApp.util.response;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseHandlerAspect {

  @Around("execution(* site.lets_onion.lets_onionApp.service.friendship.FriendshipService*(..))")
  public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = joinPoint.proceed();

    if (result instanceof ResponseDTO<?> responseDTO) {
      HttpStatus status = HttpStatus.valueOf(responseDTO.getCode());
      return new ResponseEntity<>(responseDTO, status);
    }
    return result;
  }
}
