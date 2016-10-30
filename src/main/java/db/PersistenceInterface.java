package main.java.db;

/**
 * Interface for interacting with a persistent entity (database/file)
 */
public interface PersistenceInterface {
    /**
     * Initializes the connection to persistent entity
     * @return boolean true if successful
     */
    public static boolean initialize() {
        // given implementation because static interface methods must be implemented
        // the concrete class should implement otherwise will alwyas return false
        return false;
    }

    /**
     * Saves given JSON string to persistent entity
     * all objects in the manager classes
     * @return boolean true if successful
     */
    public static boolean save() {
        // given implementation because static interface methods must be implemented
        // the concrete class should implement otherwise will alwyas return false
        return false;
    }

    /**
     * Loads persisted data from the database and creates those objects in the current model
     * @return boolean true if successful
     */
    public static boolean load() {
        // given implementation because static interface methods must be implemented
        // the concrete class should implement otherwise will alwyas return false
        return false;
    }
}
