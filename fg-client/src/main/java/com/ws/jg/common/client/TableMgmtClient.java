package com.ws.jg.common.client;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.ws.jg.common.model.GameTable;
import com.ws.jg.common.msg.ErrorMessage;
import com.ws.jg.common.msg.Message;
import com.ws.jg.common.util.Constants;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class TableMgmtClient {
    
    private static final TableMgmtClient TABLE_MGMT_CLIENT = new TableMgmtClient();
    
    private TableMgmtClient() {}
    
    /**
     * Return the singleton table management client.
     * @return TableMgmtClient
     */
    public static TableMgmtClient get() {
        return TABLE_MGMT_CLIENT;
    }
    
    /**
     * Create a new game table.
     * @param table
     */
    public GameTable createTable(GameTable table) {
        RESTClient wsClient = new RESTClient("TableManagement", "1.0", Constants.TABLE_MGMT_SERVICE_URL);
        
        RESTClient.RESTClientParam wsParam = new RESTClient.RESTClientParam();
        wsParam.addRequestHeader("Authorization", "");      // TODO
        wsParam.addSupportedMediaType(MediaType.APPLICATION_JSON_TYPE);
        wsParam.addPathParam("tables");
        
        ClientResponse response = wsClient.post(wsParam, table);
        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            return response.getEntity(GameTable.class);
        }
        // Otherwise the extract error message from the response and return...
        Message message = response.getEntity(ErrorMessage.class);
        throw new RuntimeException(((ErrorMessage)message).getErrorMessage());
    }
    
    /**
     * Update the table definition in the remote service.
     * Table updation may include the new player assignment.
     * @param table
     */
    public void updateTable(GameTable table) {
        RESTClient wsClient = new RESTClient("TableManagement", "1.0", Constants.TABLE_MGMT_SERVICE_URL);
        
        RESTClient.RESTClientParam wsParam = new RESTClient.RESTClientParam();
        wsParam.addRequestHeader("Authorization", "");      // TODO
        wsParam.addSupportedMediaType(MediaType.APPLICATION_JSON_TYPE);
        wsParam.addPathParam("tables");
        
        ClientResponse response = wsClient.put(wsParam, table);
        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            return;
        }
        // Otherwise the extract error message from the response and return...
        Message message = response.getEntity(ErrorMessage.class);
        throw new RuntimeException(((ErrorMessage)message).getErrorMessage());
    }
    
    /**
     * Check to see if a table exists as identified by this tableId.
     * @param tableId
     * @return boolean
     */
    public GameTable getTable(String tableId) {
        RESTClient wsClient = new RESTClient("TableManagement", "1.0", Constants.TABLE_MGMT_SERVICE_URL);
        
        RESTClient.RESTClientParam wsParam = new RESTClient.RESTClientParam();
        wsParam.addRequestHeader("Authorization", "");      // TODO
        wsParam.addSupportedMediaType(MediaType.APPLICATION_JSON_TYPE);
        wsParam.addPathParam("tables/" + tableId);
        
        ClientResponse response = wsClient.get(wsParam);
        if (response.getStatus() == ClientResponse.Status.OK.getStatusCode()) {
            return response.getEntity(GameTable.class);
        }
        // Otherwise the extract error message from the response and return...
        Message message = response.getEntity(ErrorMessage.class);
        throw new RuntimeException(((ErrorMessage)message).getErrorMessage());
    }
}
