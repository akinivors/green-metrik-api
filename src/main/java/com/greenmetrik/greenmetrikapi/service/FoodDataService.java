package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.FoodDataRequest;
import com.greenmetrik.greenmetrikapi.dto.FoodDataResponse;
import com.greenmetrik.greenmetrikapi.model.FoodData;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.repository.FoodDataRepository;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodDataService {

    private final FoodDataRepository foodDataRepository;
    private final UserRepository userRepository;

    public FoodDataService(FoodDataRepository foodDataRepository, UserRepository userRepository) {
        this.foodDataRepository = foodDataRepository;
        this.userRepository = userRepository;
    }

    public void addFoodData(FoodDataRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FoodData foodData = new FoodData();
        foodData.setDataDate(request.dataDate());
        foodData.setProductionKg(request.productionKg());
        foodData.setConsumptionKg(request.consumptionKg());
        foodData.setWasteOilLt(request.wasteOilLt());
        foodData.setUser(user);

        foodDataRepository.save(foodData);
    }

    public List<FoodDataResponse> getAllFoodData() {
        return foodDataRepository.findAll().stream()
                .map(FoodDataResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
