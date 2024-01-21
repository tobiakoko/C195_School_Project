package helper;

import database.AppointmentQuery;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import model.Appointment;

import java.time.*;
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


    private static void validateBusinessHours(LocalTime startTime, LocalTime endTime) {
        // Validate that an appointment is scheduled within business hours
        int appointmentHour = startTime.getHour();
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
            //validateBusinessHours(appointmentDateTime);
        };
    }

    //for AddAppointment
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

    public ZonedDateTime convertToSystemTimeZone(LocalDateTime time){
        ZoneId systemZone = ZoneId.systemDefault();
        return ZonedDateTime.of(time, systemZone);
    }
}