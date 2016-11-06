package persistence;

import java.util.List;
import model.QualityReport;
import model.User;
import model.WaterReport;

/**
 *
 * @author tybrown
 */
public interface PersistenceInterface {

    /**
     * Sets up the persistence layer (connect to DB, create/lock files, etc)
     */
    public void initialize();

    /**
     * Shuts down the persistence layer (disconnect from DB, close files, etc)
     */
    public void terminate();

    /**
     * Saves user to underlying persistence implementer
     * If user already exists, update all fields
     * @param u The user to save/update
     */
    public void saveUser(User u);

    /**
     * Loads a specific user by name into the model from the underlying persistence implementer
     * @param username The username of the user to load
     * @return Returns a constructed User object with the matching username, or null if no user existed by that name
     */
    public User loadUser(String username);

    /**
     * Deletes this user from the underlying persistence implementer
     * @param username The username of the user to delete
     */
    public void deleteUser(String username);

    /**
     * Deletes this user from the underlying persistence implementer
     * @param u The User object of the user to delete
     */
    public void deleteUser(User u);

    /**
     * Saves a water report to the underlying persistence implementer
     * @param wr The water report to save
     */
    public void saveWaterReport(WaterReport wr);

    /**
     * Loads a list of all water reports from underlying persistence layer with their child reports added
     * @return A list of all completely constructed water reports
     */
    public List<WaterReport> loadWaterReports();

    /**
     * Deletes the given water report and all its child reports from the underlying persistence layer
     * @param wr The water report to delete
     */
    public void deleteWaterReport(WaterReport wr);

    /**
     * Saves a given quality report in the given water report in the underlying persistence layer
     * @param wr The water report to save this quality report in
     * @param qr The quality report to save
     */
    public void saveQualityReport(WaterReport wr, QualityReport qr);

    /**
     * Load a list of quality reports for a given water report from the underlying persistence layer
     * @param wr The water report to search for quality reports in
     * @return A list of all the quality reports under the given water report
     */
    public List<QualityReport> loadQualityReports(WaterReport wr);

    /**
     * Deletes a given quality report in the given water report from the underlying persistence layer
     * @param wr The water report to delete this quality report from
     * @param qr The quality report to delete
     */
    public void deleteQualityReport(WaterReport wr, QualityReport qr);

}
