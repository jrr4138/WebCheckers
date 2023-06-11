package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.Message;
import com.webcheckers.application.MovePiece;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.entity.Player;
import com.webcheckers.model.states.MessageType;

import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

public class PostSubmitTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());

    private final Gson gson;

    public static final String CURRENT_PLAYER = "currentUser";

    /**
     * Create the Spark Route (UI controller) for the {@code POST /submitTurn} HTTP request.
     *
     * @param gson the gson instance
     */
    public PostSubmitTurnRoute(final Gson gson) {
        // validation
        Objects.requireNonNull(gson, "gson must not be null");
        //
        this.gson = gson;
        //
        LOG.config("PostSubmitTurnRoute is initialized.");
    }

    /**
     * Handle the player's submit turn request.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the response to the AJAX action
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostSubmitTurnRoute is invoked.");

        Session httpSession = request.session();
        Player currentPlayer = httpSession.attribute(CURRENT_PLAYER);
        PlayerLobby playerLobby = httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

        if(playerLobby.hasJumpsLeft(currentPlayer)){
            Message responseMessage = new Message("Cannot end turn. You must complete all possible jump moves", MessageType.ERROR);
            return gson.toJson(responseMessage);
        }
        else if(playerLobby.missedJumpMove(currentPlayer)){
            Message responseMessage = new Message("Cannot end turn. You must complete a jump move if one is available", MessageType.ERROR);
            return gson.toJson(responseMessage);
        }
        else {
            playerLobby.finishTurn(currentPlayer);
            Message responseMessage = new Message(" ", MessageType.INFO);
            return gson.toJson(responseMessage);
        }
    }

}