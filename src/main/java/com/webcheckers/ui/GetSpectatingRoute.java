package com.webcheckers.ui;

import com.google.gson.Gson;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.entity.Player;
import com.webcheckers.model.states.ViewMode;
import com.webcheckers.model.states.PieceColor;
import com.webcheckers.model.states.GameState;
import com.webcheckers.application.Message;
import com.webcheckers.model.states.MessageType;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class GetSpectatingRoute implements Route {

    /**
     * Logger for logging things to the console
     */
    private static final Logger LOG = Logger.getLogger(GetSpectatingRoute.class.getName());

    /**
     * Template engine for desplaying things to users
     */
    private final TemplateEngine templateEngine;

    /**
     * Player Lobby to receive info about players in game
     */
    private PlayerLobby playerLobby;
      private Gson gson;

    public static final String CURRENT_PLAYER = "currentUser";
    public static final String LAST_COLOR_KEY = "LAST_COLOR";
    public static final String PLAYER_TO_WATCH = "watching";
    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /} HTTP request.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetSpectatingRoute(final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        this.gson = gson;
        //
        LOG.config("GetSpectatingRoute is initialized.");
    }

    /**
     * Render the WebCheckers game page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSpectatingRoute is invoked.");

        Map<String, Object> vm = new HashMap<>();

        Session httpSession = request.session();
        Player user = httpSession.attribute(CURRENT_PLAYER);
        PlayerLobby playerLobby = httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

        // if the opponent name is present challenge that player
        String spectateName = request.queryParams("name");
        String opt = request.queryParams("opt");
    
        if(opt != null){
            playerLobby.stopSpectating(user);
        }
        if (spectateName != null) {
            Player spectatingPlayer = playerLobby.getPlayer(spectateName);
            playerLobby.addSpectator(user, spectatingPlayer);
            httpSession.attribute(PLAYER_TO_WATCH, spectatingPlayer);
            Game game = playerLobby.getGame(user);
            httpSession.attribute(LAST_COLOR_KEY, game.getActiveColor());
            response.redirect("/game");
            GameState gameState = game.getGameState();
            if(gameState != GameState.ACTIVE){
                String gameOverMessage = "";
                switch(gameState) {
                    case WHITE_RESIGN:
                    gameOverMessage = String.format("%s resigned", game.getWhitePlayer().getName());
                    break;
                    case RED_RESIGN:
                    gameOverMessage = String.format("%s resigned", game.getRedPlayer().getName());
                    break;
                    case RED_WIN:
                    gameOverMessage = String.format("%s wins!", game.getRedPlayer().getName());
                    break;
                    case WHITE_WIN:
                    gameOverMessage = String.format("%s wins!", game.getWhitePlayer().getName());
                    break;
                }
                final Map<String, Object> modeOptions = new HashMap<>(2);
                modeOptions.put("isGameOver", true);
                modeOptions.put("gameOverMessage", gameOverMessage);
                vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
                return new Message("true", MessageType.INFO);
            }
            throw halt(1102);
        } 
        else{
            response.redirect("/");
            throw halt(1101);
        }
    }

}