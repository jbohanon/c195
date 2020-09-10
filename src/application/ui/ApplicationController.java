package application.ui;

import application.Main;
import application.dao.CustomerDAO;
import application.datamodel.Address;
import application.datamodel.Appointment;
import application.datamodel.Customer;
import static application.localization.Localization.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static application.ui.DialogController.okModalDialog;

public class ApplicationController {

    public static String loggedInUser;

    public static Customer DisplayedCustomer;
    public static boolean CustEditable;
    public static Appointment DisplayedAppointment;
    public static boolean ApptEditable;
    public static ArrayList<String> SearchResults;
    public enum SEARCH_TYPE {
        CUST, APPT
    }
    public static SEARCH_TYPE searchType = null;

//    private Customer _displayedCustomer = null;
//    private Customer _editedCustomer;
    public static HashMap<String, Customer> CustSearchResults;

//    private static final CustomerDAO customerDAO = new CustomerDAO();
//
//    private Appointment _displayedAppt = null;
//    private Appointment _editedAppt;
    public static HashMap<String, Appointment> ApptSearchResults;

    @FXML
    public void initialize() {
        // TODO Create initialize function to set localized strings
    }

    public void exitApp() {
        Main.exitApp();
    }

    private static String getStr(String propertyLabel) {
        return getLocalizedString(propertyLabel, RESOURCE_BUNDLE.APP);
    }
//
//    public void custSearchGoBtnHandler() {
//        _custSearchResults = customerDAO.search(custSearchText.getText().replace("'", ""));
//
//        System.out.println(_custSearchResults.toString());
//        if(_custSearchResults.size()==1) {
//            setCenterAnchor(customerPane);
//            _displayedCustomer = _custSearchResults.get(0);
//            populateCustomerPane(_displayedCustomer);
//            return;
//        }
//        setCenterAnchor(searchResultPane);
//        if(_custSearchResults.isEmpty())
//            searchResultsList.setItems(FXCollections.observableArrayList(getStr("noResults")));
//        else {
//            ArrayList<String> namePhonePairs = new ArrayList<>();
//            _custSearchResults.forEach(a -> namePhonePairs.add(a.getCustomerName() + " - " + a.getAddress().getPhone()));
//            searchResultsList.setItems(FXCollections.observableArrayList(namePhonePairs));
//        }
//    }
//
//    public void SearchResultsListKeyPressHandler(KeyEvent e) {
//        if(e.getCode() == KeyCode.ENTER) {
//            if(_custSearchResults.size() > 0) {
//                CustomerSearchResults();
//            } else {
//                AppointmentSearchResults();
//            }
//        }
//    }
//
//    private void CustomerSearchResults() {
//        setCenterAnchor(customerPane);
//        _custSearchResults.forEach(customer -> {
//            if(searchResultsList.getSelectionModel().getSelectedItem().toString().contains(customer.getCustomerName())) {
//                _displayedCustomer = customer;
//            }
//        });
//        populateCustomerPane(_displayedCustomer);
//        _custSearchResults.clear();
//    }
//
//    private void AppointmentSearchResults() {
//        setCenterAnchor(apptPane);
//        _apptSearchResults.forEach(appt -> {
//            if(searchResultsList.getSelectionModel().getSelectedItem().toString().contains(appt.getCustomer().getCustomerName())) {
//                _displayedAppt = appt;
//            }
//        });
//        populateCustomerPane(_displayedCustomer);
//        _custSearchResults.clear();
//    }
//
//    public void addCustomerBtnHandler() {
//        setCenterAnchor(customerPane);
//        setCustomerEditable(true);
//    }
//
//    public void custEditBtnHandler() {
//        _editedCustomer = _displayedCustomer;
//        setCustomerEditable(true);
//    }
//    public void custBackBtnHandler() {
//        setCenterAnchor(viewApptsPane);
//    }
//    public void custSaveBtnHandler() {
//        Function<TextField, String> getText = TextInputControl::getText;
//
//        setCustomerEditable(false);
//        if(_displayedCustomer == null) {
//            Customer newCustomer = new Customer(0,
//                    custNameText.getText(),
//                    new Address(0,
//                            custAddr1Text.getText(),
//                            custAddr2Text.getText(),
//                            custCityText.getText(),
//                            custPostalCodeText.getText(),
//                            custPhoneText.getText(),
//                            custCountryText.getText()
//                    ),
//                    custActiveCheckbox.isSelected());
//            if(!customerDAO.insert(newCustomer)) {
//                okModalDialog("Issue writing new customer to database.");
//                return;
//            }
//            newCustomer = customerDAO.lookupAndSetCustomerId(newCustomer);
//            _displayedCustomer = newCustomer;
//            okModalDialog("New customer created!");
//        } else {
//            _editedCustomer = new Customer(
//                _displayedCustomer.getCustomerId(),
//                custNameText.getText(),
//                new Address(_displayedCustomer.getAddress().getAddressId(),
//                        custAddr1Text.getText(),
//                        custAddr2Text.getText(),
//                        custCityText.getText(),
//                        custPostalCodeText.getText(),
//                        custPhoneText.getText(),
//                        custCountryText.getText()
//                ),
//                    custActiveCheckbox.isSelected());
//            if(!customerDAO.update(_editedCustomer)) {
//                okModalDialog("Issue writing edited customer to database.");
//                return;
//            }
//            _displayedCustomer = _editedCustomer;
//        }
//    }

//    public void custDiscardBtnHandler() {
//        if(_displayedCustomer == null){
//            setCenterAnchor(viewApptsPane);
//            return;
//        }
//        populateCustomerPane(_displayedCustomer);
//    }

//    private void populateCustomerPane(Customer cust) {
//        setCustomerEditable(false);
//        custNameText.setText(cust.getCustomerName());
//        custPhoneText.setText(cust.getAddress().getPhone());
//        custAddr1Text.setText(cust.getAddress().getAddress());
//        custAddr2Text.setText(cust.getAddress().getAddress2());
//        custCityText.setText(cust.getAddress().getCity().getCity());
//        custCountryText.setText(cust.getAddress().getCity().getCountry().getCountry());
//        custPostalCodeText.setText(cust.getAddress().getPostalCode());
//        custActiveCheckbox.setSelected(cust.getActive());
//    }

//    private void setCustomerEditable(boolean editable) {
//
//        // Change data entry controls
//        Arrays.stream(new Control[]{
//                custNameText,
//                custPhoneText,
//                custAddr1Text,
//                custAddr2Text,
//                custCityText,
//                custCountryText,
//                custPostalCodeText,
//                custActiveCheckbox}
//        ).forEach(control -> control.setDisable(!editable));
//
//        // Change edit screen controls
//        Arrays.stream(new Button[]{custDiscardBtn, custSaveBtn}).forEach(control -> {
//            control.setDisable(!editable);
//            control.setVisible(editable);
//        });
//
//        // Change non edit screen controls
//        Arrays.stream(new Button[]{custEditBtn, custBackBtn}).forEach(control -> {
//            control.setDisable(editable);
//            control.setVisible(!editable);
//        });
//    }
//
//    private void setCenterAnchor(AnchorPane pane) {
//        // Turn off all center AnchorPanes
//        Arrays.stream(new AnchorPane[]{
//                viewApptsPane,
//                searchResultPane,
//                apptPane,
//                customerPane}
//        ).forEach(ap -> {
//            ap.setDisable(true);
//            ap.setVisible(false);
//        });
//
//        // Turn on needed pane
//        pane.setDisable(false);
//        pane.setVisible(true);
//    }
}
