package com.webcheckers.application;

import com.webcheckers.model.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import com.webcheckers.application.PlayerLobby.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The PlayerLobby class tester
 *
 * @author <a href='jpk7948@rit.edu'>Justin Kennedy</a>
 */
public class PlayerLobbyTest {

    private static final String VALID_NAME1 = "Justin";
    private static final String VALID_NAME2 = "JustinK43";
    private static final String INVALID_NAME1 = "Justin K";
    private static final String INVALID_NAME2 = "justin";
    private static final String INVALID_NAME3 = "Justin:)";

    private PlayerLobby CuT;

    @BeforeEach
    public void setup(){
        CuT = new PlayerLobby();
    }

    @Test
    public void test_create_service() {
        //verify components
        Object players = CuT.getOnlinePlayers();
        //  * player list exists
        assertNotNull(players);
        //  * player list is a set
        assertTrue(players instanceof Set);
        //  * player list is empty
        assertTrue(((Set<?>)players).isEmpty());
    }

    @Test
    public void test_invalid_names() {
        // Test name with space
        SignInResult result1 = CuT.addPlayer(INVALID_NAME1);
        assertEquals(result1, SignInResult.INVALID);

        // Test name with no capital
        SignInResult result2 = CuT.addPlayer(INVALID_NAME2);
        assertEquals(result2, SignInResult.INVALID);

        // Test name with non-alphanumeric characters
        SignInResult result3 = CuT.addPlayer(INVALID_NAME3);
        assertEquals(result3, SignInResult.INVALID);
    }

    @Test
    public void test_valid_names() {
        // Test name with only alphabetic characters
        SignInResult result1 = CuT.addPlayer(VALID_NAME1);
        assertEquals(result1, SignInResult.SUCCESS);

        // Test name with multiple capitals and numeric characters
        SignInResult result2 = CuT.addPlayer(VALID_NAME2);
        assertEquals(result2, SignInResult.SUCCESS);

        // Test players are now in online player list
        Player player1 = new Player(VALID_NAME1);
        Player player2 = new Player(VALID_NAME2);

        Set<Player> onlinePlayers= CuT.getOnlinePlayers();

        assertTrue(onlinePlayers.contains(player1));
        assertTrue(onlinePlayers.contains(player2));
    }

    @Test
    public void test_duplicate_names() {
        // Add valid player
        CuT.addPlayer(VALID_NAME1);

        // Test adding another player with the same name
        SignInResult result = CuT.addPlayer(VALID_NAME1);
        assertEquals(result, SignInResult.NAME_TAKEN);

        // Test online player list only has one player
        Set<Player> playersOnline = CuT.getOnlinePlayers();
        assertEquals(playersOnline.size(), 1);
    }

    @Test
    public void test_sign_out() {
        // Add valid player
        CuT.addPlayer(VALID_NAME1);

        // Sign player out
        SignOutResult result1 = CuT.signPlayerOut(VALID_NAME1);
        assertEquals(result1, SignOutResult.SIGNOUT_SUCCESS);

        // Test player is not in online players list
        Player player1 = new Player(VALID_NAME1);
        Set<Player> playersOnline = CuT.getOnlinePlayers();
        assertFalse(playersOnline.contains(player1));

        // Sign out player that is not online
        SignOutResult result2 = CuT.signPlayerOut(VALID_NAME2);
        assertEquals(result2, SignOutResult.DNE);
    }
}
