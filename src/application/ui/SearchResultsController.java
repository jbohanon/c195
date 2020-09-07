package application.ui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SearchResultsController {

    private boolean _isCustomer;
    private boolean _isAppt;


    public void SearchResultsListKeyPressHandler(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            if(_custSearchResults.size() > 0) {
                CustomerSearchResults();
            } else {
                AppointmentSearchResults();
            }
        }
    }

    private void CustomerSearchResults() {
        setCenterAnchor(customerPane);
        _custSearchResults.forEach(customer -> {
            if(searchResultsList.getSelectionModel().getSelectedItem().toString().contains(customer.getCustomerName())) {
                _displayedCustomer = customer;
            }
        });
        populateCustomerPane(_displayedCustomer);
        _custSearchResults.clear();
    }

    private void AppointmentSearchResults() {
        setCenterAnchor(apptPane);
        _apptSearchResults.forEach(appt -> {
            if(searchResultsList.getSelectionModel().getSelectedItem().toString().contains(appt.getCustomer().getCustomerName())) {
                _displayedAppt = appt;
            }
        });
        populateCustomerPane(_displayedCustomer);
        _custSearchResults.clear();
    }
}
