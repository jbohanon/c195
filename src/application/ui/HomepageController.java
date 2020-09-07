package application.ui;

import static application.Main.*;
import static application.ui.ApplicationController.*;
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

    private Customer _displayedCustomer = null;
    private Customer _editedCustomer;
    private ArrayList<Customer> _custSearchResults;

    private CustomerDAO customerDAO = new CustomerDAO();

    public void custSearchGoBtnHandler() {
        _custSearchResults = customerDAO.search(custSearchText.getText().replace("'", ""));

        System.out.println(_custSearchResults.toString());
        if(_custSearchResults.size()==1) {
            DisplayedCustomer = _custSearchResults.get(0);
            CustEditable = false;
            app.setScene(custPage);
            return;
        }

        app.setScene(searchResults);
        if(_custSearchResults.isEmpty())
            SearchResults.addAll(FXCollections.observableArrayList(getStr("noResults")));
        else {
            ArrayList<String> namePhonePairs = new ArrayList<>();
            _custSearchResults.forEach(a -> {
                CustSearchResults.put(a.getCustomerName(), a);
                namePhonePairs.add(a.getCustomerName() + " - " + a.getAddress().getPhone());
            });
            SearchResults.addAll(FXCollections.observableArrayList(namePhonePairs));
        }
    }

    public void addCustomerBtnHandler() {
        app.setScene(custPage);
//        setCenterAnchor(customerPane);
        CustEditable = true;
//        setCustomerEditable(true);
    }

    public void exitApp(MouseEvent mouseEvent) {
        application.Main.exitApp();
    }
}
