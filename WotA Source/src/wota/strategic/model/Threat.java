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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author plewis
 */
public class Threat {

    public Threat(ThreatCost cost) {
        this.name.set(cost.getFactionThreatType().toString());
        this.factionThreatType.set(cost.getFactionThreatType());
        this.poolCost.set(cost.getPoolCost());
        this.minigame.set(cost.getMinigame());
        this.description.set(cost.getDescription());
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
    
    private final StringProperty description = new SimpleStringProperty();

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public StringProperty descriptionProperty() {
        return description;
    }
    
    private final ObjectProperty<ThreatType> factionThreatType = 
            new SimpleObjectProperty<>();

    public ThreatType getFactionThreatType() {
        return factionThreatType.get();
    }

    public void setFactionThreatType(ThreatType value) {
        factionThreatType.set(value);
    }

    public ObjectProperty factionThreatTypeProperty() {
        return factionThreatType;
    }
    
    private final ObjectProperty<ConflictType> minigame = 
            new SimpleObjectProperty<>();

    public ConflictType getMinigame() {
        return minigame.get();
    }

    public void setMinigame(ConflictType value) {
        minigame.set(value);
    }

    public ObjectProperty minigameProperty() {
        return minigame;
    }

    private final IntegerProperty difficulty = new SimpleIntegerProperty();

    public int getDifficulty() {
        return difficulty.get();
    }

    public void setDifficulty(int value) {
        difficulty.set(value);
    }

    public IntegerProperty difficultyProperty() {
        return difficulty;
    }

    private final ObjectProperty<MinorFaction> minorFaction = 
            new SimpleObjectProperty<>();

    public MinorFaction getMinorFaction() {
        return minorFaction.get();
    }

    public void setMinorFaction(MinorFaction value) {
        minorFaction.set(value);
    }

    public ObjectProperty minorFactionProperty() {
        return minorFaction;
    }

    private final IntegerProperty poolCost = new SimpleIntegerProperty();

    public int getPoolCost() {
        return poolCost.get();
    }

    public void setPoolCost(int value) {
        poolCost.set(value);
    }

    public IntegerProperty poolCostProperty() {
        return poolCost;
    }

    /* 
     * Warfare threats most likely have a different order of 
     * battle than the default Faction order of battle.
     */
    private ObservableList<OrderOfBattle> units =
        FXCollections.observableArrayList();

    public ObservableList<OrderOfBattle> getUnits() {
        return units;
    }

    public void setUnits(ObservableList<OrderOfBattle> value) {
        this.units = value;
    }

    public ObservableList<OrderOfBattle> unitsProperty() {
        return units;
    }
    
    public String toString(){
        return name.get();
    }
    private final BooleanProperty selected = new SimpleBooleanProperty();

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean value) {
        selected.set(value);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
    private final ObjectProperty<MinorFaction> targetFaction = new SimpleObjectProperty<MinorFaction>();

    public MinorFaction getTargetFaction() {
        return targetFaction.get();
    }

    public void setTargetFaction(MinorFaction value) {
        targetFaction.set(value);
    }

    public ObjectProperty targetFactionProperty() {
        return targetFaction;
    }
    private final ObjectProperty<Hero> targetHero = new SimpleObjectProperty<Hero>();

    public Hero getTargetHero() {
        return targetHero.get();
    }

    public void setTargetHero(Hero value) {
        targetHero.set(value);
    }

    public ObjectProperty targetHeroProperty() {
        return targetHero;
    }
    private final ObjectProperty<Development> targetDevelopment = new SimpleObjectProperty<Development>();

    public Development getTargetDevelopment() {
        return targetDevelopment.get();
    }

    public void setTargetDevelopment(Development value) {
        targetDevelopment.set(value);
    }

    public ObjectProperty targetDevelopmentProperty() {
        return targetDevelopment;
    }
    private final ObjectProperty<Region> targetRegion = new SimpleObjectProperty<Region>();

    public Region getTargetRegion() {
        return targetRegion.get();
    }

    public void setTargetRegion(Region value) {
        targetRegion.set(value);
    }

    public ObjectProperty targetRegionProperty() {
        return targetRegion;
    }
    
}
