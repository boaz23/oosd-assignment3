package dnd.logic.enemies;

import dnd.logic.Tick;
import dnd.logic.board.Board;
import dnd.logic.player.Player;
import dnd.logic.random_generator.RandomGenerator;
import dnd.logic.tileOccupiers.TileOccupier;

public class Monster extends Enemy {
    private static final MoveInDirectionAction[] MoveDirections = new MoveInDirectionAction[] {
        monster -> monster.moveLeft(),
        monster -> monster.moveRight(),
        monster -> monster.moveUp(),
        monster -> monster.moveDown()
    };

    private int range;

    public Monster(String name,
                   int healthPool, int attack, int defense,
                   int experienceValue, char tile,
                   int range) {
        super(name, healthPool, attack, defense, experienceValue, tile);
        this.init(range);
    }

    protected Monster(String name,
                      int healthPool, int attack, int defense,
                      int experienceValue, char tile,
                      int range,
                      RandomGenerator randomGenerator,
                      Board board) {
        super(name, healthPool, attack, defense, randomGenerator, board, experienceValue, tile);
        this.init(range);
    }

    private void init(int range) {
        if (range <= 0) {
            throw new IllegalArgumentException("monster vision range must be a positive number.");
        }

        this.range = range;
    }

    @Override
    public void onTick(Tick current) {
        Player player = this.board.getPlayerInRange(this.position, this.range);
        if (player != null) {
            this.chasePlayer(player);
        }
        else {
            this.actRandomly();
        }
    }

    private void chasePlayer(Player player) {
        int dx = this.position.getX() - player.getPosition().getX();
        int dy = this.position.getY() - player.getPosition().getY();

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                moveLeft();
            }
            else {
                moveRight();
            }
        }
        else {
            if (dy > 0) {
                moveUp();
            }
            else {
                moveDown();
            }
        }
    }

    private void actRandomly() {
        // 0 - move left
        // 1 - move right
        // 2 - move up
        // 3 - move down

        int move = this.randomGenerator.nextInt(3);
        MoveDirections[move].move(this);
    }

    @Override
    public TileOccupier clone(RandomGenerator randomGenerator, Board board) {
        return new Monster(
                this.name,
                this.healthPool, this.attack, this.defense,
                this.experienceValue, this.tile,
                this.range,
                randomGenerator, board);
    }

    private interface MoveInDirectionAction {
        void move(Monster monster);
    }
}
