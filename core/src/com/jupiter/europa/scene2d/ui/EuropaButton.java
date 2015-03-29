/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.scene2d.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.jupiter.ganymede.event.Event;
import com.jupiter.ganymede.event.Listener;

/**
 *
 * @author Nathan Templon
 */
public class EuropaButton extends TextButton {

    // Fields
    private final Event<ClickEvent> clicked = new Event<>();


    // Initialization
    public EuropaButton(String text, TextButtonStyle style) {
        super(text, style);
        this.init();
    }

    public EuropaButton(String text, Skin skin) {
        super(text, skin);
        this.init();
    }


    // Public Methods
    public boolean addClickListener(Listener<ClickEvent> listener) {
        return this.clicked.addListener(listener);
    }

    public boolean removeClickListener(Listener<ClickEvent> listener) {
        return this.clicked.removeListener(listener);
    }


    // Private Methods
    private void init() {
        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getButton() == Input.Buttons.LEFT && !EuropaButton.this.isDisabled()) {
                    EuropaButton.this.clicked.dispatch(new ClickEvent(event, x, y));
                    EuropaButton.this.setChecked(false);
                }
            }
        });
    }


    // Nested Classes
    public class ClickEvent {

        // Fields
        public final InputEvent event;
        public final float x;
        public final float y;


        // Initialization
        public ClickEvent(InputEvent event, float x, float y) {
            this.event = event;
            this.x = x;
            this.y = y;
        }

    }

}
