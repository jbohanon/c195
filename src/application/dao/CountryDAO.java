package application.dao;

import application.Main;
import application.datamodel.Address;
import application.datamodel.Country;
import application.ui.ApplicationController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static application.ui.ApplicationController.loggedInUser;
import static application.ui.DialogController.okModalDialog;
import static application.ui.DialogController.yesNoModalDialog;

public class CountryDAO implements DAO<Country> {
    @Override
    public Optional<Country> lookup(int id) {
        try {
            Statement stmt = Main.dbConn.createStatement();
            String s = "SELECT country FROM country WHERE countryId=" + id;
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                Country c = new Country(id, rs.getString("country"));
                System.out.println("Found " + c.getCountry());
                return Optional.of(c);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Country> lookup(String country) {
        try {
            Statement stmt = Main.dbConn.createStatement();
            String s = "SELECT countryId, country FROM country WHERE country=" + country;
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if (rs.next()) {
                Country c = new Country(rs.getInt("countryId"), country);
                System.out.println("Found " + c.getCountry());
                return Optional.of(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // Country not found handling
        if(yesNoModalDialog("Country " + country + " not found. Add it to database?")) {
            if(insert(country)){
                okModalDialog("Success.");
                return lookup(country);
            } else {
                okModalDialog("Error adding country to database.");
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean insert(Country country) {
        String s = "INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ('" +
                country.getCountry() +
                ", now(), '" +
                loggedInUser +
                "', now(), '" +
                loggedInUser +
                "')";
        return Database.dbUpdate(s);
    }

    public boolean insert(String country) {
        String s = "INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES ('" +
                country +
                ", now(), '" +
                loggedInUser +
                "', now(), '" +
                loggedInUser +
                "')";
        return Database.dbUpdate(s);
    }

    @Override
    public boolean update(Country country) {
        return false;
    }

    @Override
    public boolean delete(Country country) {
        return false;
    }

    @Override
    public Country GetOptionalOrThrow(Optional<Country> optionalCountry) {
        return optionalCountry.orElseThrow(() -> new RuntimeException("No country contained in Optional<Country>"));
    }
}
