package com.greenfox.poker.service;

import com.greenfox.poker.model.GamePlayer;
import com.greenfox.poker.model.LoginRequest;
import com.greenfox.poker.model.PokerUser;
import com.greenfox.poker.model.PokerUserDTO;
import com.greenfox.poker.model.ResponseType;
import com.greenfox.poker.model.UserTokenResponse;
import com.greenfox.poker.model.StatusError;
import com.greenfox.poker.repository.PokerUserRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

  private final static Logger logger = Logger.getLogger(TokenService.class.getName());

  PokerUserRepo pokerUserRepo;
  PokerUser pokerUser;
  TokenService tokenService;
  DtoService dtoService;

  public UserService(PokerUserRepo pokerUserRepo, PokerUser pokerUser,
          TokenService tokenService, DtoService dtoService) {
    this.pokerUserRepo = pokerUserRepo;
    this.pokerUser = pokerUser;
    this.tokenService = tokenService;
    this.dtoService = dtoService;
  }

  public ResponseType responseToSuccessfulRegister(PokerUser pokerUser) {
    pokerUserRepo.save(pokerUser);
    PokerUser pokerUserFromDatabase = pokerUserRepo.findByUsername(pokerUser.getUsername());
    String token = tokenService.generateToken(pokerUserFromDatabase);
    pokerUserFromDatabase.setToken(token);
    pokerUserRepo.save(pokerUserFromDatabase);
    dtoService.makePokerUserDTO(pokerUserFromDatabase);
    logger.log(Level.INFO, "successful registration for username: " + pokerUser.getUsername());
    return new UserTokenResponse("success", token, pokerUser.getId());
  }

  public ResponseType responseToSuccessfulLogin(LoginRequest loginRequest) {
    PokerUser pokerUserFromDatabase = pokerUserRepo.findByUsername(loginRequest.getUsername());
    String token = tokenService.generateToken(pokerUserFromDatabase);
    pokerUserFromDatabase.setToken(token);
    pokerUserRepo.save(pokerUserFromDatabase);
    dtoService.makePokerUserDTO(pokerUserFromDatabase);
    logger.log(Level.INFO, "successful login for username: " + pokerUser.getUsername());
    return new UserTokenResponse("success", token, pokerUserFromDatabase.getId());
  }

  public ResponseType registerWithOccupiedEmail() {
    logger.log(Level.WARNING, "registration error, email occupied already");
    return new StatusError("fail", "email address already exists");
  }

  public ResponseType registerWithOccupiedUsername() {
    logger.log(Level.WARNING, "registration error, username occupied already");
    return new StatusError("fail", "username already exists");
  }

  public boolean isLoginValid(LoginRequest loginRequest) {
    if (pokerUserRepo.existsByUsername(loginRequest.getUsername()) &&
            pokerUserRepo.existsByPassword(loginRequest.getPassword())) {
      return true;
    }
    return false;
  }

  public ResponseType loginWithInvalidUsernameOrPassword() {
    logger.log(Level.WARNING, "login error, invalid username or password");
    return new StatusError("fail", "invalid username or password");
  }

  public boolean isEmailOccupied(PokerUser pokerUser) {
    boolean isEmailOccupied = false;
    isEmailOccupied = pokerUserRepo.existsByEmail(pokerUser.getEmail());
    return isEmailOccupied;
  }

  public boolean isUsernameOccupied(PokerUser pokerUser) {
    boolean isUsernameOccupied = false;
    isUsernameOccupied = pokerUserRepo.existsByUsername(pokerUser.getUsername());
    return isUsernameOccupied;
  }

  public long getUserIdFromUsername(String username) {
    return pokerUserRepo.findByUsername(username).getId();
  }

  public boolean isUserExistsInDB(long id) {
    if (pokerUserRepo.exists(id)) {
      return true;
    } else {
      return false;
    }
  }

  public List<PokerUserDTO> getTopTenPlayersByChipsLeaderboard() {
    List<PokerUser> topTenList = pokerUserRepo.findTop10ByOrderByChipsDesc();
    List<PokerUserDTO> topTenDTO = new ArrayList<>();
    for (PokerUser user : topTenList) {
      topTenDTO.add(dtoService.makePokerUserDTO(user));
    }
    return topTenDTO;
  }

  public void updatePokerUserChipsInDBAfterEndOfGame(long chipsDifference, long playerId) {
    pokerUserRepo.findOne(playerId).setChips(chipsDifference);
  }
}
