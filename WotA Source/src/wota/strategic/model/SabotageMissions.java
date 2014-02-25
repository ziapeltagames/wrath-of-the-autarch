/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author plewis
 */
public class SabotageMissions {

    public SabotageMissions(SabotageType type,
            String name) {
        this.type.setValue(type);
        this.name.setValue(name);
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

    private final ObjectProperty<SabotageType> type
            = new SimpleObjectProperty<>();

    public SabotageType getType() {
        return type.get();
    }

    public void setType(SabotageType value) {
        type.set(value);
    }

    public ObjectProperty typeProperty() {
        return type;
    }

    public String toString() {
        return name.get();
    }

}
