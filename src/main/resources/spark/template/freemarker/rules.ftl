<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>Web Checkers | ${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

    <h1>Web Checkers | ${title}</h1>

    <!-- Provide a navigation bar -->
    <#include "nav-bar.ftl" />

    <div class="body">

        <!-- Provide a message to the user, if provided. -->
        <#include "message.ftl" />

    </div>
    <div class="body">
        1. The checkerboard is an 8x8 grid of light and dark squares in the famous "checkerboard" pattern.
        Each player has a dark square on the far left and a light square on his far right.
        The double-corner sometimes mentioned is the distinctive pair of dark squares in the near right corner.
    </div>
    <div class="body">
        2. The checkers to be used shall be round and red and white in color.
        The pieces shall be placed on the dark squares. The starting position is with each player having twelve pieces,
        on the twelve dark squares closest to the player's edge of the board.
    </div>
    <div class="body">
        3. The red player moves first.
    </div>
    <div class="body">
        4. A player must move each turn. If the player cannot move, the player loses the game.
    </div>
    <div class="body">
        5. In each turn, a player can make a simple move, a single jump, or a multiple jump move.
    </div>
    <ul>
        <li>a. Simple move: Single pieces can move one adjacent square diagonally forward away from the player.
            A piece can only move to a vacant dark square.</li>
        <br>
        <li>b. Single jump move: A player captures an opponent's piece by jumping over it, diagonally, to an adjacent
            vacant dark square. The opponent's captured piece is removed from the board. The player can never jump over,
            even without capturing, one of the player's own pieces. A player cannot jump the same piece twice.</li>
        <br>
        <li>c. Multiple jump move: Within one turn, a player can make a multiple jump move with the same piece by
            jumping from vacant dark square to vacant dark square. The player must capture one of the opponent's pieces
            with each jump. The player can capture several pieces with a move of several jumps.</li>
    </ul>

    <div class="body">
        6. If a jump move is possible, the player must make that jump move. A multiple jump move must be completed. T
        he player cannot stop part way through a multiple jump. If the player has a choice of jumps,
        the player can choose among them, regardless of whether some of them are multiple, or not.
    </div>
    <div class="body">
        7. When a single piece reaches the row of the board furthest from the player, i.e the king-row,
        by reason of a simple move, or as the completion of a jump, it becomes a king. This ends the player's turn.
        The opponent crowns the piece by placing a second piece on top of it.
    </div>
    <div class="body">
        8. A king follows the same move rules as a single piece except that a king can move and jump diagonally
        forward away from the player or diagonally backward toward the player. Within one multiple jump move,
        the jumps can be any combination of forward or backward jumps. At any point,
        if multiple jumps are available to a king, the player can choose among them
    </div>
    <div class="body">
        9. A player who loses all of their pieces to captures loses the game.
    </div>



</div>
</body>

</html>