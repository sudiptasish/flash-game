package com.ws.jg.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * @author Sudiptasish Chanda
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class GameTable implements Serializable {
    
    // Unique description of this table.
    public static final int MAX_PLAYER = 2;
    
    private String id;
    private String name;
    private String description;
    private TableStat tableStat = TableStat.WAITING_FOR_PLAYERS;    // 0: WAITING_FOR_PLAYERS, 1: IN_GAME.
    private int maxCapacity = MAX_PLAYER;
    private int tableType = 1;       // 0: Temporary, 1: Permanent. 
    private Date createDate;
    
    private boolean frozen = false;
    private boolean active = true;
    
    // Indicates if this table is currently part of any active game or not.
    private String gameId;
        
    // Number of available chairs for this game table.
    private Chair[] chairs;
    
    public GameTable() {}
    
    public GameTable(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.chairs = new Chair[MAX_PLAYER];
        for (int i = 0; i < chairs.length; i ++) {
            chairs[i] = new Chair(i);
        }
    }
    
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the maxCapacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * @param maxCapacity the maxCapacity to set
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * @return the tableStat
     */
    public TableStat getTableStat() {
        return tableStat;
    }

    /**
     * @param tableStat the tableStat to set
     */
    public void setTableStat(TableStat tableStat) {
        this.tableStat = tableStat;
    }
        
    /**
     * Get the description of the specific table status.
     * @return String
     */
    public String getStatusDesc() {
        return tableStat.getDescription();
    }

    /**
     * @return the tableType
     */
    public int getTableType() {
        return tableType;
    }

    /**
     * @param tableType the tableType to set
     */
    public void setTableType(int tableType) {
        this.tableType = tableType;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    /**
     * Check to see if this table is currently active.
     * Active means, the game has been started for this table.
     * @return boolean
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Check to see if this table is frozen.
     * @return boolean
     */
    public boolean isFrozen() {
        return this.frozen;
    }
    
    /**
     * @param frozen the frozen to set
     */
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * @return the chairs
     */
    public Chair[] getChairs() {
        return chairs;
    }

    /**
     * @return the gameId
     */
    public String getCurrentGameId() {
        return gameId;
    }

    /**
     * @param gameId the gameId to set
     */
    public void setCurrentGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * Destroy a game table.
     * Normally this API will be called, after all players exited from the game.
     */
    public void destroy() {
        // Not implemented
    }
    
    //////////////////////////////////////////////////////
    //////// Operation related API(s) ////////////////////
    //////////////////////////////////////////////////////

    /**
     * Add a new player to this table.
     * @param player
     */
    public void join(Player player) {
        if (player.getChairId() == -1) {
            // On-board this new player (assign an available chair)
            assignChair(player);
        }
    }
    
    /**
     * Assign a chair to this new player.
     * @param player
     */
    private void assignChair(Player player) {
        if (chairs == null) {
            synchronized(this) {
                if (chairs == null) {
                    chairs = new Chair[getMaxCapacity()];
                    for (int i = 0; i < chairs.length; i ++) {
                        chairs[i] = new Chair(i);
                    }
                }
            }
        }
        for (Chair chair : chairs) {
            if (chair.isEmpty()) {
                player.setChairId(chair.getId());
                chair.setPlayerId(player.getId());
                break;
            }
        }
    }
    
    /**
     * This API will be invoked whenever a player exits from the
     * table.
     * @param player
     */
    public void exit(Player player) {
        for (Chair chair : chairs) {
            if (chair.getPlayerId() != null && chair.getPlayerId().equals(player.getId())) {
                player.setChairId(-1);
                chair.setPlayerId(null);
                break;
            }
        }
    }
    
    /**
     * Return the total number of active players in this table.
     * @return int
     */
    public int getPlayerCount() {
        if (chairs == null) {
            return 0;
        }
        int count = 0;
        for (Chair chair : chairs) {
            if (!chair.isEmpty()) {
                count ++;
            }
        }
        return count;
    }

    /**
     * Remove all player(s) from this table.
     */
    public void removeAll() {
        // Removal of player simply means making all the chairs as empty
        for (Chair chair : chairs) {
            chair.setPlayerId(null);
        }
    }
}
