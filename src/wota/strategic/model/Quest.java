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
 * @author plewis
 */
public class Quest {

    public Quest(String questName, Development artifact,
            int difficulty, ConflictType minigame, MinorFaction ally) {
        this.name.set(questName);
        this.artifact.set(artifact);
        this.difficulty.set(difficulty);
        this.minigame.set(minigame);
        this.ally.set(ally);
    }
    
    private final ObjectProperty<MinorFaction> ally = 
            new SimpleObjectProperty<>();

    public MinorFaction getAlly() {
        return ally.get();
    }

    public void setAlly(MinorFaction value) {
        ally.set(value);
    }

    public ObjectProperty allyProperty() {
        return ally;
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
    
    private final ObjectProperty<Development> artifact = 
            new SimpleObjectProperty<>();

    public Development getArtifact() {
        return artifact.get();
    }

    public void setArtifact(Development value) {
        artifact.set(value);
    }

    public ObjectProperty artifactProperty() {
        return artifact;
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
    
    @Override
    public String toString(){
        return getName();
    }
    
}
