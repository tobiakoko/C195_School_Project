package model;

import java.time.LocalDateTime;

/**
 * Represents an appointment with various attributes such as appointment ID, title, description, location, type,
 * start and end times, customer and user IDs, contact information, and type total.
 */
public class Appointment {
    // Attributes
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;
    private int contact;
    private int typeTotal;

    /**
     * Constructs an Appointment object with specified details.
     *
     * @param appointmentId The unique identifier for the appointment.
     * @param title         The title of the appointment.
     * @param description   The description of the appointment.
     * @param location      The location of the appointment.
     * @param type          The type of the appointment.
     * @param start         The start date and time of the appointment.
     * @param end           The end date and time of the appointment.
     * @param customerId    The ID of the customer associated with the appointment.
     * @param userId        The ID of the user associated with the appointment.
     * @param contact       The contact information related to the appointment.
     */
    public Appointment(int appointmentId, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contact) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contact = contact;
    }

    /**
     * Constructs an Appointment object with specified type and type total.
     *
     * @param type      The type of the appointment.
     * @param typeTotal The total count of appointments with the specified type.
     */
    public Appointment(String type, int typeTotal) {
        this.type = type;
        this.typeTotal = typeTotal;
    }

    // Additional getter and setter methods...

    /**
     * Gets the appointment ID.
     *
     * @return The appointment ID.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets the appointment ID.
     *
     * @param appointmentId The new appointment ID to set.
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Gets the title of the appointment.
     *
     * @return The title of the appointment.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the appointment.
     *
     * @param title The new appointment type to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the appointment.
     *
     * @return The description of the appointment.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the appointment.
     *
     * @param description The new appointment description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the location of the appointment.
     *
     * @return The location of the appointment.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the appointment.
     *
     * @param location The new appointment location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the type of appointment.
     *
     * @return The type of appointment.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of appointment.
     *
     * @param type The new appointment type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the start date and time of the appointment.
     *
     * @return The start date and time of the appointment.
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Sets the start date and time of the appointment.
     *
     * @param start The new appointment start date and time to set.
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * Gets the end date and time of the appointment.
     *
     * @return The end date and time of the appointment.
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Sets the start date and time of the appointment.
     *
     * @param end The new appointment end date and time to set.
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

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
     * Gets the contact ID.
     *
     * @return The contact ID.
     */
    public int getContact() {
        return contact;
    }

    /**
     * Sets the contact ID.
     *
     * @param contact The new contact ID to set.
     */
    public void setContact(int contact) {
        this.contact = contact;
    }

    /**
     * Gets the appointment ID.
     *
     * @return The appointment ID.
     */
    public int getTypeTotal() {
        return typeTotal;
    }

    /**
     * Sets the appointment total.
     *
     * @param typeTotal The new appointment type total to set.
     */
    public void setTypeTotal(int typeTotal) {
        this.typeTotal = typeTotal;
    }

    /**
     * Generates a string representation of the Appointment object.
     *
     * @return A formatted string containing relevant information about the appointment.
     */
    @Override
    public String toString() {
        return ("Appointment: {" + Integer.toString(appointmentId) + "} \nCustomer: {" + Integer.toString(customerId) +
                " } \nContact: {" + Integer.toString(contact) + "} \n Type: " + type + " \nStart: {"+ start +
                "} \nEnd: {" + end);
    }


}
