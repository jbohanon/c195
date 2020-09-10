package application.ui;

import static application.Main.*;
import static application.ui.ApplicationController.*;

import application.dao.AppointmentDAO;
import application.dao.CustomerDAO;
import application.datamodel.Appointment;
import application.datamodel.Customer;
import application.localization.Localization;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class HomepageController {


    public static String loggedInUser;

    public Button exitBtn;

    public TextField custSearchText;

    public  Button custSearchGoBtn;

    public  Button addCustomerBtn;

    public  TextField apptSearchText;

    public  Button apptSearchGoBtn;

    public  Button addApptBtn;

    public  Button apptTypesReportBtn;

    public  Button schedulesReportBtn;

    private ArrayList<Customer> _custSearchResults;

    private ArrayList<Appointment> _apptSearchResults;

    private CustomerDAO customerDAO = new CustomerDAO();

    public void custSearchGoBtnHandler() {
        _custSearchResults = customerDAO.search(custSearchText.getText().replace("'", ""));

        System.out.println(_custSearchResults.toString());
        if(_custSearchResults.size()==1) {
            DisplayedCustomer = _custSearchResults.get(0);
            CustEditable = false;
            app.setScene(custPage);
            return;
        }

        if(_custSearchResults.isEmpty())
            SearchResults.addAll(FXCollections.observableArrayList(getStr("noResults")));
        else {
            ArrayList<String> namePhonePairs = new ArrayList<>();
            _custSearchResults.forEach(a -> {
                CustSearchResults.put(a.getCustomerName(), a);
                namePhonePairs.add(a.getCustomerName() + " - " + a.getAddress().getPhone());
            });
            SearchResults.addAll(FXCollections.observableArrayList(namePhonePairs));
        }
        searchType = SEARCH_TYPE.CUST;
        app.setScene(searchResults);
    }

    public void addCustomerBtnHandler() {
        CustEditable = true;
        app.setScene(custPage);
    }

    public void apptSearchGoBtnHandler() {
        _apptSearchResults = AppointmentDAO.search(apptSearchText.getText().replace("'", ""));

        _apptSearchResults.forEach(a -> System.out.println(a.toString()));
        if(_apptSearchResults.size()==1) {
            DisplayedAppointment = _apptSearchResults.get(0);
            ApptEditable = false;
            app.setScene(apptPage);
            return;
        }

        if(_apptSearchResults.isEmpty())
            SearchResults.addAll(FXCollections.observableArrayList(getStr("noResults")));
        else {
            ArrayList<String> namePhonePairs = new ArrayList<>();
            _apptSearchResults.forEach(a -> {
                ApptSearchResults.put(a.getCustomer().getCustomerName() + a.getTitle() + a.getStart().toString(), a);
                namePhonePairs.add(a.getCustomer().getCustomerName() + " - " + a.getTitle() + " - " + Localization.);
            });
            SearchResults.addAll(FXCollections.observableArrayList(namePhonePairs));
        }
        searchType = SEARCH_TYPE.APPT;
        app.setScene(searchResults);
    }

    public void addAppointmentBtnHandler() {
        CustEditable = true;
        app.setScene(custPage);
    }

    public void exitApp() {
        application.Main.exitApp();
    }
}
