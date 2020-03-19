package com.ws.jg.ps.rest;
    
import java.util.List;

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

import com.ws.jg.common.msg.GameMessage;
import com.ws.jg.common.util.Logger;
import com.ws.jg.common.util.MessageUtil;
import com.ws.jg.ps.service.BidTrackerService;

/**
 * 
 * @author Sudiptasish Chanda
 */
@Path(value = "bidMgmt")
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public class BidTrackerREST {
    
    private final BidTrackerService service = new BidTrackerService();

    /**
     * Accept and store the new bid into the bid store.
     * All bids are currently stored in the memory.
     * 
     * @param request
     * @param bidMsg
     * @return Response
     */
    @POST
    @Path(value = "bids")
    public Response captureBid(@Context HttpServletRequest request
            , @QueryParam("transactional") @DefaultValue("true") boolean transactional
            , GameMessage bidMsg) {
        
        try {
            service.captureBid(bidMsg, transactional);
            Logger.log(String.format("Successfully captured bid [%s]", bidMsg));
            
            return Response.ok().build();
        }
        catch (RuntimeException e) {
            Logger.log(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(MessageUtil.buildErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e))
                    .build();
        }
    }
    
    /**
     * Get the list of bids made for this game as identified by the gameId.
     * If the count is set, then return the first 'count' bids for this game.
     * 
     * @param request
     * @param gameId
     * @return Response
     */
    @GET
    @Path(value = "bids/{gameId}")
    public Response getBids(@Context HttpServletRequest request
            , @PathParam("gameId") String gameId
            , @QueryParam("transactional") @DefaultValue("true") boolean transactional
            , @QueryParam("count") Integer count) {
        
        try {
            List<GameMessage> list;
            if (count != null) {
                list = service.getFirstFewBids(gameId, count);
            }
            else {
                list = service.getBids(gameId, transactional);
            }
            Logger.log(String.format("Received %d bid(s) for game [%s]", list.size(), gameId));
            
            return Response.ok().entity(list).build();
        }
        catch (RuntimeException e) {
            Logger.log(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(MessageUtil.buildErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e))
                    .build();
        }
    }
    
    /**
     * This API will be invoked whenever a game has been finalized and
     * all the players left the game.
     * 
     * @param request
     * @param gameId
     * @param transactional
     * @return Reponse
     */
    @PUT
    @Path(value = "bids/{gameId}")
    public Response finalize(@Context HttpServletRequest request
            , @PathParam("gameId") String gameId
            , @QueryParam("transactional") @DefaultValue("true") boolean transactional) {
        
        try {
            service.persistBids(gameId, transactional);
            Logger.log(String.format("Persisted All in-memory bid(s) for game [%s]", gameId));
            
            return Response.ok().build();
        }
        catch (RuntimeException e) {
            Logger.log(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(MessageUtil.buildErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e))
                    .build();
        }
    }
    
    /**
     * Return the last bid for this game.
     * 
     * @param request
     * @param gameId
     * @param transactional
     * @return Response
     */
    @GET
    @Path(value = "bids/{gameId}/latest")
    public Response getLastBid(@Context HttpServletRequest request
            , @PathParam("gameId") String gameId
            , @QueryParam("transactional") @DefaultValue("true") boolean transactional) {
        
        try {
            GameMessage msg = service.getLastBid(gameId, transactional);
            if (msg != null) {
                return Response.ok().entity(msg).build();
            }
            else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(MessageUtil.buildErrorMessage(Response.Status.NOT_FOUND.getStatusCode()
                                , "No bid information available for game " + gameId)).build();
            }
        }
        catch (RuntimeException e) {
            Logger.log(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(MessageUtil.buildErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e))
                    .build();
        }
    }
    
    /**
     * Clear all the bids made on this game.
     * 
     * @param request
     * @param gameId
     * @return Response
     */
    @DELETE
    @Path(value = "bids/{gameId}")
    public Response removeBids(@Context HttpServletRequest request
            , @PathParam("gameId") String gameId
            , @QueryParam("transactional") @DefaultValue("true") boolean transactional) {
        
        service.clearBids(gameId, transactional);
        Logger.log(String.format("Cleared all bids for game %s", gameId));
        
        return Response.ok().build();
    }
}
