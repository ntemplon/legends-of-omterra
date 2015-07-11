/*
 * The MIT License
 *
 * Copyright 2015 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
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
 *
 */

package com.jupiter.europa.scene2d.ui

import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import java.util.HashMap

/**

 * @author Nathan Templon
 */
public class TabbedPane(private val tabStyle: TextButtonStyle) : Table() {

    // Properties
    private val buttonTable: Table
    private val tabs = HashMap<String, Actor>()
    private val buttons = HashMap<String, TextButton>()

    public var currentTab: Actor? = null
        private set

    public fun setCurrentTab(tabName: String) {
        if (tabs.containsKey(tabName)) {
            if (this.currentTab != null) {
                val oldTab = this.currentTab
                this.currentTab = this.tabs.get(tabName)
                this.getCell<Actor>(oldTab).setActor<Actor>(this.currentTab)
            } else {
                this.currentTab = this.tabs.get(tabName)
                this.add<Actor>(this.currentTab).expand().fillX().top()
            }

            this.buttons.values().forEach { button ->
                button.setChecked(button.getText().toString().equals(tabName))
            }

            this.invalidate()
        }
    }


    init {
        this.buttonTable = Table()

        this.add(this.buttonTable).top().left().row()
    }


    // Public Methods
    public fun addTab(header: String, actor: Actor) {
        val tabButton = TextButton(header, this.tabStyle)
        tabButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (!tabButton.isDisabled() && event!!.getButton() == Buttons.LEFT) {
                    this@TabbedPane.tabButtonClicked(tabButton)
                }
            }
        })

        this.tabs.put(header, actor)
        this.buttons.put(header, tabButton)

        this.buttonTable.add(tabButton).left()
        this.buttonTable.invalidate()

        if (this.currentTab == null) {
            this.setCurrentTab(header)
        }
    }


    // Private Methods
    private fun tabButtonClicked(tabButton: TextButton) {
        this.setCurrentTab(tabButton.getText().toString())
    }
}
