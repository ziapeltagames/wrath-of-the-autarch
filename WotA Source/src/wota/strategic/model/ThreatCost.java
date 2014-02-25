/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author plewis
 */
public class ThreatCost {

    public ThreatCost(ThreatType type, int poolCost,
            ConflictType minigame, String description) {
        this.threatType.set(type);
        this.poolCost.set(poolCost);
        this.minigame.set(minigame);
        this.description.set(description);
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

    private final ObjectProperty<ThreatType> threatType
            = new SimpleObjectProperty<>();

    public ThreatType getFactionThreatType() {
        return threatType.get();
    }

    public void setFactionThreatType(ThreatType value) {
        threatType.set(value);
    }

    public ObjectProperty factionThreatTypeProperty() {
        return threatType;
    }

    private final ObjectProperty<ConflictType> minigame
            = new SimpleObjectProperty<>();

    public ConflictType getMinigame() {
        return minigame.get();
    }

    public void setMinigame(ConflictType value) {
        minigame.set(value);
    }

    public ObjectProperty minigameProperty() {
        return minigame;
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

}
