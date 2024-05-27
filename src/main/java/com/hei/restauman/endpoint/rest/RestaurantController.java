package com.hei.restauman.endpoint.rest;

import com.hei.restauman.entity.*;
import com.hei.restauman.repository.model.*;
import com.hei.restauman.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/restaurant")
@AllArgsConstructor
public class RestaurantController {
    private RestaurantService service;


    @PostMapping("/menu")
    public ResponseEntity<Menu> saveMenu(@RequestBody Menu menu) {
        try {
            Menu menuToSave = service.saveMenu(menu);
            return ResponseEntity.ok(menuToSave);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
    }

    @PostMapping("/unit")
    public ResponseEntity<String> saveUnit(@RequestBody Unit unit) {
        try {
            service.saveUnit(unit);
            return ResponseEntity.ok("unit saved");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(unit.getName() + e);

        }
    }


    @PostMapping("/ingredient")
    public ResponseEntity<IngredientTemplate> saveIngredient(@RequestBody IngredientTemplate ingredientTemplate) {
        try {
            IngredientTemplate saveIngredientTemplate = service.saveIngredientTemplate(ingredientTemplate);
            return ResponseEntity.ok(saveIngredientTemplate);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
    }


    @PostMapping("/design_menu")
    public ResponseEntity<String> designMenu(@RequestBody MenuRequest request) {
        try {
            service.designMenu(request);
            return ResponseEntity.ok("Menu designed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.toString());

        }
    }

    @PostMapping("/supply")
    public ResponseEntity<String> performSupply(@RequestBody Supply supply) {
        try {
            service.performSupply(supply);
            return ResponseEntity.ok("supply saved");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(supply.getIngredientTemplateName() + e);

        }

    }


    @PostMapping("/sell-menu")
    public ResponseEntity<String> sellingMenu(@RequestBody Sale sale) {
        try {
            service.sellingMenu(sale);
            return ResponseEntity.ok("sale saved");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sale.getMenuName()+e);

        }

    }


    @GetMapping("/most-used-menu")
    public ResponseEntity<List<UsedMenu>> getMostUsedMenuBetweenDate(
            @RequestParam(name = "startDatetime") Instant startDatetime,
            @RequestParam(name = "endDatetime") Instant endDatetime,
            @RequestParam(name = "limit") Integer limit) {
        List<UsedMenu> mostUsedMenu = service.getMostUsedMenuBetweenDate(startDatetime, endDatetime, limit);
        return new ResponseEntity<>(mostUsedMenu, HttpStatus.OK);
    }

    @GetMapping("/menu-sales")
    public ResponseEntity<List<MenuSale>> getMenuSales() {
        List<MenuSale> menuSales = service.getMenuSales();
        return new ResponseEntity<>(menuSales, HttpStatus.OK);
    }
}
