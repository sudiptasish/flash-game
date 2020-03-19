package com.ws.jg.common.model;

import java.util.Random;

import com.ws.jg.common.util.Logger;

/**
 * 
 * @author Sudiptasish Chanda
 */
public final class Deck {
    
    public static final int DECK_SIZE = 52;
    
    private final Card[] cards = new Card[DECK_SIZE];
    
    private Deck() {
        initialize();
    }
    
    /**
     * Return a new set of deck.
     * @return Deck
     */
    public static Deck newDeck() {
        return new Deck();
    }
    
    /**
     * Initialize this deck of cards.
     */
    private void initialize() {
        int i = 0;
        for (Card.SUIT suite : Card.SUIT.values()) {
            for (Card.RANK rank : Card.RANK.values()) {
                cards[i ++] = new PlayingCard(suite, rank);
            }
        }
    }
    
    /**
     * Shuffle this deck of cards.
     */
    public void shuffle() {
        Random r = new Random();
        int index = -1;
        for (int i = 0; i < cards.length; i ++) {
            index = r.nextInt(DECK_SIZE);
            swap(i, index);
        }
    }
    
    /**
     * Print this deck of cards.
     */
    public void print() {
        StringBuilder _buff = new StringBuilder();
        for (int i = 0; i < cards.length; i ++) {
            if (i == 0 || i % 13 == 0) {
                _buff.append("\n");
            }
            _buff.append(cards[i]).append(", ");
        }
        Logger.log(_buff.toString());
    }
    
    /**
     * Return the card at the specified index.
     * @param index
     * @return Card
     */
    public Card cardAt(int index) {
        return cards[index];
    }
    
    /**
     * Swap the cards between these two positions.
     * @param index_1
     * @param index_2
     */
    private void swap(int index_1, int index_2) {
        Card temp = cards[index_1];
        cards[index_1] = cards[index_2];
        cards[index_2] = temp;
    }
}
