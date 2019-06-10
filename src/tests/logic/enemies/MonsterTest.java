package tests.logic.enemies;

import dnd.logic.GameException;
import dnd.logic.Point;
import dnd.logic.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.logic.player.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {
    MonsterMock monster;

    @BeforeEach
    void setUp() {
        monster = new MonsterMock("Monster Test", 100, 30, 20, 5, 'M', 3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void chasePlayerTest_moveLeft() {
        PlayerMock mockPlayer = new PlayerMock();
        mockPlayer.setPosition(new Point(5, 4));

        try {
            monster.setPosition(new Point(7, 4));
            monster.chasePlayer(mockPlayer);
            Assertions.assertEquals(monster.movedLeft, true, "monster should have attempted to move left");
        }
        catch (GameException e) {
            Assertions.fail(e);
            e.printStackTrace();
        }
    }
}