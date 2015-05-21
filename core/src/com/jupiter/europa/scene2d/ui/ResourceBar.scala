package com.jupiter.europa.scene2d.ui

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Disposable
import com.jupiter.europa.entity.component.ResourceComponent.Resources
import com.jupiter.europa.entity.{Families, Mappers}

/**
 * Created by nathan on 5/20/15.
 */
class ResourceBar(val entity: Entity, val resource: Resources) extends Actor with Disposable {

  // Fields
  private val renderer = new ShapeRenderer();
  private var backTexture: Texture = null;
  private var barTexture: Texture = null;


  // Public Methods
  override def draw(batch: Batch, parentAlpha: Float) {
    if (Families.resourced.matches(this.entity)) {
      val resComp = Mappers.resources.get(entity);
      val curVal = resComp.getCurrent(this.resource);
      val max = resComp.getMax(this.resource);
      val fraction = curVal.toDouble / max.toDouble;

      val split: Float = Math.max(Math.min((fraction * this.getWidth()).round, this.getWidth()), 1);

      val resCol = this.resource.getDrawColor();
      val drawColor = new Color(resCol.r, resCol.g, resCol.b, resCol.a * parentAlpha);
      val backDrawColor = new Color(ResourceBar.backColor.r, ResourceBar.backColor.g, ResourceBar.backColor.b, ResourceBar.backColor.a * parentAlpha);

      //      batch.end();
      //
      //      this.renderer.setProjectionMatrix(batch.getProjectionMatrix());
      //      this.renderer.begin(ShapeRenderer.ShapeType.Filled);
      //
      //      this.renderer.setColor(Color.RED);
      //      this.renderer.rect(this.getX(), this.getY(), split, this.getHeight());
      //
      //      this.renderer.setColor(backDrawColor);
      //      this.renderer.rect(this.getX() + split, this.getY(), this.getWidth() - split, this.getHeight());
      //
      //      this.renderer.end();
      //
      //      batch.begin();

      if (barTexture != null) {
        barTexture.dispose();
      }
      if (backTexture != null) {
        backTexture.dispose();
      }
      barTexture = ResourceBar.getSolidTexture(drawColor);
      backTexture = ResourceBar.getSolidTexture(backDrawColor);

      batch.draw(barTexture, this.getX(), this.getY(), split, this.getHeight());
      batch.draw(backTexture, this.getX() + split, this.getY(), this.getWidth() - split, this.getHeight());
    }
  }

  override def dispose(): Unit = {
    this.renderer.dispose();

    if (barTexture != null) {
      barTexture.dispose();
    }
    if (backTexture != null) {
      backTexture.dispose();
    }
  }

}

object ResourceBar {
  val backColor = new Color(Color.LIGHT_GRAY);

  private def getSolidTexture(color: Color): Texture = {
    val pix = new Pixmap(100, 100, Pixmap.Format.RGB888);
    pix.setColor(color);
    pix.fill();
    return new Texture(pix);
  }
}
