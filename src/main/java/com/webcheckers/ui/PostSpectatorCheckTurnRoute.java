package com.webcheckers.ui;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.application.Message;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.entity.Player;
import com.webcheckers.model.states.PieceColor;
import spark.*;

import com.webcheckers.model.states.MessageType;
import spark.*;

public class PostSpectatorCheckTurnRoute implements Route {

    /**
     * Logger for logging things to the console
     */
    private static final Logger LOG = Logger.getLogger(PostSpectatorCheckTurnRoute.class.getName());

    /**
     * Template engine for desplaying things to users
     */
    private final TemplateEngine templateEngine;
    private final Gson gson;

    public static final String CURRENT_PLAYER = "currentUser";

    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /} HTTP request.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSpectatorCheckTurnRoute(final TemplateEngine templateEngine, final Gson gson) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.gson = Objects.requireNonNull(gson, "gson is required");
        //
        this.templateEngine = templateEngine;
        //
        LOG.config("GetSpectatorCheckTurnRoute.");
    }

    /**
     * Handle the player's check turn request.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the response to the AJAX action
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostSpectatorCheckTurnRoute is invoked.");

        Session httpSession = request.session();
        Player currentPlayer = httpSession.attribute(CURRENT_PLAYER);
        PlayerLobby playerLobby = httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);
        PieceColor color = httpSession.attribute(GetSpectatingRoute.LAST_COLOR_KEY);
        Message responseMessage = new Message("You are not logged in", MessageType.ERROR);
        
        //player is signed in
        if(currentPlayer != null) {
            //make sure the user is in a game
            Game game = playerLobby.getGame(currentPlayer);
            if(game != null){
                responseMessage = checkTurn(game, color);
                if(!game.isGameAlive()){
                    responseMessage = new Message("true", MessageType.INFO);
                }
            }       
        }
        return gson.toJson(responseMessage);
    }

        private Message checkTurn(Game game, PieceColor color) {
            if (color == game.getActiveColor()) {
                return new Message("true", MessageType.INFO);
            } else {
                return new Message("false", MessageType.INFO);
            }
        }
    


}