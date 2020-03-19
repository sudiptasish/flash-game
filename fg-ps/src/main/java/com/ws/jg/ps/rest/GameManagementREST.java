package com.ws.jg.ps.rest;

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

import com.ws.jg.common.exception.NoSuchGameException;
import com.ws.jg.common.exception.ResourceExistException;
import com.ws.jg.common.model.FlashGame;
import com.ws.jg.common.util.Logger;
import com.ws.jg.common.util.MessageUtil;
import com.ws.jg.ps.service.GameManagementService;

/**
 * 
 * @author Sudiptasish Chanda
 */
@Path(value = "gameMgmt")
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public class GameManagementREST {
    
    private final GameManagementService service = new GameManagementService();

    /**
     * Create/Register a new game.
     * 
     * @param request
     * @param tableDesc
     * @return Response
     */
    @POST
    @Path("games")
    public Response createFlashGame(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional
            , FlashGame game) {
        
        try {
            service.addGame(game, transactional);
            Logger.log(String.format("Created new FlashGame. Id: %s", game.getId()));
            return Response.ok().entity(game).build();
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
    @Path(value = "games/{gameId}")
    public Response getFlashGame(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional
            , @PathParam("gameId") String gameId) {
        
        try {
            FlashGame game = service.getGame(gameId, transactional);
            Logger.log(String.format("Found FlashGame for id %s", gameId));
            return Response.ok().entity(game).build();
            
        }
        catch (NoSuchGameException e) {
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
     * Create/Register a new game.
     * 
     * @param request
     * @param tableDesc
     * @return Response
     */
    @PUT
    @Path("games")
    public Response updateFlashGame(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional
            , FlashGame game) {
        
        try {
            service.updateGame(game, transactional);
            Logger.log(String.format("Updated FlashGame identified by %s", game.getId()));
            return Response.ok().build();
        }
        catch (NoSuchGameException e) {
            Logger.log(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(MessageUtil.buildErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), e))
                    .build();
        }
    }
    
    /**
     * Delete the game identified by this gameId.
     * @param request
     * @param gameId
     * @return Response
     */
    @DELETE
    @Path("games/{gameId}")
    public Response deleteFlashGame(@Context HttpServletRequest request
            , @QueryParam("transactional")  @DefaultValue("true") boolean transactional
            , @PathParam("gameId") String gameId) {
        
        try {
            service.removeGame(gameId, transactional);
            Logger.log(String.format("Removed the game identified by %s", gameId));
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
