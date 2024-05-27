package com.hei.restauman.repository;

import com.hei.restauman.entity.Menu;
import com.hei.restauman.entity.Unit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@AllArgsConstructor
public class UnitRepository {
    private Connection connection;

    public Unit getById(int idUnit) {
        String sql = """
                SELECT name FROM unit WHERE id=?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idUnit);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    return new Unit(name);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error with fetch Unit with id:" + idUnit + e);
        }

    }
    public Unit saveUnit(Unit toSave) {
        String sql = """
                INSERT INTO unit (name) VALUES (?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, toSave.getName());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating menu failed, no rows affected.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }
}
