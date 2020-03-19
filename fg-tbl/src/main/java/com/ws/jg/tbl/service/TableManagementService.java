package com.ws.jg.tbl.service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;

import com.ws.jg.common.exception.NoSuchTableException;
import com.ws.jg.common.exception.ResourceExistException;
import com.ws.jg.common.model.GameTable;
import com.ws.jg.common.service.AbstractService;
import com.ws.jg.common.util.Logger;
import com.ws.jg.common.util.UUIDGenerator;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class TableManagementService extends AbstractService {
    
    // Table Manager object that handles allocation/creation of tables.
    private static final TableManager tblManager = TableManager.get();
    
    private final TableManagementDAO tableDao = new TableManagementDAO();

    /**
     * Return the list of tables available/created with the table mgmt service.
     * The list includes the idle table as well as the active tables.
     * 
     * @return Collection
     */
    public Collection<GameTable> getAllTables(boolean transactional) {
        Collection<GameTable> tables = tblManager.list();
        if (tables.isEmpty() && transactional) {
            // Populate the table manager.
            try {
                tables = tableDao.getAllTables();
                for (GameTable table : tables) {
                    tblManager.update(table);
                }
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return tables;
    }
    
    /**
     * Create the new game table with the id as tableId and name as tableName.
     * @param table
     * @return GameTable
     * @throws ResourceExistException 
     */
    public GameTable createNew(GameTable table, boolean transactional) throws ResourceExistException {
        if (table.getId() == null) {
            table.setId(UUIDGenerator.generateUnique());
        }
        if (tblManager.contains(table.getId())) {
            throw new ResourceExistException(
                    String.format("Table %s already exists", table.getId()));
        }
        if (table.getCreateDate() == null) {
            table.setCreateDate(new Date(System.currentTimeMillis()));
        }
        // Add this table to the table manager.
        GameTable newTable = tblManager.createNew(table);
        
        // If the transactional attribute is set, then add the table
        // to the underlying storage (DB).
        if (transactional) {
            try {
                tableDao.addTable(newTable);
            }
            catch (SQLException e) {
                tblManager.remove(newTable.getId());
                
                Logger.log(e.getMessage());
                if (e.getErrorCode() == 1) {
                    throw new ResourceExistException(
                            String.format("Table %s already exists", newTable.getId()));
                }
                else {
                    throw new RuntimeException(e);
                }
            }
        }
        return newTable;
    }
    
    /**
     * Return the game table as identified by thi stableId.
     * If no such table found, then this api will throw an error.
     * 
     * @param tableId
     * @return GameTable
     * @throws NoSuchTableException
     */
    public GameTable getTable(String tableId, boolean transactional) throws NoSuchTableException {
        GameTable table = tblManager.find(tableId);
        if (table == null) {
            if (transactional) {
                try {
                    table = tableDao.getTable(tableId);
                    if (table != null) {
                        tblManager.update(table);
                    }
                }
                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (table == null) {
                throw new NoSuchTableException(
                        String.format("No such game table found for id [%s]", tableId));
            }
        }
        return table;
    }
    
    /**
     * Update the table in the underlying table store.
     * @param table
     */
    public void updateTable(GameTable table, boolean transactional) {
        tblManager.update(table);
    }
    
    /**
     * Remove the table from the underlying table store.
     * @param tableId
     */
    public void removeTable(String tableId, boolean transactional) {
        tblManager.remove(tableId);
        
        // If the transactional attribute is set, then remove the
        // table from the underlying storage (DB) as well.
        if (transactional) {
            try {
                tableDao.deleteTable(tableId);
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
