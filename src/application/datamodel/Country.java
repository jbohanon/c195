package application.datamodel;

import application.ui.DialogController;

import static application.ui.Application.countryDAO;

public class Country {
    private int _countryId;
    private String _country;
//    private final CountryDAO countryDAO = new CountryDAO();

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

//    public static Country lookupCountry(String country) {
//        try {
//            Statement stmt = Main.dbConn.createStatement();
//            String s = "SELECT countryId, country FROM country WHERE country=" + country;
//            System.out.println("Executing " + s);
//            ResultSet rs = stmt.executeQuery(s);
//            if(rs.next()) {
//                Country c = new Country(rs.getInt("countryId"), country);
//                System.out.println("Found " + c.getCountry());
//                return c;
//            }
//        } catch(SQLException ex) {
//            ex.printStackTrace();
//        }
//
//        // Country not found handling
//        if(Application.yesNoModalDialog("Country " + country + " not found. Add it to database?")) {
//            if(addCountry(country)){
//                Application.okModalDialog("Success.");
//                return lookupCountry(country);
//            } else {
//                Application.okModalDialog("Error adding country to database.");
//            }
//        }
//        return nullCountry();
//    }
//    protected static Country lookupCountry(int countryId) {
//        try {
//            Statement stmt = Main.dbConn.createStatement();
//            String s = "SELECT country FROM country WHERE countryId=" + countryId;
//            System.out.println("Executing " + s);
//            ResultSet rs = stmt.executeQuery(s);
//            if(rs.next()) {
//                Country c = new Country(countryId, rs.getString("country"));
//                System.out.println("Found " + c.getCountry());
//                return c;
//            }
//        } catch(SQLException ex) {
//            ex.printStackTrace();
//        }
//        return nullCountry();
//    }

//    protected static boolean addCountry(String country) {
//        try {
//            Statement stmt = Main.dbConn.createStatement();
//            String s = "INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ('" +
//                    country +
//                    ", now(), '" +
//                    loggedInUser +
//                    "', now(), '" +
//                    loggedInUser +
//                    "')";
//            System.out.println("Executing " + s);
//            int res = stmt.executeUpdate(s);
//                return res > 0;
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }