package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;



public class GetRulesRouteTest {
    private GetRulesRoute CuT;

    // friendly objects
    private PlayerLobby playerLobby;

    // mock objects
    private Request request;
    private Session session;
    private TemplateEngine templateEngine;
    private Response response;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        templateEngine = mock(TemplateEngine.class);

        playerLobby = new PlayerLobby();
        CuT = new GetRulesRoute(templateEngine);
    }

    @Test
    public void new_session() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetRulesRoute.TITLE_ATTR, GetRulesRoute.WELCOME_TITLE);
        testHelper.assertViewModelAttribute(GetRulesRoute.MESSAGE_ATTR, GetRulesRoute.WELCOME_MSG);
        //   * test view name
        testHelper.assertViewName("rules.ftl");


    }
    @Test
    public void old_session() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("rules.ftl");

    }
}

