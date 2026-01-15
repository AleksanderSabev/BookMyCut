package org.example.bookmycut.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureDto {
    public static final String NAME_EMPTY = "Procedure name cannot be empty";
    public static final String DURATION_INVALID = "Duration must be greater than 0";
    public static final String PRICE_INVALID = "Price cannot be negative";

    private Long id;

    @NotBlank(message = NAME_EMPTY)
    private String name;

    @Positive(message = DURATION_INVALID)
    private int durationInMinutes; // in minutes

    @PositiveOrZero(message = PRICE_INVALID)
    private double price;

}
