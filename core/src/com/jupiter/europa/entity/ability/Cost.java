package com.jupiter.europa.entity.ability;

import com.badlogic.ashley.core.Entity;
import com.jupiter.europa.entity.Families;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.component.ResourceComponent;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by nathan on 5/20/15.
 */
public abstract class Cost {

    // Constants
    public static final Cost NONE = new Cost("No Cost") {

        private final Map<ResourceComponent.Resources, Integer> costs = new EnumMap<>(ResourceComponent.Resources.class);

        {
            for (ResourceComponent.Resources resource : ResourceComponent.Resources.values()) {
                costs.put(resource, 0);
            }
        }

        private final Map<ResourceComponent.Resources, Integer> costsView = Collections.unmodifiableMap(this.costs);

        @Override
        public Map<ResourceComponent.Resources, Integer> getSpecificCosts(Entity entity) {
            return this.costsView;
        }

        @Override
        public boolean canPay(Entity entity) {
            return true;
        }

        @Override
        public void exact(Entity entity) {

        }
    };


    // Static Methods
    public static Cost constant(ResourceComponent.Resources resource, int cost) {
        return new ConstantCost(resource, cost);
    }

    public static Cost scaling(ResourceComponent.Resources resource, int levelsPerEpoch, int costPerEpoch) {
        return new ScalingCost(resource, levelsPerEpoch, costPerEpoch);
    }

    public static Cost all(Cost... costs) {
        return new CompositeCost(costs);
    }


    // Fields
    private final String description;


    // Initialization
    public Cost(String description) {
        this.description = description;
    }


    // Abstract Methods
    public abstract Map<ResourceComponent.Resources, Integer> getSpecificCosts(Entity entity);


    // Public Methods
    public String getDescription() {
        return this.description;
    }

    public boolean canPay(Entity entity) {
        if (!Families.resourced.matches(entity)) {
            return false;
        }

        Map<ResourceComponent.Resources, Integer> costs = this.getSpecificCosts(entity);
        ResourceComponent component = Mappers.resources.get(entity);
        return costs.keySet().stream()
                .map(resource -> component.getCurrent(resource) >= costs.get(resource)) // Returns if that specific cost can be payed
                .reduce(true, (first, second) -> first && second); // Returns true if all costs can be payed
    }

    public void exact(Entity entity) {
        if (!Families.resourced.matches(entity)) {
            return;
        }

        Map<ResourceComponent.Resources, Integer> costs = this.getSpecificCosts(entity);
        ResourceComponent component = Mappers.resources.get(entity);
        costs.keySet().stream()
                .forEach(resource ->
                                component.setCurrent(resource, Math.max(0, component.getCurrent(resource) - costs.get(resource)))
                ); // Pays all costs
    }

    public final Cost and(Cost other) {
        return new CompositeCost(this, other);
    }
}
