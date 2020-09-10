package application.ui;

import application.datamodel.Address;
import application.datamodel.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.function.Function;

import static application.ui.DialogController.okModalDialog;

public class CustomerPageController implements EditablePaneBehavior<Customer> {


    public Label custDetailsLabel;

    public  Label custNameLabel;

    public TextField custNameText;

    public  Label custPhoneLabel;

    public  TextField custPhoneText;

    public  Label custAddr1Label;

    public  TextField custAddr1Text;

    public CheckBox custActiveCheckbox;

    public Button custBackBtn;

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

    @Override
    public void SearchResults() {

    }

    @Override
    public void PopulatePane(Customer cust) {
        SetEditable(false);
        custNameText.setText(cust.getCustomerName());
        custPhoneText.setText(cust.getAddress().getPhone());
        custAddr1Text.setText(cust.getAddress().getAddress());
        custAddr2Text.setText(cust.getAddress().getAddress2());
        custCityText.setText(cust.getAddress().getCity().getCity());
        custCountryText.setText(cust.getAddress().getCity().getCountry().getCountry());
        custPostalCodeText.setText(cust.getAddress().getPostalCode());
        custActiveCheckbox.setSelected(cust.getActive());
    }

    @Override
    public void SetEditable(boolean editable) {

    }

    @Override
    public void EditBtnHandler() {
        _editedCustomer = _displayedCustomer;
        setCustomerEditable(true);

    }

    @Override
    public void BackBtnHandler() {
        setCenterAnchor(viewApptsPane);

    }

    @Override
    public void SaveBtnHandler() {
        Function<TextField, String> getText = TextInputControl::getText;

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

    @Override
    public void DiscardBtnHandler() {
        if(_displayedCustomer == null){
            setCenterAnchor(viewApptsPane);
            return;
        }
        populateCustomerPane(_displayedCustomer);

    }

    @FXML
    public void initialize() {

    }
}
