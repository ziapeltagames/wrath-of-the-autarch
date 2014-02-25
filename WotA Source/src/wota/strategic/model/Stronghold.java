/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static wota.strategic.model.TechTree.Arcane;
import static wota.strategic.model.TechTree.Diplomacy;
import static wota.strategic.model.TechTree.Warfare;
import static wota.strategic.model.TradeResource.BP;
import wota.strategic.ui.WotAStrategicModel;

/**
 *
 * @author plewis
 */
public class Stronghold extends Faction {

    private ObservableList<Hero> heroes
            = FXCollections.observableArrayList();
    private final ArrayList<String> heroNames;
    private final WotAStrategicModel wotaModel;
    private final Random rand;

    public Stronghold(WotAStrategicModel wotaModel) {
        super();

        rand = new Random((new Date()).getTime());
        this.wotaModel = wotaModel;
        setStability(10);
        setPopulation(10);
        setName("Generic Stronghold");

        this.food.set(0);
        this.timber.set(0);
        this.mana.set(0);
        this.ore.set(0);
        this.luxuries.set(0);

        this.heroNames = new ArrayList<>();
    }

    /**
     * Set up some random heroes.
     *
     * @param manaRegion
     */
    public void createRandomHeroes(Region manaRegion) {
        populateHeroNames();
        for (int i = 0; i < 10; i++) {
            createNextHero(manaRegion);
        }
    }

    private void createNextHero(Region manaRegion) {
        boolean spellcaster = false;
        if (rand.nextInt(8) == 0) {
            spellcaster = true;
        }
        ConflictType major = ConflictType.values()[rand.nextInt(4)];
        int minorValue = rand.nextInt(4);
        ConflictType minor = ConflictType.values()[minorValue];
        if (major == minor) {
            minorValue++;
            if (minorValue == 4) {
                minorValue = 0;
            }
            minor = ConflictType.values()[minorValue];
        }
        Hero nextHero = new Hero(heroNames.get(rand.nextInt(heroNames.size())),
                major, minor, spellcaster);
        if (spellcaster) {
            nextHero.setManaRegion(manaRegion);
        }
        heroes.add(nextHero);
    }

    private void populateHeroNames() {
        heroNames.add("Cadell");
        heroNames.add("Catrin");
        heroNames.add("Gwynnet");
        heroNames.add("Kara");
        heroNames.add("Maelgwn");
        heroNames.add("Modwen");
        heroNames.add("Rhys");
        heroNames.add("Taryn");
        heroNames.add("Tegwyn");
        heroNames.add("Trefor");
        heroNames.add("Alain");
        heroNames.add("Beylard");
        heroNames.add("Conor");
        heroNames.add("Dundas");
        heroNames.add("Ellwood");
        heroNames.add("Elinor");
        heroNames.add("Elrad");
        heroNames.add("Edwyn");
        heroNames.add("Baldor");
        heroNames.add("Ardal");
        heroNames.add("Asaf");
        heroNames.add("Arno");
        heroNames.add("Alvord");
        heroNames.add("Bracken");
        heroNames.add("Craigh");
        heroNames.add("Cyriel");
        heroNames.add("Cyryl");
        heroNames.add("Diehl");
        heroNames.add("Dirk");
        heroNames.add("Eager");
        heroNames.add("Arvo");
        heroNames.add("Axel");
        heroNames.add("Aylmer");
        heroNames.add("Baldwin");
        heroNames.add("Haigh");
        heroNames.add("Gerrish");
        heroNames.add("Gholson");
        heroNames.add("Gildersleeve");
        heroNames.add("Faraday");
        heroNames.add("Fahs");
        heroNames.add("Falcon");
        heroNames.add("Sketch");
        heroNames.add("Shadow");
        heroNames.add("Tulan");
        heroNames.add("Skrye");
        heroNames.add("Tara");
        heroNames.add("Caren");
        heroNames.add("Jolan");
        heroNames.add("Kirk");
        heroNames.add("Malhar");
        heroNames.add("Mankey");
        heroNames.add("Murrough");
        heroNames.add("Nevile");
        heroNames.add("Parr");
        heroNames.add("Pigot");
        heroNames.add("Odd");
        heroNames.add("Odo");
        heroNames.add("Pollock");
        heroNames.add("Plaisted");
        heroNames.add("Polycarp");
        heroNames.add("Preston");
        heroNames.add("Ulric");
        heroNames.add("Sutan");
        heroNames.add("Sackville");
        heroNames.add("Salter");
        heroNames.add("Salwyn");
        heroNames.add("Tench");
        heroNames.add("Newt");
        heroNames.add("York");
        heroNames.add("Adelot");
        heroNames.add("Devra");
        heroNames.add("Elinor");
        heroNames.add("Ella");
        heroNames.add("Ada");
        heroNames.add("Mora");
        heroNames.add("Naas");
        heroNames.add("Jillian");
        heroNames.add("Helga");
        heroNames.add("Hera");
        heroNames.add("Luna");
        heroNames.add("Maia");
        heroNames.add("Nesta");
        heroNames.add("Osa");
        heroNames.add("Osk");
        heroNames.add("Reina");
        heroNames.add("Palma");
        heroNames.add("Mavis");
        heroNames.add("Isolde");
        heroNames.add("Heather");
        heroNames.add("Hedwig");
        heroNames.add("Helche");
        heroNames.add("Tacey");
        heroNames.add("Trin");
        heroNames.add("Triona");
        heroNames.add("Selema");
        heroNames.add("Silna");
        heroNames.add("Zoe");
        heroNames.add("Yvette");
        heroNames.add("Sigrid");
        heroNames.add("Theda");
        heroNames.add("Sharada");
        heroNames.add("Silvia");
        heroNames.add("Mordecai");
        heroNames.add("Harold");
        heroNames.add("Swiftwind");
        heroNames.add("Hall");
        heroNames.add("Raven");
        heroNames.add("Hyala");
    }

    public Hero getHero(String heroName) {
        for (Hero nextHero : getHeroes()) {
            if (nextHero.getName().equalsIgnoreCase(heroName)) {
                return nextHero;
            }
        }
        return null;
    }

    public ObservableList<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(ObservableList<Hero> heroes) {
        this.heroes = heroes;
    }
    private final IntegerProperty strongholdBP = new SimpleIntegerProperty();

    public int getStrongholdBP() {
        return strongholdBP.get();
    }

    public void setStrongholdBP(int value) {
        strongholdBP.set(value);
    }

    public IntegerProperty strongholdBPProperty() {
        return strongholdBP;
    }
    private final IntegerProperty burganValeBP = new SimpleIntegerProperty();

    public int getBurganValeBP() {
        return burganValeBP.get();
    }

    public void setBurganValeBP(int value) {
        burganValeBP.set(value);
    }

    public IntegerProperty burganValeBPProperty() {
        return burganValeBP;
    }
    private final IntegerProperty crescentHoldBP = new SimpleIntegerProperty();

    public int getCrescentHoldBP() {
        return crescentHoldBP.get();
    }

    public void setCrescentHoldBP(int value) {
        crescentHoldBP.set(value);
    }

    public IntegerProperty crescentHoldBPProperty() {
        return crescentHoldBP;
    }
    private final IntegerProperty gravewoodBP = new SimpleIntegerProperty();

    public int getGravewoodBP() {
        return gravewoodBP.get();
    }

    public void setGravewoodBP(int value) {
        gravewoodBP.set(value);
    }

    public IntegerProperty gravewoodBPProperty() {
        return gravewoodBP;
    }
    private final IntegerProperty lilyManorBP = new SimpleIntegerProperty();

    public int getLilyManorBP() {
        return lilyManorBP.get();
    }

    public void setLilyManorBP(int value) {
        lilyManorBP.set(value);
    }

    public IntegerProperty lilyManorBPProperty() {
        return lilyManorBP;
    }
    private final IntegerProperty sunridersBP = new SimpleIntegerProperty();

    public int getSunridersBP() {
        return sunridersBP.get();
    }

    public void setSunridersBP(int value) {
        sunridersBP.set(value);
    }

    public IntegerProperty sunridersBPProperty() {
        return sunridersBP;
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

    /* 
     * This is called at the beginning of the seasonal upkeep phase.
     */
    public void harvestResources(Mission currentMission) {

        /* Update build points. */
        int currentBP = strongholdBP.get();
        int currentPop = getPopulation();
        currentBP = currentBP + (currentPop / 2);

        strongholdBP.set(currentBP);

        /* First harvest from all the regions. */
        for (int i = 0; i < getRegions().size(); i++) {
            Region nextRegion = getRegions().get(i);

            /* Check if region is under threat! */
            if (nextRegion.isThreat()) {
                continue;
            }

            food.set(nextRegion.getFood() + food.get());
            timber.set(nextRegion.getTimber() + timber.get());
            ore.set(nextRegion.getOre() + ore.get());
            mana.set(nextRegion.getMana() + mana.get());
            luxuries.set(nextRegion.getLuxuries() + luxuries.get());
        }

        /* Apply any trade agreements. */
        for (Iterator<Trade> it = activeTrades.iterator(); it.hasNext();) {
            Trade nextTrade = it.next();

            /* 
             If the trade agreement would make the Stronghold go
             negative in a resource, abort the trade and lower disposition.
             */
            if (getResourceAmount(nextTrade.getGiveResource())
                    < nextTrade.getGiveAmount()) {
                nextTrade.getReceiveFaction().setDisposition(nextTrade.getReceiveFaction().getDisposition() - 1);
                it.remove();
                continue;
            }

            updateResource(nextTrade.getReceiveFaction(),
                    nextTrade.getGiveResource(),
                    (nextTrade.getGiveAmount() * -1));
            updateResource(nextTrade.getReceiveFaction(),
                    nextTrade.getReceiveResource(),
                    nextTrade.getReceiveAmount());
            if (nextTrade.getSeasonsRemaining() == 1) {
                it.remove();
            } else {
                nextTrade.setSeasonsRemaining(nextTrade.getSeasonsRemaining() - 1);
            }
        }

    }

    /* Helper method for applying ongoing trade effects. */
    private void updateResource(MinorFaction faction,
            TradeResource tradeResource, int amount) {
        switch (tradeResource) {
            case Autarch_Stability:
                wotaModel.getAutarch().setStability(
                        wotaModel.getAutarch().getStability() - amount);
                break;
            case BP:
                if (faction.getName().equalsIgnoreCase("Burgan Vale")) {
                    this.burganValeBP.set(
                            this.burganValeBP.get() + amount);
                } else if (faction.getName().equalsIgnoreCase("Crescent Hold")) {
                    this.crescentHoldBP.set(
                            this.crescentHoldBP.get() + amount);
                } else if (faction.getName().equalsIgnoreCase("Lily Manor")) {
                    this.lilyManorBP.set(
                            this.lilyManorBP.get() + amount);
                } else if (faction.getName().equalsIgnoreCase("Gravewood")) {
                    this.gravewoodBP.set(
                            this.gravewoodBP.get() + amount);
                } else if (faction.getName().equalsIgnoreCase("Sunriders")) {
                    this.sunridersBP.set(
                            this.sunridersBP.get() + amount);
                }
                break;
            case Food:
                this.food.set(
                        this.food.get() + amount);
                break;
            case Luxuries:
                this.luxuries.set(
                        this.luxuries.get() + amount);
                break;
            case Mana:
                this.mana.set(
                        this.mana.get() + amount);
                break;
            case Ore:
                this.ore.set(
                        this.ore.get() + amount);
                break;
            case Timber:
                this.timber.set(
                        this.timber.get() + amount);
                break;
        }
    }

    private int getResourceAmount(TradeResource resource) {
        switch (resource) {
            case Food:
                return getFood();
            case Luxuries:
                return getLuxuries();
            case Mana:
                return getMana();
            case Ore:
                return getOre();
            case Timber:
                return getTimber();
            default:
                return 0;
        }
    }

    /* 
     * These are the developments the Stronghold is capable of purchasing
     * with currently available resources and developments.
     */
    private ObservableList<Development> availableDevelopments
            = FXCollections.observableArrayList();

    public ObservableList getAvailableDevelopments() {
        return availableDevelopments;
    }

    public void setAvailableDevelopments(ObservableList value) {
        this.availableDevelopments = value;
    }

    public ObservableList availableDevelopmentsProperty() {
        return availableDevelopments;
    }

    public void calculateAvailableDevelopments(
            HashMap<DevelopmentType, Development> devels) {
        availableDevelopments.clear();
        for (Development nextd : devels.values()) {

            /* Is it already there? */
            if (!nextd.isRegional() && hasDevelopment(nextd.getType())) {
                continue;
            }

            /* Pre-requisites. */
            if (!nextd.getPrerequisites().isEmpty()
                    && !hasPrerequisites(nextd)) {
                continue;
            }

            if (nextd.getTimber() > getTimber()) {
                continue;
            }
            if (nextd.getMana() > getMana()) {
                continue;
            }
            if (nextd.getOre() > getOre()) {
                continue;
            }
            if (nextd.getLuxury() > getLuxuries()) {
                continue;
            }

            int currentbp = strongholdBP.get();
            switch (nextd.getTree()) {
                case Arcane:
                    currentbp = currentbp + burganValeBP.get();
                    break;
                case Diplomacy:
                    currentbp = currentbp + lilyManorBP.get();
                    break;
                case Warfare:
                    currentbp = currentbp + sunridersBP.get();
                    break;
                case Infiltration:
                    currentbp = currentbp + gravewoodBP.get();
                    break;
                case Skirmish:
                    currentbp = currentbp + crescentHoldBP.get();
                    break;
            }

            if (nextd.getBp() > currentbp) {
                continue;
            }

            availableDevelopments.add(nextd);
        }
    }

    /* 
     * These are the developments the Stronghold is capable of purchasing
     * with currently available resources and developments.
     */
    private ObservableList<UnitCost> availableUnits
            = FXCollections.observableArrayList();

    public ObservableList getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(ObservableList value) {
        this.availableUnits = value;
    }

    public ObservableList availableUnitsProperty() {
        return availableUnits;
    }

    public void addOneUnit(UnitType unitType) {
        addUnits(unitType, 1);
    }

    public void calculateAvailableUnits(
            HashMap<UnitType, UnitCost> units) {
        availableUnits.clear();
        for (UnitCost nextu : units.values()) {
            /* Pre-requisites. */
            if (!nextu.getPrerequisites().isEmpty()
                    && !hasPrerequisites(nextu)) {
                continue;
            }

            if (nextu.getTimber() > getTimber()) {
                continue;
            }
            if (nextu.getMana() > getMana()) {
                continue;
            }
            if (nextu.getOre() > getOre()) {
                continue;
            }
            if (nextu.getLuxury() > getLuxuries()) {
                continue;
            }
            if (nextu.getFood() > getFood()) {
                continue;
            }

            int currentbp = strongholdBP.get();
            if (nextu.getUtype() == UnitType.Battle_Mages) {
                currentbp = currentbp + burganValeBP.get();
            } else {
                currentbp = currentbp + sunridersBP.get();
            }

            if (nextu.getBp() > currentbp) {
                continue;
            }

            availableUnits.add(nextu);
        }
    }

    public void addThreatRegion(Region newThreat) {
        newThreat.setThreat(true);
        threatRegions.add(newThreat);
    }

    public void removeThreatRegion(Region rmThreat) {
        rmThreat.setThreat(false);
        threatRegions.remove(rmThreat);
    }
    private ObservableList<Region> threatRegions
            = FXCollections.observableArrayList();

    public ObservableList getThreatRegions() {
        return threatRegions;
    }

    public void setThreatRegions(ObservableList value) {
        this.threatRegions = value;
    }

    public ObservableList<Region> threatRegionsProperty() {
        return threatRegions;
    }
    private ObservableList<Trade> activeTrades
            = FXCollections.observableArrayList();

    public ObservableList<Trade> getActiveTrades() {
        return activeTrades;
    }

    public void setActiveTrades(ObservableList<Trade> value) {
        this.activeTrades = value;
    }

    public ObservableList<Trade> activeTradesProperty() {
        return activeTrades;
    }

    /*
     * Heroes train or heal, heroes who went on the mission get XP,
     * the populace must be fed, and stability adjusted as appropriate.
     * 
     * The season is advanced as well.
     */
    public void endSeason(Mission currentMission) {

        /* Heroes gain experience. */
        for (Hero hero : heroes) {
            int currentXP = hero.getXp();
            if (currentMission.getHeroes().contains(hero)) {
                currentXP = currentXP + 2;
            } else {
                int consq = hero.getConsequences();
                if (consq > 0) {
                    consq = consq - 1;
                    hero.setConsequences(consq);
                } else {
                    currentXP = currentXP + 1;
                }
            }

            int currentRank = 8;
            if (currentXP < 6) {
                currentRank = 4;
            } else if (currentXP < 18) {
                currentRank = 5;
            } else if (currentXP < 39) {
                currentRank = 6;
            } else if (currentXP < 74) {
                currentRank = 7;
            }

            hero.setXp(currentXP);
            hero.setRank(currentRank);
            hero.setOnMission(false);
        }

        currentMission.getHeroes().clear();

        /* Advance the season forward. */
        wotaModel.getAutarch().setUnallocatedThreatPool(
                wotaModel.getAutarch().getUnallocatedThreatPool() + 1);
        int season = wotaModel.getSeason();
        int year = wotaModel.getYear();
        if ((season % 4) == 0) {
            season = 1;
            year = year + 1;

            /* 
             * Each faction with a stability above five and more food
             * than population adds one to population.
             */
            if (getFood() > getPopulation() && getStability() > 5) {
                setPopulation(getPopulation() + 1);
            }

        } else {
            season = season + 1;
        }
        wotaModel.setSeason(season);
        wotaModel.setYear(year);

        /* If food is too low, will take a stability hit. */
        if (getFood() < (getPopulation() / 2)) {
            setStability(getStability() - 2);
            setFood(0);
        } else if (getFood() < getPopulation()) {
            setStability(getStability() - 1);
            setFood(0);
        } else {
            setFood(getFood() - getPopulation());
        }

        /* Developments are no longer used. */
        for(Development development : getDevelopments()){
            development.setUsed(false);
        }
        
        /* Check on conditions - remove those which are expired. */
        for (Iterator<Condition> it = conditions.iterator(); it.hasNext();) {
            Condition condition = it.next();
            if (condition.getSeasons() <= 1) {
                switch (condition.getType()) {
                    case Trade_Modifier:
                        condition.getFaction().setTradeDifficulty(condition.getFaction().getTradeDifficulty()
                                - condition.getMagnitude());
                        break;
                    case Hero_Unavailable:
                        getHeroes().add(condition.getHero());
                        break;
                    case Development_Unavailable:
                        Development udev = condition.getDevelopment();
                        getDevelopments().add(udev);
                        if (udev.isRegional()) {
                            wotaModel.getRegion(udev.getRegion().getName()).getDevelopments().add(udev);
                        }
                        break;
                    case Region_Unavailable:
                        getRegions().add(condition.getRegion());
                        break;
                    case Sabotaged_Development:
                        Development sdev = condition.getDevelopment();
                        condition.getFaction().getDevelopments().add(sdev);
                        if (sdev.isRegional()) {
                            sdev.getRegion().getDevelopments().add(sdev);
                        }
                        break;
                    default:
                        break;
                }
                it.remove();
            } else {
                condition.setSeasons(condition.getSeasons() - 1);
            }
        }
    }

    private ObservableList<Condition> conditions
            = FXCollections.observableArrayList();

    public ObservableList<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(ObservableList<Condition> value) {
        this.conditions = value;
    }

    public ObservableList<Condition> conditionsProperty() {
        return conditions;
    }

    /* 
     The following methods help determine what threats the
     Stronghold is most vulnerable to.
     */
    public ConflictType bestConflictType() {
        double highestAvg
                = getAverageSkillForConflict(ConflictType.Warfare);
        ConflictType bestConflictType
                = ConflictType.Warfare;
        for (ConflictType conflictType : ConflictType.values()) {
            if (highestAvg < getAverageSkillForConflict(conflictType)) {
                highestAvg = getAverageSkillForConflict(conflictType);
                bestConflictType = conflictType;
            }
        }
        return bestConflictType;
    }

    public ConflictType worstConflictType() {
        double lowestAvg
                = getAverageSkillForConflict(ConflictType.Warfare);
        ConflictType worstConflictType
                = ConflictType.Warfare;
        for (ConflictType conflictType : ConflictType.values()) {
            if (lowestAvg > getAverageSkillForConflict(conflictType)) {
                lowestAvg = getAverageSkillForConflict(conflictType);
                worstConflictType = conflictType;
            }
        }
        return worstConflictType;
    }

    /* For now this is just randomly determined. */
    public Region getVulnerableRegion() {
        return getRegions().get(wotaModel.nextRandomInt(getRegions().size()));
    }
    
    public Hero getBestHero(){
        return getHeroes().get(wotaModel.nextRandomInt(getHeroes().size()));
    }
    
    public Development getBestDevelopment(){
        return getDevelopments().get(wotaModel.nextRandomInt(getDevelopments().size()));
    }

    /* Used in determining which threats minor factions should take. */
    public double getAverageSkillForConflict(ConflictType conflictType) {
        double avgSkill = 0.0;
        for (Hero hero : getHeroes()) {
            avgSkill = avgSkill + (double) hero.getSkill(conflictType);
        }
        avgSkill = avgSkill / getHeroes().size();

        /* 
         TO-DO: Add in rough factor for developments. 
         Every two developments of that type boost the average by one?
         */
        return avgSkill;
    }

}
