package com.ws.jg.server.service;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;

import com.ws.jg.common.client.PlayerMgmtClient;
import com.ws.jg.common.model.Player;
import com.ws.jg.common.service.AbstractService;
import com.ws.jg.common.util.Logger;

/**
 * 
 * @author Sudiptasish Chanda
 */
public class GameService extends AbstractService {

    public GameService() {}
    
    /**
     * Perform the player registration process.
     * @param request
     */
    public void register(HttpServletRequest request) throws RuntimeException {
        Player player = new Player();
        
        player.setName(request.getParameter("playername"));
        player.setEmail(request.getParameter("email"));
        player.setId(request.getParameter("playerId"));
        player.setPassword(request.getParameter("password"));
        player.setContact(request.getParameter("contact"));
        player.setAddress(request.getParameter("address"));
        player.setCreateDate(new Date(System.currentTimeMillis()));
        
        Player newPlayer = PlayerMgmtClient.get().createPlayer(player);
        Logger.log(String.format("Registered new player [%s]", newPlayer.getId()));
    }
    
    /**
     * Perform the login of a player.
     * If the login is successful, return the authenticated user, otherwise return null.
     * 
     * @param request
     * @return Player
     */
    public Player login(HttpServletRequest request) throws RuntimeException {
        String playerId = request.getParameter("playerId");
        String password = request.getParameter("password");
        
        Player player = PlayerMgmtClient.get().getPlayer(playerId);
        if (player != null && player.getPassword().equals(password)) {
            Logger.log(String.format("Login successful for player [%s]", playerId));
            return player;
        }
        return null;
    }
    
    /**
     * Fetch the password for the user.
     * @param request
     * @return String    The password.
     */
    public String forgotPassword(HttpServletRequest request) throws RuntimeException {
        String playerId = request.getParameter("playerId");
        String email = request.getParameter("email");
        
        Player player = PlayerMgmtClient.get().getPlayer(playerId);
        if (player != null && player.getId().equals(playerId) && player.getEmail().equals(email)) {
            Logger.log(String.format("Forgot password operation is successful for player [%s]", playerId));
            return player.getPassword();
        }
        return null;
    }
}
