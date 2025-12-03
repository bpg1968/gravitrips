package ca.bgiroux.gravitrips.model;

public interface MoveStrategy {
    String getName();

    int chooseMove(Board board, int playerId);
}
