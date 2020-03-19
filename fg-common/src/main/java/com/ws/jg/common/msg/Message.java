package com.ws.jg.common.msg;

/**
 * 
 * @author Sudiptasish Chanda
 */
public interface Message {
    
    /**
     * ON_BOARD: Client browser will send a notification message to server whenever
     *           a new player joins the same table. Server will in turn send
     *           a ON_BOARD message to all other players on board.
     *           
     * START   : Once a table is full, browser will automatically send START
     *           message to the server, indicating the game should be started.
     *           Optionally, a player can explicitly start a game after taking
     *           the consent of others. It is possible (in the later case), the
     *           table may not be full.
     *           Whenever server will receive such message, it will send the same
     *           START message to other members on board.
     *           
     * BID     : Post the game is started, whenever any of the players places a
     *           bid, browser will send a BID message, which will have the
     *           details of the bid.
     *           
     * PACK    : Whenever a player decides to quit, (s)he can do so by issuing a
     *           PACK command. The moment (s)he does so, a PACK message will be sent
     *           to the server.
     *           
     * SHOW    : Whenever a player decides to show, (s)he can do so by issuing a
     *           PACK command. The moment (s)he does so, a SHOW message will be sent
     *           to the server.
     *           
     * COMPLETE: Once the game is complete, browser will send a COMPLETE message
     *           to server.
     *           
     * EXIT    : Whenever a player is disconnected or exited gracefully from the game
     *           the server will send an EXIT message to remaining players on board.
     *           
     * ERROR   : Whenever any exception is thrown at server side, an error message
     *           will be sent to all clients.
     *           
     * CHAT    : A CHAT message will be sent whenever player wants to send a message
     *           to other members on board.
     */
    enum G_TYPE {ON_BOARD, START, BID, PACK, SHOW, COMPLETE, EXIT, ERROR, CHAT};
    
    /**
     * Return the message type.
     * @return G_TYPE
     */
    G_TYPE getType();
    
    /**
     * The game id, this message belongs to.
     * @return String
     */
    String getGameId();
    
    /**
     * Set the current player whosoever sent this message.
     * @param currentPlayer
     */
    void setCurrentPlayer(String currentPlayer);
    
}
