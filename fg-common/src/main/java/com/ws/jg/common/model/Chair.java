package com.ws.jg.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;


/**
 * 
 * @author Sudiptasish Chanda
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Chair implements Serializable {

    private int id;
    private String playerId;
    
    public Chair() {}
    
    public Chair(int id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the player
     */
    public String getPlayerId() {
        return playerId;
    }

    /**
     * @param player the player to set
     */
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
    
    /**
     * Check to see if this chair is empty.
     * @return
     */
    public boolean isEmpty() {
        return playerId == null;
    }
}
