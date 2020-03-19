package com.ws.jg.common.model;

/**
 * 
 * @author Sudiptasish Chanda
 */
public enum TableStat {

    WAITING_FOR_PLAYERS ("Waiting for players"),
    
    IN_GAME ("Game started");
    
    private final String description;
    
    /**
     * 
     */
    TableStat(String description) {
        this.description = description;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
