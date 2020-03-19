package com.ws.jg.server.test;

import com.ws.jg.common.client.TableMgmtClient;
import com.ws.jg.common.model.GameTable;
import com.ws.jg.common.util.Logger;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class SimpleTableGenerator {
    
    /**
     * Create a few game table for testing.
     * This API will invoke the table management service to create the table(s).
     * @param count
     */
    public static void generateTable(int count) {
        for (int i = 0; i < count; i ++) {
            TableMgmtClient.get().createTable(
                    new GameTable(
                            "test_ft_" + i
                            , "FLASH_TABLE_" + i
                            , "Test Flash Table"));
        }
        Logger.log(String.format("Created %d test flash table", count));
    }
}
