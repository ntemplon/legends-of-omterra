package com.jupiter.europa.screen.overlay;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.EntityEventArgs;
import com.jupiter.europa.entity.Families;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.Party;
import com.jupiter.europa.entity.component.MovementResourceComponent;
import com.jupiter.europa.entity.component.ResourceComponent;
import com.jupiter.europa.entity.stats.characterclass.CharacterClass;
import com.jupiter.europa.entity.stats.race.Race;
import com.jupiter.europa.io.FileLocations;
import com.jupiter.europa.scene2d.ui.ResourceBar;
import com.jupiter.europa.screen.OverlayableScreen;
import com.jupiter.ganymede.event.Event;
import com.jupiter.ganymede.event.Listener;

import java.util.Arrays;

/**
 * Created by nathan on 5/18/15.
 */
public class PartyOverlay extends Scene2DOverlay implements Disposable {

    // Constants
    public static final float PARTY_MEMBER_SCALE = 3.0f;


    // Fields
    private final Event<EntityEventArgs> entityClicked = new Event<>();
    private final Table table = new Table();
    private final Table anchor = new Table();
    private final Party party;


    // Initialization
    public PartyOverlay(Party party) {
        super(false);
        this.party = party;
        this.initComponents();
    }


    // Public Methods
    public boolean addEntityClickListener(Listener<EntityEventArgs> listener) {
        return this.entityClicked.addListener(listener);
    }

    public boolean removeEntityClickListener(Listener<EntityEventArgs> listener) {
        return this.entityClicked.removeListener(listener);
    }

    @Override
    public void added(OverlayableScreen screen) {
        super.added(screen);
        this.anchor.setFillParent(true);
        this.anchor.add(this.table).expand().top().right().pad(10);
        this.getStage().addActor(anchor);

        screen.addTintChangedListener(args -> this.setTint(args.newValue));
    }

    @Override
    public void dispose() {
        for (Actor act : this.table.getChildren()) {
            if (act instanceof Disposable) {
                ((Disposable) act).dispose();
            }
        }
    }


    // Private Methods
    private void initComponents() {
        Arrays.asList(this.party.getActivePartyMembers()).stream()
                .forEach(entity -> {
                    EntityActor act = new EntityActor(entity);
                    act.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent e, float x, float y) {
                            PartyOverlay.this.onEntityClick(entity);
                        }
                    });
                    this.table.add(act).center();
                });
    }

    private void onEntityClick(Entity entity) {
        this.entityClicked.dispatch(new EntityEventArgs(entity));
    }


    // Inner Classes
    private static class EntityActor extends Table implements Disposable {

        // Constants
        private static final int BAR_HEIGHT = 3;
        private static final int BAR_PADDING = 1;


        // Static Methods
        private static Sprite getSprite(Entity entity) {
            if (Families.classed.matches(entity) && Families.raced.matches(entity)) {
                CharacterClass characterClass = Mappers.characterClass.get(entity).getCharacterClass();
                Race race = Mappers.race.get(entity).getRace();
                String textureClassString = characterClass.getTextureSetName();
                String texture = race.getTextureString() + "-" + textureClassString + "-" + MovementResourceComponent.FRONT_STAND_TEXTURE_NAME;
                return new Sprite(EuropaGame.game.getAssetManager().get(FileLocations.SPRITES_DIRECTORY.resolve(
                        "CharacterSprites.atlas").toString(), TextureAtlas.class).findRegion(texture));
            }
            return new Sprite();
        }


        // Fields
        private final Image entityPreview = new Image();
        private final Entity entity;


        // Initialization
        private EntityActor(Entity entity) {
            this.entity = entity;
            this.initComponents();
        }


        // Public Methods
        @Override
        public void dispose() {
            for (Actor act : this.getChildren()) {
                if (act instanceof Disposable) {
                    ((Disposable) act).dispose();
                }
            }
        }


        // Private Methods
        private void initComponents() {
            Sprite sprite = getSprite(this.entity);
            this.entityPreview.setDrawable(new SpriteDrawable(sprite));

            final float width = sprite.getWidth() * PARTY_MEMBER_SCALE;

            this.add(this.entityPreview).center().width(width).height(sprite.getHeight() * PARTY_MEMBER_SCALE);
            this.row();

            for (ResourceComponent.Resources resource : ResourceComponent.Resources.values()) {
                this.add(new ResourceBar(this.entity, resource)).center().expandX().fillX().height(BAR_HEIGHT).padTop(BAR_PADDING);
                this.row();
            }
        }
    }
}
