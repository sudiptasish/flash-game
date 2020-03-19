package com.ws.jg.tbl.service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.ws.jg.common.model.GameTable;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class DefaultTableManager extends TableManager {
    
    private final ConcurrentMap<String, GameTable> tableMapping = new ConcurrentHashMap<>();
    
    DefaultTableManager() {
        super();
    }

    /* (non-Javadoc)
     * @see com.ws.jg.tbl.core.TableManager#createNew(com.ws.jg.tbl.core.GameTable)
     */
    @Override
    public GameTable createNew(GameTable table) {
        tableMapping.put(table.getId(), table);
        return table;
    }

    /* (non-Javadoc)
     * @see com.ws.jg.tbl.core.TableManager#update(com.ws.jg.common.model.GameTable)
     */
    @Override
    public void update(GameTable table) {
        tableMapping.put(table.getId(), table);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.core.TableManager#list()
     */
    @Override
    public Collection<GameTable> list() {
        return tableMapping.values();
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.core.TableManager#find(java.lang.String)
     */
    @Override
    public GameTable find(String tableId) {
        return tableMapping.get(tableId);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.core.TableManager#remove(java.lang.String)
     */
    @Override
    public void remove(String tableId) {
        tableMapping.remove(tableId);
    }

    /* (non-Javadoc)
     * @see com.ws.jg.server.core.TableManager#clear()
     */
    @Override
    public void clear() {
        tableMapping.clear();
    }

    /* (non-Javadoc)
     * @see com.ws.jg.tbl.service.TableManager#contains(java.lang.String)
     */
    @Override
    public boolean contains(String tableId) {
        return tableMapping.containsKey(tableId);
    }
}
