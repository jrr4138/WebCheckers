package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.Message;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.entity.Player;
import com.webcheckers.model.states.MessageType;

import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

public class PostBackupMoveRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostBackupMoveRoute.class.getName());

    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) for the {@code POST /backupMove} HTTP request.
     *
     * @param gson the gson instance
     */
    public PostBackupMoveRoute(final Gson gson) {
        // validation
        Objects.requireNonNull(gson, "gson must not be null");
        //
        this.gson = gson;
        //
        LOG.config("PostBackupMoveRoute is initialized.");
    }

    /**
     * Handle the player's backup move request.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the response to the AJAX action
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostBackupMoveRoute is invoked.");

        Session httpSession = request.session();
        Player currentPlayer = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        PlayerLobby playerLobby = httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

        playerLobby.backupMove(currentPlayer);

        Message responseMessage = new Message("", MessageType.INFO);
        return gson.toJson(responseMessage);
    }
}