/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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
 * Class to hold region details.
 * 
 * @author plewis
 */
public class Region {

    public Region(String name, int food, int timber, int ore, int mana,
            int luxuries, int population, Faction owner, ConflictType minigame,
            int difficulty) {
        this.name.set(name);
        this.food.set(food);
        this.timber.set(timber);
        this.mana.set(mana);
        this.ore.set(ore);
        this.luxuries.set(luxuries);
        this.population.set(population);
        this.owner.set(owner);
        this.minigame.set(minigame);
        this.difficulty.set(difficulty);
        this.selected.set(false);
        this.threat.set(false);
        this.developmentsString.set("None");
        
        /* 
         * Create a listener for the list of developments, if it changes,
         * modify the string used in the Regions table.
         */
        this.developments.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                String tempDevlString = "";
                for (Iterator<Development> it = developments.iterator(); it.hasNext();) {
                    Development development = it.next();
                    tempDevlString = tempDevlString.concat(development.getName());
                    tempDevlString = tempDevlString.concat(", ");
                }
                if(tempDevlString.isEmpty()){
                    tempDevlString = "None";
                } else{
                    tempDevlString =
                        tempDevlString.substring(0, tempDevlString.length() -2);
                }
                setDevelopmentsString(tempDevlString);
            }
        });

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
    private final IntegerProperty timber = new SimpleIntegerProperty();

    public int getTimber() {
        return timber.get();
    }

    public void setTimber(int value) {
        timber.set(value);
    }

    public IntegerProperty timberProperty() {
        return timber;
    }
    private final IntegerProperty ore = new SimpleIntegerProperty();

    public int getOre() {
        return ore.get();
    }

    public void setOre(int value) {
        ore.set(value);
    }

    public IntegerProperty oreProperty() {
        return ore;
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
    private final IntegerProperty luxuries = new SimpleIntegerProperty();

    public int getLuxuries() {
        return luxuries.get();
    }

    public void setLuxuries(int value) {
        luxuries.set(value);
    }

    public IntegerProperty luxuriesProperty() {
        return luxuries;
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
    
    private ObservableList<Development> developments = 
            FXCollections.observableArrayList();

    public ObservableList<Development> getDevelopments() {
        return developments;
    }

    public void setDevelopments(ObservableList<Development> value) {
        this.developments = value;
    }

    public ObservableList<Development> developmentsProperty() {
        return developments;
    }

    private final ObjectProperty<Faction> owner = 
            new SimpleObjectProperty<>();

    public Faction getOwner() {
        return owner.get();
    }

    public void setOwner(Faction value) {
        owner.set(value);
    }

    public ObjectProperty ownerProperty() {
        return owner;
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
    
    private final BooleanProperty threat = new SimpleBooleanProperty();

    public boolean isThreat() {
        return threat.get();
    }

    public void setThreat(boolean value) {
        threat.set(value);
    }

    public BooleanProperty threatProperty() {
        return threat;
    }
    
    @Override
    public String toString(){
        return getName();
    }
    
    private final StringProperty developmentsString = new SimpleStringProperty();

    public String getDevelopmentsString() {
        return developmentsString.get();
    }

    public void setDevelopmentsString(String value) {
        developmentsString.set(value);
    }

    public StringProperty developmentsStringProperty() {
        return developmentsString;
    }
    
}
