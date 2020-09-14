package application.ui;

import javafx.collections.FXCollections;
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

    @FXML
    private void initialize() {
        searchResultsList.setItems(FXCollections.observableArrayList(SearchResults));
        // TODO localization
    }

    public void SearchResultsListKeyPressHandler(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            if(searchType == SEARCH_TYPE.CUST) {
                CustomerSelected();
            } else {
                AppointmentSelected();
            }
        }
    }

    private void CustomerSelected() {
        CustSearchResults.values().forEach(customer -> {
            if(searchResultsList.getSelectionModel().getSelectedItem().contains(customer.getCustomerName())) {
                DisplayedCustomer = customer;
            }
        });
        sceneChanger.ChangeScene(CustomerPageFxml);
        CustSearchResults.clear();
    }

    private void AppointmentSelected() {
        ApptSearchResults.values().forEach(appt -> {
            String selStr = searchResultsList.getSelectionModel().getSelectedItem();
            if (
                    selStr.contains(appt.getCustomer().getCustomerName()) &&
                    selStr.contains(appt.getTitle()) &&
                    selStr.contains(appt.getStart().toString())
            ) {
                DisplayedAppointment = appt;
            }
        });
        sceneChanger.ChangeScene(AppointmentPageFxml);
        ApptSearchResults.clear();
    }
}
