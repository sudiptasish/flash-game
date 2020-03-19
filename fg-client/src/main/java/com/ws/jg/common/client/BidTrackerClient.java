package com.ws.jg.common.client;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.ws.jg.common.msg.GameMessage;
import com.ws.jg.common.msg.Message;
import com.ws.jg.common.util.Constants;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class BidTrackerClient {
    
    private static final BidTrackerClient BID_MGMT_CLIENT = new BidTrackerClient();
    
    private BidTrackerClient() {}
    
    /**
     * Return the singleton bid management client.
     * @return BidTrackerClient
     */
    public static BidTrackerClient get() {
        return BID_MGMT_CLIENT;
    }
    
    /**
     * Post the current bid to the remote service.
     * @param bidMsg
     */
    public void postBid(Message bidMsg) {
        RESTClient wsClient = new RESTClient("BidTracker", "1.0", Constants.BID_MGMT_SERVICE_URL);
        
        RESTClient.RESTClientParam wsParam = new RESTClient.RESTClientParam();
        wsParam.addRequestHeader("Authorization", "");      // TODO
        wsParam.addSupportedMediaType(MediaType.APPLICATION_JSON_TYPE);
        wsParam.addPathParam("bids");
        
        ClientResponse response = wsClient.post(wsParam, bidMsg);
        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            return;
        }
        throw new RuntimeException("Critical Error invoking remote bid tracker service");     
    }
    
    /**
     * Fetch the bid details for this specific game.
     * @param gameId
     * @return List
     */
    public List<GameMessage> getBids(String gameId) {
        RESTClient wsClient = new RESTClient("BidTracker", "1.0", Constants.BID_MGMT_SERVICE_URL);
        
        RESTClient.RESTClientParam wsParam = new RESTClient.RESTClientParam();
        wsParam.addRequestHeader("Authorization", "");      // TODO
        wsParam.addSupportedMediaType(MediaType.APPLICATION_JSON_TYPE);
        wsParam.addPathParam("bids/" + gameId);
        
        ClientResponse response = wsClient.get(wsParam);
        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            return response.getEntity(new GenericType<List<GameMessage>>() {});
        }
        throw new RuntimeException("Critical Error invoking remote bid tracker service");
    }
    
    /**
     * Finalize the bid for this game.
     * @param gameId
     * @return List
     */
    public void finalizeBids(String gameId) {
        RESTClient wsClient = new RESTClient("BidTracker", "1.0", Constants.BID_MGMT_SERVICE_URL);
        
        RESTClient.RESTClientParam wsParam = new RESTClient.RESTClientParam();
        wsParam.addRequestHeader("Authorization", "");      // TODO
        wsParam.addSupportedMediaType(MediaType.APPLICATION_JSON_TYPE);
        wsParam.addPathParam("bids/" + gameId);
        
        ClientResponse response = wsClient.put(wsParam, null);
        if (response.getStatus() != ClientResponse.Status.OK.getStatusCode()) {
            throw new RuntimeException("Critical Error invoking remote bid tracker service");
        }
    }
}
