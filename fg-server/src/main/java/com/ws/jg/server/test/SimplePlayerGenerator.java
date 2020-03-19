package com.ws.jg.server.test;

import java.sql.Date;

import com.ws.jg.common.client.PlayerMgmtClient;
import com.ws.jg.common.model.Player;
import com.ws.jg.common.util.Logger;
import com.ws.jg.common.util.UUIDGenerator;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class SimplePlayerGenerator {

    /**
     * Create a few players for testing purpose.
     * This API will invoke the player management service to register the new player(s).
     * @param count
     */
    public static void generatePlayer(int count) {
        for (int i = 0; i < count; i ++) {
            PlayerMgmtClient.get().createPlayer(
                    new Player(UUIDGenerator.generateUnique()
                            , "name-" + i
                            , "pwd-" + i
                            , "abc-" + i + "@home.com"
                            , "991633520" + i
                            , "BLR"
                            , new Date(System.currentTimeMillis())));
        }
        Logger.log(String.format("Created %d test players", count));
    }
}
