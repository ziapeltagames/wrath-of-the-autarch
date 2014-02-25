/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class holds all of the characteristics for heroes in the Strategic
 * Model. These consist of major and minor focus areas (as represented by
 * mini-games), ranks in those focus areas, consequences, experience points, and
 * whether or not the character is a magic-user or not.
 *
 * @author plewis
 */
public class Hero {

    public Hero(String name, ConflictType majorFocus,
            ConflictType minorFocus, boolean spellcaster) {
        this.name.set(name);
        this.rank.set(4);
        this.xp.set(0);
        this.consequences.set(0);
        this.mana.set(0);
        this.majorFocus.set(majorFocus);
        this.minorFocus.set(minorFocus);
        this.spellcaster.set(spellcaster);
        this.onMission.set(false);
        this.backlash.set(0);
    }

    /* Name. */
    private final StringProperty name = new SimpleStringProperty();

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }

    /* Spellcaster. */
    private final BooleanProperty spellcaster = new SimpleBooleanProperty();

    public boolean isSpellcaster() {
        return spellcaster.get();
    }

    public void setSpellcaster(boolean value) {
        spellcaster.set(value);
    }

    public BooleanProperty spellcasterProperty() {
        return spellcaster;
    }

    /* Major focus. */
    private final ObjectProperty<ConflictType> majorFocus
            = new SimpleObjectProperty<>();

    public ConflictType getMajorFocus() {
        return majorFocus.get();
    }

    public void setMajorFocus(ConflictType value) {
        majorFocus.set(value);
    }

    public ObjectProperty majorFocusProperty() {
        return majorFocus;
    }

    /* Minor focus. */
    private final ObjectProperty<ConflictType> minorFocus
            = new SimpleObjectProperty<>();

    public ConflictType getMinorFocus() {
        return minorFocus.get();
    }

    public void setMinorFocus(ConflictType value) {
        minorFocus.set(value);
    }

    public ObjectProperty minorFocusProperty() {
        return minorFocus;
    }

    /* XP. */
    private final IntegerProperty xp = new SimpleIntegerProperty();

    public int getXp() {
        return xp.get();
    }

    public void setXp(int value) {
        xp.set(value);
    }

    public IntegerProperty xpProperty() {
        return xp;
    }

    /* Rank. */
    private final IntegerProperty rank = new SimpleIntegerProperty();

    public int getRank() {
        return rank.get();
    }

    public void setRank(int value) {
        rank.set(value);
    }

    public IntegerProperty rankProperty() {
        return rank;
    }

    /* Consequences. */
    private final IntegerProperty consequences = new SimpleIntegerProperty();

    public int getConsequences() {
        return consequences.get();
    }

    public void setConsequences(int value) {
        consequences.set(value);
    }

    public IntegerProperty consequencesProperty() {
        return consequences;
    }

    private final BooleanProperty onMission = new SimpleBooleanProperty();

    public boolean isOnMission() {
        return onMission.get();
    }

    public void setOnMission(boolean value) {
        onMission.set(value);
    }

    public BooleanProperty onMissionProperty() {
        return onMission;
    }

    private final IntegerProperty mana = new SimpleIntegerProperty();

    public int getMana() {
        return mana.get();
    }

    public void setMana(int value) {
        mana.set(value);
    }

    public IntegerProperty manaProperty() {
        return mana;
    }

    /*
     Calculate the mana for a spellcaster.
     */
    public void updateMana(Stronghold stronghold) {
        Region region = getManaRegion();
        int regionMana = 0;
        if (region != null) {
            regionMana = region.getMana();
        }
        
        /* Artifact adds one to every mana score! */
        if(stronghold.hasDevelopment(DevelopmentType.Theddas_Palimpsest)){
            regionMana = regionMana + 1;
        }
        mana.set((rank.get() * regionMana) / 2);
    }

    private final ObjectProperty<Region> manaRegion = 
            new SimpleObjectProperty<>();

    public Region getManaRegion() {
        return manaRegion.get();
    }

    public void setManaRegion(Region value) {
        manaRegion.set(value);
    }

    public ObjectProperty manaRegionProperty() {
        return manaRegion;
    }
    private final IntegerProperty backlash = new SimpleIntegerProperty();

    public int getBacklash() {
        return backlash.get();
    }

    public void setBacklash(int value) {
        backlash.set(value);
    }

    public IntegerProperty backlashProperty() {
        return backlash;
    }

    /* TO-DO: Update for spellcasters. */
    public int getSkill(ConflictType conflictType){
        if(conflictType == getMajorFocus()){
            return getRank();
        }
        else if(conflictType == getMinorFocus()){
            return getRank() - 1;
        }
        else return getRank() - 2;
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
