package model;

/**
 * Represents a country with attributes such as country ID, country name, month count, and month.
 */
public class Country {

    //Attributes
    private int countryId;
    private String country;
    private int monthCount;
    private String month;

    /**
     * Constructs a Country object with specified details.
     *
     * @param countryId The unique identifier for the country.
     * @param country   The name of the country.
     */
    public Country(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    /**
     * Constructs a Country object with specified month and month count.
     *
     * @param month      The name of the month.
     * @param monthCount The count of occurrences for the specified month.
     */
    public Country(String month, int monthCount){
        this.month = month;
        this.monthCount = monthCount;
    }

    // Getter and Setter methods for attributes...
    /**
     * Gets the country ID.
     *
     * @return The country ID.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the country ID.
     *
     * @param countryId The new country ID to set.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Gets the name of the country.
     *
     * @return The name of the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the name of the country.
     *
     * @param country The new country name to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the count of occurrences for the month.
     *
     * @return The month count.
     */
    public int getMonthCount() {
        return monthCount;
    }

    /**
     * Sets the count of occurrences for the month.
     *
     * @param monthCount The new month count to set.
     */
    public void setMonthCount(int monthCount) {
        this.monthCount = monthCount;
    }

    /**
     * Gets the name of the month.
     *
     * @return The name of the month.
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets the name of the month.
     *
     * @param month The new month name to set.
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Generates a string representation of the Country object.
     *
     * @return A formatted string containing relevant information about the country.
     */
    @Override
    public String toString() {
        return ("Country{" +
                "countryId=" + countryId +
                ", country='" + country + '\'' +
                '}');
    }
}
