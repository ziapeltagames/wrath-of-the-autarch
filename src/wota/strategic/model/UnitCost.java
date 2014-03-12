/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author plewis
 */
public class UnitCost extends Development {

    public UnitCost(UnitType utype, int bp, int food, int timber, int ore,
            int luxury, int mana, ArrayList<Development> prerequisites) {
        super(DevelopmentType.Military_Unit,
                TechTree.Warfare, false, false,
                bp, food, timber,
                ore, luxury, mana, prerequisites, "", "",
                DevelopmentFunction.Miscellaneous);
        setName(utype.name());
        this.utype.set(utype);
    }

    private final ObjectProperty<UnitType> utype
            = new SimpleObjectProperty<>();

    public UnitType getUtype() {
        return utype.get();
    }

    public void setUtype(UnitType value) {
        utype.set(value);
    }

    public ObjectProperty utypeProperty() {
        return utype;
    }

}
