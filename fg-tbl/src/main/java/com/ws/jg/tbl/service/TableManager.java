package com.ws.jg.tbl.service;

import java.util.Collection;

import com.ws.jg.common.model.GameTable;

/**
 * 
 * @author Sudiptasish Chanda
 */
public abstract class TableManager {
    
    private static final TableManager _INSTANCE_ = new DefaultTableManager();

    protected TableManager() {}
    
    /**
     * Return the singleton factory instance.
     * @return TableFactory
     */
    public static TableManager get() {
        return _INSTANCE_;
    }
    
    /**
     * Create a new game table for new players.
     * The table will be identified by the table description object provided.
     * 
     * @param  table    Table description
     * @return GameTable
     */
    public abstract GameTable createNew(GameTable table);
    
    /**
     * Update the definition of the game table in this table manager. 
     * @param table
     */
    public abstract void update(GameTable table);
    
    /**
     * Return the number of game tables available with this table manager.
     * @return Collection
     */
    public abstract Collection<GameTable> list();
    
    /**
     * Find and return the game table as identified by this tableId.
     * @param tableId
     * @return GameTable
     */
    public abstract GameTable find(String tableId);
    
    /**
     * Check to see if this table manager already has the table created in it.
     * @param tableId
     * @return boolean
     */
    public abstract boolean contains(String tableId);
    
    /**
     * Remove the game table identified by this tableId from this table manager.
     * @param tableId
     */
    public abstract void remove(String tableId);
    
    /**
     * Flush this table manager.
     * It will cause all the tables deleted.
     */
    public abstract void clear();
}
