package com.hei.restauman.repository;

import com.hei.restauman.entity.Menu;
import com.hei.restauman.entity.Restaurant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@AllArgsConstructor
public class RestaurantRepository {
    private Connection connection;

    public Restaurant getById(int idRestaurant){
        String sql = """
                SELECT localisation FROM "restaurant" WHERE id=?;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idRestaurant);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String localisation = resultSet.getString("localisation");

                    return new Restaurant(localisation);
                }
                return null;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


