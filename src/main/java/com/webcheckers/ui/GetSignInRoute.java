package com.webcheckers.ui;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class GetSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /signin} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetSignInRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("GetSignInRoute is initialized.");
    }

    /**
     * Render the WebCheckers Sign in page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Sign in page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSignInRoute is invoked.");

        Session httpSession = request.session();

        Map<String, Object> vm = new HashMap<>();

        vm.put("title", "Sign In");
        vm.put(GetHomeRoute.CURRENT_PLAYER, httpSession.attribute(GetHomeRoute.CURRENT_PLAYER));

        // render the View
        return templateEngine.render(new ModelAndView(vm , "sign-in.ftl"));
    }

}
