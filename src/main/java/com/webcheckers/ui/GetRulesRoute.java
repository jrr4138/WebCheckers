package com.webcheckers.ui;

import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.application.Message;
import com.webcheckers.model.entity.Player;
import com.webcheckers.application.PlayerLobby;
import spark.*;

public class GetRulesRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetRulesRoute.class.getName());
    public static final String PLAYERLOBBY_KEY = "playerLobby";

    public static final String MESSAGE_ATTR = "message";
    public static final String TITLE_ATTR = "title";
    public static final Message WELCOME_MSG = Message.info("Rules of American checkers");
    public static final String CURRENT_PLAYER = "currentUser";
    public static final String PLAYERS_ONLINE = "playersOnline";
    public static final String NO_OPPONENTS = "noOpponents";
    public static final String WELCOME_TITLE = "Rules";


    private final TemplateEngine templateEngine;

    public GetRulesRoute(TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "required");
    }

    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetRulesRoute is invoked");

        Session httpSession = request.session();
        Player currentUser = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        PlayerLobby playerLobby = httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Rules");
        vm.put(GetHomeRoute.CURRENT_PLAYER, currentUser);

        if(currentUser != null && playerLobby.isInGame(currentUser))
            vm.put(GetHomeRoute.IN_GAME, true);

        // render the View
        return templateEngine.render(new ModelAndView(vm , "rules.ftl"));
    }

}
