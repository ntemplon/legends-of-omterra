package com.jupiter.europa.screen.overlay;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.io.FileLocations;
import com.jupiter.europa.screen.OverlayableScreen;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nathan on 5/23/15.
 */
public class CircleMenu extends Scene2DOverlay {

    // Constants
    public static final String BACK_TEXTURE_NAME = "popup-item";
    public static final String HOVERED_TEXTURE_NAME = "popup-item-hover";

    private static final float PIF = (float) Math.PI;
    private static final float MAX_ANGULAR_SPACING = (float) Math.toRadians(30.0f); // Radians


    // Static Variables
    private static TextureRegion back;
    private static TextureRegion backHovered;

    static {
        back = EuropaGame.game.getAssetManager().get(FileLocations.HUD_ATLAS, TextureAtlas.class).findRegion(BACK_TEXTURE_NAME);
        backHovered = EuropaGame.game.getAssetManager().get(FileLocations.HUD_ATLAS, TextureAtlas.class).findRegion(HOVERED_TEXTURE_NAME);
    }


    // Fields
    private final Point center;
    private final List<CircleMenuEntry> entries;

    private boolean firstUp = true;


    // Initialization
    public CircleMenu(Point center, List<CircleMenuItem> items) {
        super(true);
        this.center = center;
        this.entries = items.stream()
                .map(item -> new CircleMenuEntry(item))
                .collect(Collectors.toList());
    }


    // Public Methods
    @Override
    public void added(OverlayableScreen screen) {
        super.added(screen);

        Camera cam = this.getStage().getCamera();
        cam.viewportWidth = cam.viewportWidth / EuropaGame.SCALE;
        cam.viewportHeight = cam.viewportHeight / EuropaGame.SCALE;
        cam.update();

        this.entries.stream().forEach(entry -> this.getStage().addActor(entry));

        final float spacing = PIF / (entries.size() - 1);
        final float radius = back.getRegionWidth() * 1f * EuropaGame.SCALE;

        int i = 0;
        for (CircleMenuEntry entry : entries) {
            float angle = i * spacing;

            float x = center.x * EuropaGame.SCALE - radius * (float) Math.cos(angle) - back.getRegionWidth() / 2.0f;
            float y = center.y * EuropaGame.SCALE + radius * (float) Math.sin(angle) - back.getRegionHeight() / 2.0f;
            entry.setPosition(x, y);
            entry.setScale(EuropaGame.SCALE);

            i++;
        }
    }

    @Override
    public void resize(int width, int height) {
        Camera cam = this.getStage().getCamera();
        cam.viewportWidth = cam.viewportWidth / EuropaGame.SCALE;
        cam.viewportHeight = cam.viewportHeight / EuropaGame.SCALE;
        cam.update();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!this.firstUp) {
            this.getScreen().removeOverlay(this);
        }
        this.firstUp = false;
        return true;
    }


    // Inner Classes
    private class CircleMenuEntry extends Actor {

        // Fields
        private final CircleMenuItem item;


        // Initialization
        private CircleMenuEntry(CircleMenuItem item) {
            this.item = item;
            this.setBounds(0, 0, back.getRegionWidth(), back.getRegionHeight());
            this.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (button == Input.Buttons.LEFT) {
                        CircleMenuEntry.this.item.getAction().run();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }


        // Public Methods
        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(back, this.getX(), this.getY());
            batch.draw(this.item.getIcon(), this.getX(), this.getY());
        }
    }
}
