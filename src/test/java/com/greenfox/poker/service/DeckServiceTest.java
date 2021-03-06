package com.greenfox.poker.service;

import static org.junit.Assert.*;

import com.greenfox.poker.model.Card;
import com.greenfox.poker.model.Deck;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DeckServiceTest {

  private Deck testDeck;
  private List<Card> cardsInTestDeck;
  private List<String> validCardList;
  private DeckService deckService;


  @Before
  public void createTestDeckAndCardsList() {
    deckService = new DeckService();
    testDeck = deckService.getNewDeck();
    cardsInTestDeck = testDeck.getCards();
    validCardList = Arrays
            .asList("C2", "D2", "H2", "S2", "C3", "D3", "H3", "S3", "C4", "D4", "H4", "S4", "C5",
                    "D5", "H5", "S5", "C6", "D6", "H6", "S6", "C7", "D7", "H7", "S7", "C8", "D8",
                    "H8", "S8", "C9", "D9", "H9", "S9", "CT", "DT", "HT", "ST", "CJ", "DJ", "HJ",
                    "SJ", "CQ", "DQ", "HQ", "SQ", "CK", "DK", "HK", "SK", "CA", "DA", "HA", "SA"
            );
  }

  @After
  public void setDeckAndCardsListToNull() {
    testDeck = null;
    cardsInTestDeck = null;
  }

  @Test
  public void TestIfDeckContainsTheRightAmountOfCards() throws Exception {
    assertEquals(52, cardsInTestDeck.size());
  }

  @Test
  public void TestIfDeckContainsAllTheCards() throws Exception {
    List<String> validatedListOfCards = validCardList;
    List<String> actualListOfCards = new ArrayList<>();
    for (Card card : cardsInTestDeck) {
      actualListOfCards.add(card.toString());
    }
    java.util.Collections.sort(validatedListOfCards);
    java.util.Collections.sort(actualListOfCards);

    for (int i = 0; i < 52; i++) {
      assertEquals(validatedListOfCards.get(i), actualListOfCards.get(i));
    }
  }

  @Test
  public void TestShuffleMethod() throws Exception {
    DeckService deckService = new DeckService();
    Deck originalDeck = testDeck;
    deckService.shuffleDeck(testDeck);
    List<Card> originalCards = originalDeck.getCards();
    List<Card> shuffledCards = testDeck.getCards();
    List<String> originalCardsString = new ArrayList<>();
    List<String> shuffledCardsString = new ArrayList<>();
    for (Card card : originalCards) {
      originalCardsString.add(card.toString());
    }
    for (Card card : shuffledCards) {
      shuffledCardsString.add(card.toString());
    }
    java.util.Collections.sort(originalCardsString);
    java.util.Collections.sort(shuffledCardsString);
    for (int i = 0; i < 52; i++) {
      assertEquals(originalCardsString.get(i), shuffledCardsString.get(i));
    }
  }

  @Test
  public void TestDrawCardMethod() throws Exception {
    for (int i = 51; i != 0; i--) {
      String topCardInTheDeck = validCardList.get(i);
      Card drawnCard = deckService.drawCardFromDeck(testDeck);
      assertEquals(topCardInTheDeck, drawnCard.toString());
    }
  }
}
