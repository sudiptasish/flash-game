package com.ws.jg.common.exception;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class NoSuchGameException extends Exception {

    public NoSuchGameException() {
        super();
    }
    
    public NoSuchGameException(String msg) {
        super(msg);
    }
    
    public NoSuchGameException(Throwable th) {
        super(th);
    }
    
    public NoSuchGameException(String msg, Throwable th) {
        super(msg, th);
    }
}
