package com.ws.jg.common.model;

/**
 * 
 * @author Sudiptasish Chanda
 */
public final class PlayingCard implements Card {
    
    private SUIT suit;
    private RANK rank;
    
    public PlayingCard() {}

    public PlayingCard(SUIT suit, RANK rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * @param suit the suite to set
     */
    public void setSuit(SUIT suit) {
        this.suit = suit;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(RANK rank) {
        this.rank = rank;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.core.Card#getSuite()
     */
    @Override
    public SUIT getSuit() {
        return suit;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.core.Card#getRank()
     */
    @Override
    public RANK getRank() {
        return rank;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "(" + suit + " " + rank + ")";
    }

    /* (non-Javadoc)
     * @see com.ws.jg.common.model.Card#getRankValue()
     */
    @Override
    public int getRankValue() {
        return rank.ordinal();
    }
}
