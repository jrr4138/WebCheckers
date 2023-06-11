package com.webcheckers.model.entity;

import com.webcheckers.model.states.AIDifficulty;
import com.webcheckers.model.states.PlayerAchievements;
import com.webcheckers.model.states.PlayerStates;
import com.webcheckers.model.states.PieceColor;
import com.webcheckers.model.entity.Game;

import java.util.HashSet;

public class Player {

    //Attributes
    protected String name;
    private PieceColor teamColor;
    private PlayerStates currentState;

    protected int wins;
    protected int losses;
    protected float winPercentage;

    private Game game;
    protected HashSet<Integer> achievements;
    protected int winStreak;


    public Player(String name){
        this.name = name;
        this.currentState = PlayerStates.IN_LOBBY;
        achievements = new HashSet<>();
        winStreak = 0;
    }

    /**
     * Gets the name of the player
     * @return username of player
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the wins of player
     * @return wins player has
     */
    public int getWins(){ return this.wins;}

    /**
     * Gets the losses of player
     * @return losses player has
     */
    public int getLosses(){ return this.losses;}

    /**
     * Gets the win percentage of player
     * @return players win percentage
     */
    public float getWinPercentage(){ return this.winPercentage; }

    public synchronized String formatPlayerAverage(){
        String FORMAT = "%.1f%%";
        calcWinPercentage();
        if (this.winPercentage == -1) {
            return "N/A";
        }
        else {
            return String.format(FORMAT, this.winPercentage);
        }
    }

    /**
     * Calculates the win percentage of player
     */
    public void calcWinPercentage(){
        if (this.wins +  this.losses >= 1){
            this.winPercentage = (this.wins * 100.0f)/ (this.wins + this.losses);
        }
        else{
            this.winPercentage = -1;
        }
    }

    /**
     * Returns if state is IN_LOBBY
     * @return boolean
     */
    public boolean isInLobby(){
        return currentState == PlayerStates.IN_LOBBY;
    }

    /**
     * Gets whether the player is in a game or not
     * @return true if the player is in a game, false if not
     */
    public boolean isInGame() {
         return game != null;
    }

    /**
     * Gets the game the player is currently in
     * @return Game object the player is in
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Sets the game of the player
     */
    public void setInGame(Game game) {
        this.currentState = PlayerStates.IN_GAME;
        this.game = game;
    }

    public void setChallenged(Game game) {
        this.game = game;
    }

    /**
     * Accepts the challenge from the opponent and marks they are in the game
     * @return True if challenge accepted, false if there was no game associated with them
     */
    public boolean acceptChallenge() {
        if(game == null) {
            return false;
        }

        this.currentState = PlayerStates.IN_GAME;
        return true;
    }

    /**
     * Ends the game the player is in
     */
    public void endGame() {
        this.currentState = PlayerStates.IN_LOBBY;
        this.teamColor = null;
        this.game = null;
    }

    @Override
    public boolean equals(Object obj){
        if(! (obj instanceof Player))
            return false;

        return ((Player) obj).name.equals(this.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }


    public void setTeamColor(PieceColor teamColor) {
        this.teamColor = teamColor;
    }

    public PieceColor getTeamColor() {
        return this.teamColor;
    }

    /**
     * Changes state of player to Won
     */
    public void justWon() {
        this.currentState = PlayerStates.WON;

        Player opponent = (this.teamColor == PieceColor.RED) ? game.getWhitePlayer() : game.getRedPlayer();
        if(opponent instanceof AI) {
            PlayerAchievements achievement = (((AI) opponent).getDifficulty() == AIDifficulty.HARD) ? PlayerAchievements.DEFEAT_AI_HARD : PlayerAchievements.DEFEAT_AI_EASY;

            //Add correct ID for defeat easy AI or hard AI
            int achievementID = (achievement == PlayerAchievements.DEFEAT_AI_EASY) ? 0 : 1;
            achievements.add(achievementID);
        }

        winStreak++;
        wins++;

        if(winStreak >= 3) {
            //Achievement ID for win streak is 2
            achievements.add(2);
        }
    }

    /**
     * returns if state of player is Won
     */
    public boolean hasWon() {
        return this.currentState == PlayerStates.WON;
    }

    /**
     * returns if state of player has lost
     */
    public boolean hasLost() {
        return this.currentState == PlayerStates.LOSS;
    }

    /**
     * Changes state of player to Loss
     */
    public void justLost() {
        this.currentState = PlayerStates.LOSS;

        achievements.remove(2);
        winStreak = 0;

        losses++;
    }

    /**
     * Marks a player as spectating
     */
    public void setToSpectator(Game game){
        this.currentState = PlayerStates.SPECTATING;
        this.game = game;
    }

    /**
     * Gets if the user is currently in spectating mode
     * @return True if spectating another game
     */
    public boolean getSpectating() { return this.currentState == PlayerStates.SPECTATING; }

    /**
     * Sets the user out of spectating mode and disassociates the game with them
     */

    public void stopSpectating() {
        this.currentState = PlayerStates.IN_LOBBY;
        game.removeSpectator(this);
        this.game = null;

    }


    public HashSet<Integer> getAchievements() {
        return this.achievements;
    }

    public int getWinStreak() {
        return this.winStreak;
    }
}
