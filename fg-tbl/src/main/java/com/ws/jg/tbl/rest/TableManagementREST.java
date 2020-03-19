package com.ws.jg.tbl.rest;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ws.jg.common.exception.NoSuchTableException;
import com.ws.jg.common.exception.ResourceExistException;
import com.ws.jg.common.model.GameTable;
import com.ws.jg.common.util.Logger;
import com.ws.jg.common.util.MessageUtil;
import com.ws.jg.tbl.service.TableManagementService;

/**
 * 
 * @author Sudiptasish Chanda
 */
@Path(value = "tableMgmt")
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public class TableManagementREST {
    
    private TableManagementService service = new TableManagementService();

    /**
     * Return the list of tables available for this game.
     * @return Response
     */
    @GET
    @Path("tables")
    public Response getAllTables(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional) {
        
        try {
            Collection<GameTable> tables = service.getAllTables(transactional);
            Logger.log(String.format("Found %d Game Table(s)", tables.size()));
            
            return Response.ok().entity(tables).build();
        }
        catch (RuntimeException e) {
            Logger.log(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(MessageUtil.buildErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e))
                    .build();
        }
    }
    
    /**
     * Create a new game table.
     * 
     * @param request
     * @param tableDesc
     * @return Response
     */
    @POST
    @Path("tables")
    public Response createTable(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional
            , GameTable table) {
        
        try {
            GameTable gameTable = service.createNew(table, transactional);
            Logger.log(String.format("Created new table identified by [%s]", table.getId()));
            return Response.ok().entity(gameTable).build();
        }
        catch (ResourceExistException e) {
            Logger.log(e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                    .entity(MessageUtil.buildErrorMessage(Response.Status.CONFLICT.getStatusCode(), e))
                    .build();
        }
        catch (RuntimeException e) {
            Logger.log(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(MessageUtil.buildErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e))
                    .build();
        }
    }
    
    /**
     * Return the game table identified by this Id.
     * 
     * @param request
     * @param tableId
     * @return Response
     */
    @GET
    @Path("tables/{tableId}")
    public Response getTable(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional
            , @PathParam("tableId") String tableId) {
        
        try {
            GameTable table = service.getTable(tableId, transactional);
            Logger.log(String.format("Found Game Table for id %s", tableId));
            return Response.ok().entity(table).build();
            
        }
        catch (NoSuchTableException e) {
            Logger.log(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(MessageUtil.buildErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), e))
                    .build();
        }
        catch (RuntimeException e) {
            Logger.log(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(MessageUtil.buildErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e))
                    .build();
        }
    }
    
    /**
     * Update the table definition.
     * @param request
     * @param player
     * @return Response
     */
    @PUT
    @Path("tables")
    public Response updateTable(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional
            , GameTable table) {
        
        service.updateTable(table, transactional);
        Logger.log(String.format("Updated the table definition for [%s]", table.getId()));
        return Response.ok().build();
    }
    
    /**
     * Delete the game table identified by this tableId.
     * @param request
     * @param tableId
     * @return Response
     */
    @DELETE
    @Path("tables/{tableId}")
    public Response deleteTable(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional
            , @PathParam("tableId") String tableId) {
        
        try {
            service.removeTable(tableId, transactional);
            Logger.log(String.format("Removed the table identified by [%s]", tableId));
            return Response.ok().build();
        }
        catch (RuntimeException e) {
            Logger.log(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(MessageUtil.buildErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e))
                    .build();
        }
    }
}
