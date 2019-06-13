package tests.logic.enemies;

import dnd.logic.GameException;
import dnd.logic.MoveResult;
import dnd.logic.Point;
import dnd.logic.board.PositionOutOfBoundsException;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.random_generator.Randomizer;
import dnd.logic.tileOccupiers.FreeTile;
import dnd.logic.tileOccupiers.TileOccupier;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tests.logic.board.BoardMockBase;
import tests.logic.player.PlayerMock;

public class MonsterTest {
    MonsterMock monster;

    @Before
    public void setUp() {
        monster = new MonsterMock("Monster Test", 100, 30, 20, 5, 'M', 3);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void chasePlayerTest_moveLeft() {
        PlayerMock mockPlayer = new PlayerMock();
        mockPlayer.setPosition(new Point(5, 4));

        try {
            monster.setPosition(new Point(7, 4))
                   .setBoard(new BoardMockBase() {
                       @Override
                       public TileOccupier getTileOccupier(Point position) {
                           return new FreeTile();
                       }
                   });
            monster.chasePlayer(mockPlayer);
            Assert.assertEquals("monster should have attempted to move left", monster.getPosition(), new Point(6, 4));
        }
        catch (GameException e) {
            Assert.fail("failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void monsterEngagesPlayerTest() {
        try {
            monster.setPosition(new Point(3, 4))
                   .setRandomGenerator(new Randomizer())
                   .setBoard(new BoardMockBase() {
                @Override
                public TileOccupier getTileOccupier(Point position) throws PositionOutOfBoundsException {
                    return new PlayerMock()
                        .setRandomGenerator(new Randomizer());
                }
            });
            MoveResult result = monster.moveRight();
            Assert.assertTrue("monster should have engaged the player",result == MoveResult.Engaged | result == MoveResult.Dead);
        } catch (GameException e) {
            Assert.fail("failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void defendTest() {
        monster.setRandomGenerator(new RandomGenerator() {
            @Override
            public int nextInt(int n) {
                return 5;
            }
        });
        try {
            monster.defend(null, 17);
            Assert.assertEquals("monster should have 100 - (17 - 5) health after taking damage.", monster.getCurrentHealth(), 88);
        } catch (GameException e) {
            Assert.fail("failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}