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
package com.jupiter.europa.entity.stats.characterclass;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.entity.Families;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.messaging.RequestEffectAddMessage;
import com.jupiter.europa.entity.stats.SkillSet.Skills;
import com.jupiter.europa.entity.trait.TraitPool;
import com.jupiter.europa.entity.trait.feat.FeatPool;
import com.jupiter.europa.entity.stats.race.Race;
import com.jupiter.europa.entity.trait.Trait;
import com.jupiter.europa.entity.trait.feat.Feat;
import com.jupiter.europa.util.Initializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

/**
 *
 * @author Nathan Templon
 */
public abstract class CharacterClass implements Serializable, Initializable {

    // Constants
    public static final int MAX_LEVEL = 20;
    
    public static final Map<String, Class<? extends CharacterClass>> CLASS_LOOKUP = new HashMap<>();
    public static final String[] AVAILABLE_CLASSES = getAvailableClasses();

    private static final String LEVEL_KEY = "level";
    private static final String OWNER_ID_KEY = "owner-id";
    private static final String FEAT_POOL_KEY = "feats";


    // Static Methods
    public static int goodSave(int level) {
        return (int) (10.0 + (50.0 * ((level - 1.0) / (MAX_LEVEL - 1.0))));
    }

    public static int poorSave(int level) {
        return (int) (30.0 * ((level - 1.0) / (MAX_LEVEL - 1.0)));
    }

    public static int goodAttackBonus(int level) {
        double totalBonus = 0.0;
        double currentBonus = level;

        while (currentBonus > 0) {
            totalBonus += 5 * currentBonus;
            currentBonus -= 5;
        }

        return (int) Math.round(totalBonus);
    }

    public static int averageAttackBonus(int level) {
        double totalBonus = 0.0;
        double currentBonus = level * 0.75;

        while (currentBonus > 0) {
            totalBonus += 5 * currentBonus;
            currentBonus -= 5;
        }

        return (int) Math.round(totalBonus);
    }

    public static int poorAttackBonus(int level) {
        double totalBonus = 0.0;
        double currentBonus = level * 0.5;

        while (currentBonus > 0) {
            totalBonus += 5 * currentBonus;
            currentBonus -= 5;
        }

        return (int) Math.round(totalBonus);
    }
    
    private static String[] getAvailableClasses() {
        Reflections refl = new Reflections("com.jupiter.europa");
        Set<Class<? extends CharacterClass>> classes = refl.getSubTypesOf(CharacterClass.class);
        String[] classNames = new String[classes.size()];
        
        int i = 0;
        for(Class<? extends CharacterClass> charClass : classes) {
            String name = charClass.getSimpleName();
            classNames[i] = name;
            CLASS_LOOKUP.put(name, charClass);
            i++;
        }
        
        Arrays.sort(classNames);
        return classNames;
    }


    // Fields
    private int level;
    private long ownerId = -1;
    private Entity owner;
    private final Set<TraitPool<? extends Trait>> abilityPools = new LinkedHashSet<>();
    private FeatPool featPool;


    // Properties
    public int getLevel() {
        return this.level;
    }

    public abstract int getHealthPerLevel();

    public abstract int getStartingHealth();

    public abstract int getSkillPointsPerLevel();

    public abstract int getAttackBonus();

    public abstract int getFortitude();

    public abstract int getReflexes();

    public abstract int getWill();
    
    public abstract Set<Skills> getClassSkills();

    public Set<TraitPool<? extends Trait>> getAbilityPools() {
        return this.abilityPools;
    }

    public Entity getOwner() {
        return this.owner;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;

        this.abilityPools.stream().forEach((TraitPool<?> pool) -> {
            pool.setOwner(this.owner);
            pool.setAutoQualify(true);
            pool.reassesQualifications();
        });

        if (Families.raced.matches(owner)) {
            Race race = Mappers.race.get(owner).getRace();
            if (race != null) {
                if (this.featPool != null) {
                    this.featPool.increaseCapacity(race.getNumberOfFeatsAtFirstLevel());
                }
            }
        }
    }

    public FeatPool getFeatPool() {
        return this.featPool;
    }


    // Initialization
    public CharacterClass() {
        this.level = 1;
    }

    public void create() {
        this.featPool = new FeatPool();
        this.featPool.increaseCapacity(1);

//        this.initialize();
    }

    @Override
    public void initialize() {
        if (this.featPool == null) {
            this.featPool = new FeatPool();
        }

        this.abilityPools.add(this.featPool);

        if (this.ownerId > 0) {
            this.owner = EuropaGame.game.lastIdMapping.get(this.ownerId);
        }

        if (this.owner != null) {
            this.abilityPools.stream().forEach((TraitPool<?> pool) -> {
                pool.setOwner(this.owner);
                pool.setAutoQualify(true);
                pool.reassesQualifications();
            });
        }

        this.featPool.addListener((TraitPool<Feat> pool, Feat feat) -> {
            EuropaGame.game.getMessageSystem().publish(new RequestEffectAddMessage(this.owner, feat.getEffect()));
        });
    }


    // Abstract Methods
    public abstract String getTextureSetName();


    // Public Methods
    public void levelUp() {
        if (this.level < MAX_LEVEL) {
            this.level++;

            if (this.level % 3 == 0) {
                this.featPool.increaseCapacity(1);
            }
        }
    }


    // Serializable (Json) Implementation
    @Override
    public void write(Json json) {
        json.writeValue(LEVEL_KEY, this.level);
        json.writeValue(OWNER_ID_KEY, this.owner.getId());
        json.writeValue(FEAT_POOL_KEY, this.featPool, this.featPool.getClass());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (jsonData.has(LEVEL_KEY)) {
            this.level = jsonData.getInt(LEVEL_KEY);
        }

        if (jsonData.has(OWNER_ID_KEY)) {
            this.ownerId = jsonData.getLong(OWNER_ID_KEY);
        }

        if (jsonData.has(FEAT_POOL_KEY)) {
            this.featPool = json.fromJson(FeatPool.class, jsonData.get(FEAT_POOL_KEY).prettyPrint(EuropaGame.PRINT_SETTINGS));
        }
    }

}
