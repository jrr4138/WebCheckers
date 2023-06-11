package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.Message;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.states.AIDifficulty;
import com.webcheckers.model.states.GameState;
import com.webcheckers.model.entity.Player;
import com.webcheckers.model.states.MessageType;
import com.webcheckers.model.states.ViewMode;
import com.webcheckers.model.states.PieceColor;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetGameRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

  private final TemplateEngine templateEngine;

  private Gson gson;
  public static final String CURRENT_PLAYER = "currentUser";

  /**
   * Create the Spark Route (UI controller) for the
   * {@code GET /} HTTP request.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetGameRoute(final TemplateEngine templateEngine, Gson gson) {
    // validation
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    //
    this.templateEngine = templateEngine;
    this.gson = gson;
    //
    LOG.config("GetGameRoute is initialized.");
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
    LOG.finer("GetGameRoute is invoked.");

    Map<String, Object> vm = new HashMap<>();
    Session httpSession = request.session();
    Player user = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
    PlayerLobby playerLobby = httpSession.attribute(GetHomeRoute.PLAYERLOBBY_KEY);

    if (user == null) {
      httpSession.attribute(GetHomeRoute.ERROR_KEY, new Message("You must sign in before you can play a game", MessageType.ERROR));
      response.redirect("/");
      throw halt(401);
    }


    String opponentName = request.queryParams("opponentName");
    if (opponentName != null ) {
      //Check the user is not already in a game
      if(user.isInGame()) {
        httpSession.attribute(GetHomeRoute.ERROR_KEY, new Message("Cannot challenge another user as you are already in an active game", MessageType.ERROR));
        response.redirect("/");
        throw halt(401);
      }

      //case for medium difficulty
      if(opponentName.equals("computer")) {
        playerLobby.challengeComputer(user, AIDifficulty.EASY);
      }
      //case for high difficulty
      else if (opponentName.equals("sbeve")) {
        playerLobby.challengeComputer(user, AIDifficulty.HARD);
      }
      else if (opponentName.equals("jump")) {
        playerLobby.challengeJumpBoard(user);
      }
      else if (opponentName.equals("double")) {
        playerLobby.challengeDoubleJumpBoard(user);
      }
      else if (opponentName.equals("triple")) {
        playerLobby.challengeTripleJumpBoard(user);
      }
      else if (opponentName.equals("king")) {
        playerLobby.challengeKingMeBoard(user);
      }
      else if (opponentName.equals("oom")) {
        playerLobby.challengeOutOfMovesBoard(user);
      }
      else {
        //Check the opponent exists
        if(!playerLobby.playerExists(opponentName)) {
          httpSession.attribute(GetHomeRoute.ERROR_KEY, new Message("That user does not exist! Please try again", MessageType.ERROR));
          response.redirect("/");
          throw halt(401);
        }

        if(!playerLobby.playerOnline(opponentName)) {
          httpSession.attribute(GetHomeRoute.ERROR_KEY, new Message("That player is currently offline! Please try again", MessageType.ERROR));
          response.redirect("/");
          throw halt(401);
        }

        Player opponent = playerLobby.getPlayer(opponentName);

        //Check the opponent is in a game
        if(playerLobby.isInGame(opponent)) {
          httpSession.attribute(GetHomeRoute.ERROR_KEY, new Message("That user is already in a game, please try again", MessageType.ERROR));
          response.redirect("/");
          throw halt(401);
        }

        playerLobby.challenge(user, opponent);
        response.redirect("/game");
        throw halt(402);
      }
    }
    Game game = playerLobby.getGame(user);

    //Check the game exists
    if(game == null) {
      httpSession.attribute(GetHomeRoute.ERROR_KEY, new Message("You do not have an active game, please try again", MessageType.ERROR));
      response.redirect("/");
      throw halt(401);
    }

    GameState gameState = game.getGameState();
    if(gameState != GameState.ACTIVE){
      String gameOverMessage = "";
      switch(gameState) {
        case WHITE_RESIGN:
          gameOverMessage = String.format("%s resigned", game.getWhitePlayer().getName());
          break;
        case RED_RESIGN:
          gameOverMessage = String.format("%s resigned", game.getRedPlayer().getName());
          break;
        case RED_WIN:
          gameOverMessage = String.format("%s wins!", game.getRedPlayer().getName());
          break;
        case WHITE_WIN:
          gameOverMessage = String.format("%s wins!", game.getWhitePlayer().getName());
          break;
      }
      final Map<String, Object> modeOptions = new HashMap<>(2);
      modeOptions.put("isGameOver", true);
      modeOptions.put("gameOverMessage", gameOverMessage);
      vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
    }

    vm.put("title", "Game!");
    vm.put(GetHomeRoute.CURRENT_PLAYER, user);
    if(playerLobby.isSpectating(user)){
      vm.put("viewMode", ViewMode.SPECTATOR);
      vm.put("isSpectating", true);
      Player p = httpSession.attribute(GetSpectatingRoute.PLAYER_TO_WATCH);
      if(p.equals(game.getRedPlayer())){
        vm.put("board", game.getBoardView(PieceColor.RED));
      } else {
        vm.put("board", game.getBoardView(PieceColor.WHITE));
      }
      vm.put("spectating", p);
    } else if (playerLobby.isInGame(user)){
      vm.put("viewMode", ViewMode.PLAY);
      vm.put(GetHomeRoute.IN_GAME, true);
      vm.put("board", game.getBoardView(user.getTeamColor()));
    }
    
    vm.put("spectators", game.getAllSpectator());
    vm.put("redPlayer", game.getRedPlayer());
    vm.put("whitePlayer", game.getWhitePlayer());
    vm.put("activeColor", game.getActiveColor());
    

    return templateEngine.render(new ModelAndView(vm , "game.ftl"));
  }
}
