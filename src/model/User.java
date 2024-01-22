package model;

import java.time.LocalDateTime;
/**
 * Represents a user with attributes such as user ID, username, password, creation date, creator,
 * last update date, and last updater.
 */
public class User {
    //Attributes
    private int userId;
    private String userName;
    private String password;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdateBy;

    /**
     * Constructs a User object with specified details.
     *
     * @param userId    The unique identifier for the user.
     * @param userName  The username of the user.
     * @param password  The password of the user.
     */
    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Constructs a User object with additional details.
     *
     * @param userId        The unique identifier for the user.
     * @param userName      The username of the user.
     * @param password      The password of the user.
     * @param createDate    The date and time when the user was created.
     * @param createdBy     The creator of the user.
     * @param lastUpdate    The date and time of the last update to the user.
     * @param lastUpdateBy  The last user who updated the user.
     */
    public User(int userId, String userName, String password, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;

    }

    /**
     * Constructs a User object with minimal details.
     *
     * @param userId    The unique identifier for the user.
     * @param userName  The username of the user.
     */
    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    // Getter and Setter methods for attributes
    /**
     * Gets the user ID.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId The new user ID to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the username of the user.
     *
     * @param userName The new username to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The new password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the date and time when the user was created.
     *
     * @return The date and time when the user was created.
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Sets the date and time when the user was created.
     *
     * @param createDate The new created date to set.
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets the creator of the user.
     *
     * @return The creator of the user.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator of the user.
     *
     * @param createdBy The new created by to set.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the date and time of the last update to the user.
     *
     * @return The date and time of the last update to the user.
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the date and time of the last update to the user.
     *
     * @param lastUpdate The new last update to set.
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Gets the last user who updated the user.
     *
     * @return The last user who updated the user.
     */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /**
     * Sets the last user who updated the user.
     *
     * @param lastUpdateBy The new last updated by to set.
     */
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    /**
     * Generates a string representation of the User object.
     *
     * @return A formatted string containing relevant information about the user.
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
