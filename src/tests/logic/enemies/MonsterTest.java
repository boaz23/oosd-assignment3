package tests.logic.enemies;

import dnd.logic.GameException;
import dnd.logic.MoveResult;
import dnd.logic.Point;
import dnd.logic.board.PositionOutOfBoundsException;
import dnd.logic.board.Board;
import dnd.logic.enemies.Enemy;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.random_generator.Randomizer;
import dnd.logic.tileOccupiers.TileOccupier;
import dnd.logic.tileOccupiers.Unit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.logic.player.PlayerMock;

import java.util.List;

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

    @Test
    void monsterEngagesPlayerTest() {
        try {
            monster.setPosition(new Point(3, 4))
                   .setBoard(new Board() {
                @Override
                public void move(Unit unit, Point targetPosition) throws PositionOutOfBoundsException {

                }

                @Override
                public TileOccupier getTileOccupier(Point position) throws PositionOutOfBoundsException {
                    return new PlayerMock()
                        .setRandomGenerator(new Randomizer());
                }

                @Override
                public List<Point> getFreeTilesPositionsInRange(Point position, int range) throws PositionOutOfBoundsException {
                    return null;
                }

                @Override
                public List<Enemy> getEnemiesInRange(Point position, int range) {
                    return null;
                }

                @Override
                public Player getPlayerInRange(Point position, int range) {
                    return null;
                }

                @Override
                public void onDeath(Player player) throws GameException {

                }

                @Override
                public void onDeath(Enemy enemy) throws GameException {

                }
            })
                   .setRandomGenerator(new Randomizer());
            MoveResult result = monster.moveRight();
            Assertions.assertTrue(result == MoveResult.Engaged | result == MoveResult.Dead, "monster should have engaged the player");
        } catch (GameException e) {
            Assertions.fail(e);
            e.printStackTrace();
        }
    }

    @Test
    void defendTest() {
        monster.setRandomGenerator(new RandomGenerator() {
            @Override
            public int nextInt(int n) {
                return 5;
            }
        });
        try {
            monster.defend(null, 17);
            Assertions.assertEquals(monster.getCurrentHealth(), 88, "monster should have 100 - (17 - 5) health after taking damage.");
        } catch (GameException e) {
            Assertions.fail(e);
            e.printStackTrace();
        }
    }
}