package application.datamodel;

import java.time.LocalDateTime;
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
    private LocalDateTime _start;
    private LocalDateTime _end;

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

    public LocalDateTime getStart() {
        return _start;
    }
    public void setStart(LocalDateTime start) {
        _start = start;
    }

    public LocalDateTime getEnd() {
        return _end;
    }
    public void setEnd(LocalDateTime end) {
        _end = end;
    }

    public enum APPT_TYPE {
        TYPE_1, TYPE_2, TYPE_3
    }

}