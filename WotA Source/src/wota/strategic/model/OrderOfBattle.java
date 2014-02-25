/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A model class to hold details about a unit. This is used by the
 * Stronghold to store data about units, as well as used during the
 * Warfare missions.
 * 
 * @author plewis
 */
public class OrderOfBattle {

    public OrderOfBattle(UnitType type, int available, Faction owningFaction) {
        this.utype.set(type);
        this.available.set(available);
        this.offense.set(0);
        this.defense.set(0);
        this.morale.set(0);
        this.deployed.set(0);
        this.food.set(0);
        this.damage.set(0);
        this.casualties.set(0);
        this.owningFaction.set(owningFaction);
        this.quality.set(UnitQuality.Regular);
        
        /*
         * Setup a binding so that changes in troop deployment causes
         * a corresponding change in food supply requirements.
         */
        IntegerBinding foodBinding = new IntegerBinding(){
            
            {
                super.bind(deployed);
            }

            @Override
            protected int computeValue() {
                switch(utype.get()){
                    case Cavalry:
                        return 2*deployed.get();
                    case Keep:
                    case Castle:
                        return 0;
                    default:
                        return deployed.get();
                }
            }
        };        
        food.bind(foodBinding);
                
    }
    
    private final ObjectProperty<UnitType> utype = 
            new SimpleObjectProperty<>();

    public UnitType getUtype() {
        return utype.get();
    }

    public void setUtype(UnitType value) {
        utype.set(value);
    }

    public ObjectProperty utypeProperty() {
        return utype;
    }
    
    private final IntegerProperty available = new SimpleIntegerProperty();

    public int getAvailable() {
        return available.get();
    }

    public void setAvailable(int value) {
        available.set(value);
    }

    public IntegerProperty availableProperty() {
        return available;
    }
    
    private final IntegerProperty deployed = new SimpleIntegerProperty();

    public int getDeployed() {
        return deployed.get();
    }

    public void setDeployed(int value) {
        deployed.set(value);
    }

    public IntegerProperty deployedProperty() {
        return deployed;
    }
    
    private final IntegerProperty offense = new SimpleIntegerProperty();

    public int getOffense() {
        return offense.get();
    }

    public void setOffense(int value) {
        offense.set(value);
    }

    public IntegerProperty offenseProperty() {
        return offense;
    }
    
    private final IntegerProperty defense = new SimpleIntegerProperty();

    public int getDefense() {
        return defense.get();
    }

    public void setDefense(int value) {
        defense.set(value);
    }

    public IntegerProperty defenseProperty() {
        return defense;
    }
    
    private final IntegerProperty food = new SimpleIntegerProperty();

    public int getFood() {
        return food.get();
    }

    public void setFood(int value) {
        food.set(value);
    }

    public IntegerProperty foodProperty() {
        return food;
    }
    
    private final IntegerProperty damage = new SimpleIntegerProperty();

    public int getDamage() {
        return damage.get();
    }

    public void setDamage(int value) {
        damage.set(value);
    }

    public IntegerProperty damageProperty() {
        return damage;
    }
    private final IntegerProperty morale = new SimpleIntegerProperty();

    public int getMorale() {
        return morale.get();
    }

    public void setMorale(int value) {
        morale.set(value);
    }

    public IntegerProperty moraleProperty() {
        return morale;
    }

    private final ObjectProperty<Faction> owningFaction = 
            new SimpleObjectProperty<>();

    public Faction getOwningFaction() {
        return owningFaction.get();
    }

    public void setOwningFaction(Faction value) {
        owningFaction.set(value);
    }

    public ObjectProperty owningFactionProperty() {
        return owningFaction;
    }
    
    private final ObjectProperty<UnitQuality> quality = 
            new SimpleObjectProperty<>();

    public UnitQuality getQuality() {
        return quality.get();
    }

    public void setQuality(UnitQuality value) {
        quality.set(value);
    }

    public ObjectProperty qualityProperty() {
        return quality;
    }
    private final IntegerProperty casualties = new SimpleIntegerProperty();

    public int getCasualties() {
        return casualties.get();
    }

    public void setCasualties(int value) {
        casualties.set(value);
    }

    public IntegerProperty casualtiesProperty() {
        return casualties;
    }
    
}
