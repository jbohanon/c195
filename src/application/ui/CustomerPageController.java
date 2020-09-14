package application.ui;

import application.datamodel.Address;
import application.datamodel.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Arrays;

import static application.Main.*;

import static application.ui.ApplicationController.*;
import static application.ui.DialogController.okModalDialog;
import static application.ui.DialogController.yesNoModalDialog;

public class CustomerPageController implements EditablePaneBehavior<Customer> {

    // FXML Fields
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

    private Customer _editedCustomer;

    @Override
    public void SearchResults() {
    }

    @FXML
    private void initialize() {
        // TODO localization
        PopulatePane(DisplayedCustomer);
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
        // Change data entry controls
        Arrays.stream(new Control[]{
                custNameText,
                custPhoneText,
                custAddr1Text,
                custAddr2Text,
                custCityText,
                custCountryText,
                custPostalCodeText,
                custActiveCheckbox}
        ).forEach(control -> control.setDisable(!editable));

        // Change edit screen controls
        Arrays.stream(new Button[]{custDiscardBtn, custSaveBtn}).forEach(control -> {
            control.setDisable(!editable);
            control.setVisible(editable);
        });

        // Change non edit screen controls
        Arrays.stream(new Button[]{custEditBtn, custBackBtn}).forEach(control -> {
            control.setDisable(editable);
            control.setVisible(!editable);
        });
    }

    @Override
    public void EditBtnHandler() {
        _editedCustomer = DisplayedCustomer;
        SetEditable(true);

    }

    @Override
    public void BackBtnHandler() {
        sceneChanger.ChangeScene(HomepageFxml);
    }

    @Override
    public void SaveBtnHandler() {

        SetEditable(false);
        if(DisplayedCustomer == null) {
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
            DisplayedCustomer = customerDAO.lookupAndSetCustomerId(newCustomer);
            okModalDialog("New customer created!");
        } else {
            _editedCustomer = new Customer(
                    DisplayedCustomer.getCustomerId(),
                    custNameText.getText(),
                    new Address(DisplayedCustomer.getAddress().getAddressId(),
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

            DisplayedCustomer = _editedCustomer;
        }
    }

    @Override
    public void DiscardBtnHandler() {
        if(DisplayedCustomer == null){
            sceneChanger.ChangeScene(HomepageFxml);
            return;
        }
        initialize();
    }

    @Override
    public void DeleteBtnHandler() {
        if(yesNoModalDialog("Delete customer?")) {
            customerDAO.delete(DisplayedCustomer);
        }
    }
}
