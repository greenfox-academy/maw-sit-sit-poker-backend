package com.greenfox.poker.aspect;


import com.greenfox.poker.model.StatusError;
import com.greenfox.poker.service.UserService;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@Aspect
public class AuthorizationAspect {

  @Autowired
  UserService userService;

  @Around("execution(* com.greenfox.poker.controller.UserController.*(..))" +
      "&& !@annotation(com.greenfox.poker.service.Accessible)")
  public Object accessAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

    Object[] argsList = joinPoint.getArgs();
    MethodSignature signatures = (MethodSignature) joinPoint.getSignature();
    Method method = signatures.getMethod();
    String token = accessToken(argsList, method);
    if (token.equals(userService.getToken())) {
      return joinPoint.proceed();
    }
    return new ResponseEntity<>(new StatusError("fail",
        "The provided authentication token is not valid."), HttpStatus.UNAUTHORIZED);
  }

  private String accessToken(Object[] argsList, Method method) {
    String token = null;
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    for (int argIndex = 0; argIndex < argsList.length; argIndex++) {
      for (Annotation annotation : parameterAnnotations[argIndex]) {
        if (annotation instanceof RequestHeader) {
          RequestHeader requestHeader = (RequestHeader) annotation;
          token = (String) argsList[argIndex];
          System.out.println(requestHeader.value() + " = " + argsList[argIndex]);
        }
      }
    }
    return token;
  }
}
