package com.hei.restauman.repository;

import com.hei.restauman.entity.*;
import com.hei.restauman.repository.model.MenuRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class IngredientMenuRepository {
    private final UnitRepository unitRepository;
    private Connection connection;
    private MenuRepository menuRepository;
    private IngredientTemplateRepository ingredientTemplateRepository;

    public IngredientMenu getById(int id) {
        String sql = """
                SELECT id_menu, id_ingredient_template, quantity FROM ingredient_menu where id=? ;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int idIngredientTemplate = resultSet.getInt("id_ingredient_template");
                    int idMenu = resultSet.getInt("id_menu");
                    double quantity = resultSet.getDouble("quantity");
                    Menu menu = menuRepository.getById(idMenu);
                    IngredientTemplate ingredientTemplate = ingredientTemplateRepository.getById(idIngredientTemplate);
                    return new IngredientMenu(menu, ingredientTemplate, quantity);
                }
                return null;

            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error fetching IngredientMenu with ID " + id, ex);
        }
    }

    public List<IngredientMenu> getAllIngredientMenuByMenu(String menuName) {
        List<IngredientMenu> ingredientMenuList = new ArrayList<>();
        String sql = """
                    SELECT "ingredient_template".name,"ingredient_template".unit_price,"ingredient_template".id_unit ,"ingredient_menu".quantity              
                    FROM ingredient_menu 
                    JOIN ingredient_template  ON ingredient_menu.id_ingredient_template = ingredient_template.id
                    JOIN menu ON ingredient_menu.id_menu = menu.id
                    JOIN unit on unit.id = ingredient_template.id_unit
                    WHERE menu.name = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, menuName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double unitPrice = resultSet.getDouble("unit_price");
                int idUnit = resultSet.getInt("id_unit");
                double quantity = resultSet.getDouble("quantity");
                Unit unit = unitRepository.getById(idUnit);
                IngredientTemplate ingredientTemplate = new IngredientTemplate(name, unitPrice, unit);
                Menu menu = menuRepository.getByName(menuName);
                IngredientMenu ingredientMenu = new IngredientMenu(menu, ingredientTemplate, quantity);
                ingredientMenuList.add(ingredientMenu);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientMenuList;
    }

    public void createIngredientMenu(MenuRequest menuRequest) {
        String sql = """
                INSERT INTO ingredient_menu (id,id_menu, id_ingredient_template, quantity) VALUES (?,?,?,?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (Map.Entry<String, Double> entry : menuRequest.getIngredientQuantities().entrySet()) {
                String ingredientName = entry.getKey();
                Double quantity = entry.getValue();
                Menu menu = menuRepository.getByName(menuRequest.getMenuName());
                IngredientTemplate ingredientTemplate = ingredientTemplateRepository.getByName(ingredientName);
                preparedStatement.setInt(1,menuRequest.getId());
                preparedStatement.setInt(2, menu.getId());
                preparedStatement.setInt(3, ingredientTemplate.getId());
                preparedStatement.setDouble(4, quantity);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Creating IngredientMenu failed, no rows affected.");
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}