/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.emergence.scene2d.ui;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nathan Templon
 */
public class TabbedPane extends Table {

    // Fields
    private final TextButtonStyle tabStyle;
    private final Table buttonTable;
    private final Map<String, Actor> tabs = new HashMap<>();
    private final Map<String, TextButton> buttons = new HashMap<>();
    private Actor currentTab;


    // Properties
    public Actor getCurrentTab() {
        return this.currentTab;
    }

    public void setCurrentTab(String tabName) {
        if (tabs.containsKey(tabName)) {
            if (this.currentTab != null) {
                Actor oldTab = this.currentTab;
                this.currentTab = this.tabs.get(tabName);
                this.getCell(oldTab).setActor(this.currentTab);
            }
            else {
                this.currentTab = this.tabs.get(tabName);
                this.add(this.currentTab).expand().top();
            }
            
            this.buttons.keySet().stream().forEach((String key) -> {
                TextButton button = this.buttons.get(key);
                button.setChecked(button.getText().toString().equals(tabName));
            });
            
            this.invalidate();
        }
    }


    // Initialization
    public TabbedPane(TextButtonStyle tabStyle) {
        this.tabStyle = tabStyle;
        this.buttonTable = new Table();

        this.add(this.buttonTable).top().left().row();
    }


    // Public Methods
    public void addTab(String header, Actor actor) {
        TextButton tabButton = new TextButton(header, this.tabStyle);
        tabButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!tabButton.isDisabled() && event.getButton() == Buttons.LEFT) {
                    TabbedPane.this.tabButtonClicked(tabButton);
                }
            }
        });

        this.tabs.put(header, actor);
        this.buttons.put(header, tabButton);

        this.buttonTable.add(tabButton).space(20).left();
        this.buttonTable.invalidate();
        
        if (this.currentTab == null) {
            this.setCurrentTab(header);
        }
    }


    // Private Methods
    private void tabButtonClicked(TextButton tabButton) {
        this.setCurrentTab(tabButton.getText().toString());
    }
}
