package com.jupiter.europa.scene2d.ui

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Disposable
import com.jupiter.europa.entity.Families
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.component.ResourceComponent

/**
 * Created by nathan on 6/7/15.
 */
/**
 * Created by nathan on 5/20/15.
 */
class ResourceBar(val entity: Entity, val resource: ResourceComponent.Resources) : Actor(), Disposable {

    // Fields
    private val renderer = ShapeRenderer();
    private var backTexture: Texture? = null;
    private var barTexture: Texture? = null;


    // Public Methods
    override fun draw(batch: Batch, parentAlpha: Float) {
        if (Families.resourced.matches(this.entity)) {
            val resComp = Mappers.resources.get(entity);
            val curVal = resComp.getCurrent(this.resource);
            val max = resComp.getMax(this.resource);
            val fraction: Float = if (max > 0) {
                curVal.toFloat() / max.toFloat();
            } else {
                0f;
            }

            val split: Float = if (curVal > 0) {
                Math.max(Math.round(Math.min((fraction * this.getWidth()), this.getWidth())).toFloat(), 1f);
            } else {
                0f;
            };

            val resCol = this.resource.drawColor;
            val drawColor = Color(resCol.r, resCol.g, resCol.b, resCol.a * parentAlpha);
            val backDrawColor = Color(ResourceBar.backColor.r, ResourceBar.backColor.g, ResourceBar.backColor.b, ResourceBar.backColor.a * parentAlpha);

            barTexture?.dispose();
            backTexture?.dispose();
            barTexture = ResourceBar.getSolidTexture(drawColor);
            backTexture = ResourceBar.getSolidTexture(backDrawColor);

            batch.draw(barTexture, this.getX(), this.getY(), split, this.getHeight());
            batch.draw(backTexture, this.getX() + split, this.getY(), this.getWidth() - split, this.getHeight());
        }
    }

    override fun dispose(): Unit {
        this.renderer.dispose();
        this.barTexture?.dispose();
        this.backTexture?.dispose();
    }

    companion object {
        val backColor = Color(Color.LIGHT_GRAY);

        private fun getSolidTexture(color: Color): Texture {
            val pix = Pixmap(100, 100, Pixmap.Format.RGB888);
            pix.setColor(color);
            pix.fill();
            return Texture(pix);
        }
    }
}

