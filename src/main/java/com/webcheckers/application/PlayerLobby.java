package com.webcheckers.application;

import com.webcheckers.model.entity.AI;
import com.webcheckers.model.entity.Player;

import com.webcheckers.model.states.AIDifficulty;

import com.webcheckers.model.entity.PlayerComp;

import com.webcheckers.ui.GetHomeRoute;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.states.PieceColor;
import com.webcheckers.model.states.GameState;

import java.util.*;
import java.util.logging.Logger;

/**
 * The component of online and signed in checkers players
 *
 * @author <a href='jpk7948@rit.edu'>Justin Kennedy</a>
 */
public class PlayerLobby {

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    public enum SignInResult {SUCCESS, NAME_TAKEN, INVALID, RETURN_SUCCESS}
    public enum SignOutResult {SIGNOUT_SUCCESS, DNE}

    //Attributes
    private final Set<Player> playersOnline;
    private final Set<Player> allPlayers;
    private final String VALID_USERNAME = "^[A-Z]{1}[A-Za-z0-9]{0,12}$";

    /**
     * Constuctor
     */
    public PlayerLobby(){
        this.allPlayers = new HashSet<>();
        this.playersOnline = new HashSet<>();

        initialize();
    }

    private void initialize() {
        AI easy = new AI(AIDifficulty.EASY);
        AI hard = new AI(AIDifficulty.HARD);

        playersOnline.add(easy);
        playersOnline.add(hard);
        allPlayers.add(easy);
        allPlayers.add(hard);
    }

    /**
     *  Validates username from Regex pattern detecting alphanumeric characters
     * @param username
     * @return
     */
    private boolean isValidUsername(String username) {
        return username.matches(VALID_USERNAME);
    }

    /**
     *  Checks if username is in use
     * @param username
     * @return True represents username in use, false represents available
     */
    public boolean usernameAvailable(String username) {
        for(Player player : allPlayers) {
            if(player.getName().equals(username))
                return false;
        }

        return true;
    }

    /**
     * returns true if player was added or false if username is already taken
     * @param username
     * @return
     */
    public SignInResult addPlayer(String username) {

        Player player;
        if(usernameAvailable(username)) {
            player = new Player(username);

            if(!isValidUsername(username)) {
                return SignInResult.INVALID;
            }
            else if(playersOnline.add(player)) {
                allPlayers.add(player);
                LOG.info(String.format("User %s has signed in.", username));
                return SignInResult.SUCCESS;
            }
            else {
                return SignInResult.NAME_TAKEN;
            }
        } else if (!playersOnline.contains(getPlayer(username))) {
            player = getPlayer(username);
            LOG.info(String.format("User %s has signed in.", username));
            playersOnline.add(player);
            return SignInResult.RETURN_SUCCESS;
        }

        return SignInResult.NAME_TAKEN;
    }

    /**
     * Tries to sign player out
     * @param username
     * @return Result of operation to lobby
     */
    public SignOutResult signPlayerOut(String username) {
        Player player = getPlayer(username);
        if (player != null) {
            playersOnline.remove(player);

            LOG.info(String.format("User %s signed out.", player.getName()));

            return SignOutResult.SIGNOUT_SUCCESS;
        }
        return SignOutResult.DNE;
    }

    /**
     * Gets the player with name {@code username}
     * @param username
     * @return {@link Player} object with {@code username}
     */
    public Player getPlayer(String username) {
        for (Player player : allPlayers) {
            if(player.getName().equals(username))
                return player;
        }

        return null;
    }

    /**
     * Returns if the username exists or not
     * @param username
     * @return True if the user exists
     */
    public boolean playerExists(String username) {
        return !usernameAvailable(username);
    }

    /**
     * Returns if the player is online or not
     * @param username
     * @return True if the player is online, false if offline or does not exist
     */
    public boolean playerOnline(String username) {
        for(Player player : playersOnline) {
            if(player.getName().equals(username))
                return true;
        }

        return false;
    }

    public Set<Player> getOnlinePlayers() {
        return playersOnline;
    }

    /**
     * Returns a sorted list of players based on win percentage
     */
    public ArrayList<Player> listOfPlayers(){
        ArrayList<Player> playerList = new ArrayList<>();
        for (Player tmp: playersOnline){
            playerList.add(tmp);
        }
        Collections.sort(playerList, new PlayerComp());
        return playerList;
    }

    public HashMap<String, String> getSpectateableGames(Player player) {
        //ArrayList<Player> activeGames = new ArrayList<>();
        HashMap<String, String> players = new HashMap<>();

        Game game = getGame(player);

        //For each online player
        for (Player viewpoint : playersOnline) {
            //If they're in game
            if(viewpoint.isInGame() && !isSpectating(viewpoint)) {
                Game game_vp = getGame(viewpoint);
                //And the player is not the current user nor their opponent OR the current user is not in a game
                if( ( game != null && viewpoint != game.getRedPlayer() && viewpoint != game.getWhitePlayer() ) || game == null) {
                    //Add as a spectate-able viewpoint
                    //activeGames.add(viewpoint);
                    if(game_vp.getGameState() == GameState.ACTIVE) players.put(game_vp.getRedPlayer().getName(), game_vp.getWhitePlayer().getName());
                }
            }
        }
        return players;
    }


    /**
     * Allows one player to challenge another and adds them to a game
     *
     * @param player player who is doing the challenging
     * @param challenging player who is being challenged
     *
     */
    public void challenge(Player player, Player challenging){
        Game game = new Game(player, challenging);
        player.setTeamColor(PieceColor.RED);

        if(challenging instanceof AI) {
            ((AI) challenging).addGame(game);
        } else {
            challenging.setTeamColor(PieceColor.WHITE);
            challenging.setChallenged(game);
        }

        player.setInGame(game);
    }

    /**
     * Challenges a Computer AI and puts them both in a game
     * @param player
     * @param difficulty
     */
    public void challengeComputer(Player player, AIDifficulty difficulty) {
        AI computer = new AI(difficulty);
        Game game = new Game(player, computer);
        player.setTeamColor(PieceColor.RED);
        player.setInGame(game);
        computer.addGame(game);
    }

    /**
     * Challenges a Computer AI and puts them both in a game where only move is a single jump
     * @param player
     */
    public void challengeJumpBoard(Player player){
        AI computer = new AI(AIDifficulty.DEMO);
        Game game = new Game(player, computer, "jumpBoard");
       // game.setBoard("jumpBoard");
        player.setTeamColor(PieceColor.RED);
        player.setInGame(game);
        computer.addGame(game);
    }

    /**
     * Challenges a Computer AI and puts them both in a game where only move is a double jump
     * @param player
     */
    public void challengeDoubleJumpBoard(Player player){
        AI computer = new AI(AIDifficulty.DEMO);
        Game game = new Game(player, computer, "doubleJumpBoard");
      //  game.setBoard("doubleJumpBoard");
        player.setTeamColor(PieceColor.RED);
        player.setInGame(game);
        computer.addGame(game);
    }

    /**
     * Challenges a Computer AI and puts them both in a game where only move is a double jump
     * @param player
     */
    public void challengeTripleJumpBoard(Player player){
        AI computer = new AI(AIDifficulty.DEMO);
        Game game = new Game(player, computer, "tripleJumpBoard");
       // game.setBoard("tripleJumpBoard");
        player.setTeamColor(PieceColor.RED);
        player.setInGame(game);
        computer.addGame(game);
    }

    /**
     * Challenges a Computer AI and puts them both in a game where only move is a double jump
     * @param player
     */
    public void challengeKingMeBoard(Player player){
        AI computer = new AI(AIDifficulty.DEMO);
        Game game = new Game(player, computer, "kingBoard");
       // game.setBoard("kingBoard");
        player.setTeamColor(PieceColor.RED);
        player.setInGame(game);
        computer.addGame(game);
    }

    /**
     * Challenges a Computer AI and puts them both in a game where only move is a double jump
     * @param player
     */
    public void challengeOutOfMovesBoard(Player player){
        AI computer = new AI(AIDifficulty.DEMO);
        Game game = new Game(player, computer, "outOfMovesBoard");
       // game.setBoard("outOfMovesBoard");
        player.setTeamColor(PieceColor.RED);
        player.setInGame(game);
        computer.addGame(game);
    }

    /**
     * Gets if the player has a game associated with them
     * @param player
     */
    public boolean isInGame(Player player) {
        return player != null && player.isInGame();
    }

    /**
     * Gets if the game they were playing is over
     * @param player
     * @return True if game is null or inactive, false if game is still going
     */
    public boolean gameIsOver(Player player) {
        Game game = player.getGame();

        if(game != null) {
            return !game.isGameAlive();
        }

        return true;
    }

    public void leaveGame(Player player) {
        player.endGame();
    }

    /**
     * Sets that the player is now in an active game
     * @param player
     */
    public void acceptChallenge(Player player) {
        player.acceptChallenge();
    }

    /**
     * Checks if the player has just been challenged (i.e. They have a registered game but are not in the in-game state)
     * @param player
     * @return
     */
    public boolean justChallenged(Player player) {
        return isInGame(player) && player.isInLobby();
    }

    public void resignGame(Player playerResigning){
        Game game = getGame(playerResigning);
        game.resign(playerResigning);

        playerResigning.endGame();
    }

    public Game getGame(Player player){
        return player.getGame();
    }

    /**
     * Sets a player to spectator and adds them to the game
     *
     * @param player player to be added 
     * @param game game to add the player too
     */
    public void addSpectator(Player player, Game game){
        player.setToSpectator(game);
        game.addSpectator(player);
    }

    /**
     * Gets a game for the watching player and adds the new player as a spectator
     * 
     * @param watching existing player
     * @param player player to be added to spectators
     */
    public void addSpectator(Player player, Player watching){
        Game game = getGame(watching);
        addSpectator(player, game);
        player.setToSpectator(game);
    }

    /**
     * Gets the total number of spectators for a given game   
     */ 
    public int getAllSpectator(Player player){
        Game game = getGame(player);
        return game.getAllSpectator();
    }

    public boolean isSpectating(Player player) {
        return player.getSpectating();
    }

    public void stopSpectating(Player player) {
        player.stopSpectating();
    }

    public int getWinStreak(Player player) {
        return player.getWinStreak();
    }

    public void backupMove(Player activePlayer) {
        Game game = getGame(activePlayer);

        game.undoMove();
    }

    public boolean hasJumpsLeft(Player activePlayer) {
        Game game = getGame(activePlayer);

        return game.canStillMove();
    }

    public boolean missedJumpMove(Player activePlayer) {
        Game game = getGame(activePlayer);

        return game.nonJumpMove() && MovePiece.playerHasValidJumpMove(game);
    }

    public void finishTurn(Player activePlayer) {
        Game game = getGame(activePlayer);

        game.changeTurns();
    }

}
