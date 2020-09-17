package application.ui;

import application.localization.Localization;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static application.ui.Application.*;

public class SearchResults {

    public ListView<String> searchResultsList;
    public Label searchResultsLabel;

    @FXML
    private void initialize() {
        searchResultsList.setItems(FXCollections.observableArrayList(SearchResults));
        // TODO localization
    }

    public void SearchResultsListKeyPressHandler(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            switch (searchType) {
                case CUST: CustomerSelected(Localization.RESOURCE_BUNDLE.CUSTOMER_PAGE);
                case APPT: AppointmentSelected();
                default: CustomerSelected(Localization.RESOURCE_BUNDLE.APPOINTMENT_PAGE);
            }
        }
    }

    private void CustomerSelected(Localization.RESOURCE_BUNDLE nextScene) {
        CustSearchResults.values().forEach(customer -> {
            if(searchResultsList.getSelectionModel().getSelectedItem().contains(customer.getCustomerName())) {
                DisplayedCustomer = customer;
            }
        });
        sceneChanger.ChangeScene(nextScene);
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
        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.APPOINTMENT_PAGE);
        ApptSearchResults.clear();
    }
}
