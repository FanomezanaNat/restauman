package com.hei.restauman.repository;

import com.hei.restauman.entity.Menu;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@AllArgsConstructor
public class MenuRepository {
    private Connection connection;

    public Menu getById(int idMenu) {
        String sql = """
                SELECT price, name FROM "menu" WHERE id=?;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idMenu);
            return getMenu(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching Menu with ID " + idMenu, e);
        }
    }

    public Menu getByName(String menuName) {
        String sql = """
                SELECT * FROM "menu" WHERE name= ?;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, menuName);
            return getMenu(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching Menu with name " + menuName, e);
        }
    }

    public Menu saveMenu(Menu toSave) {
        String sql = """
                INSERT INTO menu (id,price, name) VALUES (?, ?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, toSave.getId());
            preparedStatement.setDouble(2, toSave.getPrice());
            preparedStatement.setString(3, toSave.getName());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating menu failed, no rows affected.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }
    @Nullable
    private Menu getMenu(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");

                return new Menu(id, name, price);
            }
            return null;

        }
    }

}
