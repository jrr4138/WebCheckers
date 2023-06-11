package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The UI HomePage tester class
 *
 * @author <a href='jpk7948@rit.edu'>Justin Kennedy</a>
 */
public class GetHomeRouteTest {
    private GetHomeRoute CuT;

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

        playerLobby = mock(PlayerLobby.class);
        CuT = new GetHomeRoute(playerLobby, templateEngine);
    }

    @Test
    public void new_session() {
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.WELCOME_TITLE);
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE_ATTR, GetHomeRoute.WELCOME_MSG);
        //   * test view name
        testHelper.assertViewName("home.ftl");
        //   * verify that a player service object and the session timeout watchdog are stored
        //   * in the session.
        verify(session).attribute(eq(GetHomeRoute.PLAYERLOBBY_KEY), any(PlayerLobby.class));
        verify(session).attribute(GetHomeRoute.CURRENT_PLAYER);

        testHelper.assertViewModelAttributeIsAbsent(GetHomeRoute.CURRENT_PLAYER);
    }

    @Test
    public void old_session() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        Set<Player> players = new HashSet<>();
        players.add(new Player("Justin"));
        players.add(new Player("Alli"));
        players.add(new Player("Hannah"));
        players.add(new Player("Sam"));
        players.add(new Player("Josh"));

        Player player = new Player("Justin");

        Set<Player> opponents = new HashSet<>();
        opponents.addAll(players);
        opponents.remove(player);

        when(playerLobby.getOnlinePlayers()).thenReturn(players);
        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player);

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewName("home.ftl");
        //   * verify there is a player logged in
        testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_PLAYER, player);
        //   * verify there is a list of opponents
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_ONLINE, opponents);
        opponents = (Set<Player>) testHelper.getViewModelAttribute(GetHomeRoute.PLAYERS_ONLINE);
        //   * verify the list of opponents does not contain the player
        assertFalse(opponents.contains(player));
    }

}
