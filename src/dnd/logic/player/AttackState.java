package dnd.logic.player;

import dnd.logic.enemies.Enemy;

public interface AttackState {
    Object visit(Enemy enemy);
}
