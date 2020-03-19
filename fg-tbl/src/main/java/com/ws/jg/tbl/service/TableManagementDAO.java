package com.ws.jg.tbl.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ws.jg.common.model.GameTable;
import com.ws.jg.common.model.TableStat;
import com.ws.jg.common.util.DatabaseUtil;
import com.ws.jg.common.util.Logger;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class TableManagementDAO {
    
    private static final String SELECT_ALL_QUERY = "SELECT ID, NAME, DESCRIPTION, MAX_CAPACITY, FROZEN, ACTIVE, STATUS, TYPE, CREATE_DATE FROM GAME_TABLES";
    private static final String SELECT_QUERY = "SELECT ID, NAME, DESCRIPTION, MAX_CAPACITY, FROZEN, ACTIVE, STATUS, TYPE, CREATE_DATE FROM GAME_TABLES WHERE ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO GAME_TABLES (ID, NAME, DESCRIPTION, MAX_CAPACITY, FROZEN, ACTIVE, STATUS, TYPE, CREATE_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM GAME_TABLES WHERE ID = ?";
    
    public TableManagementDAO() {}

    /**
     * Add this table to the underlying storage (DB).
     * @param table
     * @throws SQLException
     */
    public void addTable(GameTable table) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(INSERT_QUERY);
            pstmt.setString(1, table.getId());
            pstmt.setString(2, table.getName());
            pstmt.setString(3, table.getDescription());
            pstmt.setInt(4, table.getMaxCapacity());
            pstmt.setInt(5, table.isFrozen() ? 1 : 0);
            pstmt.setInt(6, table.isActive() ? 1 : 0);
            pstmt.setInt(7, table.getTableStat().ordinal());
            pstmt.setInt(8, table.getTableType());
            pstmt.setDate(9, table.getCreateDate());
            
            int count = pstmt.executeUpdate();
            conn.commit();
            Logger.log(String.format("Inserted %d row(s) to [%s] table", count, "GAME_TABLES"));            
        }
        catch (SQLException e) {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
            throw e;
        }
        finally {
            DatabaseUtil.closeStatement(pstmt);
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    /**
     * Return all the game tables.
     * @return List
     * @throws SQLException
     */
    public List<GameTable> getAllTables() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        List<GameTable> list = new ArrayList<>();
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(SELECT_ALL_QUERY);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(create(rs));
            }
            return list;
        }
        finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeStatement(pstmt);
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    /**
     * Return the game table as identified by this id.
     * @param tableId
     * @return GameTable
     */
    public GameTable getTable(String tableId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(SELECT_QUERY);
            pstmt.setString(1, tableId);
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Logger.log(String.format("Selected %d row(s) from [%s] table", 1, "GAME_TABLES"));                
                return create(rs);
            }
            return null;
        }
        finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeStatement(pstmt);
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    /**
     * Delete the game table as identified by this table id.
     * @param tableId
     */
    public void deleteTable(String tableId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(DELETE_QUERY);
            pstmt.setString(1, tableId);
            
            int count = pstmt.executeUpdate();
            conn.commit();
            Logger.log(String.format("Deleted %d row(s) from [%s] table", count, "GAME_TABLES"));
            
            if (count == 0) {
                throw new SQLException("No table found for id " + tableId);
            }
        }
        catch (SQLException e) {
            if (conn != null && !conn.isClosed()) {
                conn.rollback();
            }
            throw e;
        }
        finally {
            DatabaseUtil.closeStatement(pstmt);
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    private GameTable create(ResultSet rs) throws SQLException {
        GameTable table = new GameTable(
                rs.getString("ID")
                , rs.getString("NAME")
                , rs.getString("DESCRIPTION"));
        
        table.setMaxCapacity(rs.getInt("MAX_CAPACITY"));
        table.setTableStat(TableStat.values()[rs.getInt("STATUS")]);
        table.setCreateDate(rs.getDate("CREATE_DATE"));
        table.setTableType(rs.getInt("TYPE"));
        table.setActive(rs.getInt("ACTIVE") == 1);
        table.setFrozen(rs.getInt("FROZEN") == 1);
        
        return table;
    }
}
