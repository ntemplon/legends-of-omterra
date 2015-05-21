package com.jupiter.europa.entity.ability;

import com.badlogic.ashley.core.Entity;
import com.jupiter.europa.entity.component.ResourceComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by nathan on 5/21/15.
 */
public class ConstantCost extends Cost {

    // Static Methods
    private static String getCostDescription(Map<ResourceComponent.Resources, Integer> costs) {
        return costs.keySet().stream()
                .map(resource -> costs.get(resource) + " " + resource)
                .collect(Collectors.joining("\n"));
    }


    // Fields
    private final Map<ResourceComponent.Resources, Integer> costs = new EnumMap<>(ResourceComponent.Resources.class);
    private final Map<ResourceComponent.Resources, Integer> costsView = Collections.unmodifiableMap(this.costs);


    // Initialization
    public ConstantCost(ResourceComponent.Resources resource, int cost) {
        super(cost + " " + resource.getDisplayName());
        Arrays.asList(ResourceComponent.Resources.values()).stream()
                .filter(res -> res != resource)
                .forEach(res -> this.costs.put(res, 0));
        this.costs.put(resource, cost);
    }

    public ConstantCost(Map<ResourceComponent.Resources, Integer> costs) {
        super(getCostDescription(costs));
        Arrays.asList(ResourceComponent.Resources.values()).stream()
                .forEach(res -> this.costs.put(res, 0));
        this.costs.putAll(costs);
    }


    // Public Methods
    @Override
    public Map<ResourceComponent.Resources, Integer> getSpecificCosts(Entity entity) {
        return this.costsView;
    }
}
