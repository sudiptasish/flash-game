package com.ws.jg.common.util;

import java.util.UUID;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class UUIDGenerator {

    /**
     * Generate a unique UUID.
     * @return String
     */
    public static String generateUnique() {
        String uuId = UUID.randomUUID().toString();
        uuId = uuId.replaceAll("-", "");
        
        return uuId;
    }
}
