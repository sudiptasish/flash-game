package com.ws.jg.server.exec;

import com.ws.jg.common.msg.Message;
import com.ws.jg.server.session.PlayingSession;

/**
 * 
 * @author Sudiptasish Chanda
 */
public final class Packet {

    private PlayingSession gmSession;
    private Message message;
    
    private Throwable error;
    
    public Packet() {}

    public Packet(PlayingSession gmSession, Message message) {
        this.gmSession = gmSession;
        this.message = message;
    }

    public Packet(PlayingSession gmSession, Throwable error) {
        this.gmSession = gmSession;
        this.error = error;
    }

    /**
     * @return the gmSession
     */
    public final PlayingSession getGmSession() {
        return gmSession;
    }

    /**
     * @param gmSession the gmSession to set
     */
    public void setGmSession(PlayingSession gmSession) {
        this.gmSession = gmSession;
    }

    /**
     * @return the message
     */
    public final Message getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * @return the error
     */
    public Throwable getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(Throwable error) {
        this.error = error;
    }
}
