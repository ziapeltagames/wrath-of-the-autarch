/*
 * To change this template, choose Tools | Templates
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
public class Condition {

    public Condition(ConditionType type,
            int seasons) {
        this.type.set(type);
        this.seasons.set(seasons);
    }
    
    private final ObjectProperty<ConditionType> type = 
            new SimpleObjectProperty<>();

    public ConditionType getType() {
        return type.get();
    }

    public void setType(ConditionType value) {
        type.set(value);
        System.out.println(value.name());
        this.name.set(value.name().replace('_', ' '));
    }

    public ObjectProperty typeProperty() {
        return type;
    }
    private final IntegerProperty magnitude = new SimpleIntegerProperty();

    public int getMagnitude() {
        return magnitude.get();
    }

    public void setMagnitude(int value) {
        magnitude.set(value);
    }

    public IntegerProperty magnitudeProperty() {
        return magnitude;
    }
    private final ObjectProperty<MinorFaction> faction = 
            new SimpleObjectProperty<>();

    public MinorFaction getFaction() {
        return faction.get();
    }

    public void setFaction(MinorFaction value) {
        faction.set(value);
    }

    public ObjectProperty factionProperty() {
        return faction;
    }
    private final IntegerProperty seasons = new SimpleIntegerProperty();

    public int getSeasons() {
        return seasons.get();
    }

    public void setSeasons(int value) {
        seasons.set(value);
    }

    public IntegerProperty seasonsProperty() {
        return seasons;
    }
    private final ObjectProperty<Hero> hero = new SimpleObjectProperty<Hero>();

    public Hero getHero() {
        return hero.get();
    }

    public void setHero(Hero value) {
        hero.set(value);
    }

    public ObjectProperty heroProperty() {
        return hero;
    }
    private final ObjectProperty<Development> development = new SimpleObjectProperty<Development>();

    public Development getDevelopment() {
        return development.get();
    }

    public void setDevelopment(Development value) {
        development.set(value);
    }

    public ObjectProperty developmentProperty() {
        return development;
    }
    private final ObjectProperty<Region> region = new SimpleObjectProperty<Region>();

    public Region getRegion() {
        return region.get();
    }

    public void setRegion(Region value) {
        region.set(value);
    }

    public ObjectProperty regionProperty() {
        return region;
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
    
}
