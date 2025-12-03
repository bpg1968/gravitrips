package ca.bgiroux.gravitrips.model;

import java.util.Objects;

public class ComputerPlayer extends Player {
    private MoveStrategy strategy;

    public ComputerPlayer(int id, String name, MoveStrategy strategy) {
        super(id, name);
        this.strategy = Objects.requireNonNull(strategy);
    }

    public MoveStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(MoveStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }
}
