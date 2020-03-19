package com.ws.jg.common.model;

import java.io.Serializable;

import com.ws.jg.common.model.Card.RANK;
import com.ws.jg.common.model.Card.SUIT;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class GameDeck implements Serializable {

    private String gameId;
    private String playerId;
    private Card.SUIT suit;
    private Card.RANK rank;
    
    public GameDeck() {}

    public GameDeck(String gameId, String playerId, SUIT suit, RANK rank) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * @return the gameId
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * @param gameId the gameId to set
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * @return the playerId
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * @param playerId the playerId to set
     */
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    /**
     * @return the suit
     */
    public Card.SUIT getSuit() {
        return suit;
    }

    /**
     * @param suit the suit to set
     */
    public void setSuit(Card.SUIT suit) {
        this.suit = suit;
    }

    /**
     * @return the rank
     */
    public Card.RANK getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(Card.RANK rank) {
        this.rank = rank;
    }
}
