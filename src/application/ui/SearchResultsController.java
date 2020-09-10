package application.ui;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static application.Main.*;
import static application.ui.ApplicationController.*;

public class SearchResultsController {

    public ListView<String> searchResultsList;
    public Label searchResultsLabel;
    private boolean _isCustomer = false;
    private boolean _isAppt = false;

    @FXML
    public void initialize() {
        if(app.getScene().equals(custPage)) {
            _isCustomer = true;
        } else if(app.getScene().equals(apptPage)) {
            _isAppt
        }
    }


    public void SearchResultsListKeyPressHandler(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            if(CustSearchResults.size() > 0) {
                CustomerSearchResults();
            } else {
                AppointmentSearchResults();
            }
        }
    }

    private void CustomerSearchResults() {
//        setCenterAnchor(customerPane);
        CustSearchResults.values().forEach(customer -> {
            if(searchResultsList.getSelectionModel().getSelectedItem().toString().contains(customer.getCustomerName())) {
                DisplayedCustomer = customer;
            }
        });
        app.setScene(custPage);
//        populateCustomerPane(_displayedCustomer);
        CustSearchResults.clear();
    }

    private void AppointmentSearchResults() {
//        setCenterAnchor(apptPane);
        ApptSearchResults.values().forEach(appt -> {
            String selStr = searchResultsList.getSelectionModel().getSelectedItem().toString();
            if (
                    selStr.contains(appt.getCustomer().getCustomerName()) &&
                    selStr.contains(appt.getTitle()) &&
                    selStr.contains(appt.getStart().toString())
            ) {
                DisplayedAppointment = appt;
            }
        });
        app.setScene(apptPage);
//        populateCustomerPane(_displayedCustomer);
        ApptSearchResults.clear();
    }
}
