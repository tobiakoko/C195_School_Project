package helper;

import database.AppointmentQuery;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import model.Appointment;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

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

    public static boolean validateOverlapping(int customerId, LocalDateTime start, LocalDateTime end){
        ObservableList<Appointment> appointments = AppointmentQuery.getAppointments(customerId);

        for(Appointment appointment: appointments) {
            LocalDateTime appointmentStart = appointment.getStart();
            LocalDateTime appointmentEnd = appointment.getEnd();
            if(appointmentStart.isEqual(start) || appointmentEnd.isEqual(end)) {
                errorAlert("OVERLAP ERROR", "Start time cannot be the same as End time");
                return true;
            } else if(start.isAfter(appointmentStart) && end.isAfter(appointmentEnd)) {
                errorAlert("OVERLAP ERROR", "Appointment start overlap not allowed.");
                return true;
            } else if (start.isAfter(appointmentStart) && start.isBefore(appointmentEnd)) {
                errorAlert("OVERLAP ERROR", "Appointment end overlap not allowed");
                return true;
            } else if (end.isAfter(appointmentStart) && end.isBefore(appointmentEnd)) {
                errorAlert("OVERLAP ERROR","Appointment end overlap not allowed" );
                return true;
            }
        }
        return false;
    }

    public ZonedDateTime convertToSystemTimeZone(LocalDateTime time){
        ZoneId systemZone = ZoneId.systemDefault();
        return ZonedDateTime.of(time, systemZone);
    }
}