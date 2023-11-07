package com.nhuszka.reactive.assignment.orders4.nov;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class PuOr {

    private final String itemName;
    private final BigDecimal price;
}
