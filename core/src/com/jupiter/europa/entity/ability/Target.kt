package com.jupiter.europa.entity.ability

import com.badlogic.ashley.core.Entity
import java.awt.Point

/**
 * Created by nathan on 5/21/15.
 */
public abstract class Target {


    public class TileTarget(public val tile: Point) : Target()

    public class EntityTarget internal constructor(public val entity: Entity) : Target()

    companion object {
        public fun tile(x: Int, y: Int): TileTarget {
            return tile(Point(x, y))
        }

        public fun tile(point: Point): TileTarget {
            return TileTarget(point)
        }

        public fun entity(entity: Entity): EntityTarget {
            return EntityTarget(entity)
        }
    }
}
