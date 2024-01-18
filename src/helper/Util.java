package helper;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Appointment;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Util {
    private static final int BUSINESS_START_HOUR = 8;
    private static final int BUSINESS_END_HOUR = 22;

    public static void errorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void confirmAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
    }

    private static void validateBusinessHours(LocalDateTime appointmentDateTime) {
        // Validate that an appointment is scheduled within business hours
        int appointmentHour = appointmentDateTime.getHour();
        if (appointmentHour < BUSINESS_START_HOUR || appointmentHour >= BUSINESS_END_HOUR) {
            //Error Statement
            errorAlert("Out of Bounds Error", "Appointments must be scheduled between 8:00 a.m. and 10:00 p.m. ET, including weekends.");
        } else {
            String a = null;
        }
    }

    private static void validateOverlappingAppointments(ObservableList<Appointment> existingAppointments, LocalDateTime newAppointmentStart, LocalDateTime newAppointmentEnd) {
        // Validate that a new appointment doesn't overlap with existing appointments
        for (Appointment existingAppointment : existingAppointments) {
            if (existingAppointment.overlaps(newAppointmentStart, newAppointmentEnd)) {
                errorAlert("Overlap in appointment time detected.", "Overlapping appointments are not allowed.");
            }
        }
    }

    // Lambda expression 1
    public static Runnable validationFunction(LocalDateTime appointmentDateTime, ObservableList<Appointment> existingAppointments, LocalDateTime newAppointmentStart, LocalDateTime newAppointmentEnd) {
        return () -> {
            validateOverlappingAppointments(existingAppointments, newAppointmentStart, newAppointmentEnd);
            validateBusinessHours(appointmentDateTime);
        };
    }


    public static LocalDateTime localDateTime() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }
}