package com.hei.restauman.repository;

import com.hei.restauman.entity.IngredientTemplate;
import com.hei.restauman.entity.Menu;
import com.hei.restauman.entity.Unit;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@AllArgsConstructor
public class IngredientTemplateRepository {
    private Connection connection;
    private UnitRepository unitRepository;


    public IngredientTemplate getById(int idIngredientTemplate) {
        String sql = """
                SELECT  name, unit_price, id_unit FROM "ingredient_template" WHERE id = ?;
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idIngredientTemplate);
            return getIngredientTemplate(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching IngredientTemplate with ID " + idIngredientTemplate, e);
        }
    }

    public IngredientTemplate saveIngredientTemplate(IngredientTemplate toSave) {
        String sql = """
                INSERT INTO ingredient_template (id,name, unit_price, id_unit) VALUES (?,?, ?, ?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, toSave.getId());
            preparedStatement.setString(2, toSave.getName());
            preparedStatement.setDouble(3, toSave.getUnitPrice());
            preparedStatement.setInt(4, toSave.getUnit().getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating ingredient template failed, no rows affected.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toSave;
    }

    public IngredientTemplate getByName(String ingredientTemplateName) {
        String sql = """
                SELECT * FROM ingredient_template WHERE name=?;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, ingredientTemplateName);
            return getIngredientTemplate(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    private IngredientTemplate getIngredientTemplate(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double unitPrice = resultSet.getDouble("unit_price");
                int idUnit = resultSet.getInt("id_unit");
                Unit unit = unitRepository.getById(idUnit);

                return new IngredientTemplate(id, name, unitPrice, unit);
            }
            return null;
        }
    }
}
