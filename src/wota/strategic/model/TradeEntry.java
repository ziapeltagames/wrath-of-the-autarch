/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author plewis
 */
public class TradeEntry {

    public TradeEntry(TradeResource resource, int amount, int value) {
        this.resource.set(resource);
        this.amount.set(amount);
        this.value.set(value);
    }
    
    private final ObjectProperty<TradeResource> resource = new SimpleObjectProperty<TradeResource>();

    public TradeResource getResource() {
        return resource.get();
    }

    public void setResource(TradeResource value) {
        resource.set(value);
    }

    public ObjectProperty resourceProperty() {
        return resource;
    }
    
    private final IntegerProperty amount = new SimpleIntegerProperty();

    public int getAmount() {
        return amount.get();
    }

    public void setAmount(int value) {
        amount.set(value);
    }

    public IntegerProperty amountProperty() {
        return amount;
    }
    private final IntegerProperty value = new SimpleIntegerProperty();

    public int getValue() {
        return value.get();
    }

    public void setValue(int ivalue) {
        value.set(ivalue);
    }

    public IntegerProperty valueProperty() {
        return value;
    }
    
}
