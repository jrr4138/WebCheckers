package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.MovePiece;
import com.webcheckers.application.Message;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.board.Move;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.entity.Player;
import com.webcheckers.model.states.MessageType;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

public class PostValidateMoveRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());

    private final Gson gson;
    public static final String CURRENT_PLAYER = "currentUser";

    /**
     * Create the Spark Route (UI controller) for the {@code POST /validateMove} HTTP request.
     *
     * @param gson the gson instance
     */
    public PostValidateMoveRoute(final Gson gson) {
        // validation
        Objects.requireNonNull(gson, "gson must not be null");
        //
        this.gson = gson;
        //
        LOG.config("PostValidateMoveRoute is initialized.");
    }

    /**
     * Handle the player's movement request.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the response to the AJAX action
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostValidateMoveRoute is invoked.");

        Session httpSession = request.session();
        Player currentPlayer = httpSession.attribute(CURRENT_PLAYER);
        PlayerLobby playerLobby = httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

        String tmp = request.queryParams("actionData");
        Move move = gson.fromJson(tmp, Move.class);

        Game game = playerLobby.getGame(currentPlayer);

        Message responseMessage;

        if( MovePiece.isMoveValid(move, game.getBoard(), currentPlayer.getTeamColor(), game.getMoveList())) {
            game.queueMove(move);
            responseMessage = new Message("", MessageType.INFO);
        }
        else{
            responseMessage = new Message("Invalid Move" , MessageType.ERROR);
        }
        return gson.toJson(responseMessage);
    }
}