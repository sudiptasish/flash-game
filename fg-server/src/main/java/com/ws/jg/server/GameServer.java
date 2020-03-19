package com.ws.jg.server;

import javax.websocket.Session;

import com.ws.jg.common.msg.Message;

/**
 * 
 * @author Sudiptasish Chanda
 */
public interface GameServer {

    /**
     * This API will be invoked whenever a new player arrives
     * and join the selected table.
     * 
     * @param session
     */
    void onBoard(String tableId, Session session);
    
    /**
     * This API will be invoked whenever a player on the board
     * sends a message/or put a bid.
     * 
     * @param message
     * @param session
     */
    void receive(String tableId, Message message, Session session);
    
    /**
     * This API will be invoked whenever a player exits from the
     * table.
     * @param session
     */
    void exit(String tableId, Session session);
    
    /**
     * This API will be invoked if an exception occurred in the
     * client connectivity, or whule sending/receiving any message/bid.
     * 
     * @param error
     * @param session
     */
    void error(String tableId, Throwable error, Session session);    
    
}
