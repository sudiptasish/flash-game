package com.ws.jg.player.rest;

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

import com.ws.jg.common.exception.NoSuchPlayerException;
import com.ws.jg.common.exception.ResourceExistException;
import com.ws.jg.common.model.Player;
import com.ws.jg.common.util.Logger;
import com.ws.jg.common.util.MessageUtil;
import com.ws.jg.player.service.PlayerManagementService;

/**
 * 
 * @author Sudiptasish Chanda
 */
@Path(value = "playerMgmt")
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public class PlayerManagementREST {
    
    private final PlayerManagementService service = new PlayerManagementService();
    
    /**
     * Create/Register a new player.
     * 
     * @param request
     * @param tableDesc
     * @return Response
     */
    @POST
    @Path("players")
    public Response createPlayer(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional
            , Player player) {
        
        try {
            Player newPlayer = service.registerPlayer(player, transactional);
            Logger.log(String.format("Created new Player. Id: %s", newPlayer.getId()));
            return Response.ok().entity(newPlayer).build();
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

    @GET
    @Path(value = "players/{playerId}")
    public Response getPlayer(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional
            , @PathParam("playerId") String playerId) {
        
        try {
            Player player = service.findPlayer(playerId, transactional);
            Logger.log(String.format("Found Player for id %s", playerId));
            return Response.ok().entity(player).build();
            
        }
        catch (NoSuchPlayerException e) {
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
     * Create/Register a new player.
     * 
     * @param request
     * @param tableDesc
     * @return Response
     */
    @PUT
    @Path("players")
    public Response updatePlayer(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional
            , Player player) {
        
        service.updatePlayer(player, transactional);
        Logger.log(String.format("Updated Player identified by %s", player.getId()));
        return Response.ok().build();
    }
    
    /**
     * Delete the player identified by this playerId.
     * @param request
     * @param playerId
     * @return Response
     */
    @DELETE
    @Path("players/{playerId}")
    public Response deletePlayer(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional
            , @PathParam("playerId") String playerId) {
        
        try {
            service.removePlayer(playerId, transactional);
            Logger.log(String.format("Removed the player identified by %s", playerId));
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
