package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.Message;
import com.webcheckers.model.entity.Player;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.states.MessageType;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostResignRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    //Instance variables
    private Gson gson;


    /**
     * Create the Spark Route (UI controller) for the {@code POST /resignGame} HTTP request.
     *
     * @param gson the gson instance
     */
    public PostResignRoute(final Gson gson) {
        // validation
        Objects.requireNonNull(gson, "gson must not be null");
        //
        this.gson = gson;
        //
        LOG.config("PostSubmitTurnRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostResignRoute is invoked.");

        Session httpSession = request.session();
        Player user = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        PlayerLobby playerLobby = httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

        Message responseMessage;

        if(!playerLobby.isInGame(user)) {
            httpSession.attribute(GetHomeRoute.ERROR_KEY, new Message("You do not have an active game, please try again", MessageType.ERROR));
            response.redirect("/");
            throw halt(401);
        }

        if(playerLobby.gameIsOver(user)) {
            responseMessage = new Message("Cannot resign, game is over", MessageType.ERROR);
        } else {
            playerLobby.resignGame(user);
            responseMessage = new Message("Resignation successful", MessageType.INFO);
        }

        return gson.toJson(responseMessage);
    }
}
