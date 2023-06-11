package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.board.Board;
import com.webcheckers.model.entity.Player;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.board.BoardView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import spark.*;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Sign-In Route Test Class
 *
 * @author <a href='sjb8894@rit.edu'>Sam Benoist</a>
 */
public class GetSpectatingRouteTest {

    private GetSpectatingRoute CuT;
    private PlayerLobby playerLobby;
    private Response response;
    private Game game;
    private TemplateEngine engine;
    private Request request;
    private Session session;
    private Board board;
    private BoardView boardView;


    @BeforeEach
    public void setup() {
        // setup
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        boardView = mock(BoardView.class);
        game = mock(Game.class);
        board = mock(Board.class);

        playerLobby = mock(PlayerLobby.class);

        when(playerLobby.getGame(any(Player.class))).thenReturn(game);
        when(game.getBoard()).thenReturn(board);

        when(session.attribute("spectator")).thenReturn(playerLobby);


        CuT = new GetSpectatingRoute(engine);


    }

    @Test
    public void testSession() throws Exception {
        ArrayList<String> allSpectators = new ArrayList<>();
        Player spectator = new Player("Spectator");

        allSpectators.add(spectator.getName());

        when(session.attribute("spectator")).thenReturn(spectator);
        when(game.getSpectators()).thenReturn(allSpectators);

        when(session.attribute("SPECTATOR")).thenReturn(spectator);
        final TemplateEngineTester tester = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(tester.makeAnswer());

        when(request.queryParams("name")).thenReturn("Spectator");
        // issue w/ calling thenReturn on null
        //when(request.queryParams("opt")).thenReturn(spectator.stopSpectating());


        CuT.handle(request, response);

        tester.assertViewModelExists();
        tester.assertViewModelIsaMap();
        tester.assertViewName("game.ftl");
        tester.assertViewModelAttribute("viewMode", "SPECTATOR");
        tester.assertViewModelAttribute("board", game.getBoard());


    }
}
