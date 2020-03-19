package com.ws.jg.server.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.ws.jg.common.msg.Message;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class MessageEncoder implements Encoder.Text<Message> {
    
    private final ObjectMapper mapper = new ObjectMapper();

    /* (non-Javadoc)
     * @see javax.websocket.Encoder#init(javax.websocket.EndpointConfig)
     */
    @Override
    public void init(EndpointConfig arg0) {
        // Do Nothing
    }

    /* (non-Javadoc)
     * @see javax.websocket.Encoder.Text#encode(java.lang.Object)
     */
    @Override
    public String encode(Message msg) throws EncodeException {
        try {
            return mapper.writeValueAsString(msg);
        }
        catch (IOException e) {
            throw new EncodeException(msg, e.getMessage(), e);
        }
    }

    /* (non-Javadoc)
     * @see javax.websocket.Encoder#destroy()
     */
    @Override
    public void destroy() {
        // Do Nothing
    }
}
