package com.webcheckers.ui;

import com.webcheckers.application.Message;
import com.webcheckers.model.entity.Player;
import com.webcheckers.application.PlayerLobby;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class PostSignInRoute implements Route {

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
    public PostSignInRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("PostSignInRoute is initialized");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("SignIn request received");

        Session httpSession = request.session();

        PlayerLobby playerLobby = httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

        //Gets users selected username
        String username = request.queryParams("username");

        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Sign-in page");

        PlayerLobby.SignInResult signInResult = playerLobby.addPlayer(username);

        switch(signInResult){
            case SUCCESS:

            case RETURN_SUCCESS:
                Player player = playerLobby.getPlayer(username);

                // Save the player in the session
                if(player != null) {
                    httpSession.attribute(GetHomeRoute.CURRENT_PLAYER, player);
                    vm.put(GetHomeRoute.CURRENT_PLAYER, httpSession.attribute(GetHomeRoute.CURRENT_PLAYER));
                    if(signInResult == PlayerLobby.SignInResult.SUCCESS)
                        vm.put("message", Message.info(String.format("Success! You logged in as %s", username)));
                    else
                        vm.put("message", Message.info(String.format("Welcome back %s!", username)));
                }
                break;

            case INVALID:
                vm.put("message", Message.error("Your name must be only alphanumeric characters, no more than 13 characters long and start with a capital."));
                break;

            case NAME_TAKEN:
                vm.put("message", Message.error("That name is already in use, please select another"));
                break;
        }




        // render the View
        return templateEngine.render(new ModelAndView(vm , "sign-in.ftl"));
    }
}
