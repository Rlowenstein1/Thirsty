package model;

public enum UserLevel {
    USER(1), WORKER(2), MANAGER(3), ADMINISTRATOR(4);

    private final int level;

    UserLevel(int level) {
        this.level = level;
    }

    // Access int value
    public int getValue() {
        return level;
    }

    // Get UserLevel enum when you only know the int value
    public static UserLevel fromInt(int i) {
        for (UserLevel l: UserLevel.values()) {
            if (l.getValue() == i) {
                return l;
            }
        }
        return null;
    }
}