package com.jupiter.europa.entity.ability;

import com.badlogic.ashley.core.Entity;

import java.awt.*;

/**
 * Created by nathan on 5/21/15.
 */
public abstract class Target {

    public static TileTarget tile(int x, int y) {
        return tile(new Point(x, y));
    }

    public static TileTarget tile(Point point) {
        return new TileTarget(point);
    }

    public static EntityTarget entity(Entity entity) {
        return new EntityTarget(entity);
    }


    public static class TileTarget extends Target {

        // Fields
        public final Point tile;


        // Initialization
        public TileTarget(Point point) {
            this.tile = point;
        }
    }

    public static class EntityTarget extends Target {

        // Fields
        public final Entity entity;


        // Initialization
        private EntityTarget(Entity entity) {
            this.entity = entity;
        }
    }
}
