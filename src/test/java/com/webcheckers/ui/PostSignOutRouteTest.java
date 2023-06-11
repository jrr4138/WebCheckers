package com.webcheckers.ui;


import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



/**
 * Sign-Out Route Test Class
 *
 * @author <a href='sjb8894@rit.edu'>Sam Benoist</a>
 */
public class PostSignOutRouteTest {
    private PostSignOutRoute CuT;


    private PlayerLobby playerLobby;

    // Mock objects
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private Player player;

    /** Mock of object classes above **/
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        player = mock(Player.class);

        when(session.attribute("player")).thenReturn(player);
        CuT = new PostSignOutRoute(engine);
    }

    @Test
    public void newSignOut() throws Exception {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        PlayerLobby playerLobby = new PlayerLobby();
        Player player = new Player("Player1");
        playerLobby.addPlayer(player.getName());

        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player);
        when(session.attribute(GetHomeRoute.PLAYERLOBBY_KEY)).thenReturn(playerLobby);

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("home.ftl");
        testHelper.assertViewModelAttribute("player", null);
    }
}
