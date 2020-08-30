package application.dao;

import application.Main;
import application.datamodel.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static application.ui.ApplicationController.loggedInUser;
import static application.ui.DialogController.okModalDialog;

public class CustomerDAO implements DAO<Customer> {
    private final AddressDAO addressDAO = new AddressDAO();
    @Override
    public Optional<Customer> lookup(int id) {
        return Optional.empty();
    }

    @Override
    public boolean insert(Customer customer) {
        if(!addressDAO.insert(customer.getAddress())) {
            okModalDialog("Issue inserting address.");
            return false;
        }
        // TODO sketchy address id setting?
        System.out.println("AddressId before: " + customer.getAddress().getAddressId());
        customer.getAddress().setAddressId(addressDAO.lookupAndSetAddressId(customer.getAddress()));
        System.out.println("AddressId after: " + customer.getAddress().getAddressId());
        String s = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                "VALUES ('" + customer.getCustomerName() +
                "', " + customer.getAddress().getAddressId() +
                ", " + customer.getActive() +
                ", now()" +
                ", '" + loggedInUser +
                "', now()" +
                ", '" + loggedInUser + "')";

        return Database.dbUpdate(s);
    }

    @Override
    public boolean update(Customer customer) {
        addressDAO.update(customer.getAddress());

        String s = "UPDATE customer SET " +
                "customerName='" + customer.getCustomerName() +
                "', addressId=" + customer.getAddress().getAddressId() +
                ", active=" + customer.getActive() +
                ", lastUpdate=now()" +
                ", lastUpdateBy='" + loggedInUser +
                "' WHERE customerId=" + customer.getCustomerId();

        return Database.dbUpdate(s);
    }

    @Override
    public boolean delete(Customer customer) {
        return false;
    }

    public ArrayList<Customer> search(String name) {
        ArrayList<Customer> custSearchResults = new ArrayList<>();
        try {
            Statement stmt = Main.dbConn.createStatement();
            String s = "SELECT customerId, customerName, addressId, active " +
                    "FROM customer " +
                    "WHERE customerName LIKE '%" +
                    name +
                    "%'";
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                do {
                    Customer cust = new Customer(
                            rs.getInt("customerId"),
                            rs.getString("customerName"),
                            rs.getInt("addressId"),
                            rs.getBoolean("active")
                    );
                    System.out.println("Customer " + cust.getCustomerName() + " with Id " + cust.getCustomerId() + " found.");
                    custSearchResults.add(cust);

                } while (rs.next());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return custSearchResults;
    }

    public Customer lookupAndSetCustomerId(Customer customer) {
        String s = "SELECT customerId FROM customer WHERE " +
                "customerName='" + customer.getCustomerName() +
                "' AND addressId=" + customer.getAddress().getAddressId();
        try {
            Statement stmt = Main.dbConn.createStatement();
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                customer.setCustomerId(rs.getInt("customerId"));
                System.out.println("Found customer " + customer.getCustomerName() + " with customerId " + customer.getCustomerId());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return customer;
    }
}
