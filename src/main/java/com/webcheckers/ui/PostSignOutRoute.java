package com.webcheckers.ui;

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

public class PostSignOutRoute implements Route {

    public static final String USERNAME = "username";

    //Attributes
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signin} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSignOutRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("SignOut request received.");

        Session httpSession = request.session();
        PlayerLobby playerLobby = httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Welcome!");

        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);

        if(playerLobby.justChallenged(player)) {
            playerLobby.acceptChallenge(player);
            httpSession.attribute(GetHomeRoute.ERROR_KEY, new Message("You were just challenged, please resign from the game before signing out.", MessageType.ERROR));
            response.redirect("/");
            throw halt(401);
        }

        if(playerLobby.isInGame(player)) {
            httpSession.attribute(GetHomeRoute.ERROR_KEY, new Message("You cannot sign out as you are currently in a game, please try again.", MessageType.ERROR));
            response.redirect("/");
            throw halt(401);
        }

        playerLobby.signPlayerOut(player.getName());

        // clear session info
        request.session().attribute("currentUser", null);
        vm.put(GetHomeRoute.PLAYERS_ONLINE, playerLobby.listOfPlayers());
        response.redirect("/");

        return templateEngine.render(new ModelAndView(vm ,"home.ftl"));
    }
}
