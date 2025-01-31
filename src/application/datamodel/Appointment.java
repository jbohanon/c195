package application.datamodel;

import application.ui.DialogController;

import java.time.*;
import java.util.TimeZone;

import static application.ui.Application.customerDAO;
import static application.ui.Application.userDAO;

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
    private ZonedDateTime _start; // Always UTC
    private LocalDateTime _startLocal; // Converted to JVM locale and kept handy
    private ZonedDateTime _end;

    public static Appointment AppointmentFromDb(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, String url, String start, boolean fromDb) {
        return new Appointment(appointmentId, customerId, userId, title, description, location, contact, type, url, ZonedDateTime.ofLocal(LocalDateTime.parse(start), ZoneId.of("UTC"), ZoneOffset.UTC).withZoneSameInstant(TimeZone.getDefault().toZoneId()).toLocalDateTime().toString());
    }

    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, String url, String start/*, String end*/) {
        _appointmentId = appointmentId;

        try {
            _customer = customerDAO.GetOptionalOrThrow(customerDAO.lookup(customerId));
            _user = userDAO.GetOptionalOrThrow(userDAO.lookup(userId));
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogController.okModalDialog(ex.toString());
        }

        _title = title;
        _description = description;
        _location = location;
        _contact = contact;
        _type = apptTypeFromString(type);
        _url = url;
        _startLocal = LocalDateTime.parse(start);
        ZonedDateTime tmp = ZonedDateTime.ofLocal(_startLocal, TimeZone.getDefault().toZoneId(), ZoneOffset.from(ZonedDateTime.now()));
        _start = tmp.withZoneSameInstant(ZoneId.of("UTC"));
        _end = _start.plusHours(1);
    }

    public Appointment(int appointmentId, Customer customer, int userId, String title, String description, String location, String contact, String type, String url, String start/*, String end*/) {
        _appointmentId = appointmentId;
        _customer = customer;

        try {
            _user = userDAO.GetOptionalOrThrow(userDAO.lookup(userId));
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogController.okModalDialog(ex.toString());
        }

        _title = title;
        _description = description;
        _location = location;
        _contact = contact;
        _type = apptTypeFromString(type);
        _url = url;
        _startLocal = LocalDateTime.parse(start);
        ZonedDateTime tmp = ZonedDateTime.ofLocal(_startLocal, TimeZone.getDefault().toZoneId(), ZoneOffset.from(ZonedDateTime.now()));
        _start = tmp.withZoneSameInstant(ZoneId.of("UTC"));
        _end = _start.plusHours(1);
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
        _end = _start.plusHours(1);
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
    public LocalDateTime getStartLocal() {
        return _startLocal;
    }
    public void setStartLocal(LocalDateTime startLocal) {
        _startLocal = startLocal;
        _start = startLocal.atZone(TimeZone.getDefault().toZoneId()).withZoneSameInstant(TimeZone.getTimeZone("UTC").toZoneId());
    }

    public ZonedDateTime getEnd() {
        return _end;
    }
    public void setEnd(ZonedDateTime end) {
        _end = end;
    }

    public enum APPT_TYPE {
        INTRODUCTION {
            public String getString() {
                return "Introduction";
            }
        }, CONSULT_TAX {
            public String getString() {
                return "Tax Consultation";
            }
        }, CONSULT_INVEST {
            public String getString() {
                return "Investment Consultation";
            }
        }, TYPE_UNKNOWN {
            public String getString() {
                return "Unknown";
            }
        };

        public static String getString(APPT_TYPE type) {
            return type.getString();
        }

        public abstract String getString();
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
                _type.getString(),
                _url,
                _start.toString(),
                _end.toString());
    }

    public static APPT_TYPE apptTypeFromString(String type) {
        switch (type) {
            case "Introduction": return APPT_TYPE.INTRODUCTION;
            case "Tax Consultation": return APPT_TYPE.CONSULT_TAX;
            case "Investment Consultation": return APPT_TYPE.CONSULT_INVEST;
            default: return APPT_TYPE.TYPE_UNKNOWN;
        }
    }

    public static Appointment nullAppt() {
        return new Appointment();
    }
}