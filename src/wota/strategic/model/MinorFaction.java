/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author plewis
 */
public class MinorFaction extends Faction {

    public MinorFaction(String name, int disposition,
            int stability, int population, int tradeDifficulty,
            int militaryDifficulty, int infiltrationDifficulty,
            int unallocatedThreats, int diplomacyThreats,
            int infiltrationThreats, int skirmishThreats,
            int warfareThreats) {
        setName(name);
        this.disposition.set(disposition);
        setStability(stability);
        setPopulation(population);
        this.tradeDifficulty.set(tradeDifficulty);
        this.militaryDifficulty.set(militaryDifficulty);
        this.infiltrationDifficulty.set(infiltrationDifficulty);
        this.unallocatedThreatPool.set(unallocatedThreats);
        this.diplomacyPool.set(diplomacyThreats);
        this.infiltrationPool.set(infiltrationThreats);
        this.skirmishPool.set(skirmishThreats);
        this.warfarePool.set(warfareThreats);
    }
    private final IntegerProperty disposition = new SimpleIntegerProperty();

    public int getDisposition() {
        return disposition.get();
    }

    public void setDisposition(int value) {
        disposition.set(value);
    }

    public IntegerProperty dispositionProperty() {
        return disposition;
    }

    /* 
     Used in the mission class to prune the list of goods that
     are available from a faction based on disposiion.
     */
    private ObservableList<TradeEntry> currentGiveTradeChart
            = FXCollections.observableArrayList();

    public ObservableList<TradeEntry> getCurrentGiveTradeChart() {
        currentGiveTradeChart.clear();
        int disp = 1;
        if (getName().equalsIgnoreCase("autarch")) {
            disp = 2;
        }
        if (getDisposition() > 1) {
            disp = getDisposition();
        }
        for (TradeEntry nextEntry : getGiveTradeChart()) {
            if (nextEntry.getValue() <= disp) {
                currentGiveTradeChart.add(nextEntry);
            }
        }
        return currentGiveTradeChart;
    }

    public void setCurrentGiveTradeChart(ObservableList<TradeEntry> value) {
        currentGiveTradeChart = value;
    }

    public ObservableList<TradeEntry> currentGiveTradeChartProperty() {
        return currentGiveTradeChart;
    }

    private ObservableList<TradeEntry> giveTradeChart
            = FXCollections.observableArrayList();

    public ObservableList<TradeEntry> getGiveTradeChart() {
        return giveTradeChart;
    }

    public void setGiveTradeChart(ObservableList<TradeEntry> value) {
        giveTradeChart = value;
    }

    public ObservableList<TradeEntry> giveTradeChartProperty() {
        return giveTradeChart;
    }
    private ObservableList<TradeEntry> receiveTradeChart
            = FXCollections.observableArrayList();

    public ObservableList<TradeEntry> getReceiveTradeChart() {
        return receiveTradeChart;
    }

    public void setReceiveTradeChart(ObservableList<TradeEntry> value) {
        receiveTradeChart = value;
    }

    public ObservableList<TradeEntry> receiveTradeChartProperty() {
        return receiveTradeChart;
    }

    private final IntegerProperty tradeDifficulty
            = new SimpleIntegerProperty();

    public int getTradeDifficulty() {
        return tradeDifficulty.get();
    }

    public void setTradeDifficulty(int value) {
        tradeDifficulty.set(value);
    }

    public IntegerProperty tradeDifficultyProperty() {
        return tradeDifficulty;
    }

    private final IntegerProperty militaryDifficulty
            = new SimpleIntegerProperty();

    public int getMilitaryDifficulty() {
        return militaryDifficulty.get();
    }

    public void setMilitaryDifficulty(int value) {
        militaryDifficulty.set(value);
    }

    public IntegerProperty militaryDifficultyProperty() {
        return militaryDifficulty;
    }

    private final IntegerProperty infiltrationDifficulty
            = new SimpleIntegerProperty();

    public int getInfiltrationDifficulty() {
        return infiltrationDifficulty.get();
    }

    public void setInfiltrationDifficulty(int value) {
        infiltrationDifficulty.set(value);
    }

    public IntegerProperty infiltrationDifficultyProperty() {
        return infiltrationDifficulty;
    }

    private final IntegerProperty skirmishDifficulty = new SimpleIntegerProperty();

    public int getSkirmishDifficulty() {
        return skirmishDifficulty.get();
    }

    public void setSkirmishDifficulty(int value) {
        skirmishDifficulty.set(value);
    }

    public IntegerProperty skirmishDifficultyProperty() {
        return skirmishDifficulty;
    }

    private final IntegerProperty skirmishPool = new SimpleIntegerProperty();

    public int getSkirmishPool() {
        return skirmishPool.get();
    }

    public void setSkirmishPool(int value) {
        skirmishPool.set(value);
    }

    public IntegerProperty skirmishPoolProperty() {
        return skirmishPool;
    }
    private final IntegerProperty infiltrationPool = new SimpleIntegerProperty();

    public int getInfiltrationPool() {
        return infiltrationPool.get();
    }

    public void setInfiltrationPool(int value) {
        infiltrationPool.set(value);
    }

    public IntegerProperty infiltrationPoolProperty() {
        return infiltrationPool;
    }

    private final IntegerProperty warfarePool = new SimpleIntegerProperty();

    public int getWarfarePool() {
        return warfarePool.get();
    }

    public void setWarfarePool(int value) {
        warfarePool.set(value);
    }

    public IntegerProperty warfarePoolProperty() {
        return warfarePool;
    }
    private final IntegerProperty diplomacyPool = new SimpleIntegerProperty();

    public int getDiplomacyPool() {
        return diplomacyPool.get();
    }

    public void setDiplomacyPool(int value) {
        diplomacyPool.set(value);
    }

    public IntegerProperty diplomacyPoolProperty() {
        return diplomacyPool;
    }

    private final IntegerProperty unallocatedThreatPool
            = new SimpleIntegerProperty();

    public int getUnallocatedThreatPool() {
        return unallocatedThreatPool.get();
    }

    public void setUnallocatedThreatPool(int value) {
        unallocatedThreatPool.set(value);
    }

    public IntegerProperty unallocatedThreatPoolProperty() {
        return unallocatedThreatPool;
    }

    public int getPool(ConflictType conflict) {
        switch (conflict) {
            case Diplomacy:
                return getDiplomacyPool();
            case Infiltration:
                return getInfiltrationPool();
            case Skirmish:
                return getSkirmishPool();
            case Warfare:
                return getWarfarePool();
            default:
                return 0;
        }
    }

    /**
     * Adds the points to the pool, and increases the difficulty of the faction
     * as necessary.
     *
     * @param conflict
     * @param pool
     */
    public void increaseThreatPool(ConflictType conflict, int pool) {
        switch (conflict) {
            case Diplomacy:
                setTradeDifficulty(getTradeDifficulty() + 1);
                setDiplomacyPool(getDiplomacyPool() + pool);
                break;
            case Infiltration:
                setInfiltrationDifficulty(getInfiltrationDifficulty() + 1);
                setInfiltrationPool(getInfiltrationPool() + pool);
                break;
            case Skirmish:
                setSkirmishDifficulty(getSkirmishDifficulty() + 1);
                setSkirmishPool(getSkirmishPool() + pool);
                break;
            case Warfare:
                /* Also need to bump up troop effectiveness. */
                setMilitaryDifficulty(getMilitaryDifficulty() + 1);
                setWarfarePool(getWarfarePool() + pool);
                switch (getWarfarePool()) {
                    case 4:
                        addUnits(UnitType.Infantry, 4);
                        addUnits(UnitType.Archer, 4);
                        addUnits(UnitType.Cavalry, 4);
                        break;
                    case 8:
                        addUnits(UnitType.Infantry, 4);
                        addUnits(UnitType.Archer, 4);
                        addUnits(UnitType.Cavalry, 4);
                        break;
                    case 12:
                        addUnits(UnitType.Infantry, 4);
                        addUnits(UnitType.Archer, 4);
                        addUnits(UnitType.Cavalry, 4);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public int getDifficultyByConflict(ConflictType conflict) {
        switch (conflict) {
            case Diplomacy:
                return getTradeDifficulty();
            case Infiltration:
                return getInfiltrationDifficulty();
            case Skirmish:
                return getSkirmishDifficulty();
            case Warfare:
                return getMilitaryDifficulty();
            default:
                return 0;
        }
    }

    private final HashMap<UnitType, Integer> alliedUnits
            = new HashMap<>();

    public void addAlliedUnits(UnitType type, int amount) {
        alliedUnits.put(type, Integer.valueOf(amount));
    }

    public ArrayList<OrderOfBattle> getAlliedUnits() {
        ArrayList<OrderOfBattle> currentUnits
                = new ArrayList<>();
        for (UnitType nextType : alliedUnits.keySet()) {
            int committed = alliedUnits.get(nextType).intValue();

            /* Check if this number of units is available, if so, add it. */
            OrderOfBattle nextUnit = getUnit(nextType);

            if (nextUnit.getAvailable() <= 0) {
                continue;
            }

            /* Set how many of each unit will aid the ally. */
            if (nextUnit.getAvailable() > committed) {
                nextUnit.setDeployed(committed);
            } else {
                nextUnit.setDeployed(nextUnit.getAvailable());
            }

            currentUnits.add(nextUnit);
        }
        return currentUnits;
    }

}
