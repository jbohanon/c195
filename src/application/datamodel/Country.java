package application.datamodel;

import application.ui.DialogController;

import static application.ui.Application.countryDAO;

public class Country {
    private int _countryId;
    private String _country;

    public Country(int countryId, String country) {
        _countryId = countryId;
        _country = country;
    }

    public Country(int countryId) {
        try {
            Country c = countryDAO.GetOptionalOrThrow(countryDAO.lookup(countryId));
            if(c.getCountryId() > 0) {
                _countryId = countryId;
                _country = c.getCountry();
            } else {
                throw new RuntimeException("Error finding country by countryId");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogController.okModalDialog(ex.toString());
        }
    }

    public int getCountryId() {
        return _countryId;
    }

    public void setCountryId(int countryId) {
        _countryId = countryId;
    }

    public String getCountry() {
        return _country;
    }

    public void setCountry(String country) {
        _country = country;
    }

    public static Country nullCountry() {
        return new Country(-1, "Err");
    }
}