package com.ws.jg.common.exception;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class NoSuchTableException extends Exception {

    public NoSuchTableException() {
        super();
    }
    
    public NoSuchTableException(String msg) {
        super(msg);
    }
    
    public NoSuchTableException(Throwable th) {
        super(th);
    }
    
    public NoSuchTableException(String msg, Throwable th) {
        super(msg, th);
    }
}
