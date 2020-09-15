package application.datamodel;

import application.dao.AppointmentDAO;
import application.dao.CustomerDAO;
import application.dao.UserDAO;
import application.localization.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.EnumSet;

public class Appointment {
    private int _appointmentId;
    private Customer _customer;
    private User _user;
    private String _title;
    private String _description;
    private String _location;
    private String _contact;
    private APPT_TYPE _type;
    private String _url;
    private ZonedDateTime _start;
    private ZonedDateTime _end;

    private static final CustomerDAO customerDAO = new CustomerDAO();
    private static final UserDAO userDAO = new UserDAO();
    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();

    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, String url, String start, String end) {
        _appointmentId = appointmentId;
        _customer = customerDAO.lookup(customerId).orElse(Customer.nullCustomer());
        _user = userDAO.lookup(userId).orElse(User.nullUser());
        _title = title;
        _description = description;
        _location = location;
        _contact = contact;
        _type = apptTypeFromString(type);
        _url = url;
        _start = Localization.getZonedUtcTime(start);
        _end = Localization.getZonedUtcTime(end);
    }

    public Appointment() {
        _appointmentId = -1;
        _customer = Customer.nullCustomer();
        _user = User.nullUser();
        _title = "Err";
        _description = "Err";
        _location = "Err";
        _contact = "Err";
        _type = APPT_TYPE.TYPE_UNKNOWN;
        _url = "Err";
        _start = ZonedDateTime.now();
        _end = ZonedDateTime.now();
    }

    public int getAppointmentId() {
        return _appointmentId;
    }
    public void setAppointmentId(int appointmentId) {
        _appointmentId = appointmentId;
    }

    public Customer getCustomer() {
        return _customer;
    }
    public void setCustomer(Customer customer) {
        _customer = customer;
    }

    public User getUser() {
        return _user;
    }
    public void setUser(User user) {
        _user = user;
    }

    public String getTitle() {
        return _title;
    }
    public void setTitle(String title) {
        _title = title;
    }

    public String getDescription() {
        return _description;
    }
    public void setDescription(String description) {
        _description = description;
    }

    public String getLocation() {
        return _location;
    }
    public void setLocation(String location) {
        _location = location;
    }

    public String getContact() {
        return _contact;
    }
    public void setContact(String contact) {
        _contact = contact;
    }

    public APPT_TYPE getType() {
        return _type;
    }
    public void setType(APPT_TYPE type) {
        _type = type;
    }

    public String getUrl() {
        return _url;
    }
    public void setUrl(String url) {
        _url = url;
    }

    public ZonedDateTime getStart() {
        return _start;
    }
    public void setStart(ZonedDateTime start) {
        _start = start;
    }

    public ZonedDateTime getEnd() {
        return _end;
    }
    public void setEnd(ZonedDateTime end) {
        _end = end;
    }

    public enum APPT_TYPE {
        TYPE_1, TYPE_2, TYPE_3, TYPE_UNKNOWN
    }

    public String toString() {
        return String.format("appointmentId: %d\n" +
                "Customer Name: %s\n" +
                "Consultant Name: %s\n" +
                "Title: %s\n" +
                "Description: %s\n" +
                "Location: %s\n" +
                "Contact: %s\n" +
                "Type: %s\n" +
                "Url: %s\n" +
                "Start: %s\n" +
                "End: %s",
                _appointmentId,
                _customer.getCustomerName(),
                _user.getUserName(),
                _title,
                _description,
                _location,
                _contact,
                apptTypeToString(_type),
                _url,
                _start.toString(),
                _end.toString());
    }

    public static String apptTypeToString(APPT_TYPE type) {
        switch (type) {
            case TYPE_1: return "type_1";
            case TYPE_2: return "type_2";
            case TYPE_3: return "type_3";
            default: return "type_unknown";
        }
    }

    public static APPT_TYPE apptTypeFromString(String type) {
        switch (type) {
            case "type_1": return APPT_TYPE.TYPE_1;
            case "type_2": return APPT_TYPE.TYPE_2;
            case "type_3": return APPT_TYPE.TYPE_3;
            default: return APPT_TYPE.TYPE_UNKNOWN;
        }
    }

    public static Appointment nullAppt() {
        return new Appointment();
    }
}