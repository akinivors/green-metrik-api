package com.greenmetrik.greenmetrikapi.service;

import com.greenmetrik.greenmetrikapi.dto.FoodDataRequest;
import com.greenmetrik.greenmetrikapi.dto.FoodDataResponse;
import com.greenmetrik.greenmetrikapi.model.FoodData;
import com.greenmetrik.greenmetrikapi.model.User;
import com.greenmetrik.greenmetrikapi.repository.FoodDataRepository;
import com.greenmetrik.greenmetrikapi.repository.UserRepository;
import com.greenmetrik.greenmetrikapi.specifications.FoodDataSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public Page<FoodDataResponse> getAllFoodData(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        Specification<FoodData> spec = Specification
                .where(FoodDataSpecification.hasDataDateAfter(startDate))
                .and(FoodDataSpecification.hasDataDateBefore(endDate));

        Page<FoodData> dataPage = foodDataRepository.findAll(spec, pageable);
        return dataPage.map(FoodDataResponse::fromEntity);
    }
}
