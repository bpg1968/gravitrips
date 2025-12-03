package ca.bgiroux.gravitrips.model;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomMoveStrategy implements MoveStrategy {
    @Override
    public String getName() {
        return "Random";
    }

    @Override
    public int chooseMove(Board board, int playerId) {
        List<Integer> available = board.getAvailableColumns();
        if (available.isEmpty()) {
            throw new IllegalStateException("Board is full");
        }
        int index = ThreadLocalRandom.current().nextInt(available.size());
        return available.get(index);
    }

    @Override
    public String toString() {
        return getName();
    }
}
