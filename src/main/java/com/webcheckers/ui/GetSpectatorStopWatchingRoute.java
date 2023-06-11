package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.entity.Player;
import com.webcheckers.model.states.ViewMode;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class GetSpectatorStopWatchingRoute implements Route {

    /**
     * Logger for logging things to the console
     */
    private static final Logger LOG = Logger.getLogger(GetSpectatorStopWatchingRoute.class.getName());

    /**
     * Template engine for desplaying things to users
     */
    private final TemplateEngine templateEngine;

    public static final String CURRENT_PLAYER = "currentUser";
    
    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /} HTTP request.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetSpectatorStopWatchingRoute(final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.templateEngine = templateEngine;
        //
        LOG.config("GetSpectatorStopWatchingRoute.");
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
        LOG.fine("GetSpectatorStopWatchingRoute is invoked.");

        Session httpSession = request.session();
        Player currentPlayer = httpSession.attribute(CURRENT_PLAYER);
        PlayerLobby playerLobby = httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

        playerLobby.stopSpectating(currentPlayer);
        response.redirect("/");
        halt();
        return null;
    }

}