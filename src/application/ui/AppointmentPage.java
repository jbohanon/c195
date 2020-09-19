package application.ui;

import application.datamodel.Appointment;
import application.datamodel.Customer;
import application.localization.Localization;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

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

    // Stream list of available times and aggregate appropriate localized strings
    private final ObservableList<String> times = FXCollections.observableArrayList(
            Arrays.stream(new String[]{"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"})
                    .map(time -> LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
                            .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
                    .toArray(String[]::new));
    private ObservableList<String> validTimes = times;

    @FXML
    private void initialize() {
        apptStartTimeComboBox.setItems(validTimes);

        // Lambda used for functional interface java.util.Callback as a factory for day cells
        apptStartDatePicker.setDayCellFactory((DatePicker datePick) -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #EEEEEE;");
                        }
                    }
                }
        );

        if (DisplayedAppointment == null) {
            return;
        }

        PopulatePane(DisplayedAppointment);
    }

    @Override
    public void PopulatePane(Appointment a) {
        SetEditable(false);
        apptCustNameText.setText(a.getCustomer().getCustomerName());
        apptTitleText.setText(a.getTitle());
        apptDescText.setText(a.getDescription());
        apptLocationText.setText(a.getLocation());
        apptContactText.setText(a.getContact());
        apptUrlText.setText(a.getUrl());
        switch (a.getType()) {
            case INTRODUCTION:
                apptTypeRadio_Intro.setSelected(true);
                break;
            case CONSULT_TAX:
                apptTypeRadio_Tax.setSelected(true);
                break;
            case CONSULT_INVEST:
                apptTypeRadio_Invest.setSelected(true);
                break;
            default:
                break;
        }
        apptStartDatePicker.setValue(a.getStartLocal().toLocalDate());
        apptStartTimeComboBox.getSelectionModel().select(a.getStartLocal().toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
        apptStartTimeComboBox.setDisable(false);
    }

    public void DateTimeValidation() {
        validTimes = times;
        LocalDate ld = apptStartDatePicker.getValue();
        usersAppointments.forEach(appt -> {
            if (appt.getStartLocal().toLocalDate().equals(ld)) {
                validTimes.remove(appt.getStartLocal().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
            }
        });

        apptStartTimeComboBox.setItems(validTimes);
        apptStartTimeComboBox.setDisable(false);
    }

    @Override
    public void SetEditable(boolean editable) {
        // Change data entry controls - uses lambda for functional interface java.util.function.Consumer
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

        // Change edit screen controls - uses lambda for functional interface java.util.function.Consumer
        Arrays.stream(new Button[]{apptDiscardBtn, apptSaveBtn, apptDeleteBtn}).forEach(control -> {
            control.setDisable(!editable);
            control.setVisible(editable);
        });

        // Change non edit screen controls - uses lambda for functional interface java.util.function.Consumer
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
        PageHistoryStack.pop();
        sceneChanger.ChangeScene(PageHistoryStack.pop());
    }

    @Override
    public void SaveBtnHandler() {
        if (!ValidateInput()) {
            return;
        }

        SetEditable(false);

        if (DisplayedAppointment == null) {
            getCustomerFromField(apptCustNameText.getText(), SEARCH_TYPE.CUST_FOR_APPT);
            if(PageHistoryStack.peek().equals(Localization.RESOURCE_BUNDLE.CUSTOMER_PAGE)) {
                PageHistoryStack.pop();
            }
            try {
                Appointment newAppointment = createAppointment(0);

                if (!appointmentDAO.insert(newAppointment)) {
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
                if (!appointmentDAO.update(_editedAppointment)) {
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
        if (DisplayedAppointment == null) {
            sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.HOMEPAGE);
            return;
        }
        initialize();
    }

    @Override
    public void DeleteBtnHandler() {
        if (yesNoModalDialog("Delete Appointment?")) {
            appointmentDAO.delete(DisplayedAppointment);
            DisplayedAppointment = null;
            PageHistoryStack.empty();
            sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.HOMEPAGE);
        }
    }

    @Override
    public boolean ValidateInput() {
        AtomicBoolean valid = new AtomicBoolean(true);
        BiConsumer<Predicate<Control>, Control> makeRed = ((fn, ctrl) -> {
            if (fn.test(ctrl)) {
                ctrl.setStyle("-fx-text-box-border: #B22222; -fx-focus-color: #B22222;");
                valid.set(false);
            } else {
                ctrl.setStyle("");
            }
        });

        Arrays.stream(new TextField[]{
                apptCustNameText,
                apptTitleText,
                apptDescText,
                apptLocationText,
                apptContactText,
                apptUrlText
        }).forEach(tf -> makeRed.accept(t -> ((TextField) t).getText().equals(""), tf));

        makeRed.accept(t -> apptStartDatePicker.getValue() == null /*&& apptStartDatePicker.getValue().toString().equals(""))*/, apptStartDatePicker);
        makeRed.accept(t -> apptStartTimeComboBox.getValue() == null /*&& apptStartTimeComboBox.getValue().equals("")*/, apptStartTimeComboBox);


        boolean radioRed = !(apptTypeRadio_Intro.isSelected() || apptTypeRadio_Tax.isSelected() || apptTypeRadio_Invest.isSelected());

        Arrays.stream(new RadioButton[]{
                apptTypeRadio_Intro,
                apptTypeRadio_Tax,
                apptTypeRadio_Invest
        }).forEach(tb -> makeRed.accept(t -> radioRed, tb));

        return valid.get();
    }


    public void GetCustomerBtnHandler() {
        getCustomerFromField(apptCustNameText.getText(), SEARCH_TYPE.CUST);
    }

    private void getCustomerFromField(String custName, SEARCH_TYPE _searchType) {
        SearchType = _searchType;
        ArrayList<Customer> searchRes = customerDAO.search(custName);
        if (searchRes.size() > 1) {
            ArrayList<String> displayResults = new ArrayList<>();
            searchRes.forEach(a -> {
                CustSearchResults.put(a.getCustomerName(), a);
                displayResults.add(a.getCustomerName() + " - " + a.getAddress().getPhone());
            });
            SearchResults.addAll(displayResults);
            sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.SEARCH_RESULTS);
        } else if (searchRes.size() == 1) {
            DisplayedCustomer = searchRes.get(0);
            sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.CUSTOMER_PAGE);
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
