package model;

/**
 * Represents a customer with attributes such as customer ID, customer name, address, postal code, phone number,
 * division ID, country ID, division name, and country.
 */
public class Customer {
    // Attributes
    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;
    private int countryId;
    private String divisionName;
    private String country;

    /**
     * Constructs a Customer object with specified details.
     *
     * @param customerId    The unique identifier for the customer.
     * @param customerName  The name of the customer.
     * @param address       The address of the customer.
     * @param postalCode    The postal code of the customer's location.
     * @param phone         The phone number of the customer.
     * @param divisionId    The ID of the division associated with the customer.
     * @param countryId     The ID of the country associated with the customer.
     * @param divisionName  The name of the division associated with the customer.
     * @param country       The name of the country associated with the customer.
     */
    public Customer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId, int countryId, String divisionName, String country) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.countryId = countryId;
        this.divisionName = divisionName;
        this.country = country;
    }

    /**
     * Constructs a Customer object with minimal details.
     *
     * @param customerId   The unique identifier for the customer.
     * @param customerName The name of the customer.
     */
    public Customer(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    // Getter and Setter methods for attributes...
    /**
     * Gets the customer ID.
     *
     * @return The customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId The new customer ID to set.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the name of the customer.
     *
     * @return The name of the customer.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the name of the customer.
     *
     * @param customerName The new customer name to set.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets the address of the customer.
     *
     * @return The address of the customer.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     *
     * @param address The new address to set.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the postal code of the customer's location.
     *
     * @return The postal code of the customer's location.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code of the customer's location.
     *
     * @param postalCode The new postal code to set.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets the phone number of the customer.
     *
     * @return The phone number of the customer.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the customer.
     *
     * @param phone The new phone number to set.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the ID of the division associated with the customer.
     *
     * @return The ID of the division associated with the customer.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the ID of the division associated with the customer.
     *
     * @param divisionId The new division ID to set.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Gets the ID of the country associated with the customer.
     *
     * @return The ID of the country associated with the customer.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the ID of the country associated with the customer.
     *
     * @param countryId The new country ID to set.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Gets the name of the division associated with the customer.
     *
     * @return The name of the division associated with the customer.
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Sets the name of the division associated with the customer.
     *
     * @param divisionName The new division name to set.
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * Gets the name of the country associated with the customer.
     *
     * @return The name of the country associated with the customer.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the name of the country associated with the customer.
     *
     * @param country The new country name to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Generates a string representation of the Customer object.
     *
     * @return A formatted string containing relevant information about the customer.
     */
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
