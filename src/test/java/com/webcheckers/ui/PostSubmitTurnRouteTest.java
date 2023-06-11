package com.webcheckers.ui;


import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.entity.Player;
import com.webcheckers.model.states.PieceColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Submit Turn Route Test Class
 *
 * @author Hannah Hlotyak, heh7382@rit.edu
 */
public class PostSubmitTurnRouteTest {
    private PostSubmitTurnRoute CuT;
    private Gson gson;

    //Friendly objects
    private PlayerLobby playerLobby;

    //Mock objects
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private Player player;
    private Game game;
    private PieceColor color;


    /**
     * Mock of object classes above
     */
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        player = mock(Player.class);
        playerLobby = mock(PlayerLobby.class);
        game = mock(Game.class);

        when(session.attribute("player")).thenReturn(player);
        CuT = new PostSubmitTurnRoute(gson);
    }

    @Test
    public void newSubmitTurn(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        Player redPlayer = new Player("redPlayer");
        Player whitePlayer = new Player("whitePlayer");

        Game game = new Game(redPlayer, whitePlayer);
        PieceColor color = game.getActiveColor();

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        assertNotSame(color, game.getActiveColor());

        testHelper.assertViewName("game.ftl");
        testHelper.assertViewModelAttribute("currentUser", whitePlayer);
        testHelper.assertViewModelAttribute("redPlayer", redPlayer);
        testHelper.assertViewModelAttribute("whitePlayer", whitePlayer);


    }

}
