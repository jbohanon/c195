package application.ui;

import application.datamodel.Address;
import application.datamodel.Customer;
import application.localization.Localization;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static application.ui.Application.*;
import static application.ui.DialogController.okModalDialog;
import static application.ui.DialogController.yesNoModalDialog;

public class CustomerPage implements EditablePaneBehavior<Customer> {

    // FXML Fields
    public Label custDetailsLabel;

    public Label custNameLabel;

    public TextField custNameText;

    public Label custPhoneLabel;

    public TextField custPhoneText;

    public Label custAddr1Label;

    public TextField custAddr1Text;

    public CheckBox custActiveCheckbox;

    public Button custBackBtn;

    public Button custEditBtn;

    public Button custSaveBtn;

    public Button custDiscardBtn;

    public Label custAddr2Label;

    public TextField custAddr2Text;

    public Label custCityLabel;

    public TextField custCityText;

    public Label custCountryLabel;

    public TextField custCountryText;

    public Label custPostalCodeLabel;

    public TextField custPostalCodeText;

    public Button custDeleteBtn;

    private Customer _editedCustomer;

    @FXML
    private void initialize() {
        if (DisplayedCustomer != null) {
            PopulatePane(DisplayedCustomer);
        }
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
                custActiveCheckbox
        }).forEach(control -> control.setDisable(!editable));

        // Change edit screen controls
        Arrays.stream(new Button[]{custDiscardBtn, custSaveBtn, custDeleteBtn}).forEach(control -> {
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
        PageHistoryStack.pop();
        sceneChanger.ChangeScene(PageHistoryStack.pop());
    }

    @Override
    public void SaveBtnHandler() {

        if (!ValidateInput()) {
            return;
        }

        SetEditable(false);
        if (DisplayedCustomer == null) {
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

            if (!customerDAO.insert(newCustomer)) {
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

            if (!customerDAO.update(_editedCustomer)) {
                okModalDialog("Issue writing edited customer to database.");
                return;
            }

            DisplayedCustomer = _editedCustomer;
        }
    }

    @Override
    public void DiscardBtnHandler() {
        if (DisplayedCustomer == null) {
            sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.HOMEPAGE);
            return;
        }
        initialize();
    }

    @Override
    public void DeleteBtnHandler() {
        if (yesNoModalDialog("Delete customer?")) {
            customerDAO.delete(DisplayedCustomer);
            DisplayedCustomer = null;
            PageHistoryStack.empty();
            sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.HOMEPAGE);
        }
    }

    @Override
    public boolean ValidateInput() {
        AtomicBoolean valid = new AtomicBoolean(true);
        Arrays.stream(new TextField[]{
                custNameText,
                custPhoneText,
                custAddr1Text,
                custAddr2Text,
                custCityText,
                custCountryText,
                custPostalCodeText
        }).forEach(tb -> {
            if (tb.getText().equals("")) {
                tb.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
                valid.set(false);
            } else {
                tb.setStyle("");
            }
        });
        return valid.get();
    }
}
