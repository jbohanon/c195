package application.ui;

import application.Main;
import application.dao.*;
import application.datamodel.Appointment;
import application.datamodel.Customer;
import javafx.fxml.FXML;

import static application.localization.Localization.*;

import java.util.*;

public class Application {

    public static String loggedInUser;
    public static ArrayList<Appointment> usersAppointments = null;

    public static AddressDAO addressDAO = new AddressDAO();
    public static AppointmentDAO appointmentDAO = new AppointmentDAO();
    public static CityDAO cityDAO = new CityDAO();
    public static CountryDAO countryDAO = new CountryDAO();
    public static CustomerDAO customerDAO = new CustomerDAO();
    public static UserDAO userDAO = new UserDAO();

    public static Customer DisplayedCustomer = null;
    public static boolean CustEditable;
    public static Appointment DisplayedAppointment = null;
    public static boolean ApptEditable;
    public static ArrayList<String> SearchResults = new ArrayList<>();
    public static SceneChanger sceneChanger = new SceneChanger();

    public static SEARCH_TYPE SearchType = null;

    public static HashMap<String, Customer> CustSearchResults = new HashMap<>();
    public static HashMap<String, Appointment> ApptSearchResults = new HashMap<>();

    public enum SEARCH_TYPE {
        CUST, APPT, CUST_FOR_APPT
    }

    public static Stack<RESOURCE_BUNDLE> PageHistoryStack = new Stack<>();

}
