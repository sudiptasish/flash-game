package com.ws.jg.common.exception;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class ResourceExistException extends Exception {

    public ResourceExistException() {
        super();
    }
    
    public ResourceExistException(String msg) {
        super(msg);
    }
    
    public ResourceExistException(Throwable th) {
        super(th);
    }
    
    public ResourceExistException(String msg, Throwable th) {
        super(msg, th);
    }
}
