package com.example.guiex1.repository.dbrepo;

import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Validator;
import com.example.guiex1.repository.Repository;


import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PrietenieDbRepository implements Repository<Long, Prietenie> {
    private String url;
    private String username;
    private String password;
    private Validator<Prietenie> validator;

    public PrietenieDbRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    public Optional<Prietenie> findOne(Long aLong) {
        String sql = "SELECT * from friendships WHERE id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setLong(1, aLong);
            System.out.println(statement);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            System.out.println(resultSet);
            Long id = resultSet.getLong("id");
            Long idu1 = resultSet.getLong("idu1");
            Long idu2 = resultSet.getLong("idu2");
            LocalDate date = resultSet.getDate("date").toLocalDate();
            Prietenie friendship = new Prietenie(idu1, idu2,date);
            friendship.setId(id);

            return Optional.of(friendship);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idu1 = resultSet.getLong("idu1");
                Long idu2 = resultSet.getLong("idu2");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                Prietenie Prietenie = new Prietenie(idu1, idu2,date);
                Prietenie.setId(id);
                friendships.add(Prietenie);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Prietenie getOne(Long aLong) {
        if(this.findOne(aLong).isPresent()){
            return this.findOne(aLong).get();
        }
        return null;
    }
    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        String sql = "insert into friendships (id1, id2, date) values (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getIdu1());
            ps.setLong(2, entity.getIdu2());
            ps.setDate(3, Date.valueOf(entity.getDate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            //e.printStackTrace();
            return Optional.ofNullable(entity);
        }
        return Optional.empty();
    }


    @Override
    public Optional<Prietenie> delete(Long aLong) {
        String sql = "delete from friendships where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        return Optional.empty();
    }
}

