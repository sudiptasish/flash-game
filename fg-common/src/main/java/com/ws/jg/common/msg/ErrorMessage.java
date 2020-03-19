package com.ws.jg.common.msg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 * @author Sudiptasish Chanda
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class ErrorMessage implements Message {
    
    private int errorCode = -1;
    private String errorMessage = "";
    
    public ErrorMessage() {}

    public ErrorMessage(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.common.msg.Message#getType()
     */
    @Override
    public G_TYPE getType() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.common.msg.Message#getGameId()
     */
    @Override
    public String getGameId() {
        return "";
    }

    /* (non-Javadoc)
     * @see com.ws.jg.common.msg.Message#setCurrentPlayer(java.lang.String)
     */
    @Override
    public void setCurrentPlayer(String playerId) {
        
    }
}
