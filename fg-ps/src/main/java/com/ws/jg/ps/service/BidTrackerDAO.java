package com.ws.jg.ps.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ws.jg.common.msg.GameMessage;
import com.ws.jg.common.util.DatabaseUtil;
import com.ws.jg.common.util.Logger;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class BidTrackerDAO {

    private static final String SELECT_QUERY = "SELECT GAME_ID, PLAYER_ID, BID_ORDER, BID_VALUE, BID_TIME, STATUS FROM GAME_BIDS WHERE GAME_ID = ? ORDER BY BID_TIME";
    private static final String INSERT_QUERY = "INSERT INTO GAME_BIDS (GAME_ID, PLAYER_ID, BID_ORDER, BID_VALUE, BID_TIME, STATUS) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE GAME_BIDS SET STATUS = ? WHERE BID_TIME = (SELECT MAX(BID_TIME) FROM GAME_BIDS WHERE GAME_ID = ?)";
    private static final String DELETE_QUERY = "DELETE FROM GAME_BIDS WHERE GAME_ID = ?";
    
    public BidTrackerDAO() {}
    
    /**
     * Add the list of messages to the underlying storage (DB).
     * @param msgs
     * @throws SQLException
     */
    public void insert(List<GameMessage> msgs) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(INSERT_QUERY);
            
            for (int i = 0; i < msgs.size(); i ++) {
                GameMessage msg = msgs.get(i);
                pstmt.setString(1, msg.getGameId());
                pstmt.setString(2, msg.getCurrentPlayer());
                pstmt.setInt(3, msg.getOrder());
                pstmt.setInt(4, msg.getBidValue());
                pstmt.setDate(5, new Date(msg.getTime().getTime()));
                pstmt.setInt(6, msg.getStatus());
                
                pstmt.addBatch();
            }            
            int[] count = pstmt.executeBatch();
            conn.commit();
            
            Logger.log(String.format("Inserted %d row(s) to [%s] table", count.length, "GAME_BIDS"));            
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
     * Add this new bid message to the underlying storage (DB).
     * @param msg
     * @throws SQLException
     */
    public void insert(GameMessage msg) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(INSERT_QUERY);
            pstmt.setString(1, msg.getGameId());
            pstmt.setString(2, msg.getCurrentPlayer());
            pstmt.setInt(3, msg.getOrder());
            pstmt.setInt(4, msg.getBidValue());
            pstmt.setDate(5, new Date(msg.getTime().getTime()));
            pstmt.setInt(6, msg.getStatus());
            
            int count = pstmt.executeUpdate();
            conn.commit();
            Logger.log(String.format("Inserted %d row(s) to [%s] table", count, "GAME_BIDS"));            
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
     * Return the list of bids placed for this game.
     * @param gameId
     * @return List
     */
    public List<GameMessage> select(String gameId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        List<GameMessage> bids = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(SELECT_QUERY);
            pstmt.setString(1, gameId);
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                bids.add(new GameMessage(rs.getString("GAME_ID")
                        , rs.getString("PLAYER_ID")
                        , rs.getInt("BID_ORDER")
                        , rs.getInt("BID_VALUE")
                        , rs.getDate("BID_TIME")
                        , rs.getInt("STATUS")));
            }
            Logger.log(String.format("Selected %d row(s) from [%s] table", bids.size(), "GAME_BIDS"));
            return bids;
        }
        finally {
            DatabaseUtil.closeResultSet(rs);
            DatabaseUtil.closeStatement(pstmt);
            DatabaseUtil.closeConnection(conn);
        }
    }
    
    /**
     * Delete all the bids for this game.
     * @param gameId
     */
    public void delete(String gameId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(DELETE_QUERY);
            pstmt.setString(1, gameId);
            
            int count = pstmt.executeUpdate();
            conn.commit();
            Logger.log(String.format("Deleted %d row(s) from [%s] table", count, "GAME_BIDS"));
            
            if (count == 0) {
                throw new SQLException("No msg details found for id " + gameId);
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
    
    /**
     * Update this bid msg details in the DB.
     * @param msg
     */
    public boolean update(GameMessage msg) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(UPDATE_QUERY);
            pstmt.setInt(1, msg.getStatus());
            pstmt.setString(2, msg.getGameId());
            
            int count = pstmt.executeUpdate();
            conn.commit();
            Logger.log(String.format("Updated %d row(s) from [%s] table", count, "GAME_BIDS"));
            
            if (count == 0) {
                return false;
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
        return true;
    }
}
