package application.ui;

import static application.Main.*;
import static application.ui.ApplicationController.*;

import application.datamodel.Appointment;
import application.datamodel.Customer;
import application.localization.Localization;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.TimeZone;

public class HomepageController {

    // FXML Fields
    public Button exitBtn;

    public TextField custSearchText;

    public  Button custSearchGoBtn;

    public  Button addCustomerBtn;

    public  TextField apptSearchText;

    public  Button apptSearchGoBtn;

    public  Button addApptBtn;

    public  Button apptTypesReportBtn;

    public  Button schedulesReportBtn;

    @FXML
    private void initialize() {
        SearchResults.clear();
    }

    public void custSearchGoBtnHandler() {
        ArrayList<Customer> _custSearchResults = customerDAO.search(custSearchText.getText().replace("'", ""));

        System.out.println(_custSearchResults.toString());
        if(_custSearchResults.size()==1) {
            DisplayedCustomer = _custSearchResults.get(0);
            CustEditable = false;
            sceneChanger.ChangeScene(CustomerPageFxml);
            return;
        }

        if(_custSearchResults.isEmpty())
            SearchResults.addAll(FXCollections.observableArrayList(getStr("noResults")));
        else {
            ArrayList<String> displayResults = new ArrayList<>();
            _custSearchResults.forEach(a -> {
                System.out.println("Breaker 1: " + CustSearchResults.size());
                CustSearchResults.put(a.getCustomerName(), a);
                displayResults.add(a.getCustomerName() + " - " + a.getAddress().getPhone());
            });
            SearchResults.addAll(displayResults);
        }
        searchType = SEARCH_TYPE.CUST;
        sceneChanger.ChangeScene(SearchResultsFxml);
    }

    public void addCustomerBtnHandler() {
        CustEditable = true;
        sceneChanger.ChangeScene(CustomerPageFxml);
    }

    public void apptSearchGoBtnHandler() {
        ArrayList<Appointment> _apptSearchResults = appointmentDAO.search(apptSearchText.getText().replace("'", ""));

        _apptSearchResults.forEach(a -> System.out.println(a.toString()));
        if(_apptSearchResults.size()==1) {
            DisplayedAppointment = _apptSearchResults.get(0);
            ApptEditable = false;
            sceneChanger.ChangeScene(AppointmentPageFxml);
            return;
        }

        if(_apptSearchResults.isEmpty())
            SearchResults.addAll(FXCollections.observableArrayList(getStr("noResults")));
        else {
            ArrayList<String> displayResults = new ArrayList<>();
            _apptSearchResults.forEach(a -> {
                ApptSearchResults.put(a.getCustomer().getCustomerName() + a.getTitle() + Localization.getZonedLocalTime(a.getStart(), Localization.getLocalTimeZone().toZoneId()), a);
                displayResults.add(a.getCustomer().getCustomerName() + " - " + a.getTitle() + " - " + Localization.getZonedLocalTime(a.getStart(), TimeZone.getDefault().toZoneId()).toLocalDateTime().toString());
            });
            SearchResults.addAll(FXCollections.observableArrayList(displayResults));
        }
        searchType = SEARCH_TYPE.APPT;
        sceneChanger.ChangeScene(SearchResultsFxml);
    }

    public void addAppointmentBtnHandler() {
        CustEditable = true;
        sceneChanger.ChangeScene(CustomerPageFxml);
    }

    public void exitApp() {
        application.Main.exitApp();
    }
}
