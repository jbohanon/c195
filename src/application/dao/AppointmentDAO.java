package application.dao;

import application.datamodel.Appointment;

import java.util.List;
import java.util.Optional;

public class AppointmentDAO implements DAO<Appointment> {
    @Override
    public Optional<Appointment> lookup(int id) {
        return Optional.empty();
    }

    @Override
    public boolean insert(Appointment appointment) {
        return false;
    }

    @Override
    public boolean update(Appointment appointment) {
        return false;
    }

    @Override
    public boolean delete(Appointment appointment) {
        return false;
    }
}
