package db;

import model.User;
import model.WaterReport;
import model.QualityReport;

/**
 * Interface for interacting with a persistent entity (database/file)
 */
public abstract class PersistenceAbstractObject {
    /**
     * Boolean status of initialization
     */
    protected boolean isInitialized = false;

    /**
     * Initializes the connection to the persistent entity
     * only internal calls from getInstance
     * @return boolean true if successful
     */
    protected abstract boolean initialize();

    /**
     * Saves all objects in the manager classes to the persistent entity
     * @return boolean true if successful
     */
    public abstract boolean save();

    /**
     * Loads all persisted data from the persistent entity and
     * creates those objects in the current model
     * @return boolean true if successful
     */
    public abstract boolean load();

    /**
     * Saves a single user to the persistent entity
     * @return boolean status
     */
    public abstract boolean saveUser(User user);

    /**
     * Loads all users in the persistent entity and creates those
     * objects in the current model
     * @return boolean status
     */
    public abstract boolean loadUsers();

    /**
     * Saves a single water report to the persistent entity
     * @return boolean status
     */
    public abstract boolean saveReport(WaterReport report);

    /**
     * Saves a single quality report to the persistent entity
     * @return boolean status
     */
    public abstract boolean saveReport(QualityReport report);

    /**
     * Loads all the reports in the persistent entity and creates
     * those objects in the current model
     * @return boolean status
     */
    public abstract boolean loadReports();
}
