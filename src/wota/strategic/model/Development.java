/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import java.util.ArrayList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class to hold details for a development.
 * 
 * @author plewis
 */
public class Development {
    
    private TechTree tree;
    private ArrayList<Development> prerequisites;

    public Development(DevelopmentType type,
            TechTree tree, boolean regional, boolean knowledgeBased,
            int bp, int food, int timber, int ore, int mana, int luxury, 
            ArrayList<Development>prerequisites, String description,
            String detailedDescription, DevelopmentFunction function) {
        this.type.set(type);
        this.name.set(type.name().replaceAll("_", " "));
        this.tree = tree;
        this.regional.set(regional);
        this.knowledgeBased.set(knowledgeBased);
        this.bp.set(bp);
        this.timber.set(timber);
        this.ore.set(ore);
        this.luxury.set(luxury);
        this.mana.set(mana);
        this.prerequisites = prerequisites;
        this.food.set(food);
        this.description.set(description);
        this.detailedDescription.set(detailedDescription);
        this.used.set(false);
        this.function.set(function);
        if(prerequisites == null){
            this.prerequisites = new ArrayList<>();
        }
    }
    
    private final ObjectProperty<DevelopmentType> type = 
            new SimpleObjectProperty<>();

    public DevelopmentType getType() {
        return type.get();
    }

    public void setType(DevelopmentType value) {
        type.set(value);
    }

    public ObjectProperty typeProperty() {
        return type;
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
    
    public TechTree getTree() {
        return tree;
    }

    public void setTree(TechTree tree) {
        this.tree = tree;
    }
    
    private final BooleanProperty regional = new SimpleBooleanProperty();

    public boolean isRegional() {
        return regional.get();
    }

    public void setRegional(boolean value) {
        regional.set(value);
    }

    public BooleanProperty regionalProperty() {
        return regional;
    }
    
    private final BooleanProperty knowledgeBased = new SimpleBooleanProperty();

    public boolean isKnowledgeBased() {
        return knowledgeBased.get();
    }

    public void setKnowledgeBased(boolean value) {
        knowledgeBased.set(value);
    }

    public BooleanProperty knowledgeBased() {
        return knowledgeBased;
    }
    
    private final ObjectProperty<Region> region = 
            new SimpleObjectProperty<>();

    public Region getRegion() {
        return region.get();
    }

    public void setRegion(Region value) {
        region.set(value);
    }

    public ObjectProperty regionProperty() {
        return region;
    }
    
    private final IntegerProperty bp = new SimpleIntegerProperty();

    public int getBp() {
        return bp.get();
    }

    public void setBp(int value) {
        bp.set(value);
    }

    public IntegerProperty bpProperty() {
        return bp;
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
    
    private final IntegerProperty luxury = new SimpleIntegerProperty();

    public int getLuxury() {
        return luxury.get();
    }

    public void setLuxury(int value) {
        luxury.set(value);
    }

    public IntegerProperty luxuryProperty() {
        return luxury;
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

    public ArrayList<Development> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(ArrayList<Development> prerequisites) {
        this.prerequisites = prerequisites;
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
    
    private final StringProperty detailedDescription = new SimpleStringProperty();

    public String getDetailedDescription() {
        return detailedDescription.get();
    }

    public void setDetailedDescription(String value) {
        detailedDescription.set(value);
    }

    public StringProperty detailedDescriptionProperty() {
        return detailedDescription;
    }    

    private final BooleanProperty used = new SimpleBooleanProperty();

    public boolean isUsed() {
        return used.get();
    }

    public void setUsed(boolean value) {
        used.set(value);
    }

    public BooleanProperty usedProperty() {
        return used;
    }
    
    private final ObjectProperty<DevelopmentFunction> function = 
            new SimpleObjectProperty<>();

    public DevelopmentFunction getFunction() {
        return function.get();
    }

    public void setFunction(DevelopmentFunction value) {
        function.set(value);
    }

    public ObjectProperty functionProperty() {
        return function;
    }

    @Override
    public String toString() {
        if(getRegion() != null){
            return getName()+"-"+getRegion().getName();
        }
        return getName();
    }
    
}
