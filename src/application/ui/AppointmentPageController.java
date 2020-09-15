package application.ui;

import application.datamodel.Address;
import application.datamodel.Appointment;
import application.datamodel.Customer;
import application.datamodel.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

import static application.Main.SearchResultsFxml;
import static application.ui.ApplicationController.*;
import static application.ui.DialogController.okModalDialog;

public class AppointmentPageController implements EditablePaneBehavior<Appointment> {
    
    // FXML Fields
    public Label apptDetailsLabel;
    public Label apptCustNameLabel;
    public TextField apptCustNameText;
    public Label apptAssociateNameLabel;
    public TextField apptAssociateNameText;
    public Label apptTitleLabel;
    public TextField apptTitleText;
    public Button apptBackBtn;
    public Button apptEditBtn;
    public Button apptSaveBtn;
    public Button apptDiscardBtn;
    public Label apptDescLabel;
    public TextField apptDescText;
    public Label apptLocationLabel;
    public TextField apptLocationText;
    public Label apptContactLabel;
    public TextField apptContactText;
    public Label apptUrlLabel;
    public TextField apptUrlText;
    public RadioButton apptType1Radio;
    public RadioButton apptType2Radio;
    public RadioButton apptType3Radio;
    public DatePicker apptStartDatePicker;
    public TimeField apptStartTimeField;
    public DatePicker apptEndDatePicker;
    public TimeField apptEndTimeField;

    private Appointment _editedAppointment;

    @FXML
    private void initialize() {
        if (searchType == SEARCH_TYPE.CUST_FOR_APPT) {
            PopulatePane(DisplayedAppointment);
        }
    }

    @Override
    public void SearchResults() {

    }

    @Override
    public void PopulatePane(Appointment appt) {

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
        SetEditable(false);

        if(DisplayedAppointment == null) {
            getCustomerFromField(apptCustNameText.getText());
            Appointment newAppointment = new Appointment(0,
                    DisplayedCustomer.getCustomerId(),
                    userDAO.GetOptionalOrThrow(userDAO.lookup(loggedInUser)).getUserId(),
                    apptTitleText.getText(),
                    apptDescText.getText(),
                    apptLocationText.getText(),
                    apptContactText.getText(),
                    apptType1Radio.isSelected() ? "type_1" : (apptType2Radio.isSelected() ? "type_2" : "type_3"),
                    apptUrlText.getText(),
                    LocalDateTime.of(apptStartDatePicker.getValue(), apptStartTimeField.getLocalTime()).toString(),
                    LocalDateTime.of(apptEndDatePicker.getValue(), apptEndTimeField.getLocalTime()).toString()
                    // TODO TRY/CATCH for RuntimeException
                    );

            if(!appointmentDAO.insert(newAppointment)) {
                okModalDialog("Issue writing new appointment to database.");
                return;
            }
            DisplayedAppointment = appointmentDAO.lookupAndSetAppointmentId(newAppointment);
            okModalDialog("New appointment created!");
        } else {
            _editedAppointment = new Appointment(
                    DisplayedAppointment.getAppointmentId(),
                    DisplayedCustomer.getCustomerId(),
                    userDAO.GetOptionalOrThrow(userDAO.lookup(loggedInUser)).getUserId(),
                    apptTitleText.getText(),
                    apptDescText.getText(),
                    apptLocationText.getText(),
                    apptContactText.getText(),
                    apptType1Radio.isSelected() ? "type_1" : (apptType2Radio.isSelected() ? "type_2" : "type_3"),
                    apptUrlText.getText(),
                    LocalDateTime.of(apptStartDatePicker.getValue(), apptStartTimeField.getLocalTime()).toString(),
                    LocalDateTime.of(apptEndDatePicker.getValue(), apptEndTimeField.getLocalTime()).toString());

            if(!appointmentDAO.update(_editedAppointment)) {
                okModalDialog("Issue writing edited appointment to database.");
                return;
            }

            DisplayedAppointment = _editedAppointment;
        }

    }

    @Override
    public void DiscardBtnHandler() {

    }

    @Override
    public void DeleteBtnHandler() {

    }

    private void getCustomerFromField(String custName) {
        ArrayList<Customer> searchRes = customerDAO.search(custName);
        if(searchRes.size() > 1) {
            ArrayList<String> displayResults = new ArrayList<>();
            searchRes.forEach(a -> {
                System.out.println("Breaker 1: " + CustSearchResults.size());
                CustSearchResults.put(a.getCustomerName(), a);
                displayResults.add(a.getCustomerName() + " - " + a.getAddress().getPhone());
            });
            SearchResults.addAll(displayResults);
        }
        searchType = SEARCH_TYPE.CUST;
        sceneChanger.ChangeScene(SearchResultsFxml);
    }
}
