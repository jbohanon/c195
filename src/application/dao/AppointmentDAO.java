package application.dao;

import application.Main;
import application.datamodel.Appointment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import static application.dao.Database.dbUpdate;
import static application.ui.Application.loggedInUser;

public class AppointmentDAO implements DAO<Appointment> {

    public ArrayList<Appointment> getAllForUser(int userId) {
        ArrayList<Appointment> apptSearchResults = new ArrayList<>();
        try {
            Statement stmt = Database.getConnection().createStatement();
            String s = String.format("SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end " +
                    "FROM appointment " +
                    "WHERE userId=%d", userId);
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                do {
                    apptSearchResults.add(newApptAndPrint(rs));
                } while (rs.next());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return apptSearchResults;
    }

    public ArrayList<Appointment> getAll() {
        ArrayList<Appointment> apptSearchResults = new ArrayList<>();
        try {
            Statement stmt = Database.getConnection().createStatement();
            String s = "SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end " +
                    "FROM appointment";
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                do {
                    apptSearchResults.add(newApptAndPrint(rs));
                } while (rs.next());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return apptSearchResults;
    }

    public ArrayList<Appointment> search(String searchTerm) {
        ArrayList<Appointment> apptSearchResults = new ArrayList<>();
        try {
            Statement stmt = Database.getConnection().createStatement();
            String s = String.format("SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end FROM appointment WHERE " +
                    "customerId=(SELECT MIN(customerId) FROM customer WHERE customerName LIKE '%%%s%%') OR " +
                    "title LIKE '%%%s%%' OR " +
                    "description LIKE '%%%s%%' OR " +
                    "location LIKE '%%%s%%' OR " +
                    "contact LIKE '%%%s%%' OR " +
                    "url LIKE '%%%s%%'",
                    searchTerm, searchTerm, searchTerm, searchTerm, searchTerm, searchTerm);
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                do {
                    apptSearchResults.add(newApptAndPrint(rs));
                } while (rs.next());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return apptSearchResults;
    }

    @Override
    public Optional<Appointment> lookup(int id) {
        try {
            Statement stmt = Database.getConnection().createStatement();
            String s = "SELECT appointmentId, customerId, userId, title, description, location, contact, type, url, start, end FROM appointment WHERE appointmentId=" + id;
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                return Optional.of(newApptAndPrint(rs));
            }
        } catch (Exception ex) {
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
                appointment.getType().getString() + "', '" +
                appointment.getUrl() + "', '" +
                appointment.getStart().toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME) + "', '" +
                appointment.getEnd().withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME) + "', " +
                "now(), '" + loggedInUser + "', now(), '" + loggedInUser + "')";
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
                "', type='" + appointment.getType().getString() +
                "', url='" + appointment.getUrl() +
                "', start='" + appointment.getStart().toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME) +
                "', end='" + appointment.getEnd().toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME) +
                "', lastUpdate=now(), lastUpdateBy='" + loggedInUser +
                "' WHERE appointmentId=" + appointment.getAppointmentId();
        return dbUpdate(s);
    }

    @Override
    public boolean delete(Appointment appointment) {
        String s = "DELETE FROM appointment WHERE appointmentId=" + appointment.getAppointmentId();
        return dbUpdate(s);
    }

    @Override
    public Appointment GetOptionalOrThrow(Optional<Appointment> optionalAppointment) throws Exception {
        return optionalAppointment.orElseThrow(() -> new Exception("No appointment contained in Optional<Appointment>"));
    }

    public Appointment lookupAndSetAppointmentId(Appointment newAppointment) {
        return Appointment.nullAppt();
    }

    private Appointment newApptAndPrint(ResultSet rs) throws SQLException {
        Appointment a = Appointment.AppointmentFromDb(
                rs.getInt("appointmentId"),
                rs.getInt("customerId"),
                rs.getInt("userId"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getString("contact"),
                rs.getString("type"),
                rs.getString("url"),
                rs.getString("start").replace(' ', 'T'),
                true
        );
        System.out.println(
                "Matched appointment with id " +
                        a.getAppointmentId() + ", user " +
                        a.getUser().getUserName() + ", customer " +
                        a.getCustomer().getCustomerName() + ", title " +
                        a.getTitle() + ", beginning at " + a.getStartLocal());

        return a;
    }
}
