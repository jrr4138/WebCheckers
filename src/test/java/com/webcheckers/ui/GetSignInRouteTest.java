package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Sign-In Route Test Class
 *
 * @author <a href='jrr4138@rit.edu'>Joshua Ross</a>
 */
public class GetSignInRouteTest {

  private GetSignInRoute CuT;
  private PlayerLobby playerLobby;
  private Response response;
  private TemplateEngine engine;
  private Request request;
  private Session session;

  @BeforeEach
  public void initialization() {
    // setup
    request = mock(Request.class);
    session = mock(Session.class);
    when(request.session()).thenReturn(session);
    response = mock(Response.class);
    engine = mock(TemplateEngine.class);

    CuT = new GetSignInRoute(engine);
  }

  @Test
  public void testSignIn(){
    final TemplateEngineTester engineTester = new TemplateEngineTester();
    when(engine.render(any(ModelAndView.class))).thenAnswer(engineTester.makeAnswer());

    CuT.handle(request, response);

    engineTester.assertViewModelExists();
    engineTester.assertViewModelIsaMap();
    engineTester.assertViewModelAttributeIsAbsent(GetHomeRoute.CURRENT_PLAYER);
  }
}
