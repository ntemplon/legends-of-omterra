/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.entity.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.Json.Serializable
import com.badlogic.gdx.utils.JsonValue
import com.jupiter.europa.entity.stats.SkillSet
import com.jupiter.europa.entity.stats.SkillSet.Skills
import java.util.ArrayList

/**

 * @author Nathan Templon
 */
public class SkillsComponent @jvmOverloads constructor(skills: SkillSet = SkillSet(), classSkills: List<Skills> = Skills.values().toList()) : Component(), Serializable {

    // Properties
    public var skills: SkillSet = skills
        private set
    public val classSkills: List<Skills>
        get() {
            return this.internalClassSkills.toList()
        }

    private val internalClassSkills: MutableList<Skills> = ArrayList(classSkills)


    // Public Methods
    public fun getSkillPointsSpent(): Int {
        return this.classSkills.map { skill -> this.skills.getSkill(skill) }.sum()
    }

    public constructor(classSkills: List<Skills>) : this(SkillSet(), classSkills) {
    }

    init {
        this.skills = skills
    }


    // Serializable (Json) Implementation
    override fun write(json: Json) {
        json.writeValue(SKILL_SET_KEY, this.skills, javaClass<SkillSet>())
        json.writeValue(CLASS_SKILLS_KEY, this.internalClassSkills.toTypedArray())
    }

    override fun read(json: Json, jsonData: JsonValue) {
        if (jsonData.has(SKILL_SET_KEY)) {
            try {
                this.skills = json.readValue(javaClass<SkillSet>(), jsonData.get(SKILL_SET_KEY))
            } catch (ex: Exception) {
                this.skills = SkillSet()
            }

        } else {
            this.skills = SkillSet()
        }

        if (jsonData.has(CLASS_SKILLS_KEY)) {
            this.internalClassSkills.clear()
            jsonData.get(CLASS_SKILLS_KEY).forEach { value ->
                try {
                    this.internalClassSkills.add(json.readValue(javaClass<Skills>(), value))
                } catch (ex: Exception) {

                }
            }
        }
    }

    companion object {

        // Constants
        private val SKILL_SET_KEY = "skill-set"
        private val CLASS_SKILLS_KEY = "class-skills"
    }

}
