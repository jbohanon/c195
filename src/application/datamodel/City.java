package application.datamodel;

import application.dao.CityDAO;
import application.dao.CountryDAO;

public class City {
    private String _city;
    private Country _country;
    private final static CountryDAO countryDAO = new CountryDAO();
    private final static CityDAO cityDAO = new CityDAO();

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
        _country = countryDAO.lookup(countryId).orElse(Country.nullCountry());
    }

    public void setCountry(String city) {
        _country = countryDAO.lookup(city).orElse(Country.nullCountry());
    }


    public static City nullCity() {
        return new City("Err", -1);
    }
}

//    public void setCity(City city) {
//        _city = city.getCity();
//    }
//    public Country lookupCountry(String city) {
//        return countryDAO.lookup(
//                cityDAO.lookup(city, "").orElse(nullCity())
//                        .getCountry().getCountryId())
//                .orElse(Country.nullCountry());
//    }
//        public boolean insertCity() {
//        return false;
//    }
// public static City lookupCity(String city, String countryStr) {
//        try {
//            Statement stmt = Main.dbConn.createStatement();
//            String s = "SELECT countryId, city FROM city WHERE city='" + city + "'";
//            System.out.println("Executing " + s);
//            ResultSet rs = stmt.executeQuery(s);
//            if(rs.next()) {
//                City c = new City(city, rs.getInt("countryId"));
//                System.out.println("Found exact match: " + c.getCity() + ", " + c.getCountry());
//                return c;
//            } else {
//                s = "SELECT MIN(countryId) as countryId, city FROM city WHERE city LIKE '%" + city + "%'";
//                System.out.println("Executing " + s);
//                rs = stmt.executeQuery(s);
//                if(rs.next()) {
//                    City c = new City(city, rs.getInt("countryId"));
//                    System.out.println("Found loose match: " + c.getCity() + ", " + c.getCountry());
//                    return c;
//                } else {
//                    if(!countryStr.equals("")) {
//                        if (yesNoModalDialog("City " + city + " not found. Add it with country " + countryStr + "?")) {
//                            insertCity(city, countryStr);
//                        }
//                    }
//                }
//            }
//        } catch(SQLException ex) {
//            ex.printStackTrace();
//        }
//        System.out.println("No city found");
//        return nullCity();
//    }

//    public static City lookupCity(int cityId) {
//        try {
//            Statement stmt = Main.dbConn.createStatement();
//            String s = "SELECT city, countryId FROM city WHERE cityId=" + cityId;
//            System.out.println("Executing " + s);
//            ResultSet rs = stmt.executeQuery(s);
//            if(rs.next()) {
//                City c = new City(
//                        rs.getString("city"),
//                        rs.getInt("countryId"));
//                System.out.println("Found exact match: " + c.getCity() + ", " + c.getCountry());
//                return c;
//            } //No add to db option needed when lookup by id fails - indicative of other issue
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return nullCity();
//    }
//


//    public static void insertCity(String city, String country) {
//        Country c = Country.lookupCountry(country);
//        String s = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ('" +
//                city +
//                "', " +
//                c.getCountryId() +
//                ", now(), '" +
//                loggedInUser +
//                "', now(), '" +
//                loggedInUser +
//                "')";
//        String s1 =
//                dbUpdate(s) ?
//                        "Successfully added city." :
//                        "Error adding city " + city + " to database.";
//        okModalDialog(s1);
//    }

//    public static void insertCity(String city, int countryId) {
//        String s = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ('" +
//                city +
//                "', " +
//                countryId +
//                ", now(), '" +
//                loggedInUser +
//                "', now(), '" +
//                loggedInUser +
//                ")";
//        String s1 =
//                dbUpdate(s) ?
//                        "Successfully added city." :
//                        "Error adding city " + city + " to database.";
//        okModalDialog(s1);
//    }
//
//    protected static boolean cityDbHelper(String s) {
//        try {
//            Statement stmt = Main.dbConn.createStatement();
//            System.out.println("Executing " + s);
//            int res = stmt.executeUpdate(s);
//
//            if(res > 0) {
//                return true;
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
