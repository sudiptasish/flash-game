package com.ws.jg.common.util;

/**
 * 
 */
import com.fasterxml.jackson.core.type.TypeReference;
import com.ws.jg.common.msg.GameMessage;

public class Constants {

    public static final long SESSION_MAX_IDLE_TIMEOUT = 2 * 3600 * 1000;    // 2 hrs.
    public static final String QUERY_PARAM_PLAYER_ID = "playerId";
    public static final TypeReference MSG_TYPE = new TypeReference<GameMessage>() {};
    
    public static final String TABLE_MGMT_SERVICE_URL = "http://localhost:8080/table/tableMgmt";
    public static final String PLAYER_MGMT_SERVICE_URL = "http://localhost:8080/player/playerMgmt";
    public static final String GAME_MGMT_SERVICE_URL = "http://localhost:8080/ps/gameMgmt";
    public static final String BID_MGMT_SERVICE_URL = "http://localhost:8080/ps/bidMgmt";
    
    public static final boolean UNIT_TETING_MODE = false;
    
}
