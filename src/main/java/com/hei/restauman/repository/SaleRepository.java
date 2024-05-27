package com.hei.restauman.repository;

import com.hei.restauman.repository.model.MenuSale;
import com.hei.restauman.repository.model.UsedMenu;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.hei.restauman.entity.enums.MovementType.Outcome;

@Repository
@AllArgsConstructor
public class SaleRepository {
    private Connection connection;

    public List<UsedMenu> getMostUsedMenuBetweenDate(Instant startDatetime, Instant endDatetime, Integer limit) {
        List<UsedMenu> usedMenuList = new ArrayList<>();
        String sql = """
                 SELECT it.name AS ingredient_name, sum(m.quantity) AS total_quantity , max(menu.name) AS menu, u.name AS unit
                 FROM movement m
                 JOIN ingredient_menu im ON m.id_ingredient_menu = im.id
                 JOIN ingredient_template it ON im.id_ingredient_template = it.id
                 JOIN menu  on im.id_menu=menu.id
                 JOIN unit u on u.id = it.id_unit
                 WHERE m.id_restaurant = 1
                 AND m.movement_datetime BETWEEN ? AND  ?
                 AND m.type = 'Outcome'
                 GROUP BY it.name , u.name
                 ORDER BY total_quantity DESC
                 limit ? ;
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.from(startDatetime));
            preparedStatement.setTimestamp(2, Timestamp.from(endDatetime));
            preparedStatement.setInt(4, limit);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UsedMenu usedMenu = new UsedMenu();
                usedMenu.setIngredientName(resultSet.getString("ingredient_name"));
                usedMenu.setMenu(resultSet.getString("menu"));
                usedMenu.setQuantityMax(resultSet.getDouble("total_quantity"));
                usedMenu.setUnit(resultSet.getString("unit"));
                usedMenuList.add(usedMenu);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usedMenuList;
    }

    public List<MenuSale> getMenuSales() {
        List<MenuSale> menuSaleList = new ArrayList<>();
        String sql = """
                SELECT r.localisation AS restaurant_name,m.name AS menu_name,
                COUNT(*) AS num_menus_sold,
                SUM(mv.quantity) AS total_quantity_sold,
                SUM(mv.quantity*m.price) as total_amount_sold
                FROM movement mv 
                JOIN ingredient_menu im ON mv.id_ingredient_menu = im.id 
                JOIN menu m ON im.id_menu = m.id 
                JOIN restaurant r ON mv.id_restaurant = r.id
                WHERE mv.type = 'Outcome'
                GROUP BY r.localisation, m.name 
                ORDER BY r.localisation, m.name
                         """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                MenuSale menuSale = new MenuSale();
                menuSale.setRestaurantName(resultSet.getString("restaurant_name"));
                menuSale.setMenuName(resultSet.getString("menu_name"));
                menuSale.setNumMenusSold(resultSet.getInt("num_menus_sold"));
                menuSale.setTotalQuantitySold(resultSet.getDouble("total_quantity_sold"));
                menuSale.setTotalAmountSold(resultSet.getDouble("total_amount_sold"));
                menuSaleList.add(menuSale);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return menuSaleList;
    }
}

