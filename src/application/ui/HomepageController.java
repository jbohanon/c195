package application.ui;

import application.Main;
import application.dao.CustomerDAO;
import application.datamodel.Customer;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class HomepageController {


    public static String loggedInUser;

    public Button exitBtn;

    public TextField custSearchText;

    public  Button custSearchGoBtn;

    public  Button addCustomerBtn;

    public  TextField apptSearchText;

    public  Button apptSearchGoBtn;

    public  Button addApptBtn;

    public  Button apptTypesReportBtn;

    public  Button schedulesReportBtn;

    public  AnchorPane searchResultPane;

    public ListView<Object> searchResultsList;

    public  AnchorPane customerPane;

    private Customer _displayedCustomer = null;
    private Customer _editedCustomer;
    private ArrayList<Customer> _custSearchResults;

    private CustomerDAO customerDAO = new CustomerDAO();

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
            searchResultsList.setItems(FXCollections.observableArrayList(getStr("noResults")));
        else {
            ArrayList<String> namePhonePairs = new ArrayList<>();
            _custSearchResults.forEach(a -> namePhonePairs.add(a.getCustomerName() + " - " + a.getAddress().getPhone()));
            searchResultsList.setItems(FXCollections.observableArrayList(namePhonePairs));
        }
    }

    public void addCustomerBtnHandler() {
        setCenterAnchor(customerPane);
        setCustomerEditable(true);
    }

    public void exitApp(MouseEvent mouseEvent) {
        Main.exitApp();
    }
}
