package application.dao;

import application.Main;
import application.datamodel.City;
import application.datamodel.Country;
import application.ui.DialogController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static application.dao.Database.dbUpdate;
import static application.ui.Application.*;
import static application.ui.DialogController.okModalDialog;
import static application.ui.DialogController.yesNoModalDialog;

public class CityDAO implements DAO<City> {

    CountryDAO countryDAO = new CountryDAO();

    @Override
    public Optional<City> lookup(int id) {
        try {
            Statement stmt = Main.dbConn.createStatement();
            String s = "SELECT city, countryId FROM city WHERE cityId=" + id;
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                City c = new City(
                        rs.getString("city"),
                        rs.getInt("countryId"));
                System.out.println("Found exact match: " + c.getCity() + ", " + c.getCountry());
                return Optional.of(c);
            } //No add to db option needed when lookup by id fails - indicative of other issue
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<City> lookup(String city, String countryStr) {
        try {
            Statement stmt = Main.dbConn.createStatement();
            String s = "SELECT countryId, city FROM city WHERE city='" + city + "'";
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                City c = new City(city, rs.getInt("countryId"));
                System.out.println("Found exact match: " + c.getCity() + ", " + c.getCountry());
                return Optional.of(c);
            } else {
                s = "SELECT MIN(countryId) as countryId, city FROM city WHERE city LIKE '%" + city + "%'";
                System.out.println("Executing " + s);
                rs = stmt.executeQuery(s);
                if(rs.next()) {
                    City c = new City(city, rs.getInt("countryId"));
                    System.out.println("Found loose match: " + c.getCity() + ", " + c.getCountry());
                    return Optional.of(c);
                } else {
                    if(!countryStr.equals("")) {
                        if (yesNoModalDialog("City " + city + " not found. Add it with country " + countryStr + "?")) {
                            insert(city, countryStr);
                        }
                    }
                }
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("No city found");
        return Optional.empty();
    }

    @Override
    public boolean insert(City city) {
        boolean ret;
        Country c = city.getCountry();
        String s = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ('" +
                city +
                "', " +
                c.getCountryId() +
                ", now(), '" +
                loggedInUser +
                "', now(), '" +
                loggedInUser +
                "')";
        ret = dbUpdate(s);
        String s1 =
                ret ?
                        "Successfully added city." :
                        "Error adding city " + city + " to database.";
        okModalDialog(s1);
        return ret;
    }

    public boolean insert(String city, String countryStr) {
        boolean ret = false;
        try {
            Country c = countryDAO.GetOptionalOrThrow(countryDAO.lookup(countryStr));
            String s = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ('" +
                    city +
                    "', " +
                    c.getCountryId() +
                    ", now(), '" +
                    loggedInUser +
                    "', now(), '" +
                    loggedInUser +
                    "')";
            ret = dbUpdate(s);
        } catch (Exception ex) {
            ex.printStackTrace();
            DialogController.okModalDialog(ex.toString());
        }
        String s1 =
                ret ?
                        "Successfully added city." :
                        "Error adding city " + city + " to database.";
        okModalDialog(s1);
        return ret;
    }

    @Override
    public boolean update(City city) {
        return false;
    }

    @Override
    public boolean delete(City city) {
        return false;
    }

    @Override
    public City GetOptionalOrThrow(Optional<City> optionalCity) throws Exception {
        return optionalCity.orElseThrow(() -> new Exception("No city contained in Optional<City>"));
    }
}
