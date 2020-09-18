package application.ui;

import application.datamodel.Appointment;
import application.datamodel.Customer;
import application.localization.Localization;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;

import static application.ui.Application.*;
import static application.ui.DialogController.okModalDialog;
import static application.ui.DialogController.yesNoModalDialog;

public class AppointmentPage implements EditablePaneBehavior<Appointment> {
    
    // FXML Fields
    public Label apptDetailsLabel;
    public ToggleGroup tg = new ToggleGroup();
    public Label apptCustNameLabel;
    public TextField apptCustNameText;
    public Button getCustomerBtn;
    public Label apptTitleLabel;
    public TextField apptTitleText;
    public Button apptBackBtn;
    public Button apptEditBtn;
    public Button apptSaveBtn;
    public Button apptDiscardBtn;
    public Button apptDeleteBtn;
    public Label apptDescLabel;
    public TextField apptDescText;
    public Label apptLocationLabel;
    public TextField apptLocationText;
    public Label apptContactLabel;
    public TextField apptContactText;
    public Label apptUrlLabel;
    public TextField apptUrlText;
    public RadioButton apptTypeRadio_Intro;
    public RadioButton apptTypeRadio_Tax;
    public RadioButton apptTypeRadio_Invest;
    public DatePicker apptStartDatePicker;
    public ComboBox<String> apptStartTimeComboBox;

    private Appointment _editedAppointment;
    public AnchorPane apptPane;

    // Take list of available times and produce appropriate localized strings
    private final ObservableList<String> times = FXCollections.observableArrayList(
            Arrays.stream(new String[]{"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"})
                    .map(time -> LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
                                    .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
            .toArray(String[]::new));

    public AppointmentPage() {

    }

    @FXML
    private void initialize() {
        apptStartTimeComboBox.setItems(times);

//        apptTypeRadio_Intro.setToggleGroup(tg);
//        apptTypeRadio_Tax.setToggleGroup(tg);
//        apptTypeRadio_Invest.setToggleGroup(tg);

        if(DisplayedAppointment == null) {
            return;
        }

        PopulatePane(DisplayedAppointment);
    }

    @Override
    public void SearchResults() {

    }

    @Override
    public void PopulatePane(Appointment a) {
        SetEditable(false);
        apptCustNameText.setText(a.getCustomer().getCustomerName());
//        apptAssociateNameText.setText(a.getUser().getUserName());
        apptTitleText.setText(a.getTitle());
        apptDescText.setText(a.getDescription());
        apptLocationText.setText(a.getLocation());
        apptContactText.setText(a.getContact());
        apptUrlText.setText(a.getUrl());
        switch(a.getType()) {
            case INTRODUCTION: apptTypeRadio_Intro.setSelected(true); break;
            case CONSULT_TAX: apptTypeRadio_Tax.setSelected(true); break;
            case CONSULT_INVEST: apptTypeRadio_Invest.setSelected(true); break;
            default: break;
        }
        apptStartDatePicker.setValue(a.getStart().toLocalDate());
        apptStartTimeComboBox.getSelectionModel().select(a.getStart().toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
    }

    @Override
    public void SetEditable(boolean editable) {
        // Change data entry controls
        Arrays.stream(new Control[]{
                apptCustNameText,
                apptTitleText,
                apptDescText,
                apptLocationText,
                apptContactText,
                apptUrlText,
                apptTypeRadio_Intro,
                apptTypeRadio_Tax,
                apptTypeRadio_Invest,
                apptStartDatePicker,
                apptStartTimeComboBox
        }).forEach(control -> control.setDisable(!editable));

        // Change edit screen controls
        Arrays.stream(new Button[]{apptDiscardBtn, apptSaveBtn, apptDeleteBtn}).forEach(control -> {
            control.setDisable(!editable);
            control.setVisible(editable);
        });

        // Change non edit screen controls
        Arrays.stream(new Button[]{apptEditBtn, apptBackBtn}).forEach(control -> {
            control.setDisable(editable);
            control.setVisible(!editable);
        });

    }

    @Override
    public void EditBtnHandler() {
        _editedAppointment = DisplayedAppointment;
        SetEditable(true);
    }

    @Override
    public void BackBtnHandler() {
        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.HOMEPAGE);
    }

    @Override
    public void SaveBtnHandler() {
        SetEditable(false);

        if(DisplayedAppointment == null) {
            getCustomerFromField(apptCustNameText.getText());
            try {
                Appointment newAppointment = createAppointment(0);

                if(!appointmentDAO.insert(newAppointment)) {
                    okModalDialog("Issue writing new appointment to database.");
                    return;
                }
                DisplayedAppointment = appointmentDAO.lookupAndSetAppointmentId(newAppointment);
                okModalDialog("New appointment created!");
            } catch (Exception ex) {
                ex.printStackTrace();
                DialogController.okModalDialog(ex.toString());
            }
        } else {
            try {
                _editedAppointment = createAppointment(DisplayedAppointment.getAppointmentId());
                if(!appointmentDAO.update(_editedAppointment)) {
                    okModalDialog("Issue writing edited appointment to database.");
                    return;
                }

                DisplayedAppointment = _editedAppointment;
            } catch (Exception ex) {
                ex.printStackTrace();
                DialogController.okModalDialog(ex.toString());
            }
        }

    }

    @Override
    public void DiscardBtnHandler() {
        if(DisplayedAppointment == null) {
            sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.HOMEPAGE);
            return;
        }
        initialize();
    }

    @Override
    public void DeleteBtnHandler() {
        if(yesNoModalDialog("Delete Appointment?")) {
            appointmentDAO.delete(DisplayedAppointment);
        }
    }

    @FXML
    private void GetCustomerBtnHandler() {

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
            searchType = SEARCH_TYPE.CUST;
            sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.SEARCH_RESULTS);
        } else if(searchRes.size() == 1) {
            DisplayedCustomer = searchRes.get(0);
        } else {
            okModalDialog("Customer not found.");
            throw new RuntimeException("Customer not found - could not save appointment");
        }
    }

    private String getRadioSelectionToString() {
        return (apptTypeRadio_Intro.isSelected() ?
            Appointment.APPT_TYPE.INTRODUCTION.getString() :
            (apptTypeRadio_Tax.isSelected() ?
                    Appointment.APPT_TYPE.CONSULT_TAX.getString() :
                    Appointment.APPT_TYPE.CONSULT_INVEST.getString()));
    }

    private Appointment createAppointment(int id) throws Exception {
        return new Appointment(
                id,
                DisplayedCustomer,
                userDAO.GetOptionalOrThrow(userDAO.lookup(loggedInUser)).getUserId(),
                apptTitleText.getText(),
                apptDescText.getText(),
                apptLocationText.getText(),
                apptContactText.getText(),
                getRadioSelectionToString(),
                apptUrlText.getText(),
                LocalDateTime.of(apptStartDatePicker.getValue(), LocalTime.parse(apptStartTimeComboBox.getSelectionModel().getSelectedItem(), DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))).toString());
    }
}
