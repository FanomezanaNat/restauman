package com.hei.restauman.repository;

import com.hei.restauman.entity.Movement;
import com.hei.restauman.repository.model.MovementDetails;
import com.hei.restauman.entity.enums.MovementType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class MovementRepository {
    private Connection connection;

    public Movement save(Movement toSave) {
        String sql = """
                 INSERT INTO "movement"
                 (id_ingredient_menu, movement_datetime, type, quantity)
                 VALUES (?,?,?,?);
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, toSave.getIngredientMenu().getId());
            preparedStatement.setTimestamp(2, Timestamp.from((toSave.getMovementDatetime())));
            preparedStatement.setObject(3, toSave.getType(),Types.OTHER);
            preparedStatement.setDouble(4, toSave.getQuantity());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                return toSave;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<MovementDetails> getMovementDetailsBetweenDates(Instant startDatetime, Instant endDateTime) {
        List<MovementDetails> movementDetailsList = new ArrayList<>();
        String sql = """
                  SELECT movement.movement_datetime,
                         ingredient_template.name,
                         movement.type,
                         movement.quantity,
                         unit.name
                  FROM movement
                  JOIN ingredient_menu im ON movement.id_ingredient_menu = im.id
                  JOIN ingredient_template ON im.id_ingredient_template = ingredient_template.id
                  JOIN unit ON unit.id = ingredient_template.id_unit
                  WHERE movement.movement_datetime BETWEEN ? AND ?
                  ORDER BY movement.movement_datetime;
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.from(startDatetime));
            preparedStatement.setTimestamp(2, Timestamp.from(endDateTime));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Instant movementDatetime = resultSet.getTimestamp("movement_datetime").toInstant();
                String ingredientName = resultSet.getString("ingredient_template.name");
                MovementType movementType = MovementType.valueOf(resultSet.getString("movement.type")); // Assuming enum type
                double quantity = resultSet.getDouble("movement.quantity");
                String unitName = resultSet.getString("unit.name");

                MovementDetails movementDetails = new MovementDetails(
                        movementDatetime,
                        ingredientName,
                        movementType,
                        quantity,
                        unitName
                );
                movementDetailsList.add(movementDetails);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching movement details", e);
        }

        return movementDetailsList;
    }
}
