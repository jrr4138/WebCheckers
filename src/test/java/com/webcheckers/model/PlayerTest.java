package com.webcheckers.model;

import com.webcheckers.model.entity.Player;
import com.webcheckers.application.PlayerLobby;
import org.junit.jupiter.api.Test;

import com.webcheckers.application.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import spark.*;

public class PlayerTest {
    private static final String firstPlayer = "playerTest";
    private static final String secondPlayer = "testPlayer";
    private static final String firstPlayerReplica = "playerTest";

    /**
     * Tests if player names are equal
     */
    @Test
    public void equalNames(){
        final Player CuT = new Player(firstPlayer);
        final Player p2 = new Player(secondPlayer);
        final Player p3 = new Player(firstPlayerReplica);

        assertNotEquals(CuT, p2);
        assertEquals(CuT, p3);
    }

    /**
     * Tests if player name hash is equal
     */
    @Test
    public void equalHash(){
        final Player CuT = new Player(firstPlayer);
        final Player p2 = new Player(secondPlayer);
        assertEquals(firstPlayer.hashCode(), CuT.hashCode());
        assertNotEquals(secondPlayer.hashCode(), CuT.hashCode());
    }

}
