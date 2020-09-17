package application.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import application.Main;
import application.datamodel.*;

import static application.ui.Application.loggedInUser;

public class AddressDAO implements DAO<Address> {
    @Override
    public Optional<Address> lookup(int addressId) {
        try {
            Statement stmt = Main.dbConn.createStatement();
            String s = "SELECT addressId, address, address2, cityId, postalCode, phone FROM address WHERE addressId=" + addressId;
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                Address addr = new Address(
                        rs.getInt("addressId"),
                        rs.getString("address"),
                        rs.getString("address2"),
                        rs.getInt("cityId"),
                        rs.getString("postalCode"),
                        rs.getString("phone"));
                System.out.println(
                        "Matched address:\n" +
                                addr.getAddress() + "\n" +
                                addr.getAddress2() + "\n" +
                                addr.getCity().getCity() + ", " +
                                addr.getCity().getCountry().getCountry() + " " +
                                addr.getPostalCode() + "\n" +
                                addr.getPhone());
                return Optional.of(addr);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean insert(Address a) {
        String s = "INSERT INTO address " +
                "(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) " +
                "VALUES ('" + a.getAddress() +
                "', '" + a.getAddress2() +
                "', (SELECT MIN(cityId) from city where city='" +
                a.getCity().getCity() +
                "'), '" + a.getPostalCode() +
                "', '" + a.getPhone() +
                "', now(), '" + loggedInUser +
                "', now(), '" + loggedInUser + "')";
        return Database.dbUpdate(s);
    }

    @Override
    public boolean update(Address a) {
        String s = "UPDATE address SET address='" +
                a.getAddress().replace("'", "\\'") +
                "', address2='" +
                a.getAddress2().replace("'", "\\'") +
                "', cityId=(SELECT MIN(cityId) from city where city='" +
                a.getCity().getCity().replace("'", "\\'") +
                "'), postalCode='" +
                a.getPostalCode().replace("'", "\\'") +
                "', phone='" +
                a.getPhone().replace("'", "\\'") +
                "', lastUpdate=now(), lastUpdateBy='" +
                loggedInUser +
                "' WHERE addressId=" +
                a.getAddressId();
        return Database.dbUpdate(s);
    }

    @Override
    public boolean delete(Address a) {
        return false;
    }

    @Override
    public Address GetOptionalOrThrow(Optional<Address> optionalAddress) throws Exception{
        return optionalAddress.orElseThrow(() -> new Exception("No address contained in Optional<Address>"));
    }

    public int lookupAndSetAddressId(Address a) {
        String s = "SELECT addressId FROM address WHERE " +
                "address='" + a.getAddress() +
                "' AND address2='" + a.getAddress2() +
                "' AND postalCode='" + a.getPostalCode() +
                "' AND phone='" + a.getPhone() + "'";
        try{
            Statement stmt = Main.dbConn.createStatement();
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);
            if(rs.next()) {
                return rs.getInt("addressId");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
