package com.greenfox.poker.model;


import java.util.List;

public class GameState {

  private Game game;
  private List<PokerUserDTO> players;
  private long actorPlayerId;
  private long dealerPlayerId;
  private String round;
  private List<Card> cardsOnTable;
  private int pot;

  public GameState() {
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public List<PokerUserDTO> getPlayers() {
    return players;
  }

  public void setPlayers(List<PokerUserDTO> players) {
    this.players = players;
  }

  public long getActorPlayerId() {
    return actorPlayerId;
  }

  public void setActorPlayerId(long actorPlayerId) {
    this.actorPlayerId = actorPlayerId;
  }

  public long getDealerPlayerId() {
    return dealerPlayerId;
  }

  public void setDealerPlayerId(long dealerPlayerId) {
    this.dealerPlayerId = dealerPlayerId;
  }

  public String getRound() {
    return round;
  }

  public void setRound(Round round) {
    this.round = round.toString();
  }

  public List<Card> getCardsOnTable() {
    return cardsOnTable;
  }

  public void setCardsOnTable(List<Card> cardsOnTable) {
    this.cardsOnTable = cardsOnTable;
  }

  public int getPot() {
    return pot;
  }

  public void setPot(int pot) {
    this.pot = pot;
  }
}