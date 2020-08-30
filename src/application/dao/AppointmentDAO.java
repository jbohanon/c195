package application.dao;

import application.datamodel.Appointment;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static application.dao.Database.dbUpdate;
import static application.ui.ApplicationController.loggedInUser;

public class AppointmentDAO implements DAO<Appointment> {
    @Override
    public Optional<Appointment> lookup(int id) {
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
        return false;
    }

    @Override
    public boolean delete(Appointment appointment) {
        return false;
    }
}
