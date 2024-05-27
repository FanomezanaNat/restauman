package com.hei.restauman.repository;

import com.hei.restauman.entity.IngredientTemplate;
import com.hei.restauman.entity.Storage;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class StorageRepository {
    private Connection connection;
    private IngredientTemplateRepository ingredientTemplateRepository;


    public void saveStorage(Storage toSave) {
        String sql = """
                INSERT INTO "storage"(id_restaurant, id_ingredient_template, value, supply_date)
                VALUES (?,?,?,?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, toSave.getIdRestaurant());
            preparedStatement.setInt(2, toSave.getIngredientTemplate().getId());
            preparedStatement.setDouble(3, toSave.getValue());
            preparedStatement.setTimestamp(4, Timestamp.from(toSave.getSupplyDate()));
        } catch (SQLException e) {
            throw new RuntimeException("Error with creating storage");
        }
    }

    public void updateStorage(Storage storage) {
        String sql = """
                UPDATE storage 
                SET value = ? WHERE id_restaurant = ? AND id_ingredient_template = ? AND id=?
                 """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, storage.getValue());
            preparedStatement.setInt(2, storage.getIdRestaurant());
            preparedStatement.setInt(3, storage.getIngredientTemplate().getId());
            preparedStatement.setInt(4, storage.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error with updating storage" + e);
        }
    }

    public Storage getStorageByIngredientTemplateAtDatetime(Instant datetime, Integer idRestaurant, IngredientTemplate ingredientTemplate) {
        String sql = """
                SELECT * FROM "storage"
                WHERE supply_date <=? AND id_restaurant=? AND id_ingredient_template=?
                ORDER BY supply_date DESC LIMIT 1;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.from(datetime));
            preparedStatement.setInt(2, idRestaurant);
            preparedStatement.setInt(3, ingredientTemplate.getId());
            return getStorage(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException("Error with getting storage" + e);
        }

    }

    public List<Storage> getStorageByIngredientTemplateBetweenDatetime(Instant startDatetime, Instant endDateTime, IngredientTemplate ingredientTemplate) {
        List<Storage> storageList = new ArrayList<>();
        String sql = """
                SELECT * FROM "storage" WHERE supply_date BETWEEN ? AND ? AND id_ingredient_template = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.from(startDatetime));
            preparedStatement.setTimestamp(2, Timestamp.from(endDateTime));
            preparedStatement.setInt(3, ingredientTemplate.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Storage storage = new Storage();
                    int id = resultSet.getInt("id");
                    int storedRestaurantId = resultSet.getInt("id_restaurant");
                    int value = resultSet.getInt("value");
                    Timestamp supplyDate = resultSet.getTimestamp("supply_date");
                    storage.setId(id);
                    storage.setIngredientTemplate(ingredientTemplate);
                    storage.setIdRestaurant(storedRestaurantId);
                    storage.setValue(value);
                    storage.setSupplyDate(supplyDate.toInstant());
                    storageList.add(storage);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching storage" + e);
        }
        return storageList;
    }

    public Storage findStorageByRestaurantAndIngredient(int restaurantId, String ingredientTemplateName) {
        String sql = """
                SELECT  storage.value,it.name FROM "storage"
                 INNER JOIN ingredient_template it on it.id = storage.id_ingredient_template
                WHERE id_restaurant = ? AND it.name = ? ;
                       """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, restaurantId);
            preparedStatement.setString(2, ingredientTemplateName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int idIngredientTemplate = resultSet.getInt("id_ingredient_template");
                    IngredientTemplate ingredientTemplate = ingredientTemplateRepository.getById(idIngredientTemplate);
                    Storage storage = new Storage();
                    storage.setId(resultSet.getInt("id"));
                    storage.setIngredientTemplate(ingredientTemplate);
                    storage.setIdRestaurant(resultSet.getInt("id_restaurant"));
                    storage.setValue(resultSet.getDouble("value"));
                    storage.setSupplyDate(resultSet.getTimestamp("supply_date").toInstant());
                    return storage;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Storage getLastStorageByIngredientTemplate(String ingredientTemplateName) {
        String sql = """
                SELECT storage.value, it.name FROM "storage"
                JOIN public.ingredient_template it on it.id = storage.id_ingredient_template
                WHERE it.name=?
                ORDER BY supply_date DESC LIMIT 1;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, ingredientTemplateName);
            return getStorage(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException("Error with getting storage" + e);
        }

    }

    @Nullable
    private Storage getStorage(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int idIngredientTemplate = resultSet.getInt("id_ingredient_template");
                IngredientTemplate ingredientTemplate = ingredientTemplateRepository.getById(idIngredientTemplate);
                int id = resultSet.getInt("id");
                int storedRestaurantId = resultSet.getInt("id_restaurant");
                int ingredientTemplateId = resultSet.getInt("id_ingredient_template");
                int value = resultSet.getInt("value");
                Timestamp supplyDate = resultSet.getTimestamp("supply_date");
                return new Storage(id, ingredientTemplate, storedRestaurantId, value, supplyDate.toInstant());

            }
            return null;
        }
    }
}
