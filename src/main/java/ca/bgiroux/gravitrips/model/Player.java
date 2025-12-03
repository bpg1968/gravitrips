package ca.bgiroux.gravitrips.model;

public abstract class Player {
    private final int id;
    private final String name;

    protected Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
