package com.ws.jg.common.util;

import com.ws.jg.common.msg.ErrorMessage;
import com.ws.jg.common.msg.Message;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class MessageUtil {

    /**
     * Build the error message from this throwable object.
     * 
     * @param th
     * @return Message
     */
    public static Message buildErrorMessage(int retCode, Throwable th) {
        return buildErrorMessage(retCode, th.getMessage());
    }
    
    /**
     * 
     * @param retCode
     * @param msg
     * @return
     */
    public static Message buildErrorMessage(int retCode, String msg) {
        ErrorMessage errMsg = new ErrorMessage();
        errMsg.setErrorCode(retCode);
        errMsg.setErrorMessage(msg);
        
        return errMsg;
    }
}
