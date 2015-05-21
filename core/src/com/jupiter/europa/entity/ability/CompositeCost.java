package com.jupiter.europa.entity.ability;

import com.badlogic.ashley.core.Entity;
import com.jupiter.europa.entity.component.ResourceComponent;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by nathan on 5/21/15.
 */
public class CompositeCost extends Cost {

    // Static Methods
    private static String getCompositeDescription(List<Cost> costs) {
        return costs.stream()
                .map(Cost::getDescription)
                .collect(Collectors.joining("\n"));
    }

    private static Map<ResourceComponent.Resources, Integer> emptyMap() {
        Map<ResourceComponent.Resources, Integer> map = new EnumMap<>(ResourceComponent.Resources.class);
        for (ResourceComponent.Resources res : ResourceComponent.Resources.values()) {
            map.put(res, 0);
        }
        return map;
    }


    // Fields
    private final List<Cost> costs;


    // Initialization
    public CompositeCost(Cost... costs) {
        super(getCompositeDescription(Arrays.asList(costs)));
        this.costs = Arrays.asList(costs);
    }


    // Public Methods
    @Override
    public Map<ResourceComponent.Resources, Integer> getSpecificCosts(Entity entity) {
        return this.costs.stream()
                .map(cost -> cost.getSpecificCosts(entity))
                .reduce(emptyMap(), (first, second) -> {
                    for (ResourceComponent.Resources resource : second.keySet()) {
                        first.put(resource, first.get(resource) + second.get(resource));
                    }
                    return first;
                });
    }
}
