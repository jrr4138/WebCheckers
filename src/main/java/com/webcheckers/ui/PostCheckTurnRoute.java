package com.webcheckers.ui;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.application.Message;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.entity.Player;

import com.webcheckers.model.states.MessageType;
import spark.*;

/**
 * The UI Controller to check if the opponent has finished a turn.
 *
 */
public class PostCheckTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());

    private final Gson gson;
    public static final String CURRENT_PLAYER = "currentUser";

    /**
     * Create the Spark Route (UI controller) for the {@code POST /checkTurn} HTTP request.
     *
     * @param gson the gson instance
     */
    public PostCheckTurnRoute(final Gson gson) {
        // validation
        Objects.requireNonNull(gson, "gson must not be null");
        //
        this.gson = gson;
        //
        LOG.config("PostCheckTurnRoute is initialized.");
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
        LOG.finer("PostCheckTurnRoute is invoked.");

        Session httpSession = request.session();
        Player currentPlayer = httpSession.attribute(CURRENT_PLAYER);
        PlayerLobby playerLobby = httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

        Message responseMessage = new Message("You are not logged in", MessageType.ERROR);
        if( currentPlayer != null) {
            Game game = playerLobby.getGame(currentPlayer);
            responseMessage = checkTurn(currentPlayer, game);
            game.isGameOver(); //check if the game needs to end

            if(!game.isGameAlive()){
                responseMessage = new Message("true", MessageType.INFO);
            }
        }
        return gson.toJson(responseMessage);
    }

    private Message checkTurn(Player currentPlayer, Game game) {

        if (currentPlayer.getTeamColor() == game.getActiveColor()) {
            return new Message("true", MessageType.INFO);
        } else {
            return new Message("false", MessageType.INFO);
        }
    }
} 