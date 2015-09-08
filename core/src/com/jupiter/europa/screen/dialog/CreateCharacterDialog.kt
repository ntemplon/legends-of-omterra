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

package com.jupiter.europa.screen.dialog

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.entity.EuropaEntity
import com.jupiter.europa.entity.Mappers
import com.jupiter.europa.entity.Party
import com.jupiter.europa.entity.component.MovementResourceComponent
import com.jupiter.europa.entity.effects.Effect
import com.jupiter.europa.entity.stats.AttributeSet
import com.jupiter.europa.entity.stats.SkillSet.Skills
import com.jupiter.europa.entity.stats.characterclass.CharacterClass
import com.jupiter.europa.entity.stats.race.PlayerRaces
import com.jupiter.europa.entity.stats.race.Race
import com.jupiter.europa.entity.traits.EffectPool
import com.jupiter.europa.entity.traits.feat.Feat
import com.jupiter.europa.io.FileLocations
import com.jupiter.europa.scene2d.ui.AttributeSelector
import com.jupiter.europa.scene2d.ui.EuropaButton
import com.jupiter.europa.scene2d.ui.EuropaButton.ClickEvent
import com.jupiter.europa.scene2d.ui.MultipleNumberSelector
import com.jupiter.europa.scene2d.ui.MultipleNumberSelector.MultipleNumberSelectorStyle
import com.jupiter.europa.scene2d.ui.ObservableDialog
import com.jupiter.europa.screen.MainMenuScreen
import com.jupiter.europa.screen.MainMenuScreen.DialogExitStates
import com.jupiter.ganymede.event.Listener
import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.HashSet

/**

 * @author Nathan Templon
 */
public class CreateCharacterDialog : ObservableDialog(CreateCharacterDialog.DIALOG_NAME, CreateCharacterDialog.getDefaultSkin().get(javaClass<Window.WindowStyle>())) {

    // Enumerations
    public enum class CreateCharacterExitStates {

        OK,
        CANCELED
    }


    // Properties
    private val selectRaceClass: SelectRaceClassAttributesDialog
    private var selectSkills: SelectSkillsDialog? = null
    private var selectFeats: SelectTraitDialog<Feat>? = null
    private var manager: DialogManager? = null
    private val otherPools = ArrayList<EffectPool<out Effect>>()
    private val skinInternal: Skin = getDefaultSkin()

    public var exitState: CreateCharacterExitStates = CreateCharacterExitStates.CANCELED
        private set
    public var createdEntity: EuropaEntity? = null
        private set

    private var lastDialog: Dialog? = null
    private var widthInternal: Float = 0.toFloat()
    private var heightInternal: Float = 0.toFloat()

    public fun getDialogs(): Collection<ObservableDialog> {
        val dialogs = HashSet<ObservableDialog>(3)
        dialogs.add(this.selectRaceClass)
        dialogs.add(this.selectSkills)
        dialogs.add(this.selectFeats)
        return dialogs.filterNotNull().toSet()
    }


    init {
        this.selectRaceClass = SelectRaceClassAttributesDialog(skinInternal)
        this.selectRaceClass.addDialogListener({ args -> this.onSelectRaceClassHide(args) }, ObservableDialog.DialogEvents.HIDDEN)
        this.selectSkills = SelectSkillsDialog(skinInternal, 8, 4, Arrays.asList(*Skills.values()))
        this.selectSkills?.addDialogListener({ args -> this.onSelectSkillsHide(args) }, ObservableDialog.DialogEvents.HIDDEN)

        this.lastDialog = this.selectRaceClass

        this.addDialogListener({ args -> this.onShown(args) }, ObservableDialog.DialogEvents.SHOWN)
    }


    // Public Methods
    override fun setSize(width: Float, height: Float) {
        super.setSize(width, height)
        this.getDialogs().forEach { it.setSize(width, height) }
        this.manager?.dialogs?.forEach { it.setSize(width, height) }

        this.widthInternal = width
        this.heightInternal = height
    }


    // Private Methods
    private fun onShown(args: ObservableDialog.DialogEventArgs) {
        val last = this.lastDialog
        if (last != null) {
            if (last is SelectTraitDialog<*>) {
                val manager = this.manager
                if (manager != null && manager.dialogs.contains(last)) {
                    manager.index = manager.dialogs.indexOf(last)
                    manager.start()
                } else {
                    this.showDialog(last)
                }
            } else {
                this.showDialog(last)
            }
        }
    }

    private fun onSelectRaceClassHide(args: ObservableDialog.DialogEventArgs) {
        if (this.selectRaceClass.exitState === DialogExitStates.NEXT) {
            this.createEntity()

            val skills = Mappers.skills.get(this.createdEntity)
            val skillList = skills.classSkills
            val classComp = Mappers.characterClass.get(this.createdEntity)

            this.selectSkills = SelectSkillsDialog(this.skinInternal, classComp.characterClass.availableSkillPoints - skills.getSkillPointsSpent(), classComp.characterClass.maxPointsPerSkill, skillList)
            this.selectSkills!!.addDialogListener({ args -> this.onSelectSkillsHide(args) }, ObservableDialog.DialogEvents.HIDDEN)
            this.showDialog(this.selectSkills)
        } else {
            this.exitState = CreateCharacterExitStates.CANCELED
            this.concludeDialog()
        }
    }

    private fun onSelectSkillsHide(args: ObservableDialog.DialogEventArgs) {
        if (this.selectSkills!!.exitState === DialogExitStates.NEXT) {
            // Debug Code
            this.selectFeats = SelectTraitDialog("Select Feats", this.skinInternal, Mappers.characterClass.get(this.createdEntity).characterClass.featPool)
            selectFeats!!.setDialogBackground(this.skinInternal.get(MainMenuScreen.DIALOG_BACKGROUND_KEY, javaClass<SpriteDrawable>()))
            this.selectFeats!!.addDialogListener({ args -> this.onSelectFeatsHide(args) }, ObservableDialog.DialogEvents.HIDDEN)
            this.showDialog(selectFeats)
        } else {
            this.showDialog(this.selectRaceClass)
        }
    }

    private fun onSelectFeatsHide(args: ObservableDialog.DialogEventArgs) {
        if (this.selectFeats!!.exitState === DialogExitStates.NEXT) {
            this.selectFeats!!.applyChanges()
            val charClass = Mappers.characterClass.get(this.createdEntity).characterClass

            this.otherPools.clear()
            this.otherPools.addAll(charClass.abilityPools
                    .filter { it != charClass.featPool }
                    .toList())

            val dialogs = this.otherPools.map { pool ->
                SelectTraitDialog("Select " + pool.name, this.skinInternal, pool) as SelectTraitDialog<*> // Unnecessary cast due to java-kotlin interop quirk
            }.toList()

            // Switch to a DialogManager that can handle the "back" button
            if (dialogs.size() > 0) {
                for (dialog in dialogs) {
                    dialog.setDialogBackground(this.skinInternal.get(MainMenuScreen.DIALOG_BACKGROUND_KEY, javaClass<SpriteDrawable>()))
                }

                val manager = DialogManager(dialogs, { dialog -> this.showDialog(dialog) })
                manager.onForward = {
                    this.exitState = CreateCharacterExitStates.OK
                    this.concludeDialog()
                }
                manager.onBack = {
                    // Going back one dialog
                    this.showDialog(this.selectFeats)
                }
                manager.start()
                this.manager = manager
            } else {
                this.exitState = CreateCharacterExitStates.OK
                this.concludeDialog()
            }
        } else {
            // Going back one dialog
            this.showDialog(this.selectSkills)
        }
    }

    private fun showDialog(dialog: Dialog?) {
        if (dialog != null) {
            dialog.show(this.getStage())
            dialog.setSize(this.widthInternal, this.heightInternal)
            this.lastDialog = dialog
        }
    }

    private fun concludeDialog() {
        if (this.createdEntity != null) {
            val comp = Mappers.skills.get(this.createdEntity)

            if (comp != null) {
                val skills = comp.skills

                val skillLevels = this.selectSkills!!.getSelectedSkills()
                for ((skill, value) in skillLevels) {
                    skills.setSkill(skill, value)
                }
            }

            for (dialog in this.manager?.dialogs ?: listOf()) {
                dialog.applyChanges()
            }
        }

        this.hide()
    }

    private fun createEntity() {
        this.createdEntity = Party.createPlayer(this.selectRaceClass.getCharacterName(), CharacterClass.CLASS_LOOKUP.get(this.selectRaceClass.getSelectedClass())!!, this.selectRaceClass.getSelectedRace(), this.selectRaceClass.getAttributes())
    }


    // Nested Classes
    private class SelectRaceClassAttributesDialog(private val skinInternal: Skin) : ObservableDialog(CreateCharacterDialog.SelectRaceClassAttributesDialog.DIALOG_NAME, skinInternal.get(javaClass<Window.WindowStyle>())) {

        private var mainTable: Table? = null
        private var selectBoxTable: Table? = null
        private var nameTable: Table? = null
        private var nameLabel: Label? = null
        private var nameField: TextField? = null
        private var raceSelectBox: SelectBox<Race>? = null
        private var classSelectBox: SelectBox<String>? = null
        private var titleLabelInternal: Label? = null
        private var raceLabel: Label? = null
        private var classLabel: Label? = null
        private var raceClassPreview: Image? = null
        private var backButton: EuropaButton? = null
        private var nextButton: EuropaButton? = null
        private var attributeSelector: AttributeSelector? = null

        public var exitState: DialogExitStates = DialogExitStates.BACK
            private set

        public fun getSelectedClass(): String {
            return this.classSelectBox!!.getSelected()
        }

        public fun getSelectedRace(): Race {
            return this.raceSelectBox!!.getSelected()
        }

        public fun getCharacterPortrait(): Drawable {
            return this.raceClassPreview!!.getDrawable()
        }

        public fun getCharacterName(): String {
            return this.nameField!!.getText()
        }

        public fun getAttributes(): AttributeSet {
            return this.attributeSelector!!.getAttributes()
        }


        init {
            this.initComponents()

            this.addDialogListener({ args ->
                this.nextButton?.setChecked(false)
                this.backButton?.setChecked(false)
            }, ObservableDialog.DialogEvents.SHOWN)
        }


        // Private Methods
        private fun initComponents() {
            this.titleLabelInternal = Label(TITLE, skinInternal.get(javaClass<LabelStyle>()))
            this.raceLabel = Label("Race: ", skinInternal.get(javaClass<LabelStyle>()))
            this.classLabel = Label("Class: ", skinInternal.get(javaClass<LabelStyle>()))
            this.nameLabel = Label("Name: ", skinInternal.get(javaClass<LabelStyle>()))

            this.nameField = TextField("default", skinInternal.get(javaClass<TextFieldStyle>()))

            this.raceClassPreview = Image(EuropaGame.game.assetManager!!.get(FileLocations.SPRITES_DIRECTORY.resolve("CharacterSprites.atlas").toString(), javaClass<TextureAtlas>()).findRegion(PlayerRaces.Human.textureString + "-champion-" + MovementResourceComponent.FRONT_STAND_TEXTURE_NAME))
            this.raceClassPreview?.setScale(IMAGE_SCALE.toFloat())

            this.raceSelectBox = SelectBox<Race>(skinInternal.get(javaClass<SelectBox.SelectBoxStyle>()))
            this.raceSelectBox?.setItems(*PlayerRaces.values())
            this.raceSelectBox?.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    this@SelectRaceClassAttributesDialog.updateNewCharacterPreview()
                }
            })

            this.classSelectBox = SelectBox<String>(skinInternal.get(javaClass<SelectBox.SelectBoxStyle>()))
            this.classSelectBox?.setItems(*CharacterClass.AVAILABLE_CLASSES.toTypedArray())
            this.classSelectBox?.addListener(object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    this@SelectRaceClassAttributesDialog.updateNewCharacterPreview()
                }
            })

            this.backButton = EuropaButton("Back", skinInternal.get(javaClass<TextButton.TextButtonStyle>()))
            this.backButton?.addClickListener({ args -> this.onRaceClassBackButton(args) })

            this.nextButton = EuropaButton("Next", skinInternal.get(javaClass<TextButton.TextButtonStyle>()))
            this.nextButton!!.addClickListener({ args -> this.onRaceClassNextButton(args) })

            this.attributeSelector = AttributeSelector(50, skinInternal.get(javaClass<MultipleNumberSelectorStyle>()), 2)

            this.nameTable = Table()
            this.nameTable!!.add<Label>(this.nameLabel).left().space(MainMenuScreen.COMPONENT_SPACING.toFloat())
            this.nameTable!!.add<TextField>(this.nameField).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).padTop(10f).expandX().fillX()

            this.mainTable = Table()

            this.selectBoxTable = Table()
            this.selectBoxTable!!.add<Label>(this.raceLabel).fillX().space(MainMenuScreen.COMPONENT_SPACING.toFloat())
            this.selectBoxTable!!.add<Label>(this.classLabel).fillX().space(MainMenuScreen.COMPONENT_SPACING.toFloat()).spaceLeft((5 * MainMenuScreen.COMPONENT_SPACING).toFloat())
            this.selectBoxTable!!.row()
            this.selectBoxTable!!.add<SelectBox<Race>>(this.raceSelectBox).minWidth(MainMenuScreen.TITLE_BUTTON_WIDTH.toFloat()).fillX()
            this.selectBoxTable!!.add<SelectBox<String>>(this.classSelectBox).minWidth(MainMenuScreen.TITLE_BUTTON_WIDTH.toFloat()).fillX().spaceLeft((5 * MainMenuScreen.COMPONENT_SPACING).toFloat())
            this.selectBoxTable!!.row()

            this.mainTable!!.add<Label>(this.titleLabelInternal).center().colspan(2)
            this.mainTable!!.row()
            this.mainTable!!.add(Image()).expandY().fillY()
            this.mainTable!!.row()
            this.mainTable!!.add<Table>(this.nameTable).colspan(2).expandX().fillX().bottom()
            this.mainTable!!.row()
            this.mainTable!!.add<Table>(this.selectBoxTable).left()
            this.mainTable!!.add<Image>(this.raceClassPreview).center()
            this.mainTable!!.row()
            this.mainTable!!.add<AttributeSelector>(this.attributeSelector).colspan(2).center().top().padTop(40f)
            this.mainTable!!.row()
            this.mainTable!!.add(Image()).expandY().fillY()
            this.mainTable!!.row()

            val buttonTable = Table()
            buttonTable.add<EuropaButton>(this.backButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right().expandX()
            buttonTable.add<EuropaButton>(this.nextButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right()

            this.mainTable!!.add(buttonTable).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).right().colspan(2).expandX().fillX()
            this.mainTable!!.row()

            this.mainTable!!.pad(MainMenuScreen.TABLE_PADDING.toFloat())
            this.mainTable!!.background(skinInternal.get(MainMenuScreen.DIALOG_BACKGROUND_KEY, javaClass<SpriteDrawable>()))

            this.getContentTable().add<Table>(this.mainTable).expand().fillY().width(MainMenuScreen.DIALOG_WIDTH.toFloat())
        }

        private fun updateNewCharacterPreview() {
            val race = this.raceSelectBox?.getSelected()
            val charClass = CharacterClass.CLASS_LOOKUP.get(this.classSelectBox?.getSelected())
            try {
                val classInstance = charClass?.newInstance()
                val textureClassString = classInstance?.textureSetName
                val texture = race?.textureString + "-" + textureClassString + "-" + MovementResourceComponent.FRONT_STAND_TEXTURE_NAME
                val drawable = TextureRegionDrawable(EuropaGame.game.assetManager!!.get(FileLocations.SPRITES_DIRECTORY.resolve("CharacterSprites.atlas").toString(), javaClass<TextureAtlas>()).findRegion(texture))
                this.raceClassPreview!!.setDrawable(drawable)
            } catch (ex: IllegalAccessException) {
            } catch (ex: InstantiationException) {
            }

        }

        private fun onRaceClassBackButton(event: ClickEvent) {
            this.exitState = DialogExitStates.BACK
            this.hide()
        }

        private fun onRaceClassNextButton(event: ClickEvent) {
            this.exitState = DialogExitStates.NEXT
            this.hide()
        }

        companion object {

            // Constants
            private val DIALOG_NAME = ""
            private val TITLE = "Select a Race and Class"

            private val IMAGE_SCALE = 4
        }

    }


    private class SelectSkillsDialog(private val skinInternal: Skin, private val skillPointsAvailable: Int, public val maxPointsPerSkill: Int, private val selectableSkills: List<Skills>) : ObservableDialog(CreateCharacterDialog.SelectSkillsDialog.DIALOG_TITLE, skinInternal.get(javaClass<Window.WindowStyle>())) {

        private var mainTable: Table? = null
        private var titleLabelInternal: Label? = null
        private var skillSelector: MultipleNumberSelector? = null
        private var buttonTableInternal: Table? = null
        private var nextButton: EuropaButton? = null
        private var backButton: EuropaButton? = null

        public var exitState: DialogExitStates? = null
            private set

        public fun getSelectedSkills(): Map<Skills, Int> = this.skillSelector?.getValues()?.map { entry ->
            val skill = Skills.getByDisplayName(entry.getKey())
            if (skill != null) {
                Pair(skill, entry.getValue())
            } else {
                null
            }
        }?.filterNotNull()?.toMap() ?: mapOf()


        init {

            this.initComponent()
        }


        // Private Methods
        private fun initComponent() {
            this.titleLabelInternal = Label(TITLE, skinInternal.get(javaClass<LabelStyle>()))

            this.mainTable = Table()

            // Create Attribute Selector
            val skillNames = this.selectableSkills.map { skill -> skill.displayName }.toList()
            this.skillSelector = MultipleNumberSelector(this.skillPointsAvailable, this.skinInternal.get(javaClass<MultipleNumberSelectorStyle>()), Collections.unmodifiableList<String>(skillNames), 2)
            this.skillSelector?.setUseMaximumNumber(true)
            this.skillSelector?.maximumNumber = this.maxPointsPerSkill
            this.skillSelector?.setIncrement(5)

            this.nextButton = EuropaButton("Next", skinInternal.get(javaClass<TextButton.TextButtonStyle>()))
            this.nextButton?.addClickListener { args -> this.onNextButton(args) }

            this.backButton = EuropaButton("Back", skinInternal.get(javaClass<TextButton.TextButtonStyle>()))
            this.backButton!!.addClickListener { args -> this.onBackButton(args) }

            this.buttonTableInternal = Table()
            this.buttonTableInternal!!.add<EuropaButton>(this.backButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right().expandX()
            this.buttonTableInternal!!.add<EuropaButton>(this.nextButton).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).width(MainMenuScreen.DIALOG_BUTTON_WIDTH.toFloat()).right()

            this.mainTable!!.add<Label>(this.titleLabelInternal).center().top()
            this.mainTable!!.row()
            this.mainTable!!.add<MultipleNumberSelector>(this.skillSelector).expandY().center().top()
            this.mainTable!!.row()
            this.mainTable!!.add<Table>(buttonTableInternal).space(MainMenuScreen.COMPONENT_SPACING.toFloat()).bottom().right().expandX().fillX()
            this.mainTable!!.row()

            this.mainTable!!.pad(MainMenuScreen.TABLE_PADDING.toFloat())
            this.mainTable!!.background(skinInternal.get(MainMenuScreen.DIALOG_BACKGROUND_KEY, javaClass<SpriteDrawable>()))

            this.getContentTable().add<Table>(this.mainTable).expand().fillY().width(MainMenuScreen.DIALOG_WIDTH.toFloat())
        }

        private fun onNextButton(event: ClickEvent) {
            this.exitState = DialogExitStates.NEXT
            this.hide()
        }

        private fun onBackButton(event: ClickEvent) {
            this.exitState = DialogExitStates.BACK
            this.hide()
        }

        companion object {

            // Constants
            private val DIALOG_TITLE = ""
            private val TITLE = "Select Skills"
        }

    }// Properties


    private class DialogManager(internal val dialogs: List<SelectTraitDialog<*>>, private val show: (ObservableDialog) -> Unit) : Listener<ObservableDialog.DialogEventArgs> {

        internal var index: Int = 0

        public var onForward: () -> Unit = {}
        public var onBack: () -> Unit = {}

        public fun start() {
            if (dialogs.size() > 0) {
                this.show(this.index)
            } else {
                this.onForward()
            }
        }

        private fun show(index: Int) {
            val dialog = this.dialogs[index]
            dialog.addDialogListener(this, ObservableDialog.DialogEvents.HIDDEN)
            this.show(dialog)
        }

        override fun handle(args: ObservableDialog.DialogEventArgs) {
            this.dialogs[this.index].removeDialogListener(this)

            when (this.dialogs[this.index].exitState) {
                DialogExitStates.BACK -> this.index--
                DialogExitStates.NEXT -> this.index++
            }

            if (this.index <= 0) {
                this.onBack()
            } else if (this.index >= this.dialogs.size()) {
                this.onForward()
            } else {
                this.show(this.index)
            }
        }
    }


    companion object {


        // Constants
        public val DIALOG_NAME: String = "Create a Character"


        // Static Methods
        private fun getDefaultSkin(): Skin {
            return MainMenuScreen.createMainMenuSkin()
        }
    }

}
