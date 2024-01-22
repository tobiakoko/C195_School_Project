package model;

import java.time.LocalDateTime;

/**
 * Represents a division with attributes such as division ID, division name, creation date, creator, last update date,
 * last updater, and associated country ID.
 */
public class Division {
    // Attributes
    private int divisionId;
    private String division;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int countryId;

    /**
     * Constructs a Division object with specified details.
     *
     * @param divisionId      The unique identifier for the division.
     * @param division        The name of the division.
     * @param createDate      The date and time when the division was created.
     * @param createdBy       The creator of the division.
     * @param lastUpdate      The date and time of the last update to the division.
     * @param lastUpdatedBy   The last user who updated the division.
     * @param countryId       The ID of the country associated with the division.
     */
    public Division(int divisionId, String division, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    /**
     * Constructs a Division object with minimal details.
     *
     * @param divisionId The unique identifier for the division.
     * @param division   The name of the division.
     */
    public Division(int divisionId, String division) {
        this.divisionId = divisionId;
        this.division = division;
    }

    // Getter and Setter methods for attributes...
    /**
     * Gets the division ID.
     *
     * @return The division ID.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the division ID.
     *
     * @param divisionId The new division ID to set.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Gets the name of the division.
     *
     * @return The name of the division.
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets the name of the division.
     *
     * @param division The new division name to set.
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Gets the date and time when the division was created.
     *
     * @return The date and time when the division was created.
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Sets the date and time when the division was created.
     *
     * @param createDate The new created date to set.
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets the creator of the division.
     *
     * @return The creator of the division.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the creator of the division.
     *
     * @param createdBy The new created by to set.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the date and time of the last update to the division.
     *
     * @return The date and time of the last update to the division.
     */
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the date and time of the last update to the division.
     *
     * @param lastUpdate The new last update to set.
     */
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Gets the last user who updated the division.
     *
     * @return The last user who updated the division.
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Sets the last user who updated the division.
     *
     * @param lastUpdatedBy The new last updated by to set.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Gets the ID of the country associated with the division.
     *
     * @return The ID of the country associated with the division.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the ID of the country associated with the division.
     *
     * @param countryId The new country ID to set.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Generates a string representation of the Division object.
     *
     * @return A formatted string containing relevant information about the division.
     */
    @Override
    public String toString() {
        return "FirstLevelDiv{" +
                "division='" + division + '\'' +
                '}';
    }
}
