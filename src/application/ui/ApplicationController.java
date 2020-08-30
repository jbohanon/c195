package application.ui;

import application.Main;
import application.dao.CustomerDAO;
import application.datamodel.Address;
import application.datamodel.Customer;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static application.ui.DialogController.okModalDialog;

public class ApplicationController {

    public static String loggedInUser;
    
    public  Button exitBtn;
    
    public  TextField custSearchText;
    
    public  Button custSearchGoBtn;
    
    public  Button addCustomerBtn;
    
    public  TextField apptSearchText;
    
    public  Button apptSearchGoBtn;
    
    public  Button addApptBtn;
    
    public  Button apptTypesReportBtn;
    
    public  Button schedulesReportBtn;
    
    public  AnchorPane scrollPaneAnchor;
    
    public  AnchorPane viewApptsPane;
    
    public  Button calViewChangeBtn;
    
    public  Label calViewLabel;
    
    public  AnchorPane searchResultPane;
    
    public  ListView<Object> searchResultsList;
    
    public  AnchorPane customerPane;
    
    public  Label custDetailsLabel;
    
    public  Label custNameLabel;
    
    public  TextField custNameText;
    
    public  Label custPhoneLabel;
    
    public  TextField custPhoneText;
    
    public  Label custAddr1Label;
    
    public  TextField custAddr1Text;
    
    public  CheckBox custActiveCheckbox;
    
    public  Button custBackBtn;
    
    public  Button custEditBtn;
    
    public  Button custSaveBtn;
    
    public  Button custDiscardBtn;
    
    public  Label custAddr2Label;
    
    public  TextField custAddr2Text;
    
    public  Label custCityLabel;
    
    public  TextField custCityText;
    
    public  Label custCountryLabel;
    
    public  TextField custCountryText;
    
    public  Label custPostalCodeLabel;
    
    public  TextField custPostalCodeText;
    
    public  AnchorPane apptPane;
    
    public  Label apptDetailsLabel;
    
    public  Label apptCustNameLabel;
    
    public  TextField apptCustNameText;
    
    public  Label apptAssociateNameLabel;
    
    public  TextField apptAssociateNameText;
    
    public  Label apptTitleLabel;
    
    public  TextField apptTitleText;
    
    public  Button apptBackBtn;
    
    public  Button apptEditBtn;
    
    public  Button apptSaveBtn;
    
    public  Button apptDiscardBtn;
    
    public  Label apptDescLabel;
    
    public  TextField apptDescText;
    
    public  Label apptLocationLabel;
    
    public  TextField apptLocationText;
    
    public  Label apptContactLabel;
    
    public  TextField apptContactText;
    
    public  Label apptUrlLabel;
    
    public  TextField apptUrlText;
    
    public  RadioButton apptType1Radio;
    
    public  RadioButton apptType2Radio;
    
    public  RadioButton apptType3Radio;
    
    public  DatePicker apptStartTimePicker;
    
    public  DatePicker apptEndTimePicker;

    private Customer _displayedCustomer = null;
    private Customer _editedCustomer;
    private ArrayList<Customer> _custSearchResults;

    private static final CustomerDAO customerDAO = new CustomerDAO();

    public void exitApp() {
        Main.exitApp();
    }

    public void custSearchGoBtnHandler() {
        _custSearchResults = customerDAO.search(custSearchText.getText().replace("'", ""));

        System.out.println(_custSearchResults.toString());
        if(_custSearchResults.size()==1) {
            setCenterAnchor(customerPane);
            _displayedCustomer = _custSearchResults.get(0);
            populateCustomerPane(_displayedCustomer);
            return;
        }
        setCenterAnchor(searchResultPane);
        if(_custSearchResults.isEmpty())
            searchResultsList.setItems(FXCollections.observableArrayList("No results"));
        else {
            ArrayList<String> namePhonePairs = new ArrayList<>();
            _custSearchResults.forEach(a -> namePhonePairs.add(a.getCustomerName() + " - " + a.getAddress().getPhone()));
            searchResultsList.setItems(FXCollections.observableArrayList(namePhonePairs));
        }
    }

    public void SearchResultsListKeyPressHandler(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            if(_custSearchResults.size() > 0) {
                setCenterAnchor(customerPane);
                _custSearchResults.forEach(customer -> {
                    if(searchResultsList.getSelectionModel().getSelectedItem().toString().contains(customer.getCustomerName())) {
                        _displayedCustomer = customer;
                    }
                });
                populateCustomerPane(_displayedCustomer);
                _custSearchResults.clear();
            }

        }
    }

    public void addCustomerBtnHandler() {
        setCenterAnchor(customerPane);
        setCustomerEditable(true);
    }

    public void custEditBtnHandler() {
        _editedCustomer = _displayedCustomer;
        setCustomerEditable(true);
    }
    public void custBackBtnHandler() {
        setCenterAnchor(viewApptsPane);
    }
    public void custSaveBtnHandler() {

        setCustomerEditable(false);
        if(_displayedCustomer == null) {
            Customer newCustomer = new Customer(0,
                    custNameText.getText(),
                    new Address(0,
                            custAddr1Text.getText(),
                            custAddr2Text.getText(),
                            custCityText.getText(),
                            custPostalCodeText.getText(),
                            custPhoneText.getText(),
                            custCountryText.getText()
                    ),
                    custActiveCheckbox.isSelected());
            if(!customerDAO.insert(newCustomer)) {
                okModalDialog("Issue writing new customer to database.");
                return;
            }
            newCustomer = customerDAO.lookupAndSetCustomerId(newCustomer);
            _displayedCustomer = newCustomer;
            okModalDialog("New customer created!");
        } else {
            _editedCustomer = new Customer(
                _displayedCustomer.getCustomerId(),
                custNameText.getText(),
                new Address(_displayedCustomer.getAddress().getAddressId(),
                        custAddr1Text.getText(),
                        custAddr2Text.getText(),
                        custCityText.getText(),
                        custPostalCodeText.getText(),
                        custPhoneText.getText(),
                        custCountryText.getText()
                ),
                    custActiveCheckbox.isSelected());
            if(!customerDAO.update(_editedCustomer)) {
                okModalDialog("Issue writing edited customer to database.");
                return;
            }
            _displayedCustomer = _editedCustomer;
        }
    }

    public void custDiscardBtnHandler() {
        if(_displayedCustomer == null){
            setCenterAnchor(viewApptsPane);
            return;
        }
        populateCustomerPane(_displayedCustomer);
    }

    private void populateCustomerPane(Customer cust) {
        setCustomerEditable(false);
        custNameText.setText(cust.getCustomerName());
        custPhoneText.setText(cust.getAddress().getPhone());
        custAddr1Text.setText(cust.getAddress().getAddress());
        custAddr2Text.setText(cust.getAddress().getAddress2());
        custCityText.setText(cust.getAddress().getCity().getCity());
        custCountryText.setText(cust.getAddress().getCity().getCountry().getCountry());
        custPostalCodeText.setText(cust.getAddress().getPostalCode());
        custActiveCheckbox.setSelected(cust.getActive());
    }

    private void setCustomerEditable(boolean editable) {
        custNameText.setDisable(!editable);
        custPhoneText.setDisable(!editable);
        custAddr1Text.setDisable(!editable);
        custAddr2Text.setDisable(!editable);
        custCityText.setDisable(!editable);
        custCountryText.setDisable(!editable);
        custPostalCodeText.setDisable(!editable);
        custActiveCheckbox.setDisable(!editable);

        custEditBtn.setDisable(editable);
        custEditBtn.setVisible(!editable);
        custBackBtn.setDisable(editable);
        custBackBtn.setVisible(!editable);

        custDiscardBtn.setDisable(!editable);
        custDiscardBtn.setVisible(editable);
        custSaveBtn.setDisable(!editable);
        custSaveBtn.setVisible(editable);
    }

    private void setCenterAnchor(AnchorPane pane) {
        viewApptsPane.setDisable(true);
        viewApptsPane.setVisible(false);
        searchResultPane.setDisable(true);
        searchResultPane.setVisible(false);
        apptPane.setDisable(true);
        apptPane.setVisible(false);
        customerPane.setDisable(true);
        customerPane.setVisible(false);
        pane.setDisable(false);
        pane.setVisible(true);
    }
}
