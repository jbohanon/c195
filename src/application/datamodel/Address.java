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