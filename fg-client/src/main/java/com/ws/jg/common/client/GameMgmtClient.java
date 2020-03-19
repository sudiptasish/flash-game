package com.ws.jg.common.client;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.ws.jg.common.model.FlashGame;
import com.ws.jg.common.msg.ErrorMessage;
import com.ws.jg.common.msg.Message;
import com.ws.jg.common.util.Constants;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class GameMgmtClient {
    
    private static final GameMgmtClient GAME_MGMT_CLIENT = new GameMgmtClient();
    
    private GameMgmtClient() {}
    
    /**
     * Return the singleton table management client.
     * @return GameMgmtClient
     */
    public static GameMgmtClient get() {
        return GAME_MGMT_CLIENT;
    }
    
    /**
     * Create a new game.
     * @param game
     */
    public void createGame(FlashGame game) {
        RESTClient wsClient = new RESTClient("GameManagement", "1.0", Constants.GAME_MGMT_SERVICE_URL);
        
        RESTClient.RESTClientParam wsParam = new RESTClient.RESTClientParam();
        wsParam.addRequestHeader("Authorization", "");      // TODO
        wsParam.addSupportedMediaType(MediaType.APPLICATION_JSON_TYPE);
        wsParam.addPathParam("games");
        
        ClientResponse response = wsClient.post(wsParam, game);
        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            return;
        }
        // Otherwise the extract error message from the response and return...
        Message message = response.getEntity(ErrorMessage.class);
        throw new RuntimeException(((ErrorMessage)message).getErrorMessage());
    }
    
    /**
     * Update the game definition in the remote service.
     * Game updation may include modifying the status and having a end time.
     * @param game
     */
    public void updateGame(FlashGame game) {
        RESTClient wsClient = new RESTClient("GameManagement", "1.0", Constants.GAME_MGMT_SERVICE_URL);
        
        RESTClient.RESTClientParam wsParam = new RESTClient.RESTClientParam();
        wsParam.addRequestHeader("Authorization", "");      // TODO
        wsParam.addSupportedMediaType(MediaType.APPLICATION_JSON_TYPE);
        wsParam.addPathParam("games");
        
        ClientResponse response = wsClient.put(wsParam, game);
        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            return;
        }
        // Otherwise the extract error message from the response and return...
        Message message = response.getEntity(ErrorMessage.class);
        throw new RuntimeException(((ErrorMessage)message).getErrorMessage());
    }
    
    /**
     * Check to see if a game exists as identified by this gameId.
     * @param gameId
     * @return FlashGame
     */
    public FlashGame getGame(String gameId) {
        RESTClient wsClient = new RESTClient("GameManagement", "1.0", Constants.GAME_MGMT_SERVICE_URL);
        
        RESTClient.RESTClientParam wsParam = new RESTClient.RESTClientParam();
        wsParam.addRequestHeader("Authorization", "");      // TODO
        wsParam.addSupportedMediaType(MediaType.APPLICATION_JSON_TYPE);
        wsParam.addPathParam("games/" + gameId);
        
        ClientResponse response = wsClient.get(wsParam);
        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            return response.getEntity(FlashGame.class);
        }
        // Otherwise the extract error message from the response and return...
        Message message = response.getEntity(ErrorMessage.class);
        throw new RuntimeException(((ErrorMessage)message).getErrorMessage());
    }
}
