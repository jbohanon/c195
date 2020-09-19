package application.datamodel;

import application.ui.DialogController;

import static application.ui.Application.addressDAO;

public class Customer {
    private int _customerId;
    private String _customerName;
    private boolean _active;
    private Address _address;

    public Customer(int customerId, String customerName, int addressId, boolean active){
        _customerId = customerId;
        _customerName = customerName;
        _active = active;
        setAddress(addressId);
    }

    public Customer(int customerId, String customerName, Address address, boolean active) {
        _customerId = customerId;
        _customerName = customerName;
        _active = active;
        _address = address;
    }

    public Customer() {
        _customerId = -1;
        _customerName = "Err";
        _active = false;
        _address = Address.nullAddress();
    }

    public int getCustomerId() {
        return _customerId;
    }
    
    public void setCustomerId(int customerId) {
        _customerId = customerId;
    }

    public String getCustomerName() {
        return _customerName;
    }
    
    public void setCustomerName(String customerName) {
        _customerName = customerName;
    }

    public boolean getActive() {
        return _active;
    }
    
    public void setActive(boolean active) {
        _active = active;
    }

    public Address getAddress() {
        return _address;
    }

    public void setAddress(int addressId) {
        try {
            _address = addressDAO.GetOptionalOrThrow(addressDAO.lookup(addressId));
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogController.okModalDialog(ex.toString());
        }
    }
    
    public void setAddress(Address address) {
        _address = address;
    }

    public static Customer nullCustomer() {
        return new Customer();
    }
}