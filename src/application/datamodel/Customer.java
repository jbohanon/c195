package application.datamodel;

import application.dao.AddressDAO;

public class Customer {
    private int _customerId;
    private String _customerName;
    private boolean _active;
    private Address _address;
    private final static AddressDAO addressDAO = new AddressDAO();

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
        _address = addressDAO.lookup(addressId).orElse(Address.nullAddress());
    }
    
    public void setAddress(Address address) {
        _address = address;
    }

//    public static ArrayList<Customer> searchCustomer(String name) {
//        ArrayList<Customer> custSearchResults = new ArrayList<>();
//        try {
//            Statement stmt = Main.dbConn.createStatement();
//            String s = "SELECT customerId, customerName, addressId, active " +
//                    "FROM customer " +
//                    "WHERE customerName LIKE '%" +
//                    name +
//                    "%'";
//            System.out.println("Executing " + s);
//            ResultSet rs = stmt.executeQuery(s);
//            if(rs.next()) {
//                do {
//                    Customer cust = new Customer(
//                            rs.getInt("customerId"),
//                            rs.getString("customerName"),
//                            rs.getInt("addressId"),
//                            rs.getBoolean("active")
//                    );
//                    System.out.println("Customer " + cust.getCustomerName() + " with Id " + cust.getCustomerId() + " found.");
//                    custSearchResults.add(cust);
//
//                } while (rs.next());
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return custSearchResults;
//    }

//    public boolean updateCustomer() {
//        addressDAO.update(_address);
//
//        String s = "UPDATE customer SET " +
//                "customerName='" + _customerName +
//                "', addressId=" + _address.getAddressId() +
//                ", active=" + _active +
//                ", lastUpdate=now()" +
//                ", lastUpdateBy='" + loggedInUser +
//                "' WHERE customerId=" + _customerId;
//
//        return Database.dbUpdate(s);
////    }
//
//    public boolean lookupAndSetCustomerId() {
//        String s = "SELECT customerId FROM customer WHERE " +
//                "customerName='" + _customerName +
//                "' AND addressId=" + _address.getAddressId();
//        try {
//            Statement stmt = Main.dbConn.createStatement();
//            System.out.println("Executing " + s);
//            ResultSet rs = stmt.executeQuery(s);
//            if(rs.next()) {
//                _customerId = rs.getInt("customerId");
//                System.out.println("Found customer " + _customerName + " with customerId " + _customerId);
//                return true;
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }

//
//    public boolean insertCustomer() {
//        if(!addressDAO.insert(_address)) {
//            okModalDialog("Issue inserting address.");
//            return false;
//        }
//        _address.setAddressId(addressDAO.lookupAndSetAddressId(_address));
//        String s = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
//                "VALUES ('" + _customerName +
//                "', " + _address.getAddressId() +
//                ", " + _active +
//                ", now()" +
//                ", '" + loggedInUser +
//                "', now()" +
//                ", '" + loggedInUser + "')";
//
//        return Database.dbUpdate(s);
//    }
}
