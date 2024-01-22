package helper;

import database.AppointmentQuery;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Appointment;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Utility class providing various helper methods.
 */
public class Util {

    /**
     * Displays an error alert with the specified title and content.
     *
     * @param title   The title of the error alert.
     * @param content The content text of the error alert.
     */
    public static void errorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation alert with the specified title and content.
     *
     * @param title   The title of the confirmation alert.
     * @param content The content text of the confirmation alert.
     */
    public static void confirmAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
    }

    //for AddAppointment
    /**
     * Validates overlapping appointments for the specified customer and time range.
     * Validation for addAppointment Controller class
     *
     * @param customerId    The ID of the customer.
     * @param startDateTime The start date and time of the appointment.
     * @param endDateTime   The end date and time of the appointment.
     * @return True if there is an overlap, false otherwise.
     */
    public static boolean validateOverlapping(int customerId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        ObservableList<Appointment> appointments = AppointmentQuery.getAppointments(customerId);

        for (Appointment appointment : appointments) {
            LocalDateTime appointmentStart = appointment.getStart();
            LocalDateTime appointmentEnd = appointment.getEnd();

            if (appointmentStart.isEqual(startDateTime) || appointmentEnd.isEqual(endDateTime)) {
                // Check for exact match of start or end times
                errorAlert("OVERLAP ERROR", "Start time or End time cannot be the same as existing appointment");
                return true;
            } else if (startDateTime.isAfter(appointmentStart) && startDateTime.isBefore(appointmentEnd)) {
                // Check for overlap in the start time
                errorAlert("OVERLAP ERROR", "Appointment start overlap not allowed.");
                return true;
            } else if (endDateTime.isAfter(appointmentStart) && endDateTime.isBefore(appointmentEnd)) {
                // Check for overlap in the end time
                errorAlert("OVERLAP ERROR", "Appointment end overlap not allowed.");
                return true;
            } else if (customerId != appointment.getCustomerId()) {
                continue;
            }
        }
        return false;
    }


    //for Update Appointment
    /**
     * Validates overlapping appointments during an update for the specified customer and time range.
     * Validation for the updateAppointment Controller class
     *
     * @param customerId    The ID of the customer.
     * @param startDateTime The start date and time of the appointment.
     * @param endDateTime   The end date and time of the appointment.
     * @return True if there is an overlap, false otherwise.
     */
    public static boolean validatingOverlap(int customerId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        ObservableList<Appointment> appointments = AppointmentQuery.getAppointments(customerId);
        for(Appointment appointment : appointments) {
            LocalDateTime appointmentStart = appointment.getStart();
            LocalDateTime appointmentEnd = appointment.getEnd();

            if (!(customerId == appointment.getCustomerId()) && !(startDateTime.isEqual(appointmentStart)) && !(endDateTime.isEqual(appointmentEnd))){
                validateOverlapping(customerId, startDateTime, endDateTime);
            }
        }
        return false;
    }

    /**
     * Converts the given LocalDateTime to the system time zone.
     *
     * @param time The LocalDateTime to be converted.
     */
    public void convertToSystemTimeZone(LocalDateTime time){
        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime.of(time, systemZone);
    }
}