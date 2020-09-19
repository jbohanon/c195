package application.dao;

import application.Main;
import application.datamodel.Customer;
import application.ui.DialogController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import static application.ui.Application.addressDAO;
import static application.ui.Application.loggedInUser;
import static application.ui.DialogController.okModalDialog;

public class CustomerDAO implements DAO<Customer> {

    @Override
    public Optional<Customer> lookup(int id) {
        try {
            Statement stmt = Database.getConnection().createStatement();
            String s = "SELECT customerName, addressId, active FROM customer WHERE customerId=" + id;
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                Customer c = new Customer(
                        id,
                        rs.getString("customerName"),
                        addressDAO.GetOptionalOrThrow(addressDAO.lookup(rs.getInt("addressId"))),
                        rs.getBoolean("active")
                );
                System.out.println("Customer " + c.getCustomerName() + " with Id " + c.getCustomerId() + " found.");
                return Optional.of(c);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean insert(Customer customer) {
        if(!addressDAO.insert(customer.getAddress())) {
            okModalDialog("Issue inserting address.");
            return false;
        }
        customer.getAddress().setAddressId(addressDAO.lookupAndSetAddressId(customer.getAddress()));
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
        if(!addressDAO.update(customer.getAddress())) {
            DialogController.okModalDialog("Issue occurred updating address.");
            return false;
        }

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
        String s = "DELETE FROM CUSTOMER WHERE customerId=" + customer.getCustomerId();
        return Database.dbUpdate(s);
    }

    @Override
    public Customer GetOptionalOrThrow(Optional<Customer> optionalCustomer) throws Exception {
        return optionalCustomer.orElseThrow(() -> new Exception("No customer contained in Optional<Customer>"));
    }

    public ArrayList<Customer> search(String name) {
        ArrayList<Customer> custSearchResults = new ArrayList<>();
        try {
            Statement stmt = Database.getConnection().createStatement();
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return custSearchResults;
    }

    public Customer lookupAndSetCustomerId(Customer customer) {
        String s = "SELECT customerId FROM customer WHERE " +
                "customerName='" + customer.getCustomerName() +
                "' AND addressId=" + customer.getAddress().getAddressId();
        try {
            Statement stmt = Database.getConnection().createStatement();
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                customer.setCustomerId(rs.getInt("customerId"));
                System.out.println("Found customer " + customer.getCustomerName() + " with customerId " + customer.getCustomerId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return customer;
    }
}
