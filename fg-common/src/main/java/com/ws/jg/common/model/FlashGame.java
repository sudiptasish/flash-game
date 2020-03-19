package com.ws.jg.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Sudiptasish Chanda
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class FlashGame implements Serializable {

    private String id;
    private String description;
    private String tableId;
    private int playerCount = 0;
    private Date startTime;
    private int status;     // 0: ACTIVE, 1: COMPLETE. 2: NOT_CONCLUDED
    private Date endTime;
    
    private List<GameDeck> cards = new ArrayList<>();
    
    public FlashGame() {}

    public FlashGame(String id, String description, String tableId,
            int playerCount, Date startTime, int status, Date endTime) {
        
        this.id = id;
        this.description = description;
        this.tableId = tableId;
        this.playerCount = playerCount;
        this.startTime = startTime;
        this.status = status;
        this.endTime = endTime;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the tableId
     */
    public String getTableId() {
        return tableId;
    }

    /**
     * @param tableId the tableId to set
     */
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    /**
     * @return the playerCount
     */
    public int getPlayerCount() {
        return playerCount;
    }

    /**
     * @param playerCount the playerCount to set
     */
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the cards
     */
    public List<GameDeck> getCards() {
        return cards;
    }

    /**
     * @param cards the cards to set
     */
    public void setCards(List<GameDeck> cards) {
        this.cards = cards;
    }
}
