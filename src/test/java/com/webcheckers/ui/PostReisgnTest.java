package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.entity.Player;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.states.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostReisgnTest {
    private PostResignRoute CuT;

    // mock objects
    private Request request;
    private Session session;
    private Gson gson;
    private Response response;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        gson = new Gson();

        CuT = new PostResignRoute(gson);
    }

    @Test
    public void resign_player() {
        //Simulate players signed in
        PlayerLobby playerLobby = new PlayerLobby();

        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");

        playerLobby.addPlayer(player1.getName());
        playerLobby.addPlayer(player2.getName());

        //Start game between player1 and player2
        playerLobby.challenge(player1, player2);

        //Mock session objects
        when(session.attribute(GetHomeRoute.PLAYERLOBBY_KEY)).thenReturn(playerLobby);
        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player1);

        // Invoke the test
        CuT.handle(request, response);

        //Get game objects
        Game game1 = playerLobby.getGame(player1);
        Game game2 = playerLobby.getGame(player2);

        //Assert game no longer exists for player1
        assertNull(game1);
        //Assert game ended with red (player1) resigning for player2
        assertEquals(game2.getGameState(), GameState.RED_RESIGN);

        //Assert players who resigned is no longer in a game
        assertFalse(player1.isInGame());

        //Assert opponent is still in game until they return to the home page
        assertTrue(player2.isInGame());

        //Send player2 to the home page
        Request request2 = mock(Request.class);
        when(request2.session()).thenReturn(session);
        Response response2 = mock(Response.class);
        TemplateEngine templateEngine = mock(TemplateEngine.class);

        GetHomeRoute homeRoute = new GetHomeRoute(playerLobby, templateEngine);

        when(session.attribute(GetHomeRoute.CURRENT_PLAYER)).thenReturn(player2);

        homeRoute.handle(request2, response2);

        //Assert player2 is no longer in a game after going home
        assertFalse(player2.isInGame());
        assertNull(playerLobby.getGame(player2));
    }
}
