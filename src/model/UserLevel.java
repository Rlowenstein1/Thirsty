package model;

public enum UserLevel {
    USER(1), WORKER(2), MANAGER(3), ADMINISTRATOR(4);

    private final int level;

    UserLevel(int level) {
        this.level = level;
    }
}