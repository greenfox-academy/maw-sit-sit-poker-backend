package com.greenfox.poker.aspect;


import com.greenfox.poker.model.StatusError;
import com.greenfox.poker.repository.PokerUserRepo;
import com.greenfox.poker.service.DeckService;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@Aspect
public class AuthorizationAspect {

  private final static Logger logger = Logger.getLogger(DeckService.class.getName());

  @Autowired
  PokerUserRepo pokerUserRepo;


  @Pointcut("execution(* com.greenfox.poker.controller.*.*(..))" +
      "&& !@annotation(com.greenfox.poker.service.Accessible)")
  public void protectedEndPoints() {
  }

  @Around("protectedEndPoints()")
  public Object accessAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

    Object[] argsList = joinPoint.getArgs();
    MethodSignature signatures = (MethodSignature) joinPoint.getSignature();
    Method method = signatures.getMethod();
    String tokenFromHeader = accessToken(argsList, method);
    if (pokerUserRepo.existsByToken(tokenFromHeader)) {
      return joinPoint.proceed();
    }
    return new ResponseEntity<>(new StatusError("fail",
        "The provided authentication token is not valid."), HttpStatus.UNAUTHORIZED);
  }

  private String accessToken(Object[] argsList, Method method) {
    String tokenFromHeader = null;
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for (int argIndex = 0; argIndex < argsList.length; argIndex++) {
      for (Annotation annotation : parameterAnnotations[argIndex]) {
        if (annotation instanceof RequestHeader) {
          RequestHeader requestHeader = (RequestHeader) annotation;
          tokenFromHeader = (String) argsList[argIndex];
          logger.log(Level.INFO, "accessToken " + requestHeader + ", "+ argsList + ", "+ argIndex);
//          loggingService.getRequestHeaderAndItsValue(requestHeader, argsList, argIndex);
        }
      }
    }
    return tokenFromHeader;
  }
}
