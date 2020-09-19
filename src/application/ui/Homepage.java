package application.ui;

import static application.ui.Application.*;

import application.datamodel.Appointment;
import application.datamodel.Customer;
import application.datamodel.User;
import application.localization.Localization;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;

public class Homepage {

    // FXML Fields
    public Button exitBtn;
    public TextField custSearchText;
    public Button custSearchGoBtn;
    public Button addCustomerBtn;
    public TextField apptSearchText;
    public Button apptSearchGoBtn;
    public Button addApptBtn;
    public Button apptTypesReportBtn;
    public Button schedulesReportBtn;
    public Button customersPerConsultantReportBtn;
    public Button calViewChangeBtn;
    public ListView<String> calListView;
    public Label calViewLabel;

    public LocalDate startOfWeek = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().ordinal() - 1);
    public LocalDate endOfWeek = startOfWeek.plusDays(4);
    public LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
    public LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

    private boolean showByWeek = true;

    @FXML
    private void initialize() {
        try {
            usersAppointments = appointmentDAO.getAllForUser(userDAO.GetOptionalOrThrow(userDAO.lookup(loggedInUser)).getUserId());
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogController.okModalDialog("Unable to find any appointments for " + loggedInUser);
        }

        CalViewChangeBtnHandler();

        SearchResults.clear();

        // Check for appointments starting within 15 min of login
        usersAppointments.forEach(a -> {
            LocalDateTime start = a.getStartLocal();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nowPlus15 = now.plusMinutes(15);
            if (start.isAfter(now) && start.isBefore(nowPlus15)) {
                DialogController.okModalDialog(String.format("%s appointment with %s at %s", a.getType().getString(), a.getCustomer().getCustomerName(), now.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))));
            }
        });
    }

    public void custSearchGoBtnHandler() {
        ArrayList<Customer> _custSearchResults = customerDAO.search(custSearchText.getText().replace("'", ""));

        System.out.println(_custSearchResults.toString());
        if (_custSearchResults.size() == 1) {
            DisplayedCustomer = _custSearchResults.get(0);
            CustEditable = false;
            sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.CUSTOMER_PAGE);
            return;
        }

        if (_custSearchResults.isEmpty())
            SearchResults.addAll(FXCollections.observableArrayList("No Results"));
        else {
            ArrayList<String> displayResults = new ArrayList<>();
            _custSearchResults.forEach(a -> {
                System.out.println("Breaker 1: " + CustSearchResults.size());
                CustSearchResults.put(a.getCustomerName(), a);
                displayResults.add(a.getCustomerName() + " - " + a.getAddress().getPhone());
            });
            SearchResults.addAll(displayResults);
        }
        SearchType = SEARCH_TYPE.CUST;
        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.SEARCH_RESULTS);
    }

    public void addCustomerBtnHandler() {
        DisplayedCustomer = null;
        CustEditable = true;
        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.CUSTOMER_PAGE);
    }

    public void apptSearchGoBtnHandler() {
        ArrayList<Appointment> _apptSearchResults = appointmentDAO.search(apptSearchText.getText().replace("'", ""));

        _apptSearchResults.forEach(a -> System.out.println(a.toString()));
        if (_apptSearchResults.size() == 1) {
            DisplayedAppointment = _apptSearchResults.get(0);
            ApptEditable = false;
            sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.APPOINTMENT_PAGE);
            return;
        }

        if (_apptSearchResults.isEmpty())
            SearchResults.addAll(FXCollections.observableArrayList("No Results"));
        else {
            ArrayList<String> displayResults = new ArrayList<>();
            _apptSearchResults.forEach(a -> {
                ApptSearchResults.put(a.getCustomer().getCustomerName() + a.getTitle() + a.getStartLocal().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)), a);
                displayResults.add(a.getCustomer().getCustomerName() + " - " + a.getTitle() + " - " + a.getStartLocal().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
            });
            SearchResults.addAll(FXCollections.observableArrayList(displayResults));
        }
        SearchType = SEARCH_TYPE.APPT;
        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.SEARCH_RESULTS);
    }

    public void addAppointmentBtnHandler() {
        DisplayedAppointment = null;
        ApptEditable = true;
        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.APPOINTMENT_PAGE);
    }

    public void exitApp() {
        application.Main.exitApp();
    }

    public void CalViewChangeBtnHandler() {
        if (!showByWeek) {
            calViewChangeBtn.setText("Month");
            calViewLabel.setText("Week of " + startOfWeek.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        } else {
            calViewChangeBtn.setText("Week");
            calViewLabel.setText("Month of " + startOfMonth.getMonth() + " " + startOfMonth.getYear());
        }
        showByWeek = !showByWeek;
        setCal();
    }

    public void setCal() {
        calListView.setItems(appointmentsForDisplay(usersAppointments));
    }

    private ObservableList<String> appointmentsForDisplay(ArrayList<Appointment> appts) {
        return FXCollections.observableArrayList(appts.stream()
                .filter(a -> {
                    LocalDate date = a.getStartLocal().toLocalDate();
                    LocalDate start = (showByWeek ? startOfWeek : startOfMonth);
                    LocalDate end = (showByWeek ? endOfWeek : endOfMonth);
                    return date.isAfter(start) && date.isBefore(end);
                })
                .map(Homepage::formatAppointmentForDisplay).toArray(String[]::new));
    }

    private static String formatAppointmentForDisplay(Appointment a) {
        return a.getCustomer().getCustomerName() + " - " + a.getTitle() + " - " + a.getStartLocal().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    private void AppointmentSelected() {
        usersAppointments.forEach(appt -> {
            String selStr = calListView.getSelectionModel().getSelectedItem();
            if (
                    selStr.contains(appt.getCustomer().getCustomerName()) &&
                            selStr.contains(appt.getTitle())
            ) {
                DisplayedAppointment = appt;
            }
        });
        sceneChanger.ChangeScene(Localization.RESOURCE_BUNDLE.APPOINTMENT_PAGE);
    }

    public void CalListViewKeyPressHandler(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            AppointmentSelected();
        }
    }

    public void AppointmentTypesReportBtnHandler() {
        BiPredicate<Appointment.APPT_TYPE, Appointment> typeAndMonthFilter =
                (Appointment.APPT_TYPE type, Appointment appt) ->
                        appt.getType().equals(type) &&
                        appt.getStartLocal().toLocalDate().isAfter(startOfMonth) &&
                        appt.getStartLocal().toLocalDate().isBefore(endOfMonth);
        ArrayList<Appointment> allAppts = appointmentDAO.getAll();
        showReport(String.format("Number of appointments per type for %s:\n" +
                "Introductions: %d\n" +
                "Tax consultations: %d\n" +
                "Investment consultations: %d",
                startOfMonth.getMonth(),
                allAppts.stream().filter(a -> typeAndMonthFilter.test(Appointment.APPT_TYPE.INTRODUCTION, a)).count(),
                allAppts.stream().filter(a -> typeAndMonthFilter.test(Appointment.APPT_TYPE.CONSULT_TAX, a)).count(),
                allAppts.stream().filter(a -> typeAndMonthFilter.test(Appointment.APPT_TYPE.CONSULT_INVEST, a)).count()
                ));
    }

    public void SchedulesReportBtnHandler() {

        ArrayList<Appointment> allAppts = appointmentDAO.getAll();
        HashSet<String> allUsers = new HashSet<>();
        allAppts.forEach(a -> allUsers.add(a.getUser().getUserName()));
        AtomicReference<String> s = new AtomicReference<>("");
        allUsers.forEach(u -> {
            System.out.println("Breaker");
            String title = "Schedule for " + u + "\n";
            AtomicReference<String> tmpS = new AtomicReference<>("");
            allAppts.stream().filter(a -> a.getUser().getUserName().equals(u)).forEach(a -> tmpS.set(tmpS.get() + formatAppointmentForDisplay(a) + "\n"));
            s.set("\n" + s.get() + title + tmpS.get() + "\n");
        }
        );
        showReport(s.get());
    }

    public void CustomersPerConsultantReportBtnHandler() {

        ArrayList<Appointment> allAppts = appointmentDAO.getAll();

        HashSet<String> allUsers = new HashSet<>();
        allAppts.forEach(a -> allUsers.add(a.getUser().getUserName()));

        AtomicReference<String> s = new AtomicReference<>("");
        allUsers.forEach(u -> {
            HashSet<String> custsForUser = new HashSet<>();
            System.out.println("Breaker");
            String title = "Customers scheduled with " + u + "\n";
            AtomicReference<String> tmpS = new AtomicReference<>("");
            allAppts.stream().filter(a -> a.getUser().getUserName().equals(u)).forEach(a -> custsForUser.add(a.getCustomer().getCustomerName()));
            custsForUser.forEach(str -> tmpS.set(tmpS.get() + str + "\n"));
            s.set("\n" + s.get() + title + tmpS.get() + "\n");
        }
        );
        showReport(s.get());
    }

    private void showReport(String contents) {
        Stage popup = new Stage();
        BorderPane root = new BorderPane();
        Text text = new Text(contents);
        text.setTranslateY(15);
        root.getChildren().add(text);

        Scene scene = new Scene(root, 400, 400);
        popup.setScene(scene);

        popup.showAndWait();

    }

}
