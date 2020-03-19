package com.ws.jg.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ws.jg.common.model.Player;
import com.ws.jg.common.util.Logger;
import com.ws.jg.server.service.GameService;

/**
 * 
 * @author Sudiptasish Chanda
 */
@WebServlet(name = "GameServlet", urlPatterns = {"/domain/*"})
public class GameServletContainer extends HttpServlet {
    
    private final String DOMAIN_URI = "/domain";
    
    private final String HOME_URI = "/home";    
    private final String LOGIN_URI = "/login";   
    private final String LOGOUT_URI = "/logout";
    private final String REGISTER_URI = "/register";
    private final String FORGOT_PWD_URI = "/forgotpwd";
    private final String TABLE_URI = "/table";
    
    private final GameService service = new GameService();
    
    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Logger.log("Initialized GameServletContainer");
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo.equals(LOGIN_URI)) {
                Player player = service.login(request);
                if (player != null) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("session.player", player.getId());
                    request.getRequestDispatcher("/jsp/flash_home.jsp").forward(request, response);
                }
                else {
                    request.setAttribute("login.invalid.msg", "Invalid Username/Password");
                    request.getRequestDispatcher("/jsp/flash_login.jsp").forward(request, response);
                }
            }
            else if (pathInfo.equals(LOGOUT_URI)) {
                HttpSession session = request.getSession();
                session.removeAttribute("session.player");
                session.invalidate();
                
                response.sendRedirect(request.getContextPath() + "/jsp/flash_home.jsp");
            }
            else if (pathInfo.equals(TABLE_URI)) {
                String playerId = request.getParameter("playerId");
                String tableId = request.getParameter("tableId");
                String action = request.getParameter("action");
                
                if (playerId != null && tableId != null) {
                    HttpSession session = request.getSession();
                    String sessionPlayer = (String)session.getAttribute("session.player");
                    
                    if (sessionPlayer == null || !sessionPlayer.equals(playerId)) {
                        throw new RuntimeException(
                                String.format("Session player [%s] is different than the requested player [%s] !!. Please login ."
                                        , sessionPlayer != null ? sessionPlayer : "none"
                                        , playerId));
                    }
                    request.setAttribute("request.table", tableId);
                    request.setAttribute("game.action", action);
                    request.getRequestDispatcher("/jsp/flash_table.jsp").forward(request, response);
                }
                else {
                    throw new RuntimeException("No valid player was identified. Please login/register to play.");
                }
            }
            else if (pathInfo.equals(REGISTER_URI)) {
                service.register(request);
                request.getRequestDispatcher("/jsp/flash_login.jsp").forward(request, response);
            } 
            else if (pathInfo.equals(FORGOT_PWD_URI)) {
                String password = service.forgotPassword(request);
                if (password != null) {
                    request.setAttribute("password.msg", "Your password is: " + password);
                }
                else {
                    request.setAttribute("password.msg", "Invalid PlayerId / Email Address");
                }
                request.getRequestDispatcher("/jsp/flash_forgotpwd.jsp").forward(request, response);
            }
            else {
                throw new RuntimeException("Sorry, the page you requested cannot be found !!");
            }
        }
        catch (RuntimeException e) {
            request.setAttribute("error.msg", e.getMessage());
            request.getRequestDispatcher("/jsp/flash_error.jsp").forward(request, response);
        }
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        doGet(request, response);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.GenericServlet#destroy()
     */
    @Override
    public void destroy() {
        Logger.log("Destroyed GameServletContainer");
    }
}
