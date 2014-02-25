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
public class Trade {
    
    public Trade(TradeResource giveResource, int giveAmount,
            TradeResource receiveResource, int receiveAmount,
            MinorFaction receiveFaction, int seasonsRemaining){
        this.receiveFaction.set(receiveFaction);
        this.giveResource.set(giveResource);
        this.giveAmount.set(giveAmount);
        this.receiveResource.set(receiveResource);
        this.receiveAmount.set(receiveAmount);
        this.seasonsRemaining.set(seasonsRemaining);
    }

    private final IntegerProperty seasonsRemaining = 
            new SimpleIntegerProperty();

    public int getSeasonsRemaining() {
        return seasonsRemaining.get();
    }

    public void setSeasonsRemaining(int value) {
        seasonsRemaining.set(value);
    }

    public IntegerProperty seasonsRemainingProperty() {
        return seasonsRemaining;
    }  
    
    private final ObjectProperty<TradeResource> giveResource = 
            new SimpleObjectProperty<>();

    public TradeResource getGiveResource() {
        return giveResource.get();
    }

    public void setGiveResource(TradeResource value) {
        giveResource.set(value);
    }

    public ObjectProperty giveResourceProperty() {
        return giveResource;
    }
    private final IntegerProperty giveAmount = new SimpleIntegerProperty();

    public int getGiveAmount() {
        return giveAmount.get();
    }

    public void setGiveAmount(int value) {
        giveAmount.set(value);
    }

    public IntegerProperty giveAmountProperty() {
        return giveAmount;
    }
    
    private final ObjectProperty<TradeResource> receiveResource = 
            new SimpleObjectProperty<>();

    public TradeResource getReceiveResource() {
        return receiveResource.get();
    }

    public void setReceiveResource(TradeResource value) {
        receiveResource.set(value);
    }

    public ObjectProperty receiveResourceProperty() {
        return receiveResource;
    }
    
    private final IntegerProperty receiveAmount = new SimpleIntegerProperty();

    public int getReceiveAmount() {
        return receiveAmount.get();
    }

    public void setReceiveAmount(int value) {
        receiveAmount.set(value);
    }

    public IntegerProperty receiveAmountProperty() {
        return receiveAmount;
    }
    
    private final ObjectProperty<MinorFaction> receiveFaction = 
            new SimpleObjectProperty<>();

    public MinorFaction getReceiveFaction() {
        return receiveFaction.get();
    }

    public void setReceiveFaction(MinorFaction value) {
        receiveFaction.set(value);
    }

    public ObjectProperty receiveFactionProperty() {
        return receiveFaction;
    }
    
}
