package application.ui;

import application.datamodel.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    }

    @Override
    public void BackBtnHandler() {

    }

    @Override
    public void SaveBtnHandler() {

    }

    @Override
    public void DiscardBtnHandler() {

    }

    @FXML
    public void initialize() {

    }
}
