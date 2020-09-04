package application.dao;

import application.Main;
import application.datamodel.Address;
import application.datamodel.Appointment;
import javafx.application.Application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static application.dao.Database.dbUpdate;
import static application.ui.ApplicationController.loggedInUser;

public class AppointmentDAO implements DAO<Appointment> {
    @Override
    public Optional<Appointment> lookup(int id) {
        try {
            Statement stmt = Main.dbConn.createStatement();
            String s = "SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end FROM appointment WHERE appointmentId=" + id;
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                Appointment a = new Appointment(
                        rs.getInt("appointmentId"),
                        rs.getInt("customerId"),
                        rs.getInt("userId"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("contact"),
                        rs.getString("type"),
                        rs.getString("url"),
                        rs.getString("start"),
                        rs.getString("end"));
                System.out.println(
                        "Matched appointment with id " +
                                a.getAppointmentId() + ", user " +
                                a.getUser().getUserName() + ", customer " +
                                a.getCustomer().getCustomerName() + ", title " +
                                a.getTitle() + ", beginning at " +
                                a.getStart().toString() + " and ending at " +
                                a.getEnd().toString());
                return Optional.of(a);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public boolean insert(Appointment appointment) {
        String s = "INSERT INTO appointment (customerId, userId, " +
                "title, description, location, contact, type, url, start, end, " +
                "createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (" +
                appointment.getCustomer().getCustomerId() + ", " +
                appointment.getUser().getUserId() + ", '" +
                appointment.getTitle() + "', '" +
                appointment.getDescription() + "', '" +
                appointment.getLocation() + "', '" +
                appointment.getContact() + "', '" +
                appointment.getType().toString() + "', '" +
                appointment.getUrl() + "', '" +
                appointment.getStart().format(DateTimeFormatter.ISO_DATE_TIME) + "', '" +
                appointment.getEnd().format(DateTimeFormatter.ISO_DATE_TIME) + "', " +
                "now(), " + loggedInUser + ", now(), " + loggedInUser + ")";
        return dbUpdate(s);
    }

    @Override
    public boolean update(Appointment appointment) {
        String s = "UPDATE appointment SET " +
                "userId=" + appointment.getUser().getUserId() +
                ", title='" + appointment.getTitle() +
                "', description='" + appointment.getDescription() +
                "', location='" + appointment.getLocation() +
                "', contact='" + appointment.getContact() +
                "', type='" + appointment.getType().toString() +
                "', url='" + appointment.getUrl() +
                "', start='" + appointment.getStart().format(DateTimeFormatter.ISO_DATE_TIME) +
                "', end='" + appointment.getEnd().format(DateTimeFormatter.ISO_DATE_TIME) +
                "', lastUpdate=now(), lastUpdateBy='" + loggedInUser +
                "' WHERE appointmentId=" + appointment.getAppointmentId();
        return dbUpdate(s);
    }

    @Override
    public boolean delete(Appointment appointment) {
        String s = "DELETE FROM appointment WHERE appointmentId=" + appointment.getAppointmentId();
        return dbUpdate(s);
    }
}
