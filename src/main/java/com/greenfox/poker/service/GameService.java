package com.greenfox.poker.service;

import com.greenfox.poker.model.Game;
import com.greenfox.poker.model.GamePlayer;
import com.greenfox.poker.model.GameState;
import com.greenfox.poker.model.GamesList;
import com.greenfox.poker.model.ResponseType;
import com.greenfox.poker.model.StatusError;
import com.greenfox.poker.model.PokerUserDTO;
import com.greenfox.poker.repository.GameRepo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class GameService {

  GameRepo gameRepo;

  @Autowired
  ErrorMessageService errorMessageService;

  @Autowired
  UserService userService;

  @Autowired
  DtoService dtoService;

  @Autowired
  public GameService(GameRepo gameRepo) {
    this.gameRepo = gameRepo;
  }


  HashMap<Long, GameState> gameStateMap = new HashMap<>();


  public void createNewGame() {
    Game newGame = new Game();
    newGame.setGamestateId(newGame.getId());
    gameRepo.save(newGame);
    gameStateMap.put(newGame.getId(), new GameState(newGame.getId()));
  }

  public HashMap<Long, GameState> getGameStateMap() {
    return gameStateMap;
  }

  public void setGameStateMap(
          HashMap<Long, GameState> gameStateMap) {
    this.gameStateMap = gameStateMap;
  }

  public Game saveGame(Game game) {
    gameRepo.save(game);
    return game;
  }

  public void deleteGame(Game game) {
    gameRepo.delete(game);
  }

  public GamesList getAllGamesOrderedByBigBlind() {
    GamesList gamesList = new GamesList();
    gamesList.setGames(gameRepo.findAllByOrderByBigBlindDesc());
    return gamesList;
  }

  public boolean isGameExist(long id) {
    return (gameRepo.exists(id));
  }

  public Game getGameById(long id) {
    return gameRepo.findOne(id);
  }

  public GameState getGameState(long id) {
    return gameStateMap.get(id);
  }

  public ResponseEntity<?> getGameStateById(long id) {
    if (isGameExist(id)) {
      long currentStateId = getGameById(id).getGamestateId();
      return new ResponseEntity(getGameState(currentStateId), HttpStatus.OK);
    }
    return new ResponseEntity(new StatusError("fail", "game id doesn’t exist"),
            HttpStatus.NOT_FOUND);
  }

  public ResponseEntity<?> saveNewGame(Game game, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return new ResponseEntity(errorMessageService.respondToMissingParameters(bindingResult),
              HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity(saveGame(game), HttpStatus.OK);
  }

  public boolean isPlayerAlreadyInTheGame(long gameId, long userId){
    for (GamePlayer player : gameStateMap.get(gameId).getPlayers()){
      if (player.getId() == userId){
        return true;
      }
    }
    return false;
  }

  public ResponseType joinPlayerToGame(long playerId, long gameId, long chipsToPlayWith){
    PokerUserDTO player = dtoService.userDTOHashMap.get(playerId);
    String gameName = gameRepo.findOne(gameId).getName();
    String playerName = dtoService.userDTOHashMap.get(playerId).getUsername();
    GamePlayer newPlayer = new GamePlayer(chipsToPlayWith, player);
    dtoService.deductChipsFromAvailableChips(chipsToPlayWith, playerId);
    getGameState(gameId).getPlayers().add(newPlayer);
    return new StatusError("success", playerName + " joined game: " + gameName);
  }

  public int getTableBigBlind (long gameId){
    return gameRepo.findOne(gameId).getBigBlind();
  }
}
