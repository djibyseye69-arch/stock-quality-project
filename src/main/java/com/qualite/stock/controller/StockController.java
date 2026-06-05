package com.qualite.stock.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/v1")
public class StockController {

    private final Random random = new Random();

    private static final Map<Predicate<Integer>, String> RISK_RULES = new LinkedHashMap<>();

    static {
        RISK_RULES.put(lvl -> lvl > 0 && lvl < 10 && lvl % 2 == 0, "Low-B");
        RISK_RULES.put(lvl -> lvl > 0 && lvl < 10, "Low-A");
        RISK_RULES.put(lvl -> lvl >= 10 && lvl < 50, "Medium");
    }

    @GetMapping("/risk-index")
    public String calculateRisk(
            @RequestParam(name = "lvl") int lvl) {

        return RISK_RULES.entrySet()
                .stream()
                .filter(entry -> entry.getKey().test(lvl))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("High");
    }

    @GetMapping("/process-data")
    public Map<String, Object> heavyProcessing(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "100") int size) {

        int total = 10000;

        int from = Math.min(page * size, total);
        int to = Math.min(from + size, total);

        List<Double> results = IntStream.range(from, to)
                .mapToObj(i -> random.nextDouble() * Math.pow(i, 2))
                .collect(Collectors.toList());

        return Map.of(
                "page", page,
                "size", size,
                "total", total,
                "results", results
        );
    }
}