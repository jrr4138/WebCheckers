package com.webcheckers.ui;

import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Player;

import com.webcheckers.model.states.MessageType;
import spark.*;

import com.webcheckers.application.Message;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='jpk7948@rit.edu'>Justin Kennedy</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  public static final String MESSAGE_ATTR = "message";
  public static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  public static final String WIN_STREAK_MESSAGE = "You're on fire! You're on a win streak of %d!";
  public static final String TITLE_ATTR = "title";
  public static final String WELCOME_TITLE = "Welcome!";
  public static final String CURRENT_PLAYER = "currentUser";
  public static final String PLAYERLOBBY_KEY = "playerLobby";
  public static final String PLAYERS_ONLINE = "playersOnline";
  public static final String ERROR_KEY = "Error";
  public static final String IN_GAME = "userInGame";
  public static final String ACTIVE_GAMES = "gameList";

  private final TemplateEngine templateEngine;
  private final PlayerLobby playerLobby;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final PlayerLobby playerLobby, TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required");
    //
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetHomeRoute is invoked.");

    Session httpSession = request.session();

    if(httpSession.attribute(PLAYERLOBBY_KEY) == null) {
      httpSession.attribute(PLAYERLOBBY_KEY, playerLobby);
    }

    Map<String, Object> vm = new HashMap<>();
    vm.put(TITLE_ATTR, WELCOME_TITLE);

    Player currentPlayer = httpSession.attribute(CURRENT_PLAYER);

    // supply user's name if logged in
    if(currentPlayer != null) {
      if(playerLobby.getPlayer(currentPlayer.getName()) == null) {
        httpSession.attribute(GetHomeRoute.ERROR_KEY, new Message("Error: that user no longer exists, please try again", MessageType.ERROR));
        response.redirect("/");
        throw halt(401);
      }

      if(playerLobby.isInGame(currentPlayer)) {
        if(playerLobby.isSpectating(currentPlayer)) {
          playerLobby.stopSpectating(currentPlayer);
        }

        vm.put(IN_GAME, true);
        if (playerLobby.gameIsOver(currentPlayer)) {
          playerLobby.leaveGame(currentPlayer);
          vm.put(IN_GAME, false);
        } else if (playerLobby.justChallenged(currentPlayer)) {
          playerLobby.acceptChallenge(currentPlayer);
          response.redirect("/game");
        }
      }
      vm.put(CURRENT_PLAYER, currentPlayer);
    }

    //Add online players to display them as opponents or display total online players
    vm.put(PLAYERS_ONLINE, playerLobby.listOfPlayers());

    String demo = request.queryParams("name");
    if(demo != null ){
      if(demo.equals("demomode")){
        System.out.println("TRUE");
        vm.put("isDemo", true);
      }
    } else {
      vm.put("isDemo", false);
    }

    // display a user message in the Home page
    if(httpSession.attribute(ERROR_KEY) == null) {
      if(currentPlayer != null && playerLobby.getWinStreak(currentPlayer) >= 3) {
          vm.put(MESSAGE_ATTR, new Message(String.format(WIN_STREAK_MESSAGE, playerLobby.getWinStreak(currentPlayer)), MessageType.INFO));
      } else {
        vm.put(MESSAGE_ATTR, WELCOME_MSG);
      }
    } else {
      vm.put(MESSAGE_ATTR, httpSession.attribute(ERROR_KEY));
      httpSession.attribute(ERROR_KEY, null);
    }

    if(currentPlayer != null){
      vm.put(ACTIVE_GAMES, playerLobby.getSpectateableGames(currentPlayer));
    }

    // render the View
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }
}
