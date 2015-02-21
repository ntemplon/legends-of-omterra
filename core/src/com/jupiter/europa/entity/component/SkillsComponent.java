/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jupiter.europa.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.entity.stats.SkillSet;
import com.jupiter.europa.entity.stats.SkillSet.Skills;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Nathan Templon
 */
public class SkillsComponent extends Component implements Serializable {

    // Constants
    private static final String SKILL_SET_KEY = "skill-set";
    private static final String CLASS_SKILLS_KEY = "class-skills";


    // Fields
    private SkillSet skills;
    private final List<Skills> classSkills = new ArrayList<>();
    private final List<Skills> classSkillsAccess = Collections.unmodifiableList(this.classSkills);


    // Public Methods
    public final SkillSet getSkills() {
        return this.skills;
    }

    public final List<Skills> getClassSkills() {
        return this.classSkillsAccess;
    }


    // Initialization
    public SkillsComponent() {
        this(new SkillSet(), Arrays.asList(Skills.values()));
    }

    public SkillsComponent(SkillSet skills) {
        this(skills, Arrays.asList(Skills.values()));
    }

    public SkillsComponent(List<Skills> classSkills) {
        this(new SkillSet(), classSkills);
    }

    public SkillsComponent(SkillSet skills, List<Skills> classSkills) {
        this.skills = skills;
        this.classSkills.addAll(classSkills);
    }


    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        json.writeValue(SKILL_SET_KEY, this.skills, SkillSet.class);
        json.writeValue(CLASS_SKILLS_KEY, this.classSkills.toArray(new Skills[this.classSkills.size()]));
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(SKILL_SET_KEY)) {
            try {
                this.skills = json.readValue(SkillSet.class, jsonData.get(SKILL_SET_KEY));
            }
            catch (Exception ex) {
                this.skills = new SkillSet();
            }
        }
        else {
            this.skills = new SkillSet();
        }

        if (jsonData.has(CLASS_SKILLS_KEY)) {
            this.classSkills.clear();
            jsonData.get(CLASS_SKILLS_KEY).iterator().forEach((JsonValue value) -> {
                try {
                    this.classSkills.add(json.readValue(Skills.class, value));
                }
                catch (Exception ex) {

                }
            });
        }
    }

}
