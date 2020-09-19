package application.datamodel;

import application.ui.DialogController;
import static application.ui.Application.*;

public class City {
    private String _city;
    private Country _country;

    public City(String city) {
        _city = city;
        setCountry(city);
    }

    public City(String city, int countryId) {
        _city = city;
        if(countryId > 0) {
            setCountry(countryId);
        } else {
            _country = Country.nullCountry();
        }
    }

    public String getCity() {
        return _city;
    }

    public void setCity(String city) {
        _city = city;
    }


    public Country getCountry() {
        return _country;
    }

    public void setCountry(Country country) {
        _country = country;
    }

    public void setCountry(int countryId) {
        try {
        _country = countryDAO.GetOptionalOrThrow(countryDAO.lookup(countryId));
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogController.okModalDialog(ex.toString());
        }
    }

    public void setCountry(String city) {
        try {
        _country = countryDAO.GetOptionalOrThrow(countryDAO.lookup(city));
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogController.okModalDialog(ex.toString());
        }
    }


    public static City nullCity() {
        return new City("Err", -1);
    }
}