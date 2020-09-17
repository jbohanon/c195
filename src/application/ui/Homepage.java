package application.ui;

import static application.Main.*;
import static application.ui.Application.*;

import application.datamodel.Appointment;
import application.datamodel.Customer;
import application.localization.Localization;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.TimeZone;

public class Homepage {

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
            sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.CUSTOMER_PAGE);
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
        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.SEARCH_RESULTS);
    }

    public void addCustomerBtnHandler() {
        CustEditable = true;
        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.CUSTOMER_PAGE);
    }

    public void apptSearchGoBtnHandler() {
        ArrayList<Appointment> _apptSearchResults = appointmentDAO.search(apptSearchText.getText().replace("'", ""));

        _apptSearchResults.forEach(a -> System.out.println(a.toString()));
        if(_apptSearchResults.size()==1) {
            DisplayedAppointment = _apptSearchResults.get(0);
            ApptEditable = false;
            sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.APPOINTMENT_PAGE);
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
        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.SEARCH_RESULTS);
    }

    public void addAppointmentBtnHandler() {
        ApptEditable = true;
        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.APPOINTMENT_PAGE);
    }

    public void exitApp() {
        application.Main.exitApp();
    }
}
