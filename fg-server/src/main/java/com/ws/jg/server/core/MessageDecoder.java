package com.ws.jg.server.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.ws.jg.common.msg.Message;
import com.ws.jg.common.util.Constants;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class MessageDecoder implements Decoder.Text<Message> {
    
    private final ObjectMapper mapper = new ObjectMapper();

    /* (non-Javadoc)
     * @see javax.websocket.Decoder#init(javax.websocket.EndpointConfig)
     */
    @Override
    public void init(EndpointConfig arg0) {
        // Do Nothing
    }

    /* (non-Javadoc)
     * @see javax.websocket.Decoder.Text#willDecode(java.lang.String)
     */
    @Override
    public boolean willDecode(String arg0) {
        return true;
    }

    /* (non-Javadoc)
     * @see javax.websocket.Decoder.Text#decode(java.lang.String)
     */
    @Override
    public Message decode(String msg) throws DecodeException {
        try {
            return (Message)mapper.readValue(msg, Constants.MSG_TYPE);
        }
        catch (IOException e) {
            throw new DecodeException(msg, e.getMessage(), e);
        }
    }

    /* (non-Javadoc)
     * @see javax.websocket.Decoder#destroy()
     */
    @Override
    public void destroy() {
        // Do Nothing
    }
}
