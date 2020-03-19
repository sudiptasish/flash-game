package com.ws.jg.common.msg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ws.jg.common.model.Card;
import com.ws.jg.common.model.Player;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class GameMessage implements Message {
    
    // Type of the message (ON_BOARD, START, BID, etc)
    private G_TYPE type;
    
    // The player id, whosoever has just sent this message.
    private String currentPlayer = "";
    
    // List of other players with their chair assignment details.
    private List<Player> players = new ArrayList<>();
    
    // Unique game id, for which this message has been generated.
    private String gameId;
    
    // Following 4 messages are used for placing any bid.
    private int bidValue;
    private int order;
    private Date time;
    private int status = 0; // 0:IN_GAME, 1: PACK, 2: SHOW
    
    // Error message (if any)
    private String error = "";
    
    // Optional chat message.
    private String chat = "";
    
    private Card[] cards;
    
    // Whenever a player is exited, the message that would be sent from
    // server will have the chair that needs to be freed.
    private int exitChairId = -1;
    
    private int winningChair = -1;
    
    // This attribute will be populated once the game is completed, so
    // that post declaration of winner, players can see others hand.
    private Card[][] allCards;
    
    public GameMessage() {}
    
    // Constructor used for Bid Message.
    public GameMessage(String gameId
            , String currentPlayer
            , int order
            , int bidValue
            , Date time
            , int status) {
        
        this.gameId = gameId;
        this.currentPlayer = currentPlayer;
        this.order = order;
        this.bidValue = bidValue;
        this.time = time;
        this.status = status;
    }

    /**
     * @return the currentPlayer
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Add the new player to the connected list.
     * @param player
     */
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    /**
     * @param players the players to set
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * @return the players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @param gameId the gameId to set
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * @param type the type to set
     */
    public void setType(G_TYPE type) {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.common.msg.Message#getType()
     */
    @Override
    public G_TYPE getType() {
        return type;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.common.msg.Message#getGameId()
     */
    @Override
    public String getGameId() {
        return gameId;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.common.msg.Message#setCurrentPlayer(java.lang.String)
     */
    @Override
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * @return the chat
     */
    public String getChat() {
        return chat;
    }

    /**
     * @param chat the chat to set
     */
    public void setChat(String chat) {
        this.chat = chat;
    }

    /**
     * @return the bidValue
     */
    public int getBidValue() {
        return bidValue;
    }

    /**
     * @param bidValue the bidValue to set
     */
    public void setBidValue(int bidValue) {
        this.bidValue = bidValue;
    }

    /**
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return the cards
     */
    public Card[] getCards() {
        return cards;
    }

    /**
     * @param cards the cards to set
     */
    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    /**
     * @return the exitChairId
     */
    public int getExitChairId() {
        return exitChairId;
    }

    /**
     * @param exitChairId the exitChairId to set
     */
    public void setExitChairId(int exitChairId) {
        this.exitChairId = exitChairId;
    }

    /**
     * @return the winningChair
     */
    public int getWinningChair() {
        return winningChair;
    }

    /**
     * @param winningChair the winningChair to set
     */
    public void setWinningChair(int winningChair) {
        this.winningChair = winningChair;
    }

    /**
     * @return the allCards
     */
    public Card[][] getAllCards() {
        return allCards;
    }

    /**
     * @param allCards the allCards to set
     */
    public void setAllCards(Card[][] allCards) {
        this.allCards = allCards;
    }
}
