package com.hei.restauman.service;

import com.hei.restauman.entity.*;
import com.hei.restauman.entity.enums.MovementType;
import com.hei.restauman.entity.exceptions.InsufficientIngredientsException;
import com.hei.restauman.entity.exceptions.RetrieveExecption;
import com.hei.restauman.repository.*;
import com.hei.restauman.repository.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.hei.restauman.entity.enums.MovementType.Income;
import static com.hei.restauman.entity.enums.MovementType.Outcome;

@Service
@AllArgsConstructor
public class RestaurantService {
    private final IngredientTemplateRepository ingredientTemplateRepository;
    private IngredientMenuRepository ingredientMenuRepository;
    private MovementRepository movementRepository;
    private StorageRepository storageRepository;
    private MenuRepository menuRepository;
    private UnitRepository unitRepository;
    private RestaurantRepository restaurantRepository;
    private SaleRepository saleRepository;


    private boolean checkStockIngredientByMenuName(String menuName) {
        List<IngredientMenu> ingredientMenuList = ingredientMenuRepository.getAllIngredientMenuByMenu(menuName);
        for (IngredientMenu ingredient : ingredientMenuList) {
            double quantityRequired = ingredient.getQuantity();
            Storage storage = storageRepository.getLastStorageByIngredientTemplate(ingredient.getIngredientTemplate().getName());
            double quantityAvailable = storage == null ? 0 : storage.getValue();
            if (quantityAvailable < quantityRequired) {
                return false;
            }
        }
        return true;
    }

    private List<IngredientMenu> getAllIngredientMenuByMenuName(String menuName) {
        Menu menu = menuRepository.getByName(menuName);
        if (!(menu == null)) {
            return ingredientMenuRepository.getAllIngredientMenuByMenu(menuName);
        }
        throw new RetrieveExecption("the menu that you want to retrieve doesn't exist");
    }

    public void performSupply(Supply supply) {
        IngredientTemplate ingredientTemplate = ingredientTemplateRepository.getByName(supply.getIngredientTemplateName());
        Restaurant restaurant = restaurantRepository.getById(supply.getIdRestaurant());
        IngredientMenu ingredientMenu = new IngredientMenu(ingredientTemplate.getId(), null, ingredientTemplate, 0);
        Movement movementToSave = new Movement(supply.getIdRestaurant(), ingredientMenu, Instant.now(), Income, restaurant, supply.getQuantity());
        Storage existingStorage = storageRepository.findStorageByRestaurantAndIngredient(supply.getIdRestaurant(), supply.getIngredientTemplateName());
        if (existingStorage == null) {
            Storage newStorage = new Storage();
            newStorage.setId(movementToSave.getId());
            newStorage.setSupplyDate(movementToSave.getMovementDatetime());
            newStorage.setIngredientTemplate(movementToSave.getIngredientMenu().getIngredientTemplate());
            newStorage.setIdRestaurant(movementToSave.getRestaurant().getId());
            ;
            storageRepository.saveStorage(newStorage);
        } else {
            existingStorage.setValue(existingStorage.getValue() + supply.getQuantity());
            existingStorage.setSupplyDate(Instant.now());
            storageRepository.updateStorage(existingStorage);
        }
        movementRepository.save(movementToSave);
    }

    public void sellingMenu(Sale sale) {
        Restaurant restaurant = restaurantRepository.getById(sale.getIdRestaurant());
        List<IngredientMenu> ingredientMenuList = getAllIngredientMenuByMenuName(sale.getMenuName());

        if (!(checkStockIngredientByMenuName(sale.getMenuName()))) {
            throw new InsufficientIngredientsException("Insufficient stock for one or more ingredients required for the menu: " + sale.getMenuName());

        }
        for (IngredientMenu ingredientMenu : ingredientMenuList) {
            Storage storage = storageRepository.getLastStorageByIngredientTemplate(ingredientMenu.getIngredientTemplate().getName());
            if (storage != null) {
                storage.setValue(storage.getValue() - ingredientMenu.getQuantity());
                storage.setSupplyDate(Instant.now());
                storageRepository.updateStorage(storage);

                Movement movement = new Movement(ingredientMenu, Instant.now(), Outcome, restaurant, ingredientMenu.getQuantity());
                movementRepository.save(movement);
            }
        }


    }

    public Storage getStorageAtDatetime(Instant datetime, int idRestaurant, IngredientTemplate ingredientTemplate) {
        return storageRepository.getStorageByIngredientTemplateAtDatetime(datetime, idRestaurant, ingredientTemplate);
    }

    public List<MovementDetails> getMovementDetailBetweenDate(Instant startDatetime, Instant endDatetime) {
        return movementRepository.getMovementDetailsBetweenDates(startDatetime, endDatetime);
    }

    public Storage getLastStorageByIngredientTemplate(String ingredientTemplateName) {
        return storageRepository.getLastStorageByIngredientTemplate(ingredientTemplateName);
    }

    public List<Storage> getStorageByIngredientTemplateBetweenDatetime(Instant startDatetime, Instant endDatetime, IngredientTemplate ingredientTemplate) {
        return storageRepository.getStorageByIngredientTemplateBetweenDatetime(startDatetime, endDatetime, ingredientTemplate);
    }

    public void designMenu(MenuRequest menuRequest) {
        ingredientMenuRepository.createIngredientMenu(menuRequest);
    }


    public Menu saveMenu(Menu toSave) {
        return menuRepository.saveMenu(toSave);
    }

    public IngredientTemplate saveIngredientTemplate(IngredientTemplate toSave) {
        return ingredientTemplateRepository.saveIngredientTemplate(toSave);
    }

    public Unit saveUnit(Unit toSave) {
        return unitRepository.saveUnit(toSave);
    }

    public List<UsedMenu> getMostUsedMenuBetweenDate(Instant startDatetime, Instant endDatetime, Integer limit) {
        return saleRepository.getMostUsedMenuBetweenDate(startDatetime, endDatetime, limit);
    }

    public List<MenuSale> getMenuSales() {
        return saleRepository.getMenuSales();
    }
}
