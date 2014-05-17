/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import java.util.Iterator;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Elements common to all factions, both minor factions, the Autarch, and the
 * Stronghold.
 *
 * @author plewis
 */
public class Faction {

    public Faction() {
    }
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
    private final IntegerProperty stability = new SimpleIntegerProperty();

    public int getStability() {
        return stability.get();
    }

    public void setStability(int value) {
        stability.set(value);
    }

    public IntegerProperty stabilityProperty() {
        return stability;
    }
    private final IntegerProperty population = new SimpleIntegerProperty();

    public int getPopulation() {
        return population.get();
    }

    public void setPopulation(int value) {
        population.set(value);
    }

    public IntegerProperty populationProperty() {
        return population;
    }

    private ObservableList<Development> developments
            = FXCollections.observableArrayList();

    public ObservableList<Development> getDevelopments() {
        return developments;
    }

    public void setDevelopments(ObservableList<Development> value) {
        this.developments = value;
    }

    public ObservableList<Development> developmentsProperty() {
        return developments;
    }

    /* Pull out a specific development from the list. */
    public Development getDevelopment(DevelopmentType type) {
        for (Development development : developments) {
            if (development.getType() == type) {
                return development;
            }
        }
        return null;
    }

    /* 
      Remove it from the faction's list as well as from any associated
      region.
    */
    public void removeDevelopment(Development dev){
        getDevelopments().remove(dev);
        if(dev.isRegional()){
            dev.getRegion().getDevelopments().remove(dev);
        }
    }
    
    public boolean hasDevelopment(DevelopmentType type) {
        return getDevelopment(type) != null;
    }
    
    /* Used to determine if all the prerequisites are there. */
    public boolean hasPrerequisites(Development dev){
        for(Development per : dev.getPrerequisites()){
            if(!hasDevelopment(per.getType())){
                return false;
            }
        }
        return true;
    }

    private ObservableList<Region> regions
            = FXCollections.observableArrayList();

    public ObservableList<Region> getRegions() {
        return regions;
    }

    public void setRegions(ObservableList<Region> value) {
        this.regions = value;
    }

    public ObservableList<Region> regionsProperty() {
        return regions;
    }
    private ObservableList<OrderOfBattle> units
            = FXCollections.observableArrayList();

    public ObservableList<OrderOfBattle> getUnits() {
        return units;
    }

    public void setUnits(ObservableList<OrderOfBattle> value) {
        this.units = value;
    }

    public ObservableList<OrderOfBattle> unitsProperty() {
        return units;
    }

    public OrderOfBattle getUnit(UnitType type) {
        return getUnit(type, this);
    }

    public OrderOfBattle getUnit(UnitType type, Faction owningFaction) {
        for (OrderOfBattle unit : units) {
            if (unit.getUtype() == type
                    && unit.getOwningFaction() == owningFaction) {
                return unit;
            }
        }
        return null;
    }

    private final IntegerProperty totalUnits = new SimpleIntegerProperty();

    public int getTotalUnits() {
        return totalUnits.get();
    }

    public void setTotalUnits(int value) {
        totalUnits.set(value);
    }

    public IntegerProperty totalUnitsProperty() {
        return totalUnits;
    }

    /**
     * Add the indicated number of units to the faction. Set the quality based
     * on developments the faction has.
     *
     * @param unitType
     * @param amount
     */
    public void addUnits(UnitType unitType, int amount) {
        addUnits(unitType, this, amount);
    }

    public void addUnits(OrderOfBattle newUnits) {
        getUnits().add(newUnits);
    }

    public void addUnits(UnitType unitType, Faction owningFaction, int amount) {
        OrderOfBattle targetUnit = getUnit(unitType, owningFaction);
        if (targetUnit != null) {
            targetUnit.setAvailable(targetUnit.getAvailable() + amount);
        } else {
            getUnits().add(new OrderOfBattle(unitType, amount, owningFaction));
        }
        updateUnitQuality();
    }

    /* Used after warfare to clear out allied units. */
    public void clearAlliedUnits() {
        for (Iterator<OrderOfBattle> it = getUnits().iterator(); it.hasNext();) {
            OrderOfBattle nextUnit = it.next();
            if (!nextUnit.getOwningFaction().getName().equalsIgnoreCase(getName())) {
                it.remove();
            }            
        }
    }

    /**
     * Updates the quality of any units which may not be set correctly.
     */
    public void updateUnitQuality() {
        int offense = 0;
        int defense = 0;
        int morale = 0;

        OrderOfBattle infantry = getUnit(UnitType.Infantry);
        if (infantry != null) {
            if (getDevelopment(DevelopmentType.Superb_Barracks) != null) {
                infantry.setQuality(UnitQuality.Elite);
                offense = 3;
                defense = 3;
                morale = 3;
            } else if (getDevelopment(DevelopmentType.Great_Barracks) != null) {
                infantry.setQuality(UnitQuality.Advanced);
                offense = 2;
                defense = 2;
                morale = 2;
            } else if (getDevelopment(DevelopmentType.Barracks) != null) {
                infantry.setQuality(UnitQuality.Regular);
                offense = 1;
                defense = 1;
                morale = 2;
            }
            infantry.setOffense(offense);
            infantry.setDefense(defense);
            infantry.setMorale(morale);
        }
        OrderOfBattle archers = getUnit(UnitType.Archer);
        if (archers != null) {
            if (getDevelopment(DevelopmentType.Fletcher) != null) {
                archers.setQuality(UnitQuality.Regular);
                offense = 1;
                defense = 2;
                morale = 1;
            }
            archers.setOffense(offense);
            archers.setDefense(defense);
            archers.setMorale(morale);
        }
        OrderOfBattle cavalry = getUnit(UnitType.Cavalry);
        if (cavalry != null) {
            if (getDevelopment(DevelopmentType.Stables) != null) {
                cavalry.setQuality(UnitQuality.Regular);
                offense = 2;
                defense = 2;
                morale = 2;
            }
            cavalry.setOffense(offense);
            cavalry.setDefense(defense);
            cavalry.setMorale(morale);
        }
        OrderOfBattle mages = getUnit(UnitType.Battle_Mages);
        if (mages != null) {
            mages.setQuality(UnitQuality.Regular);
            mages.setOffense(4);
            mages.setDefense(3);
            mages.setMorale(1);
        }
        OrderOfBattle militia = getUnit(UnitType.Militia);
        if (militia != null) {
            militia.setQuality(UnitQuality.Regular);
            militia.setOffense(0);
            militia.setDefense(1);
            militia.setMorale(1);
        }
        OrderOfBattle sunrider = getUnit(UnitType.Horse_Archers);
        if (sunrider != null) {
            sunrider.setQuality(UnitQuality.Regular);
            sunrider.setOffense(5);
            sunrider.setDefense(5);
            sunrider.setMorale(3);
        }
        OrderOfBattle catapult = getUnit(UnitType.Catapult);
        if (catapult != null) {
            catapult.setQuality(UnitQuality.Regular);
            catapult.setOffense(4);
            catapult.setDefense(2);
            catapult.setMorale(4);
        }
        OrderOfBattle cannon = getUnit(UnitType.Cannon);
        if (cannon != null) {
            cannon.setQuality(UnitQuality.Regular);
            cannon.setOffense(6);
            cannon.setDefense(3);
            cannon.setMorale(6);
        }
        OrderOfBattle keep = getUnit(UnitType.Keep);
        if (keep != null) {
            keep.setQuality(UnitQuality.Regular);
            keep.setOffense(0);
            keep.setDefense(0);
            keep.setMorale(5);
        }
        OrderOfBattle castle = getUnit(UnitType.Castle);
        if (castle != null) {
            castle.setQuality(UnitQuality.Regular);
            castle.setOffense(0);
            castle.setDefense(7);
            castle.setMorale(7);
        }
        OrderOfBattle dragon = getUnit(UnitType.Dragon);
        if (dragon != null) {
            dragon.setQuality(UnitQuality.Regular);
            dragon.setOffense(7);
            dragon.setDefense(4);
            dragon.setMorale(7);
        }

    }

    public void clearDefensiveFortifications() {
        for (Iterator<OrderOfBattle> it = units.iterator(); it.hasNext();) {
            OrderOfBattle unit = it.next();
            if (unit.getUtype() == UnitType.Keep
                    || unit.getUtype() == UnitType.Castle) {
                it.remove();
            }
        }
    }

    /*
     * This is called after a season is over, to remove all the different
     * state that could accumulate.
     */
    public void clearState() {

        for (OrderOfBattle unit : units) {
            unit.setDeployed(0);
            unit.setDamage(0);
            unit.setCasualties(0);
        }

        /* After clearing state, all of the units are redeployed. */
        int available = getPopulation();
        available = deployUnitType(UnitType.Cavalry, available);
        available = deployUnitType(UnitType.Archer, available);
        available = deployUnitType(UnitType.Infantry, available);
        deployUnitType(UnitType.Militia, available);

        OrderOfBattle dragon = getUnit(UnitType.Dragon);
        if (dragon != null) {
            dragon.setDefense(dragon.getAvailable());
        }

        regions.stream().forEach((region) -> {
            region.setSelected(false);
        });
    }

    /* Deploy a specific unit type. */
    private int deployUnitType(UnitType type, int available) {

        if (available == 0) {
            return available;
        }

        OrderOfBattle checkUnit = getUnit(type);
        if (checkUnit != null) {
            int tpop = available - checkUnit.getAvailable();
            if (tpop >= 0) {
                checkUnit.setDeployed(checkUnit.getAvailable());
                available = available - checkUnit.getAvailable();
            } else {
                checkUnit.setDeployed(available);
                available = 0;
            }
        }
        return available;
    }

    @Override
    public String toString() {
        return getName();
    }

}
