package application.datamodel;

import static application.ui.Application.*;
import application.ui.DialogController;

public class Address {
    private int _addressId;
    private String _address;
    private String _address2;
    private String _postalCode;
    private String _phone;
    private City _city;
    private final String _countryStr;

    public Address(int addressId, String address, String address2, int cityId, String postalCode, String phone, String countryStr) {
        _addressId = addressId;
        _address = address;
        _address2 = address2;
        _postalCode = postalCode;
        _phone = phone;
        _countryStr = countryStr;

        try {
            _city = cityDAO.GetOptionalOrThrow(cityDAO.lookup(cityId));
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogController.okModalDialog(ex.toString());
        }
    }

    public Address(int addressId, String address, String address2, String city, String postalCode, String phone, String countryStr) {
        _addressId = addressId;
        _address = address;
        _address2 = address2;
        _postalCode = postalCode;
        _phone = phone;
        setCity(city);
        _countryStr = countryStr;
    }
    public Address(int addressId, String address, String address2, int cityId, String postalCode, String phone) {
        _addressId = addressId;
        _address = address;
        _address2 = address2;
        _postalCode = postalCode;
        _phone = phone;
        _countryStr = "";

        try {
            _city = cityDAO.GetOptionalOrThrow(cityDAO.lookup(cityId));
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogController.okModalDialog(ex.toString());
        }
    }

    public Address(int addressId, String address, String address2, String city, String postalCode, String phone) {
        _addressId = addressId;
        _address = address;
        _address2 = address2;
        _postalCode = postalCode;
        _phone = phone;
        setCity(city);
        _countryStr = "";
    }

    private Address() {
        _addressId = -1;
        _address = "Err";
        _address2 = "Err";
        _postalCode = "Err";
        _phone = "Err";
        _city = City.nullCity();
        _countryStr = "Err";
    }

    public int getAddressId() {
        return _addressId;
    }

    public void setAddressId(int addressId) {
        _addressId = addressId;
    }

    public String getAddress() {
        return _address;
    }
    public void setAddress(String address) {
        _address = address;
    }

    public String getAddress2() {
        return _address2;
    }
    public void setAddress2(String address2) {
        _address2 = address2;
    }

    public String getPostalCode() {
        return _postalCode;
    }
    public void setPostalCode(String postalCode) {
        _postalCode = postalCode;
    }

    public String getPhone() {
        return _phone;
    }
    public void setPhone(String phone) {
        _phone = phone;
    }

    public City getCity() {
        return _city;
    }
    public void setCity(String city) {
        try {
            _city = cityDAO.GetOptionalOrThrow(cityDAO.lookup(city, _countryStr));
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogController.okModalDialog(ex.toString());
        }
    }
    public static Address nullAddress() {
        return new Address();
    }
}


//    public boolean updateAddress() {
//        String s = "UPDATE address SET address='" +
//                _address.replace("'", "\\'") +
//                "', address2='" +
//                _address2.replace("'", "\\'") +
//                "', cityId=(SELECT MIN(cityId) from city where city='" +
//                _city.getCity().replace("'", "\\'") +
//                "'), postalCode='" +
//                _postalCode.replace("'", "\\'") +
//                "', phone='" +
//                _phone.replace("'", "\\'") +
//                "', lastUpdate=now(), lastUpdateBy='" +
//                loggedInUser +
//                "' WHERE addressId=" +
//                _addressId;
//        return Database.dbUpdate(s);
//    }

//    public boolean insertAddress() {
//        String s = "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) " +
//                "VALUES ('" + _address +
//                "', '" + _address2 +
//                "', (SELECT MIN(cityId) from city where city='" +
//                _city.getCity() +
//                "'), '" + _postalCode +
//                "', '" + _phone +
//                "', now(), '" + loggedInUser +
//                "', now(), '" + loggedInUser + "')";
//        return Database.dbUpdate(s);
//    }

//    public static Address lookupAddress(int addressId) {
//        try {
//            Statement stmt = Main.dbConn.createStatement();
//            String s = "SELECT addressId, address, address2, cityId, postalCode, phone FROM address WHERE addressId=" + addressId;
//            System.out.println("Executing " + s);
//            ResultSet rs = stmt.executeQuery(s);
//            if(rs.next()) {
//                Address a = new Address(
//                        rs.getInt("addressId"),
//                        rs.getString("address"),
//                        rs.getString("address2"),
//                        rs.getInt("cityId"),
//                        rs.getString("postalCode"),
//                        rs.getString("phone"));
//                System.out.println(
//                        "Matched address:\n" +
//                                a.getAddress() +
//                                "\n" +
//                                a.getAddress2() +
//                                "\n" +
//                                a.getCity().getCity() +
//                                ", " +
//                                a.getCity().getCountry().getCountry() +
//                                " " +
//                                a.getPostalCode() +
//                                "\n" +
//                                a.getPhone());
//                return a;
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return nullAddress();
//    }
//
//    public boolean lookupAndSetAddressId() {
//        String s = "SELECT addressId FROM address WHERE " +
//                "address='" + _address +
//                "' AND address2='" + _address2 +
//                "' AND postalCode='" + _postalCode +
//                "' AND phone='" + _phone + "'";
//        try{
//            Statement stmt = Main.dbConn.createStatement();
//            System.out.println("Executing " + s);
//            ResultSet rs = stmt.executeQuery(s);
//            if(rs.next()) {
//                _addressId = rs.getInt("addressId");
//                return true;
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }

