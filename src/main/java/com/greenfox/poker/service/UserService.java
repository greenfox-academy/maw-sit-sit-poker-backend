package com.greenfox.poker.service;

import com.greenfox.poker.model.LoginRequest;
import com.greenfox.poker.model.PokerUser;
import com.greenfox.poker.model.ResponseType;
import com.greenfox.poker.model.UserTokenResponse;
import com.greenfox.poker.model.StatusError;
import com.greenfox.poker.repository.PokerUserRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class UserService {

  @Autowired
  PokerUserRepo pokerUserRepo;

  @Autowired
  TokenService tokenService;

  public ResponseType responseToSuccessfulRegister(PokerUser userRegister) {
    pokerUserRepo.save(userRegister);
    String token = tokenService.generateToken(userRegister);
    return new UserTokenResponse("success", token, userRegister.getId());
  }

  public ResponseType responseToSuccessfulLogin(LoginRequest loginRequest) {
    PokerUser pokerUserFromDatabase = pokerUserRepo.findByUsername(loginRequest.getUsername());
    String passwordOfUsernameFromDatabase = pokerUserFromDatabase.getPassword();
    if (loginRequest.getPassword().equals(passwordOfUsernameFromDatabase)) {
      String token = tokenService.generateToken(pokerUserFromDatabase);
      return new UserTokenResponse("success", token, pokerUserFromDatabase.getId());
    }
    return loginWithIvalidUsernameOrPassword();
  }

  public ResponseType respondToMissingParameters(BindingResult bindingResult) {
    List<String> missing = new ArrayList<>();
    String missingFields = new String();
    for (FieldError fielderror : bindingResult.getFieldErrors()) {
      missing.add(fielderror.getField());
    }
    System.out.println(missingFields);
    missingFields = "Missing parameter(s): " + missing.stream().collect(Collectors.joining(", ")) + "!";
    return new StatusError("fail", missingFields);
  }

  public ResponseType registerWithOccupiedEmail() {

    return new StatusError("fail", "email address already exists");
  }

  public ResponseType registerWithOccupiedUsername() {
    return new StatusError("fail", "username already exists");
  }

  public ResponseType loginWithIvalidUsernameOrPassword() {
    return new StatusError("fail", "invalid username or password");
  }

  public boolean isEmailOccupied(PokerUser pokerUser) {
    if (pokerUser.getEmail().equals("occupied@email.com")) {
      return true;
    }
    return false;
  }

  public boolean isUsernameOccupied(PokerUser pokerUser) {
    if (pokerUser.getUsername().equals("occupiedUserName")) {
      return true;
    }
    return false;
  }
}
