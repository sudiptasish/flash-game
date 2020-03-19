package com.ws.jg.common.exception;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class NoSuchPlayerException extends Exception {

    public NoSuchPlayerException() {
        super();
    }
    
    public NoSuchPlayerException(String msg) {
        super(msg);
    }
    
    public NoSuchPlayerException(Throwable th) {
        super(th);
    }
    
    public NoSuchPlayerException(String msg, Throwable th) {
        super(msg, th);
    }
}
