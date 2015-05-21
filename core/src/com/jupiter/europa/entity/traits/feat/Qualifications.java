package com.jupiter.europa.entity.traits.feat;

import com.badlogic.ashley.core.Entity;
import com.jupiter.europa.entity.Families;
import com.jupiter.europa.entity.Mappers;
import com.jupiter.europa.entity.stats.AttributeSet;
import com.jupiter.europa.entity.traits.FeatNotPresentQualifier;
import com.jupiter.europa.entity.traits.FeatPresentQualifier;
import com.jupiter.europa.entity.traits.Qualifier;

/**
 * Created by nathan on 5/18/15.
 */
public class Qualifications implements Qualifier {

    // Static Methods
    public static Qualifications has(Class<? extends Feat> featClass) {
        return new Qualifications(new FeatPresentQualifier(featClass));
    }

    public static Qualifications lacks(Class<? extends Feat> featClass) {
        return new Qualifications(new FeatNotPresentQualifier(featClass));
    }

    public static Qualifications all(Qualifications... quals) {
        return new Qualifications(entity -> {
            boolean qualifies = true;
            int i = 0;
            while (qualifies && i < quals.length) {
                qualifies = qualifies && quals[i].qualifies(entity);
            }
            return qualifies;
        });
    }

    public static Qualifications none(Qualifications... quals) {
        return new Qualifications(entity -> {
            boolean qualifies = true;
            int i = 0;
            while (qualifies && i < quals.length) {
                qualifies = qualifies && !quals[i].qualifies(entity);
            }
            return qualifies;
        });
    }

    public static Qualifications minStat(AttributeSet.Attributes attribute, int minValue) {
        return new Qualifications(entity -> {
            if (Families.attributed.matches(entity)) {
                return Mappers.attributes.get(entity).getBaseAttributes().getAttribute(attribute) >= minValue;
            }
            return false;
        });
    }


    // Fields
    private final Qualifier quals;


    // Initialization
    private Qualifications(Qualifier quals) {
        this.quals = quals;
    }


    // Public Methods
    @Override
    public boolean qualifies(Entity entity) {
        return this.quals.qualifies(entity);
    }

    public Qualifications or(Qualifications other) {
        return new Qualifications(entity -> this.qualifies(entity) || other.qualifies(entity));
    }

    public Qualifications and(Qualifications other) {
        return new Qualifications(entity -> this.qualifies(entity) && other.qualifies(entity));
    }

    public Qualifications not() {
        return new Qualifications(entity -> !this.qualifies(entity));
    }
}
