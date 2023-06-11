package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.entity.Player;
import com.webcheckers.model.entity.AI;
import com.webcheckers.model.states.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for the GetGameRoute
 */

 public class GetGameRouteTest{

    private GetGameRoute gameRoute;

    private Game game;
    private Gson gson;
    private Response response;
    private TemplateEngine engine;
    private Request request;
    private Session session;

    @BeforeEach
    public void initializeGameRoute() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        gson = new Gson();
        //make player lobby

        gameRoute = new GetGameRoute(engine, gson);
    }

    @Test
    public void testGameInfo(){
        final TemplateEngineTester engineTester = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(engineTester.makeAnswer());

        PlayerLobby playerLobby = new PlayerLobby();

        // Return the current user when asked
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        playerLobby.addPlayer(player1.getName());
        playerLobby.addPlayer(player2.getName());

        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);

        playerLobby.challenge(player1, player2);

        when(session.attribute(GetHomeRoute.PLAYERLOBBY_KEY)).thenReturn(playerLobby);

        // Test statements
        gameRoute.handle(request, response);
        engineTester.assertViewModelExists();
        engineTester.assertViewModelIsaMap();
        engineTester.assertViewName("game.ftl");
        engineTester.assertViewModelAttribute("currentUser", player1);
        engineTester.assertViewModelAttribute("redPlayer", player1);
        engineTester.assertViewModelAttribute("whitePlayer", player2);
        engineTester.assertViewModelAttribute("activeColor", PieceColor.RED);

    }

    @Test
    public void test_nullPlayer(){
        //user = null;
        //when(session.attribute(GetHomeRoute.CURRENT_USER_KEY)).thenReturn([player1]);
        //when(session.attribute(GetGameRoute.CURRENT_OPPONENT_KEY)).thenReturn(null);
        try{
            gameRoute.handle(request, response);
        }catch(HaltException e){
            assertTrue(e instanceof HaltException);
        }
    }

 }