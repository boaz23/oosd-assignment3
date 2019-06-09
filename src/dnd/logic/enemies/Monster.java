package dnd.logic.enemies;

import dnd.logic.GameException;
import dnd.logic.Tick;
import dnd.logic.player.Player;

public class Monster extends Enemy {
    private static final MoveInDirectionAction[] MoveDirections = new MoveInDirectionAction[]{
        monster -> monster.moveDown(),
        monster -> monster.moveUp(),
        monster -> monster.moveRight(),
        monster -> monster.moveLeft(),
        monster -> monster.doNothing(),
    };

    private int range;

    public Monster(String name,
                   int healthPool, int attack, int defense,
                   int experienceValue, char tile,
                   int range) {
        super(name, healthPool, attack, defense, experienceValue, tile);
        init(range);
    }

    private void init(int range) {
        if (range <= 0) {
            throw new IllegalArgumentException("monster vision range must be a positive number.");
        }

        this.range = range;
    }

    @Override
    public void onTick(Tick current) throws GameException {
        Player player = board.getPlayerInRange(position, range);
        if (player != null) {
            chasePlayer(player);
        }
        else {
            actRandomly();
        }
    }

    private void chasePlayer(Player player) throws GameException {
        int dx = position.getX() - player.getPosition().getX();
        int dy = position.getY() - player.getPosition().getY();

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

    private void actRandomly() throws GameException {
        // 0 - move down
        // 1 - move up
        // 2 - move right
        // 3 - move left
        // 4 - do nothing

        int move = randomGenerator.nextInt(MoveDirections.length - 1);
        MoveDirections[move].move(this);
    }

    private void doNothing() {
    }

    @Override
    public Monster clone() {
        return new Monster(
            name,
            healthPool, attack, defense,
            experienceValue, tile,
            range
        );
    }

    private interface MoveInDirectionAction {
        void move(Monster monster) throws GameException;
    }
}
