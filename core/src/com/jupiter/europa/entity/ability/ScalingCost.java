package com.jupiter.europa.entity.ability;

import com.badlogic.ashley.core.Entity;
import com.jupiter.europa.entity.Families;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.component.ResourceComponent;
import com.jupiter.europa.entity.stats.characterclass.CharacterClass;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by nathan on 5/21/15.
 */
public class ScalingCost extends Cost {

    // Static Methods
    private static String getDescription(ResourceComponent.Resources resource, int levelsPerEpoch, int costPerEpoch) {
        StringBuilder sb = new StringBuilder();
        sb.append(costPerEpoch);
        sb.append(' ').append(resource.getDisplayName());
        sb.append(" per ");
        if (levelsPerEpoch == 1) {
            sb.append("Level");
        } else {
            sb.append(levelsPerEpoch).append(" Levels");
        }
        return sb.toString();
    }


    // Fields
    private final ResourceComponent.Resources resource;
    private final int levelsPerEpoch;
    private final int costPerEpoch;
    private final Map<ResourceComponent.Resources, Integer> costs = new EnumMap<>(ResourceComponent.Resources.class);
    private final Map<ResourceComponent.Resources, Integer> costsView = Collections.unmodifiableMap(this.costs);


    // Initialization
    public ScalingCost(ResourceComponent.Resources resource, int levelsPerEpoch, int costPerEpoch) {
        super(getDescription(resource, levelsPerEpoch, costPerEpoch));
        this.resource = resource;
        this.levelsPerEpoch = levelsPerEpoch;
        this.costPerEpoch = costPerEpoch;

        for (ResourceComponent.Resources res : ResourceComponent.Resources.values()) {
            this.costs.put(res, 0);
        }
    }


    // Public Methods
    @Override
    public Map<ResourceComponent.Resources, Integer> getSpecificCosts(Entity entity) {
        if (Families.classed.matches(entity)) {
            CharacterClass characterClass = Mappers.characterClass.get(entity).getCharacterClass();
            int cost = Math.max(0, costPerEpoch * (characterClass.getLevel() / this.levelsPerEpoch));
            this.costs.put(this.resource, cost);
        } else {
            this.costs.put(this.resource, 0);
        }
        return this.costsView;
    }
}
