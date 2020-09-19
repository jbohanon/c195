package application.ui;

import application.localization.Localization;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static application.ui.Application.*;

public class SearchResults {

    public ListView<String> searchResultsList;
    public Label searchResultsLabel;
    public Button searchResultsBackBtn;

    @FXML
    private void initialize() {
        searchResultsList.setItems(FXCollections.observableArrayList(SearchResults));
    }

    public void SearchResultsListKeyPressHandler(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            switch (SearchType) {
                case CUST:
                case CUST_FOR_APPT:
                    CustomerSelected(Localization.RESOURCE_BUNDLE.CUSTOMER_PAGE);
                    break;
                case APPT:
                    AppointmentSelected();
                    break;
                default:
                    CustomerSelected(Localization.RESOURCE_BUNDLE.APPOINTMENT_PAGE);
            }
        }
    }

    public void SearchResultsBackBtnHandler() {
        PageHistoryStack.pop();
        sceneChanger.ChangeScene(PageHistoryStack.pop());
    }

    private void CustomerSelected(Localization.RESOURCE_BUNDLE nextScene) {
        CustSearchResults.values().forEach(customer -> {
            if (searchResultsList.getSelectionModel().getSelectedItem().contains(customer.getCustomerName())) {
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
                            selStr.contains(appt.getStartLocal().toString())
            ) {
                DisplayedAppointment = appt;
            }
        });
        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.APPOINTMENT_PAGE);
        ApptSearchResults.clear();
    }
}
