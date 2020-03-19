package com.ws.jg.common.model;

import java.io.Serializable;

/**
 * 
 * @author Sudiptasish Chanda
 */
public interface Card extends Serializable {

    enum SUIT {CLUB, DIAMOND, HEART, SPADE};
    
    enum RANK {TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE};
    
    /**
     * Return the suite of the card.
     * @return SUITE
     */
    SUIT getSuit();
    
    /**
     * Return the rank of this card.
     * @return RANK
     */
    RANK getRank();
    
    /**
     * Return the integer representing the rank of the card.
     * @return int
     */
    int getRankValue();
}
