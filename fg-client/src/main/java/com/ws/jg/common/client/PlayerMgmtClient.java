package com.ws.jg.common.client;

import java.util.List;

import javax.websocket.Session;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.ws.jg.common.model.Player;
import com.ws.jg.common.msg.ErrorMessage;
import com.ws.jg.common.msg.Message;
import com.ws.jg.common.util.Constants;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class PlayerMgmtClient {
    
    private static final PlayerMgmtClient PLAYER_MGMT_CLIENT = new PlayerMgmtClient();
    
    private PlayerMgmtClient() {}
    
    /**
     * Return the singleton player management client.
     * @return PlayerMgmtClient
     */
    public static PlayerMgmtClient get() {
        return PLAYER_MGMT_CLIENT;
    }
    
    /**
     * Find the information about the current player which is associated
     * with this session.
     * Typically, the request parameter should have the player id in it.
     * This API will extract the playerId from the request parameter
     * and try finding the player identified by this id.
     * 
     * @param session
     * @return Player
     */
    public Player findThisPlayer(Session session) {
        List<String> list = session.getRequestParameterMap().get(Constants.QUERY_PARAM_PLAYER_ID);
        if (list == null || list.size() != 1) {
            throw new IllegalArgumentException("Player Id missing from request parameter");
        }
        return getPlayer(list.get(0));
    }
    
    /**
     * Create a new player.
     * @param player
     */
    public Player createPlayer(Player player) {
        RESTClient wsClient = new RESTClient("PlayerManagement", "1.0", Constants.PLAYER_MGMT_SERVICE_URL);
        
        RESTClient.RESTClientParam wsParam = new RESTClient.RESTClientParam();
        wsParam.addRequestHeader("Authorization", "");      // TODO
        wsParam.addSupportedMediaType(MediaType.APPLICATION_JSON_TYPE);
        wsParam.addPathParam("players");
        
        ClientResponse response = wsClient.post(wsParam, player);
        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            return response.getEntity(Player.class);
        }
        // Otherwise the extract error message from the response and return...
        Message message = response.getEntity(ErrorMessage.class);
        throw new RuntimeException(((ErrorMessage)message).getErrorMessage());
    }
    
    /**
     * Update the player definition in the remote service.
     * Player updation may include the table assignment.
     * 
     * @param player
     */
    public void updatePlayer(Player player) {
        RESTClient wsClient = new RESTClient("PlayerManagement", "1.0", Constants.PLAYER_MGMT_SERVICE_URL);
        
        RESTClient.RESTClientParam wsParam = new RESTClient.RESTClientParam();
        wsParam.addRequestHeader("Authorization", "");      // TODO
        wsParam.addSupportedMediaType(MediaType.APPLICATION_JSON_TYPE);
        wsParam.addPathParam("players");
        
        ClientResponse response = wsClient.put(wsParam, player);
        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            return;
        }
        // Otherwise the extract error message from the response and return...
        Message message = response.getEntity(ErrorMessage.class);
        throw new RuntimeException(((ErrorMessage)message).getErrorMessage());
    }
    
    /**
     * Fetch the player information against this playerId.
     * @param playerId
     * @return Player
     */
    public Player getPlayer(String playerId) {
        RESTClient wsClient = new RESTClient("PlayerManagement", "1.0", Constants.PLAYER_MGMT_SERVICE_URL);
        
        RESTClient.RESTClientParam wsParam = new RESTClient.RESTClientParam();
        wsParam.addRequestHeader("Authorization", "");      // TODO
        wsParam.addSupportedMediaType(MediaType.APPLICATION_JSON_TYPE);
        wsParam.addPathParam("players/" + playerId);
        
        ClientResponse response = wsClient.get(wsParam);
        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            return response.getEntity(Player.class);
        }
        else if (response.getStatus() == ClientResponse.Status.NOT_FOUND.getStatusCode()) {
            return null;
        }
        // Otherwise the extract error message from the response and return...
        Message message = response.getEntity(ErrorMessage.class);
        throw new RuntimeException(((ErrorMessage)message).getErrorMessage());
    }
}
