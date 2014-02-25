/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wota.strategic.ui.WotAStrategicModel;

/**
 * Holds details for seasonal threats determined at the end of the season.
 *
 * @author plewis
 */
public class SeasonalThreats {

    private final Random randNum;
    private final WotAStrategicModel wotaModel;

    public SeasonalThreats(WotAStrategicModel wotaModel) {
        this.wotaModel = wotaModel;
        randNum = new Random((new Date()).getTime());
    }

    /* 
     A global list of threats used at the end of every season,
     these are broken out into regional, diplomatic, and faction
     threats after possibly being dealt with via defensive developments.
     */
    private ObservableList<Threat> preSortedThreats
            = FXCollections.observableArrayList();

    public ObservableList<Threat> getPreSortedThreats() {
        return preSortedThreats;
    }

    public void setPreSortedThreats(ObservableList<Threat> value) {
        this.preSortedThreats = value;
    }

    public ObservableList<Threat> preSortedThreatsProperty() {
        return preSortedThreats;
    }

    /*
     * Threats which occur on regions.
     */
    private ObservableList<Threat> regionalThreats
            = FXCollections.observableArrayList();

    public ObservableList<Threat> getRegionalThreats() {
        return regionalThreats;
    }

    public void setRegionalThreats(ObservableList<Threat> value) {
        this.regionalThreats = value;
    }

    public ObservableList<Threat> regionalThreatsProperty() {
        return regionalThreats;
    }
    /*
     * Threats related to factions.
     */
    private ObservableList<Threat> diplomaticThreats
            = FXCollections.observableArrayList();

    public ObservableList<Threat> getDiplomaticThreats() {
        return diplomaticThreats;
    }

    public void setDiplomaticThreats(ObservableList<Threat> value) {
        this.diplomaticThreats = value;
    }

    public ObservableList<Threat> diplomaticThreatsProperty() {
        return diplomaticThreats;
    }
    /*
     * Major aggressive threats from factions.
     */
    private ObservableList<Threat> factionThreats
            = FXCollections.observableArrayList();

    public ObservableList<Threat> getFactionThreats() {
        return factionThreats;
    }

    public void setFactionThreats(ObservableList<Threat> value) {
        this.factionThreats = value;
    }

    public ObservableList<Threat> factionThreatsProperty() {
        return factionThreats;
    }

    /*
     * Factions which may create faction threats.
     */
    private ObservableList<MinorFaction> factionsWithThreats
            = FXCollections.observableArrayList();

    public ObservableList<MinorFaction> getFactionsWithThreats() {
        return factionsWithThreats;
    }

    public void setFactionsWithThreats(ObservableList<MinorFaction> value) {
        factionsWithThreats = value;
    }

    public ObservableList<MinorFaction> factionsWithThreatsProperty() {
        return factionsWithThreats;
    }

    public void clearThreats() {
        /* Also clear from list of regions. */
        getRegionalThreats().clear();
        for (int i = 0; i < wotaModel.getStronghold().getRegions().size(); i++) {
            wotaModel.getStronghold().getRegions().get(i).setThreat(false);
        }
        getDiplomaticThreats().clear();
        getFactionThreats().clear();
        getPreSortedThreats().clear();
        getFactionsWithThreats().clear();
    }

    /*
     * Determine if threats will occur for the season.
     */
    public void determineThreats() {

        clearThreats();

        /* Check all the regions the Stronghold has. */
        for (Region nextRegion : wotaModel.getStronghold().getRegions()) {
            boolean threat = false;
            if (randNum.nextInt(20) == 0) {
                threat = true;
            }
            if (threat) {
                /* Determine threat type, etc. */
                createNewRegionalThreat(nextRegion);
            }
        }

        /* Check for each of the five factions. */
        for (MinorFaction faction : wotaModel.getFactions()) {

            /* 
             * The Autarch and factions with negative stability don't
             * generate diplomatic threats, but may generate faction threats. 
             */
            if (faction.getName().equalsIgnoreCase("autarch")) {

                /* Determine if Autarch threats will occur. */
                if (faction.getStability() < 1
                        || wotaModel.getStronghold().getStability() < 1
                        || randNum.nextInt(8) <= wotaModel.getYear()) {
                    chooseWorstFactionThreat(faction);
                }
                continue;
            }

            /* 
             * Factions that don't like the Stronghold.
             */
            if (faction.getDisposition() < 0) {
                if (randNum.nextInt(8) <= Math.abs(faction.getDisposition())) {
                    chooseWorstFactionThreat(faction);
                }
                continue;
            }

            /* d20 method */
            boolean threat = false;
            if (randNum.nextInt(20) == 0) {
                threat = true;
            }

            if (threat) {
                createNewDiplomaticThreat(faction);
            }
        }

    }

    /*
     * Determine difficulty and details of regional threat
     * from regional threat table.
     */
    private void createNewRegionalThreat(Region region) {

        /* Roll a d6 - d6. */
        int roll = (randNum.nextInt(6) + 1) - (randNum.nextInt(6) + 1);
        ConflictType miniGame = ConflictType.Skirmish;
        int diff = 4;
        switch (roll) {
            case -5:
                diff = 5;
                miniGame = ConflictType.Warfare;
                break;
            case -4:
                diff = 5;
                miniGame = ConflictType.Diplomacy;
                break;
            case -3:
                diff = 4;
                miniGame = ConflictType.Diplomacy;
                break;
            case -2:
                diff = 3;
                miniGame = ConflictType.Skirmish;
                break;
            case -1:
                diff = 4;
                miniGame = ConflictType.Skirmish;
                break;
            case 0:
                diff = 5;
                miniGame = ConflictType.Skirmish;
                break;
            case 1:
                diff = 6;
                miniGame = ConflictType.Skirmish;
                break;
            case 2:
                diff = 7;
                miniGame = ConflictType.Skirmish;
                break;
            case 3:
                diff = 3;
                miniGame = ConflictType.Infiltration;
                break;
            case 4:
                diff = 4;
                miniGame = ConflictType.Infiltration;
                break;
            case 5:
                diff = 5;
                miniGame = ConflictType.Infiltration;
                break;
        }

        region.setThreat(true);
        Threat regionalThreat
                = new Threat(wotaModel.getThreatCost(ThreatType.Regional_Threat));
        regionalThreat.setDifficulty(diff);
        regionalThreat.setMinigame(miniGame);
        regionalThreat.setTargetRegion(region);
        getPreSortedThreats().add(regionalThreat);
    }

    /*
     * Create a new diplomatic threat from the diplomatic threat table.
     */
    private void createNewDiplomaticThreat(MinorFaction faction) {

        /* Roll a d6 - d6. */
        int roll = (randNum.nextInt(6) + 1) - (randNum.nextInt(6) + 1);
        ConflictType miniGame = ConflictType.Skirmish;
        int diff = 4;
        switch (roll) {
            case -5:
                diff = 5;
                miniGame = ConflictType.Warfare;
                break;
            case -4:
                diff = 5;
                miniGame = ConflictType.Skirmish;
                break;
            case -3:
                diff = 4;
                miniGame = ConflictType.Skirmish;
                break;
            case -2:
                diff = 3;
                miniGame = ConflictType.Diplomacy;
                break;
            case -1:
                diff = 4;
                miniGame = ConflictType.Diplomacy;
                break;
            case 0:
                diff = 5;
                miniGame = ConflictType.Diplomacy;
                break;
            case 1:
                diff = 6;
                miniGame = ConflictType.Diplomacy;
                break;
            case 2:
                diff = 7;
                miniGame = ConflictType.Diplomacy;
                break;
            case 3:
                diff = 3;
                miniGame = ConflictType.Infiltration;
                break;
            case 4:
                diff = 4;
                miniGame = ConflictType.Infiltration;
                break;
            case 5:
                diff = 5;
                miniGame = ConflictType.Infiltration;
                break;
        }

        Threat diplomaticThreat
                = new Threat(wotaModel.getThreatCost(ThreatType.Diplomatic_Threat));
        diplomaticThreat.setDifficulty(diff);
        diplomaticThreat.setMinigame(miniGame);
        diplomaticThreat.setTargetFaction(faction);
        getPreSortedThreats().add(diplomaticThreat);
    }

    /*
     Very simple logic for assigning threats to the Stronghold.
    
     TO-DO: Could make this more involved. Some sort of comparison of
     the numeric ability of the Stronghold in each conflict vs.
     the ability of the faction.
     */
    private void chooseWorstFactionThreat(MinorFaction faction) {

        /* 
         The difference between the faction's ability and the
         Stronghold's ability gives a rough idea of which threat to pick.
         */        
        ConflictType bestConflict = ConflictType.Skirmish;
        double bestDiff = 0.0;
        ConflictType worstConflict = ConflictType.Skirmish;
        double worstDiff = 1000.0;
        
        /* Find the best and worst conlifct types. */
        for(ConflictType conflict : ConflictType.values()){
            double diff = faction.getDifficultyByConflict(conflict)
                    - wotaModel.getStronghold().getAverageSkillForConflict(conflict);
            if(diff > bestDiff){
                bestDiff = diff;
                bestConflict = conflict;
            }
            if(diff < worstDiff){
                worstDiff = diff;
                worstConflict = conflict;
            }
        }

        /* 
         The Autarch needs an additional step to allocate pool points
         if necessary.
         */
        if (faction.getName().equalsIgnoreCase("autarch")) {
            allocateAutarchThreatPoints(faction, bestConflict);
            allocateAutarchThreatPoints(faction, worstConflict);
        }

        int chosenPool = faction.getPool(bestConflict);
        ThreatCost threat
                = wotaModel.getWorstThreatByType(bestConflict, chosenPool);
        if (threat != null) {
            createFactionThreat(faction, threat);
        }
    }

    /**
     * This method bumps the ability of the Autarch by one (which 
     * corresponds to increasing the pool by four).
     * 
     * @param autarch
     * @param conflict 
     */
    private void allocateAutarchThreatPoints(MinorFaction autarch,
            ConflictType conflict) {        
        int unallocated = autarch.getUnallocatedThreatPool();
        int currentPool = autarch.getPool(conflict);
        while(unallocated > 3 && currentPool < 12){
            int toNextThreat = 4 - (currentPool % 4);
            autarch.setUnallocatedThreatPool(autarch.
                    getUnallocatedThreatPool() - toNextThreat);
            autarch.increaseThreatPool(conflict, toNextThreat);
            unallocated = unallocated - toNextThreat;
            currentPool = currentPool + toNextThreat;
        }
    }

    /**
     * Adds the threat to the list, choosing target regions, heroes, and
     * developments as appropriate.
     *
     * @param threat
     */
    private void createFactionThreat(MinorFaction faction,
            ThreatCost threatCost) {

        /* Create the new threat to add. */
        Threat threat = new Threat(threatCost);
        threat.setDifficulty(faction.getDifficultyByConflict(ConflictType.Skirmish));
        threat.setMinorFaction(faction);

        switch (threat.getFactionThreatType()) {

            /* Diplomacy */
            /* Choose faction with highest disposition. */
            case Trade_Embargo:
                threat.setTargetFaction(getHighestFactionDisposition());
                break;
            /* Choose faction with lowest disposition. */
            case Targeted_Bribes:
                threat.setTargetFaction(getLowestFactionDisposition());
                break;
            case Strategic_Trade_Alliance:
                threat.setTargetFaction(getLowestFactionDisposition());
                break;

            /* Skirmish. */
            /* Choose best region. */
            case Sabotage_Supply:
                threat.setTargetRegion(wotaModel.
                        getStronghold().getVulnerableRegion());
                break;
            /* Nothing to do. */
            case Raid:
                break;
            /* Choose the highest skilled hero, prefer mages. */
            case Assassinate_Hero:
                threat.setTargetHero(wotaModel.
                        getStronghold().getBestHero());
                break;
            /* Infiltration. */
            case Minor_Extortion:
                threat.setTargetHero(wotaModel.
                        getStronghold().getBestHero());
                break;
            case Sabotage_Development:
                threat.setTargetDevelopment(wotaModel.
                        getStronghold().getBestDevelopment());
                break;
            case Propaganda_Campaign:
                /* Nothing. */
                break;
            case Limited_Strike:
                threat.setTargetRegion(wotaModel.
                        getStronghold().getVulnerableRegion());
                break;
            case Minor_Campaign:
                threat.setTargetRegion(wotaModel.
                        getStronghold().getVulnerableRegion());
                break;
            case Total_War:
                threat.setTargetRegion(wotaModel.
                        getStronghold().getVulnerableRegion());
                break;
        }

        getPreSortedThreats().add(threat);
    }

    private MinorFaction getHighestFactionDisposition() {
        MinorFaction faction = wotaModel.getFaction("Burgan Vale");
        for (MinorFaction candidate : wotaModel.getFactions()) {
            if (candidate.getName().equalsIgnoreCase("Autarch")) {
                continue;
            }
            if (candidate.getDisposition() > faction.getDisposition()) {
                faction = candidate;
            }
        }
        return faction;
    }

    private MinorFaction getLowestFactionDisposition() {
        MinorFaction faction = wotaModel.getFaction("Burgan Vale");
        for (MinorFaction candidate : wotaModel.getFactions()) {
            if (candidate.getName().equalsIgnoreCase("Autarch")) {
                continue;
            }
            if (candidate.getDisposition() < faction.getDisposition()) {
                faction = candidate;
            }
        }
        return faction;
    }

    /* Apply results from ignoring the threats, then clear them out. */
    public void applyIgnoredThreats(Stronghold stronghold) {
        for (Iterator<Threat> it = regionalThreats.iterator(); it.hasNext();) {
            Threat regionalThreat = it.next();
            if (regionalThreat.getName().equalsIgnoreCase("none")) {
                continue;
            }
            regionalThreat.getTargetRegion().setConsequences(regionalThreat.getTargetRegion().getConsequences() + 1);
            stronghold.setStability(stronghold.getStability() - 1);
        }

        regionalThreats.clear();

        for (Iterator<Threat> it = diplomaticThreats.iterator(); it.hasNext();) {
            Threat diplomaticThreat = it.next();
            if (diplomaticThreat.getName().equalsIgnoreCase("none")) {
                continue;
            }
            int dispositionLoss = 2;
            if (wotaModel.getStronghold().hasDevelopment(DevelopmentType.Center_of_Culture)) {
                dispositionLoss = 1;
            }
            diplomaticThreat.getTargetFaction().setDisposition(diplomaticThreat.getTargetFaction().getDisposition() - dispositionLoss);
        }

        diplomaticThreats.clear();

        /* Applying ignored conditions is the most complex part of threats. */
        for (Iterator<Threat> it = factionThreats.iterator(); it.hasNext();) {
            Threat factionThreat = it.next();
            if (factionThreat.getName().equalsIgnoreCase("none")) {
                continue;
            }
            Condition threatCondition = null;
            int dispositionLoss = 0;
            int stabilityLoss = 0;
            switch (factionThreat.getFactionThreatType()) {
                /* Diplomacy threats */
                case Trade_Embargo:
                    /* 
                     * Create a new condition that makes trade missions more
                     * difficult for one year.
                     */
                    threatCondition = new Condition(ConditionType.Trade_Modifier,
                            5);
                    threatCondition.setMagnitude(2);
                    threatCondition.setFaction(factionThreat.getTargetFaction());
                    stronghold.getConditions().add(threatCondition);
                    factionThreat.getTargetFaction().setTradeDifficulty(
                            factionThreat.getTargetFaction().getTradeDifficulty() + 2);
                    break;
                case Targeted_Bribes:
                    dispositionLoss = 2;
                    if (wotaModel.getStronghold().hasDevelopment(DevelopmentType.Center_of_Culture)) {
                        dispositionLoss = 1;
                    }
                    factionThreat.getTargetFaction().setDisposition(
                            factionThreat.getTargetFaction().getDisposition() - dispositionLoss);
                    break;
                case Strategic_Trade_Alliance:
                    dispositionLoss = 3;
                    if (wotaModel.getStronghold().hasDevelopment(DevelopmentType.Center_of_Culture)) {
                        dispositionLoss = 2;
                    }
                    factionThreat.getTargetFaction().setDisposition(
                            factionThreat.getTargetFaction().getDisposition() - dispositionLoss);
                    break;
                /* Infiltration threats */
                case Minor_Extortion:
                    threatCondition = new Condition(ConditionType.Hero_Unavailable,
                            5);
                    threatCondition.setHero(factionThreat.getTargetHero());
                    stronghold.getConditions().add(threatCondition);
                    stronghold.getHeroes().remove(factionThreat.getTargetHero());
                    break;
                case Sabotage_Development:
                    threatCondition = new Condition(ConditionType.Development_Unavailable,
                            5);
                    threatCondition.setDevelopment(factionThreat.getTargetDevelopment());
                    stronghold.getConditions().add(threatCondition);
                    stronghold.removeDevelopment(factionThreat.getTargetDevelopment());
                    break;
                case Propaganda_Campaign:
                    stabilityLoss = 2;
                    stronghold.setStability(stronghold.getStability() - stabilityLoss);
                    break;
                /* Skirmish threats */
                case Sabotage_Supply:
                    threatCondition = new Condition(ConditionType.Region_Unavailable,
                            5);
                    threatCondition.setRegion(factionThreat.getTargetRegion());
                    stronghold.getConditions().add(threatCondition);
                    stronghold.getRegions().remove(factionThreat.getTargetRegion());
                    break;
                case Raid:
                    stronghold.setPopulation(stronghold.getPopulation() - 1);
                    break;
                case Assassinate_Hero:
                    stronghold.getHeroes().remove(factionThreat.getTargetHero());
                    break;
                /* Warfare threats */
                case Limited_Strike:
                case Minor_Campaign:
                case Total_War:
                    factionThreat.getMinorFaction().getRegions().add(factionThreat.getTargetRegion());
                    stronghold.getRegions().remove(factionThreat.getTargetRegion());
                    stabilityLoss = 2;
                    stronghold.setStability(stronghold.getStability() - stabilityLoss);
                    break;
                default:
                    break;
            }
        }
        factionThreats.clear();
    }

    public void addFactionThreat(MinorFaction faction,
            Threat factionThreat, MinorFaction targetFaction,
            Region targetRegion, Hero targetHero, Development targetDevelopment) {
        Threat newThreat
                = new Threat(wotaModel.getThreatCost(factionThreat.getFactionThreatType()));
        newThreat.setMinorFaction(faction);
        switch (factionThreat.getMinigame()) {
            case Diplomacy:
                newThreat.setTargetFaction(targetFaction);
                switch (newThreat.getFactionThreatType()) {
                    case Trade_Embargo:
                        newThreat.setDifficulty(targetFaction.getTradeDifficulty());
                        break;
                    case Targeted_Bribes:
                        newThreat.setDifficulty(targetFaction.getTradeDifficulty() + 1);
                        break;
                    case Strategic_Trade_Alliance:
                        newThreat.setDifficulty(targetFaction.getTradeDifficulty() + 2);
                        break;
                    default:
                        break;
                }
                break;
            case Infiltration:
                switch (newThreat.getFactionThreatType()) {
                    case Minor_Extortion:
                        newThreat.setDifficulty(faction.getInfiltrationDifficulty());
                        newThreat.setTargetHero(targetHero);
                        break;
                    case Sabotage_Development:
                        newThreat.setDifficulty(faction.getInfiltrationDifficulty() + 1);
                        newThreat.setTargetDevelopment(targetDevelopment);
                        break;
                    case Propaganda_Campaign:
                        newThreat.setDifficulty(faction.getInfiltrationDifficulty() + 2);
                        break;
                    default:
                        break;
                }
                break;
            case Skirmish:
                switch (newThreat.getFactionThreatType()) {
                    case Sabotage_Supply:
                        newThreat.setTargetRegion(targetRegion);
                        newThreat.setDifficulty(faction.getSkirmishDifficulty());
                        break;
                    case Raid:
                        newThreat.setDifficulty(faction.getSkirmishDifficulty() + 1);
                        break;
                    case Assassinate_Hero:
                        newThreat.setDifficulty(faction.getSkirmishDifficulty() + 2);
                        newThreat.setTargetHero(targetHero);
                        break;
                    case Wrath_of_the_Autarch:
                        if (faction.getStability() < 1) {
                            newThreat.setDifficulty(faction.getSkirmishDifficulty());
                        } else {
                            newThreat.setDifficulty(8);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case Warfare:
                newThreat.setDifficulty(faction.getMilitaryDifficulty());
                newThreat.setTargetRegion(targetRegion);
                switch (newThreat.getFactionThreatType()) {
                    case Limited_Strike:
                        break;
                    case Minor_Campaign:
                        break;
                    case Total_War:
                        break;
                }
                break;
        }
        factionThreats.add(newThreat);
    }
}
