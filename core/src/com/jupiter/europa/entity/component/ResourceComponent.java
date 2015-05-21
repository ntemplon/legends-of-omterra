package com.jupiter.europa.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by nathan on 5/19/15.
 */
public class ResourceComponent extends Component implements Json.Serializable, OwnedComponent {

    // Constants
    public static final String MAX_KEY = "max";
    public static final String CURRENT_KEY = "current";


    // Enumerations
    public enum Resources {
        HEALTH(new Color(Color.RED)),
        MANA(new Color(Color.BLUE)),
        STAMINA(new Color(Color.GREEN)),
        AETHER(new Color(0.5f, 0.5f, 0.5f, 0.0f));

        public static Resources fromDisplayName(String displayName) {
            return Resources.valueOf(displayName.toUpperCase());
        }

        private final String displayName;
        private final Color drawColor;

        public final String getDisplayName() {
            return this.displayName;
        }

        public final Color getDrawColor() {
            return this.drawColor;
        }

        Resources(Color color) {
            this.displayName = this.toString().substring(0, 1) + this.toString().substring(1).toLowerCase();
            this.drawColor = color;
        }
    }


    // Fields
    private final Map<Resources, Integer> maxAmounts = new EnumMap<>(Resources.class);
    private final Map<Resources, Integer> currentAmounts = new EnumMap<>(Resources.class);
    private Entity owner;


    // Properties
    @Override
    public Entity getOwner() {
        return this.owner;
    }

    @Override
    public void setOwner(Entity entity) {
        this.owner = entity;
        this.updateMaxResources();
    }

    public final void setCurrent(Resources res, Integer amount) {
        if (res != null) {
            this.currentAmounts.put(res, amount);
        }
    }

    public final Integer getCurrent(Resources res) {
        if (res != null) {
            return this.currentAmounts.get(res);
        }
        return 0;
    }

    public final Integer getMax(Resources res) {
        if (res != null) {
            return this.maxAmounts.get(res);
        }
        return 0;
    }


    // Initialization
    public ResourceComponent() {
        for (Resources res : Resources.values()) {
            this.maxAmounts.put(res, 0);
            this.currentAmounts.put(res, 0);
        }
    }


    // Private Methods
    private void updateMaxResources() {
        // TODO: Resource computing code
    }


    // Serializable Implementation
    @Override
    public void read(Json json, JsonValue jsonValue) {
        if (jsonValue.hasChild(MAX_KEY)) {
            for (JsonValue child : jsonValue.get(MAX_KEY).iterator()) {
                try {
                    String name = child.name();
                    Resources res = Resources.valueOf(name);
                    this.maxAmounts.put(res, child.asInt());
                } catch (Exception ex) {

                }
            }
        }

        if (jsonValue.hasChild(CURRENT_KEY)) {
            for (JsonValue child : jsonValue.get(CURRENT_KEY).iterator()) {
                try {
                    String name = child.name();
                    Resources res = Resources.valueOf(name);
                    this.currentAmounts.put(res, child.asInt());
                } catch (Exception ex) {

                }
            }
        }
    }

    @Override
    public void write(Json json) {
        json.writeObjectStart(MAX_KEY);
        Arrays.asList(Resources.values()).forEach(res -> json.writeValue(res.toString(), this.maxAmounts.get(res)));
        json.writeObjectEnd();

        json.writeObjectStart(CURRENT_KEY);
        Arrays.asList(Resources.values()).forEach(res -> json.writeValue(res.toString(), this.currentAmounts.get(res)));
        json.writeObjectEnd();
    }
}
