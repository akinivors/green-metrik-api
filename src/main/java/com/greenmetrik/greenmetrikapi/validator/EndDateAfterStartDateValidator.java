package com.greenmetrik.greenmetrikapi.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            LocalDate startDate = (LocalDate) value.getClass().getMethod("periodStartDate").invoke(value);
            LocalDate endDate = (LocalDate) value.getClass().getMethod("periodEndDate").invoke(value);

            if (startDate == null || endDate == null) {
                return true; // Let @NotNull handle this
            }

            return !endDate.isBefore(startDate);
        } catch (Exception e) {
            // If fields are not accessible, assume valid and let other validators handle it.
            return true;
        }
    }
}
