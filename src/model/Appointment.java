package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Appointment {
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

    public Appointment(String type, int typeTotal) {
        this.type = type;
        this.typeTotal = typeTotal;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }


    public boolean overlaps(LocalDateTime newStart, LocalDateTime newEnd) {
        return start.isBefore(newEnd) && end.isAfter(newStart);
    }

    @Override
    public String toString() {
        return ("Appointment: {" + Integer.toString(appointmentId) + "} \nCustomer: {" + Integer.toString(customerId) +
                " } \nContact: {" + Integer.toString(contact) + "} \n Type: " + type + " \nStart: {"+ start +
                "} \nEnd: {" + end);
    }

    public int getTypeTotal() {
        return typeTotal;
    }

    public void setTypeTotal(int typeTotal) {
        this.typeTotal = typeTotal;
    }
}
