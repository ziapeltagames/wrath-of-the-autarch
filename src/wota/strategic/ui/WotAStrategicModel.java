/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.ui;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import wota.strategic.model.Autarch;
import wota.strategic.model.Condition;
import wota.strategic.model.ConditionType;
import wota.strategic.model.Development;
import wota.strategic.model.DevelopmentType;
import wota.strategic.model.EspionageMissions;
import wota.strategic.model.EspionageType;
import wota.strategic.model.Threat;
import wota.strategic.model.ThreatType;
import wota.strategic.model.Hero;
import wota.strategic.model.ConflictType;
import wota.strategic.model.DevelopmentFunction;
import wota.strategic.model.MinorFaction;
import wota.strategic.model.Mission;
import wota.strategic.model.OrderOfBattle;
import wota.strategic.model.Quest;
import wota.strategic.model.Region;
import wota.strategic.model.SabotageMissions;
import wota.strategic.model.SabotageType;
import wota.strategic.model.SeasonalThreats;
import wota.strategic.model.Stronghold;
import wota.strategic.model.TechTree;
import wota.strategic.model.ThreatCost;
import wota.strategic.model.Trade;
import wota.strategic.model.TradeEntry;
import wota.strategic.model.TradeResource;
import wota.strategic.model.UnitCost;
import wota.strategic.model.UnitType;
import wota.strategic.saveschema.Conditiontype;
import wota.strategic.saveschema.Developmenttype;
import wota.strategic.saveschema.Diplomaticthreattype;
import wota.strategic.saveschema.Factionthreattype;
import wota.strategic.saveschema.Factiontype;
import wota.strategic.saveschema.Herotype;
import wota.strategic.saveschema.Regionthreattype;
import wota.strategic.saveschema.Unittype;
import wota.strategic.saveschema.Regiontype;
import wota.strategic.saveschema.Strongholdtype;
import wota.strategic.saveschema.Tradetype;
import wota.strategic.saveschema.Wrathsavefile;

/**
 *
 * @author plewis
 */
public class WotAStrategicModel extends Application {

    private final ObservableList<ConflictType> miniGames
            = FXCollections.observableArrayList();
    private final ObservableList<EspionageMissions> espionageMissions
            = FXCollections.observableArrayList();
    private final ObservableList<SabotageMissions> sabotageMissions
            = FXCollections.observableArrayList();

    private HashMap<DevelopmentType, Development> developments;
    private HashMap<String, Region> regions;
    private HashMap<UnitType, UnitCost> units;
    private HashMap<ThreatType, ThreatCost> threatCosts;

    private final ObservableList<Quest> quests
            = FXCollections.observableArrayList();

    private Stronghold stronghold;
    private Autarch autarch;

    private final Random randNum
            = new Random((new Date()).getTime());

    private ObservableList<MinorFaction> factions
            = FXCollections.observableArrayList();
    private final ObservableList<Region> neutralRegions
            = FXCollections.observableArrayList();

    private File wotaSaveFile;

    /* 
     Due to a memory leak, instead of creating and 
     destroying the windows, all the windows will be created up front,
     and then hide/shown as necessary.
     */
    Stage missionResolutionStage;
    MissionResolutionController missionResolutionController;
    Stage seasonalUpkeepStage;
    SeasonalUpkeepController seasonalUpkeepController;
    Stage threatDeterminationStage;
    ThreatDeterminationController threatDeterminationController;

    /*
     * At the end of every season, used to track ignored and 
     * pressing seasonal threats.
     */
    private SeasonalThreats seasonalThreats;

    @Override
    public void start(Stage stage) throws Exception {

        /* Set up all the different minigames available. */
        miniGames.add(ConflictType.Diplomacy);
        miniGames.add(ConflictType.Infiltration);
        miniGames.add(ConflictType.Skirmish);
        miniGames.add(ConflictType.Warfare);

        /* Set up all the different factional threat options. */
        initializeFactionalThreats();
        initializeEspionageMissions();
        initializeSabotageMissions();

        seasonalThreats = new SeasonalThreats(this);
        this.season.set(1);
        this.year.set(1);

        initializeDevelopments();
        initializeUnits();
        initializeRegions();
        initializeFactions();
        initializeArtifacts();
        wotaSaveFile = null;

        initializeWindows();

        /* Set up the first scene. */
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TheRealm.fxml"));
        stage.setScene(new Scene((Pane) loader.load()));
        TheRealmController controller = loader.getController();
        stage.setTitle("Wrath of the Autarch Strategic Model");
        controller.setMain(this, stage);
        stage.show();
    }

    /* 
     Load data from load file, and set as default save file. 
     */
    public void loadGame(File loadFile) {
        wotaSaveFile = loadFile;
        loadGame();
    }

    /* 
     Save once on selection, then automatically each year thereafter.
     */
    public void setSaveFile(File saveFile) {
        wotaSaveFile = saveFile;
    }

    public boolean hasSaveFile() {
        return wotaSaveFile != null;
    }

    public int nextRandomInt(int bound) {
        return randNum.nextInt(bound);
    }

    private void initializeWindows() throws IOException {

        /* Mission resolution window. */
        missionResolutionStage = new Stage();
        FXMLLoader fxmlLoader
                = new FXMLLoader(getClass().getResource("MissionResolution.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        missionResolutionController = fxmlLoader.getController();
        missionResolutionController.setMain(this);
        Scene scene = new Scene(root);
        missionResolutionStage.setScene(scene);
        missionResolutionStage.setTitle("Mission Selection");

        /* Seasonal upkeep window. */
        seasonalUpkeepStage = new Stage();
        fxmlLoader
                = new FXMLLoader(getClass().getResource("SeasonalUpkeep.fxml"));
        root = (Parent) fxmlLoader.load();
        seasonalUpkeepController = fxmlLoader.getController();
        seasonalUpkeepController.setMain(this);
        scene = new Scene(root);
        seasonalUpkeepStage.setScene(scene);
        seasonalUpkeepStage.setTitle("Season Upkeep");

        /* Threat resolution window. */
        threatDeterminationStage = new Stage();
        fxmlLoader
                = new FXMLLoader(getClass().getResource("ThreatDetermination.fxml"));
        root = (Parent) fxmlLoader.load();
        threatDeterminationController = fxmlLoader.getController();
        threatDeterminationController.setMain(this);
        scene = new Scene(root);
        threatDeterminationStage.setScene(scene);
        threatDeterminationStage.setTitle("Determine Threats");
    }

    public void showMissionResolution() {
        missionResolutionController.newMission();
        missionResolutionStage.show();
    }

    public void hideMissionResolution() {
        missionResolutionStage.hide();
    }

    public void showSeasonalUpkeep() {
        seasonalUpkeepController.adjustSeasonalResources(missionResolutionController.getMission());
        seasonalUpkeepStage.show();
    }

    public void hideSeasonalUpkeep() {
        seasonalUpkeepStage.hide();
    }

    public void showThreatDetermination(Mission currentMission) {
        threatDeterminationController.determineThreats(currentMission);
        threatDeterminationStage.show();
    }

    public void hideThreatDetermination() {
        threatDeterminationStage.hide();
    }

    public boolean loadGame() {
        if (wotaSaveFile == null) {
            return false;
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Wrathsavefile.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Wrathsavefile wrathSaveFile
                    = (Wrathsavefile) jaxbUnmarshaller.unmarshal(wotaSaveFile);

            Strongholdtype strongholdtype = wrathSaveFile.getStronghold();

            /* Load the core stronghold attributes. */
            stronghold.setName(strongholdtype.getName());
            stronghold.setPopulation(strongholdtype.getPopulation().intValue());
            stronghold.setStability(strongholdtype.getStability().intValue());
            setSeason(strongholdtype.getSeason().intValue());
            setYear(strongholdtype.getYear().intValue());
            stronghold.setFood(strongholdtype.getInvestedFood().intValue());
            stronghold.setTimber(strongholdtype.getInvestedTimber().intValue());
            stronghold.setOre(strongholdtype.getInvestedOre().intValue());
            stronghold.setMana(strongholdtype.getInvestedMana().intValue());
            stronghold.setLuxuries(strongholdtype.getInvestedLuxuries().intValue());
            stronghold.setStrongholdBP(strongholdtype.getInvestedBP().intValue());
            stronghold.setBurganValeBP(strongholdtype.getBurganValeBP().intValue());
            stronghold.setCrescentHoldBP(strongholdtype.getCrescentHoldBP().intValue());
            stronghold.setGravewoodBP(strongholdtype.getGravewoodBP().intValue());
            stronghold.setLilyManorBP(strongholdtype.getLilyManorBP().intValue());
            stronghold.setSunridersBP(strongholdtype.getSunridersBP().intValue());

            /*
             Clear out the global hash of all the regions. As the regions
             are loaded into the Stronghold, etc, add them first to the global
             hash, then add them to the appropriate faction or the neutral
             region list.
             */
            regions.clear();

            /* Load the neutral regions. */
            neutralRegions.clear();
            for (Regiontype nextRegion : wrathSaveFile.getNeutralregion()) {
                Region region = loadRegion(nextRegion);
                regions.put(region.getName(), region);
                neutralRegions.add(regions.get(nextRegion.getName()));
            }

            /* Load the heroes, but first clear them all out. */
            stronghold.getHeroes().clear();
            for (Herotype inputHero : strongholdtype.getHero()) {
                stronghold.getHeroes().add(loadHero(inputHero));
            }

            /* Load the Stronghold regions. */
            stronghold.getRegions().clear();
            for (Regiontype nextRegion : strongholdtype.getRegion()) {
                Region region = loadRegion(nextRegion);
                region.getDevelopments().clear();
                regions.put(region.getName(), region);
                stronghold.getRegions().add(regions.get(region.getName()));
            }

            /* Load the developments. */
            stronghold.getDevelopments().clear();
            for (Developmenttype inputDevl : strongholdtype.getDevelopment()) {
                Development development
                        = createDevelopment(DevelopmentType.valueOf(inputDevl.getName()));
                development.setUsed(inputDevl.isUsed());
                if (development.isRegional()) {
                    Region devRegion = getRegion(inputDevl.getRegion());
                    development.setRegion(devRegion);
                    devRegion.getDevelopments().add(development);
                }
                stronghold.getDevelopments().add(development);
            }

            /* Load the units. */
            stronghold.getUnits().clear();
            for (Unittype inputUnit : strongholdtype.getUnit()) {
                OrderOfBattle unit
                        = new OrderOfBattle(UnitType.valueOf(inputUnit.getName()),
                                inputUnit.getAmount().intValue(), stronghold);
                stronghold.getUnits().add(unit);
            }

            /* Load the active trades. */
            stronghold.getActiveTrades().clear();
            for (Tradetype inputTrade : strongholdtype.getTrade()) {
                Trade trade = new Trade(TradeResource.valueOf(inputTrade.getGive()),
                        inputTrade.getGiveamount().intValue(),
                        TradeResource.valueOf(inputTrade.getReceive()),
                        inputTrade.getReceiveamount().intValue(),
                        getFaction(inputTrade.getFaction()),
                        inputTrade.getSeasons().intValue());
                stronghold.getActiveTrades().add(trade);
            }

            /* Load the factions. */
            for (Factiontype inputFaction : wrathSaveFile.getFaction()) {

                MinorFaction minorFaction = getFaction(inputFaction.getName());

                minorFaction.setDisposition(inputFaction.getDisposition().intValue());
                minorFaction.setStability(inputFaction.getStability().intValue());
                minorFaction.setPopulation(inputFaction.getPopulation().intValue());
                minorFaction.setUnallocatedThreatPool(inputFaction.getUnallocatedPool().intValue());
                minorFaction.setDiplomacyPool(inputFaction.getDiplomacyPool().intValue());
                minorFaction.setSkirmishPool(inputFaction.getSkirmishPool().intValue());
                minorFaction.setInfiltrationPool(inputFaction.getInfiltrationPool().intValue());
                minorFaction.setWarfarePool(inputFaction.getWarfarePool().intValue());

                /* Load the minor faction's regions. */
                minorFaction.getRegions().clear();
                for (Regiontype nextRegion : inputFaction.getRegion()) {
                    Region region = loadRegion(nextRegion);
                    region.getDevelopments().clear();
                    regions.put(region.getName(), region);
                    minorFaction.getRegions().add(regions.get(region.getName()));
                }

                /* Load the minor faction's developments. */
                minorFaction.getDevelopments().clear();
                for (Developmenttype inputDevl : inputFaction.getDevelopment()) {
                    Development development
                            = createDevelopment(DevelopmentType.valueOf(inputDevl.getName()));
                    development.setUsed(inputDevl.isUsed());
                    if (development.isRegional()) {
                        Region devRegion = getRegion(inputDevl.getRegion());
                        development.setRegion(devRegion);
                        devRegion.getDevelopments().add(development);
                    }
                    minorFaction.getDevelopments().add(development);
                }

                /* Load the minor faction's units. */
                minorFaction.getUnits().clear();
                for (Unittype inputUnit : inputFaction.getUnit()) {
                    OrderOfBattle unit
                            = new OrderOfBattle(UnitType.valueOf(inputUnit.getName()),
                                    inputUnit.getAmount().intValue(), minorFaction);
                    minorFaction.getUnits().add(unit);
                }

            }

            /* Load the conditions. */
            for (Conditiontype nextCondition : strongholdtype.getCondition()) {
                int seasons = nextCondition.getRemainingSeasons().intValue();
                switch (ConditionType.valueOf(nextCondition.getName())) {
                    case Development_Unavailable:
                        Development unavailableDev = createDevelopment(DevelopmentType.valueOf(nextCondition.getDevelopment()));
                        if (unavailableDev.isRegional()) {
                            unavailableDev.setRegion(getRegion(nextCondition.getRegion()));
                        }
                        Condition devUnavailable = new Condition(ConditionType.Development_Unavailable,
                                seasons);
                        devUnavailable.setDevelopment(unavailableDev);
                        stronghold.getConditions().add(devUnavailable);
                        break;
                    case Sabotaged_Development:
                        Development sabotagedDev = createDevelopment(DevelopmentType.valueOf(nextCondition.getDevelopment()));
                        if (sabotagedDev.isRegional()) {
                            sabotagedDev.setRegion(getRegion(nextCondition.getRegion()));
                        }
                        Condition devSabotaged = new Condition(ConditionType.Sabotaged_Development,
                                seasons);
                        devSabotaged.setDevelopment(sabotagedDev);
                        devSabotaged.setFaction(getFaction(nextCondition.getFaction()));
                        stronghold.getConditions().add(devSabotaged);
                        break;
                    case Hero_Unavailable:
                        Hero unavailableHero = loadHero(nextCondition.getHero());
                        Condition heroUnavailable = new Condition(ConditionType.Hero_Unavailable,
                                seasons);
                        heroUnavailable.setHero(unavailableHero);
                        stronghold.getConditions().add(heroUnavailable);
                        break;
                    case Region_Unavailable:
                        Region unavailableRegion = getRegion(nextCondition.getRegion());
                        Condition regionUnavailable = new Condition(ConditionType.Region_Unavailable,
                                seasons);
                        regionUnavailable.setRegion(unavailableRegion);
                        stronghold.getConditions().add(regionUnavailable);
                        break;
                    case Trade_Modifier:
                        MinorFaction targetFaction = getFaction(nextCondition.getFaction());
                        Condition threatCondition = new Condition(ConditionType.Trade_Modifier,
                                seasons);
                        threatCondition.setMagnitude(2);
                        threatCondition.setFaction(targetFaction);
                        stronghold.getConditions().add(threatCondition);
                        break;
                }
            }

            /* Load threats. */
            getSeasonalThreats().clearThreats();

            /* Load regional threats. */
            for (Regionthreattype regType : wrathSaveFile.getRegionalthreat()) {
                Threat threat = new Threat(getThreatCost(ThreatType.Regional_Threat));
                threat.setDifficulty(regType.getDifficulty().intValue());
                threat.setMinigame(ConflictType.valueOf(regType.getMinigame()));
                threat.setTargetRegion(getRegion(regType.getRegion()));
                getSeasonalThreats().getRegionalThreats().add(threat);
            }

            /* Load diplomatic threats. */
            for (Diplomaticthreattype regType : wrathSaveFile.getDiplomaticthreat()) {
                Threat threat = new Threat(getThreatCost(ThreatType.Diplomatic_Threat));
                threat.setDifficulty(regType.getDifficulty().intValue());
                threat.setMinigame(ConflictType.valueOf(regType.getMinigame()));
                threat.setTargetFaction(getFaction(regType.getFaction()));
                getSeasonalThreats().getDiplomaticThreats().add(threat);
            }

            /* Load faction threats. */
            for (Factionthreattype factionThreatType : wrathSaveFile.getFactionthreat()) {
                MinorFaction targetFaction = null;
                if (factionThreatType.getTargetfaction() != null
                        && factionThreatType.getTargetfaction().length() > 0) {
                    targetFaction = getFaction(factionThreatType.getTargetfaction());
                }
                Region targetRegion = null;
                if (factionThreatType.getTargetregion() != null
                        && factionThreatType.getTargetregion().length() > 0) {
                    targetRegion = getRegion(factionThreatType.getTargetregion());
                }
                Hero targetHero = null;
                if (factionThreatType.getTargethero() != null
                        && factionThreatType.getTargethero().length() > 0) {
                    for (Hero nextHero : getStronghold().getHeroes()) {
                        if (nextHero.getName().equalsIgnoreCase(factionThreatType.getTargethero())) {
                            targetHero = nextHero;
                        }
                    }
                }
                Development targetDevelopment = null;
                if (factionThreatType.getTargetdevelopment() != null
                        && factionThreatType.getTargetdevelopment().length() > 0) {
                    /* Get the listed development. */
                    for (Development dev : getStronghold().getDevelopments()) {
                        if (dev.getType().name().equalsIgnoreCase(factionThreatType.getTargetdevelopment())) {
                            if (dev.isRegional()) {
                                if (factionThreatType.getTargetregion().equalsIgnoreCase(dev.getRegion().getName())) {
                                    targetDevelopment = dev;
                                    break;
                                }
                            } else {
                                targetDevelopment = dev;
                                break;
                            }
                        }
                    }
                }
                Threat factionThreat
                        = new Threat(getThreatCost(ThreatType.valueOf(factionThreatType.getType())));
                factionThreat.setMinorFaction(getFaction(factionThreatType.getFaction()));
                factionThreat.setTargetDevelopment(targetDevelopment);
                factionThreat.setTargetFaction(targetFaction);
                factionThreat.setTargetHero(targetHero);
                factionThreat.setTargetRegion(targetRegion);
            }

        } catch (JAXBException exc) {
            System.err.println(exc);
            return false;
        }

        return true;
    }

    /* Helper method to create new regions from load file. */
    private Region loadRegion(Regiontype regType) {
        Region region = new Region(regType.getName(),
                regType.getFood().intValue(),
                regType.getTimber().intValue(),
                regType.getOre().intValue(),
                regType.getMana().intValue(),
                regType.getLuxuries().intValue(),
                regType.getPopulation().intValue(),
                null,
                ConflictType.valueOf(regType.getMinigame()),
                regType.getDifficulty().intValue());
        return region;
    }

    private Hero loadHero(Herotype inputHero) {
        Hero newHero = new Hero(inputHero.getName(),
                ConflictType.valueOf(inputHero.getMajorfocus()),
                ConflictType.valueOf(inputHero.getMinorfocus()),
                inputHero.isSpellcaster());
        if (inputHero.getRegion() != null) {
            newHero.setManaRegion(getRegion(inputHero.getRegion()));
        }
        newHero.setXp(inputHero.getXP().intValue());
        newHero.setRank(inputHero.getRank().intValue());
        newHero.setConsequences(inputHero.getConsequences().intValue());
        return newHero;
    }

    /* 
     If there's a current save file, save the state of the game at the
     end of every year.
     */
    public boolean saveGame() {
        if (wotaSaveFile == null) {
            return false;
        }
        try {
            Wrathsavefile wrathSaveFile = new Wrathsavefile();

            Strongholdtype strongholdtype = new Strongholdtype();

            strongholdtype.setName(stronghold.getName());
            strongholdtype.setPopulation(BigInteger.valueOf(stronghold.getPopulation()));
            strongholdtype.setStability(BigInteger.valueOf(stronghold.getStability()));
            strongholdtype.setSeason(BigInteger.valueOf(getSeason()));
            strongholdtype.setYear(BigInteger.valueOf(getYear()));
            strongholdtype.setInvestedFood(BigInteger.valueOf(stronghold.getFood()));
            strongholdtype.setInvestedTimber(BigInteger.valueOf(stronghold.getTimber()));
            strongholdtype.setInvestedOre(BigInteger.valueOf(stronghold.getOre()));
            strongholdtype.setInvestedMana(BigInteger.valueOf(stronghold.getMana()));
            strongholdtype.setInvestedLuxuries(BigInteger.valueOf(stronghold.getLuxuries()));
            strongholdtype.setInvestedBP(BigInteger.valueOf(stronghold.getStrongholdBP()));
            strongholdtype.setBurganValeBP(BigInteger.valueOf(stronghold.getBurganValeBP()));
            strongholdtype.setCrescentHoldBP(BigInteger.valueOf(stronghold.getCrescentHoldBP()));
            strongholdtype.setGravewoodBP(BigInteger.valueOf(stronghold.getGravewoodBP()));
            strongholdtype.setLilyManorBP(BigInteger.valueOf(stronghold.getLilyManorBP()));
            strongholdtype.setSunridersBP(BigInteger.valueOf(stronghold.getSunridersBP()));

            /* Save off the neutral regions. */
            for (Region nextRegion : getNeutralRegions()) {
                Regiontype regType = new Regiontype();
                regType.setName(nextRegion.getName());
                regType.setFood(BigInteger.valueOf(nextRegion.getFood()));
                regType.setTimber(BigInteger.valueOf(nextRegion.getTimber()));
                regType.setOre(BigInteger.valueOf(nextRegion.getOre()));
                regType.setMana(BigInteger.valueOf(nextRegion.getMana()));
                regType.setLuxuries(BigInteger.valueOf(nextRegion.getLuxuries()));
                regType.setPopulation(BigInteger.valueOf(nextRegion.getPopulation()));
                regType.setDifficulty(BigInteger.valueOf(nextRegion.getDifficulty()));
                regType.setMinigame(nextRegion.getMinigame().name());
                for (Development regDev : nextRegion.getDevelopments()) {
                    Developmenttype devlType = new Developmenttype();
                    devlType.setName(regDev.getType().name());
                    devlType.setRegion(nextRegion.getName());
                    devlType.setUsed(regDev.isUsed());
                    regType.getDevelopment().add(devlType);
                }
                wrathSaveFile.getNeutralregion().add(regType);
            }

            /* Save off the heroes. */
            for (Hero nextHero : stronghold.getHeroes()) {
                strongholdtype.getHero().add(saveHero(nextHero));
            }

            /* Save off the Stronghold's developments. */
            for (Development nextDevelopment : stronghold.getDevelopments()) {
                Developmenttype devlType = new Developmenttype();
                devlType.setName(nextDevelopment.getType().name());
                devlType.setUsed(devlType.isUsed());
                if (nextDevelopment.isRegional()) {
                    devlType.setRegion(nextDevelopment.getRegion().getName());
                }
                strongholdtype.getDevelopment().add(devlType);
            }

            /* Save off the units. */
            for (OrderOfBattle oob : stronghold.getUnits()) {
                Unittype unitType = new Unittype();
                unitType.setName(oob.getUtype().name());
                unitType.setAmount(BigInteger.valueOf(oob.getAvailable()));
                strongholdtype.getUnit().add(unitType);
            }

            /* Save off the Stronghold's regions. */
            for (Region nextRegion : stronghold.getRegions()) {
                Regiontype regType = new Regiontype();
                regType.setName(nextRegion.getName());
                regType.setFood(BigInteger.valueOf(nextRegion.getFood()));
                regType.setTimber(BigInteger.valueOf(nextRegion.getTimber()));
                regType.setOre(BigInteger.valueOf(nextRegion.getOre()));
                regType.setMana(BigInteger.valueOf(nextRegion.getMana()));
                regType.setLuxuries(BigInteger.valueOf(nextRegion.getLuxuries()));
                regType.setPopulation(BigInteger.valueOf(nextRegion.getPopulation()));
                regType.setDifficulty(BigInteger.valueOf(nextRegion.getDifficulty()));
                regType.setMinigame(nextRegion.getMinigame().name());
                strongholdtype.getRegion().add(regType);
            }

            /* Save off the trades. */
            for (Trade nextTrade : stronghold.getActiveTrades()) {
                Tradetype tradeType = new Tradetype();
                tradeType.setFaction(nextTrade.getReceiveFaction().getName());
                tradeType.setGive(nextTrade.getGiveResource().name());
                tradeType.setGiveamount(BigInteger.valueOf(nextTrade.getGiveAmount()));
                tradeType.setReceive(nextTrade.getReceiveResource().name());
                tradeType.setReceiveamount(BigInteger.valueOf(nextTrade.getReceiveAmount()));
                tradeType.setSeasons(BigInteger.valueOf(nextTrade.getSeasonsRemaining()));
                strongholdtype.getTrade().add(tradeType);
            }

            /* Save off the conditions. */
            for (Condition nextCond : stronghold.getConditions()) {
                Conditiontype condType = new Conditiontype();
                condType.setName(nextCond.getType().name());
                condType.setRemainingSeasons(BigInteger.valueOf(nextCond.getSeasons()));
                if (nextCond.getRegion() != null) {
                    condType.setRegion(nextCond.getRegion().getName());
                }
                if (nextCond.getHero() != null) {
                    condType.setHero(saveHero(nextCond.getHero()));
                }
                if (nextCond.getFaction() != null) {
                    condType.setFaction(nextCond.getFaction().getName());
                }
                if (nextCond.getDevelopment() != null) {
                    condType.setDevelopment(nextCond.getDevelopment().getType().name());
                    if (nextCond.getDevelopment().isRegional()) {
                        condType.setRegion(nextCond.getDevelopment().getRegion().getName());
                    }
                }
                strongholdtype.getCondition().add(condType);
            }

            /* Save off regional threats. */
            for (Threat regThreat : getSeasonalThreats().getRegionalThreats()) {
                if (regThreat.getName().equalsIgnoreCase("none")) {
                    continue;
                }
                Regionthreattype regType = new Regionthreattype();
                regType.setRegion(regThreat.getTargetRegion().getName());
                regType.setMinigame(regThreat.getMinigame().name());
                regType.setDifficulty(BigInteger.valueOf(regThreat.getDifficulty()));
                wrathSaveFile.getRegionalthreat().add(regType);
            }

            /* Save off diplomatic threats. */
            for (Threat dipThreat : getSeasonalThreats().getDiplomaticThreats()) {
                if (dipThreat.getName().equalsIgnoreCase("none")) {
                    continue;
                }
                Diplomaticthreattype regType = new Diplomaticthreattype();
                regType.setFaction(dipThreat.getTargetFaction().getName());
                regType.setMinigame(dipThreat.getMinigame().name());
                regType.setDifficulty(BigInteger.valueOf(dipThreat.getDifficulty()));
                wrathSaveFile.getDiplomaticthreat().add(regType);
            }

            /* Save off faction threats. */
            for (Threat regThreat : getSeasonalThreats().getFactionThreats()) {
                Factionthreattype regType = new Factionthreattype();
                regType.setFaction(regThreat.getMinorFaction().getName());
                regType.setType(regThreat.getFactionThreatType().name());

                if (regThreat.getTargetFaction() != null) {
                    regType.setTargetfaction(regThreat.getTargetFaction().getName());
                }
                if (regThreat.getTargetRegion() != null) {
                    regType.setTargetregion(regThreat.getTargetRegion().getName());
                }
                if (regThreat.getTargetDevelopment() != null) {
                    regType.setTargetdevelopment(regThreat.getTargetDevelopment().getType().name());
                    if (regThreat.getTargetDevelopment().isRegional()) {
                        regType.setTargetregion(regThreat.getTargetDevelopment().getRegion().getName());
                    }
                }
                if (regThreat.getTargetHero() != null) {
                    regType.setTargethero(regThreat.getTargetHero().getName());
                }
                wrathSaveFile.getFactionthreat().add(regType);
            }

            /* Save off the factions. */
            for (MinorFaction nextFaction : factions) {
                Factiontype factType = new Factiontype();
                factType.setName(nextFaction.getName());
                factType.setPopulation(BigInteger.valueOf(nextFaction.getPopulation()));
                factType.setStability(BigInteger.valueOf(nextFaction.getStability()));
                factType.setDisposition(BigInteger.valueOf(nextFaction.getDisposition()));
                factType.setUnallocatedPool(BigInteger.valueOf(nextFaction.getUnallocatedThreatPool()));
                factType.setSkirmishPool(BigInteger.valueOf(nextFaction.getSkirmishPool()));
                factType.setInfiltrationPool(BigInteger.valueOf(nextFaction.getInfiltrationPool()));
                factType.setDiplomacyPool(BigInteger.valueOf(nextFaction.getDiplomacyPool()));
                factType.setWarfarePool(BigInteger.valueOf(nextFaction.getWarfarePool()));

                /* Save off the faction's developments. */
                for (Development nextDevelopment : nextFaction.getDevelopments()) {
                    Developmenttype devlType = new Developmenttype();
                    devlType.setName(nextDevelopment.getType().name());
                    devlType.setUsed(nextDevelopment.isUsed());
                    if (nextDevelopment.isRegional()) {
                        devlType.setRegion(nextDevelopment.getRegion().getName());
                    }
                    factType.getDevelopment().add(devlType);
                }

                /* Save off the faction's units. */
                for (OrderOfBattle oob : nextFaction.getUnits()) {
                    Unittype unitType = new Unittype();
                    unitType.setName(oob.getUtype().name());
                    unitType.setAmount(BigInteger.valueOf(oob.getAvailable()));
                    factType.getUnit().add(unitType);
                }

                /* Save off the faction's regions. */
                for (Region nextRegion : nextFaction.getRegions()) {
                    Regiontype regType = new Regiontype();
                    regType.setName(nextRegion.getName());
                    regType.setFood(BigInteger.valueOf(nextRegion.getFood()));
                    regType.setTimber(BigInteger.valueOf(nextRegion.getTimber()));
                    regType.setOre(BigInteger.valueOf(nextRegion.getOre()));
                    regType.setMana(BigInteger.valueOf(nextRegion.getMana()));
                    regType.setLuxuries(BigInteger.valueOf(nextRegion.getLuxuries()));
                    regType.setPopulation(BigInteger.valueOf(nextRegion.getPopulation()));
                    regType.setDifficulty(BigInteger.valueOf(nextRegion.getDifficulty()));
                    regType.setMinigame(nextRegion.getMinigame().name());
                    factType.getRegion().add(regType);
                }

                wrathSaveFile.getFaction().add(factType);
            }

            wrathSaveFile.setStronghold(strongholdtype);

            JAXBContext jaxbContext = JAXBContext.newInstance(Wrathsavefile.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(wrathSaveFile, wotaSaveFile);
        } catch (JAXBException exception) {
            exception.toString();
            return false;
        }
        return true;
    }

    private Herotype saveHero(Hero nextHero) {
        Herotype heroType = new Herotype();
        heroType.setXP(BigInteger.valueOf(nextHero.getXp()));
        heroType.setRank(BigInteger.valueOf(nextHero.getRank()));
        heroType.setConsequences(BigInteger.valueOf(nextHero.getConsequences()));
        heroType.setName(nextHero.getName());
        heroType.setMajorfocus(nextHero.getMajorFocus().name());
        heroType.setMinorfocus(nextHero.getMinorFocus().name());
        heroType.setSpellcaster(nextHero.isSpellcaster());
        if (nextHero.getManaRegion() != null) {
            heroType.setRegion(nextHero.getManaRegion().getName());
        }
        return heroType;
    }

    private void initializeFactionalThreats() {

        threatCosts = new HashMap<>();

        threatCosts.put(ThreatType.Trade_Embargo,
                new ThreatCost(ThreatType.Trade_Embargo,
                        4, ConflictType.Diplomacy,
                        "Trade difficulty increased by two for one year"));
        threatCosts.put(ThreatType.Targeted_Bribes,
                new ThreatCost(ThreatType.Targeted_Bribes,
                        8, ConflictType.Diplomacy,
                        "-2 disposition with target faction"));
        threatCosts.put(ThreatType.Strategic_Trade_Alliance,
                new ThreatCost(ThreatType.Strategic_Trade_Alliance,
                        14, ConflictType.Diplomacy,
                        "-3 disposition with target faction"));
        threatCosts.put(ThreatType.Sabotage_Supply,
                new ThreatCost(ThreatType.Sabotage_Supply,
                        8, ConflictType.Skirmish,
                        "Region unavailable for four seasons"));
        threatCosts.put(ThreatType.Raid,
                new ThreatCost(ThreatType.Raid,
                        8, ConflictType.Skirmish,
                        "-1 population"));
        threatCosts.put(ThreatType.Assassinate_Hero,
                new ThreatCost(ThreatType.Assassinate_Hero,
                        14, ConflictType.Skirmish,
                        "Hero is killed"));
        threatCosts.put(ThreatType.Minor_Extortion,
                new ThreatCost(ThreatType.Minor_Extortion,
                        4, ConflictType.Infiltration,
                        "Targeted hero not available for four seasons"));
        threatCosts.put(ThreatType.Sabotage_Development,
                new ThreatCost(ThreatType.Sabotage_Development,
                        8, ConflictType.Infiltration,
                        "Development not available for four seasons"));
        threatCosts.put(ThreatType.Propaganda_Campaign,
                new ThreatCost(ThreatType.Propaganda_Campaign,
                        14, ConflictType.Infiltration,
                        "-2 stability"));
        threatCosts.put(ThreatType.Limited_Strike,
                new ThreatCost(ThreatType.Limited_Strike,
                        4, ConflictType.Warfare,
                        "Basic infantry, archers, cavalry"));
        threatCosts.put(ThreatType.Minor_Campaign,
                new ThreatCost(ThreatType.Minor_Campaign,
                        8, ConflictType.Warfare,
                        "Advanced forces, catapults"));
        threatCosts.put(ThreatType.Total_War,
                new ThreatCost(ThreatType.Total_War,
                        14, ConflictType.Warfare,
                        "All available units"));
        threatCosts.put(ThreatType.Wrath_of_the_Autarch,
                new ThreatCost(ThreatType.Wrath_of_the_Autarch,
                        1000, ConflictType.Skirmish,
                        "Autarch attacks"));
        threatCosts.put(ThreatType.Regional_Threat,
                new ThreatCost(ThreatType.Regional_Threat,
                        1000, ConflictType.Skirmish,
                        "Lose region for the season and -2 stability"));
        threatCosts.put(ThreatType.Diplomatic_Threat,
                new ThreatCost(ThreatType.Diplomatic_Threat,
                        1000, ConflictType.Diplomacy,
                        "-2 disposition"));
    }

    public ThreatCost getWorstThreatByType(ConflictType conflict,
            int pool) {
        ThreatCost worstThreat = null;
        for (ThreatCost threat : threatCosts.values()) {
            if (threat.getMinigame() == conflict) {
                if (threat.getPoolCost() <= pool) {
                    if (worstThreat == null
                            || worstThreat.getPoolCost() < threat.getPoolCost()) {
                        worstThreat = threat;
                    }
                }
            }
        }
        return worstThreat;
    }

    private void initializeEspionageMissions() {
        espionageMissions.add(new EspionageMissions(EspionageType.Tech_Secrets,
                "Tech and Arcane Secrets"));
        espionageMissions.add(new EspionageMissions(EspionageType.Strategic_Plans,
                "Strategic Plans"));
        espionageMissions.add(new EspionageMissions(EspionageType.Diplomatic_Secrets,
                "Diplomatic Secrets"));
    }

    private void initializeSabotageMissions() {
        sabotageMissions.add(new SabotageMissions(SabotageType.Foment_Rebellion,
                "Foment Rebellion"));
        sabotageMissions.add(new SabotageMissions(SabotageType.Target_Development,
                "Sabotage Development"));
    }

    /* 
     * Set up all the developments.
     */
    private void initializeDevelopments() {

        developments = new HashMap<>();
        ArrayList<Development> preq;

        /* Arcane */
        Development manForge
                = new Development(DevelopmentType.Mana_Forge,
                        "Mana Forge", TechTree.Arcane,
                        true, 6, 0, 6, 6, 5, 0, null,
                        "Channel one more mana in region",
                        DevelopmentFunction.Miscellaneous);
        developments.put(manForge.getType(), manForge);

        preq = new ArrayList<>();
        Development improvedCasting
                = new Development(DevelopmentType.Improved_Casting,
                        "Improved Casting", TechTree.Arcane,
                        false, 3, 0, 2, 0, 4, 0, preq,
                        "Spellcaster abilities increased by one",
                        DevelopmentFunction.Offense);
        developments.put(improvedCasting.getType(), improvedCasting);

        preq = new ArrayList<>();
        Development improvedChanneling
                = new Development(DevelopmentType.Improved_Channeling,
                        "Improved Channeling", TechTree.Arcane,
                        false, 4, 0, 0, 2, 4, 0, preq,
                        "Roll d8 - d6 for backlash checks",
                        DevelopmentFunction.Offense);
        developments.put(improvedChanneling.getType(), improvedChanneling);

        preq = new ArrayList<>();
        preq.add(improvedCasting);
        Development advancedCasting
                = new Development(DevelopmentType.Advanced_Casting,
                        "Advanced Casting", TechTree.Arcane,
                        false, 4, 0, 3, 0, 5, 0, preq,
                        "Spellcaster abilities increased by two",
                        DevelopmentFunction.Offense);
        developments.put(advancedCasting.getType(), advancedCasting);

        preq = new ArrayList<>();
        preq.add(improvedChanneling);
        Development advancedChanneling
                = new Development(DevelopmentType.Advanced_Channeling,
                        "Advanced Channeling", TechTree.Arcane,
                        false, 5, 0, 0, 3, 5, 0, preq,
                        "Roll d10 - d6 for backlash checks",
                        DevelopmentFunction.Offense);
        developments.put(advancedChanneling.getType(), advancedChanneling);

        preq = new ArrayList<>();
        preq.add(advancedCasting);
        preq.add(advancedChanneling);
        Development battleMages
                = new Development(DevelopmentType.Arcane_Academy,
                        "Arcane Academy", TechTree.Arcane,
                        false, 6, 0, 0, 0, 5, 0, preq,
                        "May recruit battle mages for war",
                        DevelopmentFunction.Offense);
        developments.put(battleMages.getType(), battleMages);

        preq = new ArrayList<>();
        preq.add(advancedCasting);
        Development expertCasting
                = new Development(DevelopmentType.Expert_Casting,
                        "Expert Casting", TechTree.Arcane,
                        false, 11, 0, 6, 2, 3, 0, preq,
                        "Spellcaster abilities increased by three",
                        DevelopmentFunction.Offense);
        developments.put(expertCasting.getType(), expertCasting);

        preq = new ArrayList<>();
        preq.add(advancedChanneling);
        Development expertChanneling
                = new Development(DevelopmentType.Expert_Channeling,
                        "Expert Channeling", TechTree.Arcane,
                        false, 6, 0, 0, 3, 3, 0, preq,
                        "Roll d12 - d6 for backlash checks",
                        DevelopmentFunction.Offense);
        developments.put(expertChanneling.getType(), expertChanneling);

        preq = new ArrayList<>();
        preq.add(expertCasting);
        preq.add(expertChanneling);
        Development magesGuild
                = new Development(DevelopmentType.Mages_Guild,
                        "Mages Guild", TechTree.Arcane,
                        false, 24, 0, 8, 9, 16, 2, preq,
                        "Spell abilities improved by one rank",
                        DevelopmentFunction.Offense);
        developments.put(magesGuild.getType(), magesGuild);

        /* Infiltration */
        preq = new ArrayList<>();
        Development superbSpies
                = new Development(DevelopmentType.Superb_Spies,
                        "Superb Spies", TechTree.Infiltration,
                        false, 6, 10, 4, 4, 0, 2, preq,
                        "Ignore one infiltration threat per year",
                        DevelopmentFunction.Defense);
        developments.put(superbSpies.getType(), superbSpies);

        preq = new ArrayList<>();
        preq.add(superbSpies);
        Development fantasticSpies
                = new Development(DevelopmentType.Fantastic_Spies,
                        "Fantastic Spies", TechTree.Infiltration,
                        false, 8, 12, 6, 6, 0, 4, preq,
                        "Ignore two infiltration threats per year",
                        DevelopmentFunction.Defense);
        developments.put(fantasticSpies.getType(), fantasticSpies);

        preq = new ArrayList<>();
        preq.add(fantasticSpies);
        Development epicSpies
                = new Development(DevelopmentType.Epic_Spies,
                        "Epic Spies", TechTree.Infiltration,
                        false, 10, 14, 6, 6, 0, 6, preq,
                        "Ignore three infiltration threats per year",
                        DevelopmentFunction.Defense);
        developments.put(epicSpies.getType(), epicSpies);

        preq = new ArrayList<>();
        Development impThievesTools
                = new Development(DevelopmentType.Improved_Thieves_Tools,
                        "Improved Thieves Tools", TechTree.Infiltration,
                        false, 6, 0, 3, 12, 0, 0, preq,
                        "Extra re-roll on infiltration missions",
                        DevelopmentFunction.Offense);
        developments.put(impThievesTools.getType(), impThievesTools);

        preq = new ArrayList<>();
        preq.add(impThievesTools);
        Development advThievesTools
                = new Development(DevelopmentType.Advanced_Thieves_Tools,
                        "Advanced Thieves Tools", TechTree.Infiltration,
                        false, 12, 0, 4, 18, 0, 4, preq,
                        "Extra re-roll on infiltration missions",
                        DevelopmentFunction.Offense);
        developments.put(advThievesTools.getType(), advThievesTools);

        preq = new ArrayList<>();
        preq.add(advThievesTools);
        Development expThievesTools
                = new Development(DevelopmentType.Expert_Thieves_Tools,
                        "Expert Thieves Tools", TechTree.Infiltration,
                        false, 14, 0, 8, 24, 0, 8, preq,
                        "Extra re-roll on infiltration missions",
                        DevelopmentFunction.Offense);
        developments.put(expThievesTools.getType(), expThievesTools);

        preq = new ArrayList<>();
        Development localContacts
                = new Development(DevelopmentType.Local_Contacts,
                        "Local Contacts", TechTree.Infiltration,
                        false, 14, 0, 2, 2, 0, 2, preq,
                        "Extra re-roll on infiltration missions",
                        DevelopmentFunction.Offense);
        developments.put(localContacts.getType(), localContacts);

        preq = new ArrayList<>();
        preq.add(localContacts);
        Development influentialContacts
                = new Development(DevelopmentType.Influential_Contacts,
                        "Influential Contacts", TechTree.Infiltration,
                        false, 16, 0, 4, 4, 0, 4, preq,
                        "Extra re-roll on infiltration missions",
                        DevelopmentFunction.Offense);
        developments.put(influentialContacts.getType(), influentialContacts);

        preq = new ArrayList<>();
        preq.add(influentialContacts);
        preq.add(advThievesTools);
        Development floorPlans
                = new Development(DevelopmentType.Floor_Plans,
                        "Floor Plans", TechTree.Infiltration,
                        false, 22, 0, 6, 6, 0, 8, preq,
                        "Extra re-roll on infiltration missions",
                        DevelopmentFunction.Offense);
        developments.put(floorPlans.getType(), floorPlans);

        /* Diplomacy */
        preq = new ArrayList<>();
        Development superDiplomats
                = new Development(DevelopmentType.Superb_Diplomats,
                        "Superb Diplomats", TechTree.Diplomacy,
                        false, 6, 10, 4, 4, 0, 2, preq,
                        "Ignore one diplomacy threat per year",
                        DevelopmentFunction.Defense);
        developments.put(superDiplomats.getType(), superDiplomats);

        preq = new ArrayList<>();
        preq.add(superDiplomats);
        Development fantasticDiplomats
                = new Development(DevelopmentType.Fantastic_Diplomats,
                        "Fantastic Diplomats", TechTree.Diplomacy,
                        false, 8, 12, 6, 6, 0, 4, preq,
                        "Ignore two diplomacy threats per year",
                        DevelopmentFunction.Defense);
        developments.put(fantasticDiplomats.getType(), fantasticDiplomats);

        preq = new ArrayList<>();
        preq.add(fantasticDiplomats);
        Development epicDiplomats
                = new Development(DevelopmentType.Epic_Diplomats,
                        "Epic Diplomats", TechTree.Diplomacy,
                        false, 10, 14, 6, 6, 0, 6, preq,
                        "Ignore three diplomacy threats per year",
                        DevelopmentFunction.Defense);
        developments.put(epicDiplomats.getType(), epicDiplomats);

        preq = new ArrayList<>();
        Development tradeGuild
                = new Development(DevelopmentType.Trade_Guild,
                        "Trade Guild", TechTree.Diplomacy,
                        false, 6, 0, 6, 6, 0, 3, preq,
                        "Extra re-roll on diplomacy missions",
                        DevelopmentFunction.Offense);
        developments.put(tradeGuild.getType(), tradeGuild);

        preq = new ArrayList<>();
        preq.add(tradeGuild);
        Development artsAndEntertainment
                = new Development(DevelopmentType.Arts_and_Entertainment,
                        "Arts and Entertainment", TechTree.Diplomacy,
                        false, 6, 0, 12, 9, 0, 4, preq,
                        "Extra re-roll on diplomacy missions",
                        DevelopmentFunction.Offense);
        developments.put(artsAndEntertainment.getType(), artsAndEntertainment);

        preq = new ArrayList<>();
        preq.add(artsAndEntertainment);
        Development centerOfCulture
                = new Development(DevelopmentType.Center_of_Culture,
                        "Center of Culture", TechTree.Diplomacy,
                        false, 8, 0, 18, 12, 0, 6, preq,
                        "Extra re-roll on diplomacy missions",
                        DevelopmentFunction.Offense);
        developments.put(centerOfCulture.getType(), centerOfCulture);

        preq = new ArrayList<>();
        preq.add(tradeGuild);
        Development tradeRelationships
                = new Development(DevelopmentType.Trade_Relationships,
                        "Trade Relationships", TechTree.Diplomacy,
                        false, 12, 0, 6, 6, 0, 6, preq,
                        "Extra re-roll on diplomacy missions",
                        DevelopmentFunction.Offense);
        developments.put(tradeRelationships.getType(), tradeRelationships);

        preq = new ArrayList<>();
        preq.add(tradeRelationships);
        Development tradeCapital
                = new Development(DevelopmentType.Trade_Capital,
                        "Trade Capital", TechTree.Diplomacy,
                        false, 16, 0, 8, 8, 0, 8, preq,
                        "Extra re-roll on diplomacy missions",
                        DevelopmentFunction.Offense);
        developments.put(tradeCapital.getType(), tradeCapital);

        preq = new ArrayList<>();
        Development festival
                = new Development(DevelopmentType.Festival,
                        "Festival", TechTree.Diplomacy,
                        false, 0, 0, 0, 0, 0, 0, preq,
                        "Add one stability",
                        DevelopmentFunction.Miscellaneous);
        developments.put(festival.getType(), festival);

        /* Skirmish */
        preq = new ArrayList<>();
        Development superbGuardForce
                = new Development(DevelopmentType.Superb_Guard_Force,
                        "Superb Guard Force", TechTree.Skirmish,
                        false, 6, 10, 4, 4, 0, 2, preq,
                        "Ignore one skirmish threat per year",
                        DevelopmentFunction.Defense);
        developments.put(superbGuardForce.getType(), superbGuardForce);

        preq = new ArrayList<>();
        preq.add(superbGuardForce);
        Development fantasticGuardForce
                = new Development(DevelopmentType.Fantastic_Guard_Force,
                        "Fantastic Guard Force", TechTree.Skirmish,
                        false, 8, 12, 6, 6, 0, 4, preq,
                        "Ignore two skirmish threats per year",
                        DevelopmentFunction.Defense);
        developments.put(fantasticGuardForce.getType(), fantasticGuardForce);

        preq = new ArrayList<>();
        preq.add(fantasticGuardForce);
        Development epicGuardForce
                = new Development(DevelopmentType.Epic_Guard_Force,
                        "Epic Guard Force", TechTree.Skirmish,
                        false, 10, 14, 6, 6, 0, 6, preq,
                        "Ignore three skirmish threats per year",
                        DevelopmentFunction.Defense);
        developments.put(epicGuardForce.getType(), epicGuardForce);

        preq = new ArrayList<>();
        Development impSwordSmith
                = new Development(DevelopmentType.Improved_Swordsmith,
                        "Improved Swordsmith", TechTree.Skirmish,
                        false, 8, 0, 10, 12, 0, 0, preq,
                        "Extra re-roll on skirmish missions",
                        DevelopmentFunction.Offense);
        developments.put(impSwordSmith.getType(), impSwordSmith);

        preq = new ArrayList<>();
        preq.add(impSwordSmith);
        Development improvedArmorer
                = new Development(DevelopmentType.Improved_Armorer,
                        "Improved Armorer", TechTree.Skirmish,
                        false, 10, 0, 12, 16, 0, 0, preq,
                        "Extra re-roll on skirmish missions",
                        DevelopmentFunction.Offense);
        developments.put(improvedArmorer.getType(), improvedArmorer);

        preq = new ArrayList<>();
        preq.add(improvedArmorer);
        Development advSwordSmith
                = new Development(DevelopmentType.Advanced_Swordsmith,
                        "Advanced Swordsmith", TechTree.Skirmish,
                        false, 12, 0, 14, 18, 0, 2, preq,
                        "Extra re-roll on skirmish missions",
                        DevelopmentFunction.Offense);
        developments.put(advSwordSmith.getType(), advSwordSmith);

        preq = new ArrayList<>();
        preq.add(advSwordSmith);
        Development advArmorer
                = new Development(DevelopmentType.Advanced_Armorer,
                        "Advanced Armorer", TechTree.Skirmish,
                        false, 14, 0, 16, 20, 0, 4, preq,
                        "Extra re-roll on skirmish missions",
                        DevelopmentFunction.Offense);
        developments.put(advArmorer.getType(), advArmorer);

        preq = new ArrayList<>();
        preq.add(impSwordSmith);
        Development improvedBowyer
                = new Development(DevelopmentType.Improved_Bowyer,
                        "Improved Bowyer", TechTree.Skirmish,
                        false, 10, 0, 24, 6, 0, 0, preq,
                        "Extra re-roll on skirmish missions",
                        DevelopmentFunction.Offense);
        developments.put(improvedBowyer.getType(), improvedBowyer);

        preq = new ArrayList<>();
        preq.add(improvedBowyer);
        Development advBowyer
                = new Development(DevelopmentType.Advanced_Bowyer,
                        "Advanced Bowyer", TechTree.Skirmish,
                        false, 10, 0, 30, 8, 0, 2, preq,
                        "Extra re-roll on skirmish missions",
                        DevelopmentFunction.Offense);
        developments.put(advBowyer.getType(), advBowyer);

        /* Warfare */
        preq = new ArrayList<>();
        Development barracks
                = new Development(DevelopmentType.Barracks,
                        "Barracks", TechTree.Warfare,
                        false, 6, 6, 6, 8, 0, 0, preq,
                        "May recruit infantry for war",
                        DevelopmentFunction.Defense);
        developments.put(barracks.getType(), barracks);

        preq = new ArrayList<>();
        preq.add(barracks);
        Development impBarracks
                = new Development(DevelopmentType.Improved_Barracks,
                        "Improved Barracks", TechTree.Warfare,
                        false, 8, 8, 6, 10, 0, 0, preq,
                        "Infantry roll d8 - d6 for offense and defense",
                        DevelopmentFunction.Defense);
        developments.put(impBarracks.getType(), impBarracks);

        preq = new ArrayList<>();
        preq.add(impBarracks);
        Development advancedBarracks
                = new Development(DevelopmentType.Advanced_Barracks,
                        "Advanced Barracks", TechTree.Warfare,
                        false, 10, 10, 6, 12, 0, 0, preq,
                        "Infantry roll d10 - d6 for offense and defense",
                        DevelopmentFunction.Defense);
        developments.put(advancedBarracks.getType(), advancedBarracks);

        preq = new ArrayList<>();
        preq.add(barracks);
        Development stables
                = new Development(DevelopmentType.Stables,
                        "Stables", TechTree.Warfare,
                        false, 12, 20, 22, 20, 0, 0, preq,
                        "May recruit cavalry",
                        DevelopmentFunction.Miscellaneous);
        developments.put(stables.getType(), stables);

        preq = new ArrayList<>();
        preq.add(barracks);
        Development fletcher
                = new Development(DevelopmentType.Fletcher,
                        "Fletcher", TechTree.Warfare,
                        false, 8, 6, 22, 2, 0, 2, preq,
                        "May recruit archers",
                        DevelopmentFunction.Miscellaneous);
        developments.put(fletcher.getType(), fletcher);

        preq = new ArrayList<>();
        preq.add(impBarracks);
        Development siegecraft
                = new Development(DevelopmentType.Siegecraft,
                        "Siegecraft", TechTree.Warfare,
                        false, 12, 0, 22, 4, 0, 0, preq,
                        "May construct catapults",
                        DevelopmentFunction.Miscellaneous);
        developments.put(siegecraft.getType(), siegecraft);

        preq = new ArrayList<>();
        preq.add(siegecraft);
        Development gunpowder
                = new Development(DevelopmentType.Gunpowder,
                        "Gunpowder", TechTree.Warfare,
                        false, 18, 0, 21, 36, 0, 0, preq,
                        "May construct cannons",
                        DevelopmentFunction.Miscellaneous);
        developments.put(gunpowder.getType(), gunpowder);

        preq = new ArrayList<>();
        Development keep
                = new Development(DevelopmentType.Keep,
                        "Keep", TechTree.Warfare,
                        true, 8, 0, 10, 14, 0, 0, preq,
                        "Region gains a keep",
                        DevelopmentFunction.Defense);
        developments.put(keep.getType(), keep);

        preq = new ArrayList<>();
        preq.add(keep);
        Development castle
                = new Development(DevelopmentType.Castle,
                        "Castle", TechTree.Warfare,
                        true, 20, 0, 60, 27, 0, 4, preq,
                        "Keep is upgraded to a castle",
                        DevelopmentFunction.Defense);
        developments.put(castle.getType(), castle);

        /* Add all the developments to the main list. */
        for (Development nextDevelopment : getDevelopments().values()) {
            getDevelopmentList().add(nextDevelopment);
        }

        for (TechTree nextDevType : TechTree.values()) {
            int dfood = 0;
            int dmana = 0;
            int dore = 0;
            int dtimber = 0;
            int dlux = 0;
            int dbp = 0;
            for (Development nextDevelopment : getDevelopments().values()) {
                if (nextDevelopment.getTree() == nextDevType) {
                    dfood += nextDevelopment.getFood();
                    dmana += nextDevelopment.getMana();
                    dore += nextDevelopment.getOre();
                    dtimber += nextDevelopment.getTimber();
                    dlux += nextDevelopment.getLuxury();
                    dbp += nextDevelopment.getBp();
                }
            }
            System.out.println("Development type: " + nextDevType);
            System.out.println("Food: " + dfood);
            System.out.println("Mana: " + dmana);
            System.out.println("Ore: " + dore);
            System.out.println("Timber: " + dtimber);
            System.out.println("Luxuries: " + dlux);
            System.out.println("BP: " + dbp);
        }

    }

    /* 
     * Set up all the developments.
     */
    private void initializeArtifacts() {

        ArrayList<Development> preq = new ArrayList<>();

        Development theddas
                = new Development(DevelopmentType.Theddas_Palimpsest,
                        "Thedda's Palimpsest", TechTree.Artifact,
                        false, 100, 100, 100, 100, 100, 100, preq,
                        "Draw one extra mana from regions for Channeling",
                        DevelopmentFunction.Miscellaneous);
        quests.add(new Quest("Quest for Thedda's Palimpsest",
                theddas, 7, ConflictType.Skirmish, getFaction("Burgan Vale")));

        Development arens
                = new Development(DevelopmentType.Arens_Forge,
                        "Aren's Forge", TechTree.Artifact,
                        false, 100, 100, 100, 100, 100, 100, preq,
                        "Keeps and Castles may attack in combat",
                        DevelopmentFunction.Defense);
        quests.add(new Quest("Quest for Aren's Forge",
                arens, 7, ConflictType.Skirmish, getFaction("Crescent Hold")));

        Development gossamer
                = new Development(DevelopmentType.Gossamer_Spirit,
                        "Gossamer Spirit", TechTree.Artifact,
                        false, 100, 100, 100, 100, 100, 100, preq,
                        "Bonuses to Infiltration missions",
                        DevelopmentFunction.Offense);
        quests.add(new Quest("Quest for the Gossamer Spirit",
                gossamer, 7, ConflictType.Infiltration, getFaction("Gravewood")));

        Development allure
                = new Development(DevelopmentType.Serum_of_Allure,
                        "Serum of Allure", TechTree.Artifact,
                        false, 100, 100, 100, 100, 100, 100, preq,
                        "Bonuses to Diplomacy missions",
                        DevelopmentFunction.Offense);
        quests.add(new Quest("Quest for the Serum of Allure",
                allure, 7, ConflictType.Infiltration, getFaction("Lily Manor")));

        Development pelakhar
                = new Development(DevelopmentType.Pelakhars_Loyalty,
                        "Pelakhar's Loyalty", TechTree.Artifact,
                        false, 100, 100, 100, 100, 100, 100, preq,
                        "Bonuses to Warfare missions",
                        DevelopmentFunction.Miscellaneous);
        quests.add(new Quest("Quest for Pelakhar's Loyalty",
                pelakhar, 7, ConflictType.Skirmish, getFaction("Sunriders")));

    }

    /*
     Create a hash of all the unit types.
     */
    private void initializeUnits() {

        units = new HashMap<>();

        ArrayList<Development> preq = new ArrayList<>();
        preq.add(developments.get(DevelopmentType.Barracks));
        UnitCost infantry
                = new UnitCost(UnitType.Infantry, 0, 0, 0, 2, 0, 0,
                        preq);
        units.put(UnitType.Infantry, infantry);
        preq = new ArrayList<>();
        preq.add(developments.get(DevelopmentType.Fletcher));
        UnitCost archer
                = new UnitCost(UnitType.Archer, 0, 0, 4, 0, 0, 0,
                        preq);
        units.put(UnitType.Archer, archer);
        preq = new ArrayList<>();
        preq.add(developments.get(DevelopmentType.Stables));
        UnitCost cavalry
                = new UnitCost(UnitType.Cavalry, 0, 0, 0, 4, 0, 0,
                        preq);
        units.put(UnitType.Cavalry, cavalry);
        UnitCost milita
                = new UnitCost(UnitType.Militia, 0, 0, 0, 1, 0, 0,
                        null);
        units.put(UnitType.Militia, milita);
        preq = new ArrayList<>();
        preq.add(developments.get(DevelopmentType.Arcane_Academy));
        UnitCost battleMages
                = new UnitCost(UnitType.Battle_Mages, 0, 0, 0, 0, 0, 5,
                        preq);
        units.put(UnitType.Battle_Mages, battleMages);
    }

    /*
     * Set up all the regions.
     */
    private void initializeRegions() {

        regions = new HashMap<>();

        /* Starting Regions */
        regions.put("Echo Lake",
                new Region("Echo Lake", 5, 2, 2, 0, 1, 0, stronghold,
                        ConflictType.Warfare, 0));
        regions.put("Tarrydale Farms",
                new Region("Tarrydale Farms", 5, 1, 2, 0, 0, 0, stronghold,
                        ConflictType.Warfare, 0));
        regions.put("Obsidian Wood",
                new Region("Obsidian Wood", 3, 2, 0, 2, 0, 0, stronghold,
                        ConflictType.Warfare, 0));

        /* Neutral Regions */
        regions.put("Boar's Hollow",
                new Region("Boar's Hollow", 6, 1, 1, 0, 0, 2, null,
                        ConflictType.Diplomacy, 5));
        neutralRegions.add(regions.get("Boar's Hollow"));
        regions.put("Cantlands",
                new Region("Cantlands", 5, 3, 3, 0, 0, 1, null,
                        ConflictType.Skirmish, 5));
        neutralRegions.add(regions.get("Cantlands"));
        regions.put("Dusk's Ayrie",
                new Region("Dusk's Ayrie", 7, 2, 2, 3, 3, 0, null,
                        ConflictType.Skirmish, 7));
        neutralRegions.add(regions.get("Dusk's Ayrie"));
        regions.put("Fennel Marshes",
                new Region("Fennel Marshes", 4, 2, 0, 1, 2, 1, null,
                        ConflictType.Skirmish, 5));
        neutralRegions.add(regions.get("Fennel Marshes"));
        regions.put("Ferry's Glenn",
                new Region("Ferry's Glenn", 8, 1, 1, 3, 1, 3, null,
                        ConflictType.Warfare, 6));
        neutralRegions.add(regions.get("Ferry's Glenn"));
        regions.put("Gray Forest",
                new Region("Gray Forest", 3, 3, 2, 0, 2, 0, null,
                        ConflictType.Skirmish, 5));
        neutralRegions.add(regions.get("Gray Forest"));
        regions.put("Green Vale",
                new Region("Green Vale", 1, 2, 1, 1, 0, 1, null,
                        ConflictType.Skirmish, 4));
        neutralRegions.add(regions.get("Green Vale"));
        regions.put("North Oaks",
                new Region("North Oaks", 1, 2, 0, 2, 1, 0, null,
                        ConflictType.Skirmish, 3));
        neutralRegions.add(regions.get("North Oaks"));
        regions.put("Sightrock",
                new Region("Sightrock", 4, 3, 3, 0, 0, 0, null,
                        ConflictType.Skirmish, 4));
        neutralRegions.add(regions.get("Sightrock"));
        regions.put("Sunset Isles",
                new Region("Sunset Isles", 4, 0, 3, 2, 0, 2, null,
                        ConflictType.Infiltration, 6));
        neutralRegions.add(regions.get("Sunset Isles"));
    }

    private void initializeFactions() {

        /* Stronghold */
        stronghold = new Stronghold(this);
        stronghold.getRegions().add(regions.get("Echo Lake"));
        stronghold.getRegions().add(regions.get("Tarrydale Farms"));
        stronghold.getRegions().add(regions.get("Obsidian Wood"));
        stronghold.addUnits(UnitType.Militia, stronghold.getPopulation());
        stronghold.createRandomHeroes(regions.get("Obsidian Wood"));

        /* A placeholder */
        Development dev;

        /* Autarch */
        autarch = new Autarch();

        autarch.getDevelopments().add(createDevelopment(DevelopmentType.Barracks));
        autarch.getDevelopments().add(createDevelopment(DevelopmentType.Fletcher));
        autarch.getDevelopments().add(createDevelopment(DevelopmentType.Stables));
        autarch.getDevelopments().add(createDevelopment(DevelopmentType.Advanced_Barracks));
        autarch.getDevelopments().add(createDevelopment(DevelopmentType.Improved_Barracks));
        autarch.getDevelopments().add(createDevelopment(DevelopmentType.Gunpowder));
        autarch.getDevelopments().add(createDevelopment(DevelopmentType.Siegecraft));
        autarch.getDevelopments().add(createDevelopment(DevelopmentType.Improved_Channeling));
        autarch.getDevelopments().add(createDevelopment(DevelopmentType.Expert_Channeling));

        autarch.addUnits(UnitType.Cavalry, 4);
        autarch.addUnits(UnitType.Infantry, 12);
        autarch.addUnits(UnitType.Archer, 10);
        autarch.addUnits(UnitType.Catapult, 2);
        autarch.addUnits(UnitType.Cannon, 2);

        regions.put("Southkeep",
                new Region("Southkeep", 4, 1, 1, 0, 0, 0, autarch,
                        ConflictType.Warfare, 0));
        dev = createDevelopment(DevelopmentType.Keep, regions.get("Southkeep"));
        regions.get("Southkeep").getDevelopments().add(dev);
        autarch.getDevelopments().add(dev);
        autarch.getRegions().add(regions.get("Southkeep"));

        regions.put("Guilder Farms",
                new Region("Guilder Farms", 7, 2, 0, 1, 0, 0, autarch,
                        ConflictType.Warfare, 0));
        dev = createDevelopment(DevelopmentType.Keep, regions.get("Guilder Farms"));
        regions.get("Guilder Farms").getDevelopments().add(dev);
        autarch.getDevelopments().add(dev);
        autarch.getRegions().add(regions.get("Guilder Farms"));

        regions.put("Dawncaves",
                new Region("Dawncaves", 2, 0, 3, 1, 0, 0, autarch,
                        ConflictType.Warfare, 0));
        dev = createDevelopment(DevelopmentType.Keep, regions.get("Dawncaves"));
        regions.get("Dawncaves").getDevelopments().add(dev);
        autarch.getDevelopments().add(dev);
        autarch.getRegions().add(regions.get("Dawncaves"));

        regions.put("Prominence",
                new Region("Prominence", 0, 2, 0, 0, 2, 0, autarch,
                        ConflictType.Warfare, 0));
        autarch.getRegions().add(regions.get("Prominence"));

        regions.put("Tremble Brooks",
                new Region("Tremble Brooks", 2, 1, 1, 0, 0, 0, autarch,
                        ConflictType.Warfare, 0));
        autarch.getRegions().add(regions.get("Tremble Brooks"));

        regions.put("Obelisk Ridge",
                new Region("Obelisk Ridge", 4, 0, 1, 2, 1, 0, autarch,
                        ConflictType.Warfare, 0));
        autarch.getRegions().add(regions.get("Obelisk Ridge"));

        regions.put("Yearly Field",
                new Region("Yearly Field", 7, 2, 1, 1, 0, 0, autarch,
                        ConflictType.Warfare, 0));
        dev = createDevelopment(DevelopmentType.Castle, regions.get("Yearly Field"));
        regions.get("Yearly Field").getDevelopments().add(dev);
        autarch.getDevelopments().add(dev);
        autarch.getRegions().add(regions.get("Yearly Field"));

        regions.put("Firstborn Peak",
                new Region("Firstborn Peak", 0, 2, 1, 2, 1, 0, autarch,
                        ConflictType.Warfare, 0));
        autarch.getRegions().add(regions.get("Firstborn Peak"));

        regions.put("Crimson Oaks",
                new Region("Crimson Oaks", 0, 3, 2, 0, 2, 0, autarch,
                        ConflictType.Warfare, 0));
        autarch.getRegions().add(regions.get("Crimson Oaks"));

        autarch.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Autarch_Stability, 1, 2));
        autarch.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Autarch_Stability, 2, 4));

        autarch.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 4, 1));
        autarch.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 8, 2));
        autarch.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 12, 3));
        autarch.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 16, 4));
        autarch.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 20, 5));

        factions.add(autarch);

        /* Burgan Vale */
        MinorFaction burganVale = new MinorFaction("Burgan Vale", 1, 10, 5,
                4, 3, 4, 0, 8, 14, 4, 0);

        burganVale.getDevelopments().add(createDevelopment(DevelopmentType.Barracks));
        burganVale.getDevelopments().add(createDevelopment(DevelopmentType.Fletcher));
        burganVale.getDevelopments().add(createDevelopment(DevelopmentType.Arcane_Academy));
        burganVale.getDevelopments().add(createDevelopment(DevelopmentType.Improved_Channeling));
        burganVale.getDevelopments().add(createDevelopment(DevelopmentType.Improved_Casting));
        burganVale.getDevelopments().add(createDevelopment(DevelopmentType.Advanced_Casting));
        burganVale.getDevelopments().add(createDevelopment(DevelopmentType.Advanced_Channeling));
        burganVale.getDevelopments().add(createDevelopment(DevelopmentType.Expert_Channeling));
        burganVale.getDevelopments().add(createDevelopment(DevelopmentType.Expert_Casting));
        burganVale.getDevelopments().add(createDevelopment(DevelopmentType.Mages_Guild));
        burganVale.getDevelopments().add(createDevelopment(DevelopmentType.Center_of_Culture));

        burganVale.addUnits(UnitType.Archer, 2);
        burganVale.addAlliedUnits(UnitType.Archer, 1);
        burganVale.addUnits(UnitType.Infantry, 4);
        burganVale.addAlliedUnits(UnitType.Infantry, 1);
        burganVale.addUnits(UnitType.Battle_Mages, 6);
        burganVale.addAlliedUnits(UnitType.Battle_Mages, 2);

        regions.put("Harrow's Glen",
                new Region("Harrow's Glen", 4, 2, 0, 3, 1, 0, burganVale,
                        ConflictType.Warfare, 0));
        dev = createDevelopment(DevelopmentType.Mana_Forge, regions.get("Harrow's Glen"));
        regions.get("Harrow's Glen").getDevelopments().add(dev);
        burganVale.getRegions().add(regions.get("Harrow's Glen"));
        burganVale.getDevelopments().add(dev);

        regions.put("The Feldmarch",
                new Region("The Feldmarch", 6, 0, 0, 2, 0, 0, burganVale,
                        ConflictType.Warfare, 0));
        burganVale.getRegions().add(regions.get("The Feldmarch"));

        regions.put("Blue Rock",
                new Region("Blue Rock", 3, 3, 2, 3, 0, 0, burganVale,
                        ConflictType.Warfare, 0));
        burganVale.getRegions().add(regions.get("Blue Rock"));

        burganVale.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Mana, 1, 1));
        burganVale.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Mana, 2, 2));
        burganVale.getGiveTradeChart().add(
                new TradeEntry(TradeResource.BP, 1, 2));
        burganVale.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Mana, 3, 3));
        burganVale.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Mana, 4, 4));
        burganVale.getGiveTradeChart().add(
                new TradeEntry(TradeResource.BP, 2, 4));
        burganVale.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Mana, 5, 5));

        burganVale.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Ore, 2, 1));
        burganVale.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Ore, 4, 2));
        burganVale.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Ore, 6, 3));
        burganVale.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Ore, 8, 4));
        burganVale.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Ore, 10, 5));

        factions.add(burganVale);

        /* Crescent Hold */
        MinorFaction crescentHold = new MinorFaction("Crescent Hold", 1, 8, 8,
                4, 5, 4, 0, 4, 0, 14, 8);

        crescentHold.getDevelopments().add(createDevelopment(DevelopmentType.Barracks));
        crescentHold.getDevelopments().add(createDevelopment(DevelopmentType.Fletcher));
        crescentHold.getDevelopments().add(createDevelopment(DevelopmentType.Improved_Barracks));
        crescentHold.getDevelopments().add(createDevelopment(DevelopmentType.Gunpowder));
        crescentHold.getDevelopments().add(createDevelopment(DevelopmentType.Siegecraft));

        crescentHold.addUnits(UnitType.Infantry, 9);
        crescentHold.addAlliedUnits(UnitType.Infantry, 3);
        crescentHold.addUnits(UnitType.Archer, 3);
        crescentHold.addAlliedUnits(UnitType.Archer, 1);

        regions.put("The Great Deeps",
                new Region("The Great Deeps", 0, 0, 3, 1, 1, 0, crescentHold,
                        ConflictType.Warfare, 0));
        dev = createDevelopment(DevelopmentType.Keep, regions.get("The Great Deeps"));
        crescentHold.getDevelopments().add(dev);
        regions.get("The Great Deeps").getDevelopments().add(dev);
        crescentHold.getRegions().add(regions.get("The Great Deeps"));

        regions.put("North Ridge",
                new Region("North Ridge", 7, 0, 2, 0, 1, 0, crescentHold,
                        ConflictType.Warfare, 0));
        dev = createDevelopment(DevelopmentType.Castle, regions.get("North Ridge"));
        crescentHold.getDevelopments().add(dev);
        regions.get("North Ridge").getDevelopments().add(dev);
        crescentHold.getRegions().add(regions.get("North Ridge"));

        regions.put("Upper Reach",
                new Region("Upper Reach", 4, 2, 1, 1, 1, 0, crescentHold,
                        ConflictType.Warfare, 0));
        dev = createDevelopment(DevelopmentType.Keep, regions.get("Upper Reach"));
        crescentHold.getDevelopments().add(dev);
        regions.get("Upper Reach").getDevelopments().add(dev);
        crescentHold.getRegions().add(regions.get("Upper Reach"));

        crescentHold.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Ore, 1, 1));
        crescentHold.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Ore, 2, 2));
        crescentHold.getGiveTradeChart().add(
                new TradeEntry(TradeResource.BP, 1, 2));
        crescentHold.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Ore, 3, 3));
        crescentHold.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Ore, 4, 4));
        crescentHold.getGiveTradeChart().add(
                new TradeEntry(TradeResource.BP, 2, 4));
        crescentHold.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Ore, 5, 5));

        crescentHold.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Timber, 2, 1));
        crescentHold.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Timber, 4, 2));
        crescentHold.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Timber, 6, 3));
        crescentHold.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Timber, 8, 4));
        crescentHold.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Timber, 10, 5));

        factions.add(crescentHold);

        /* Gravewood */
        MinorFaction gravewood = new MinorFaction("Gravewood", 1, 10, 7, 5,
                4, 6, 0, 0, 14, 8, 4);

        gravewood.getDevelopments().add(createDevelopment(DevelopmentType.Barracks));
        gravewood.getDevelopments().add(createDevelopment(DevelopmentType.Improved_Barracks));
        gravewood.getDevelopments().add(createDevelopment(DevelopmentType.Improved_Thieves_Tools));
        gravewood.getDevelopments().add(createDevelopment(DevelopmentType.Expert_Thieves_Tools));

        gravewood.addUnits(UnitType.Archer, 6);
        gravewood.addAlliedUnits(UnitType.Archer, 2);
        gravewood.addUnits(UnitType.Infantry, 5);
        gravewood.addAlliedUnits(UnitType.Infantry, 2);
        gravewood.addUnits(UnitType.Militia, 2);

        regions.put("Gravewood",
                new Region("Gravewood", 6, 3, 0, 2, 0, 0, gravewood,
                        ConflictType.Warfare, 0));
        gravewood.getRegions().add(regions.get("Gravewood"));
        regions.put("Spinner Lakes",
                new Region("Spinner Lakes", 5, 2, 1, 0, 0, 0, gravewood,
                        ConflictType.Warfare, 0));
        gravewood.getRegions().add(regions.get("Spinner Lakes"));
        regions.put("Maplewood",
                new Region("Maplewood", 2, 2, 2, 1, 1, 0, gravewood,
                        ConflictType.Warfare, 0));
        gravewood.getRegions().add(regions.get("Maplewood"));

        gravewood.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Timber, 2, 1));
        gravewood.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Timber, 4, 2));
        gravewood.getGiveTradeChart().add(
                new TradeEntry(TradeResource.BP, 1, 2));
        gravewood.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Timber, 6, 3));
        gravewood.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Timber, 8, 4));
        gravewood.getGiveTradeChart().add(
                new TradeEntry(TradeResource.BP, 2, 4));
        gravewood.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Timber, 10, 5));

        gravewood.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 1, 1));
        gravewood.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 2, 2));
        gravewood.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 3, 3));
        gravewood.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 4, 4));
        gravewood.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 5, 5));

        factions.add(gravewood);

        /* Sunriders */
        MinorFaction sunriders = new MinorFaction("Sunriders", 1, 5, 15,
                3, 6, 3, 0, 0, 4, 8, 14);

        sunriders.getDevelopments().add(createDevelopment(DevelopmentType.Barracks));
        sunriders.getDevelopments().add(createDevelopment(DevelopmentType.Fletcher));
        sunriders.getDevelopments().add(createDevelopment(DevelopmentType.Stables));
        sunriders.getDevelopments().add(createDevelopment(DevelopmentType.Improved_Barracks));
        sunriders.getDevelopments().add(createDevelopment(DevelopmentType.Advanced_Barracks));

        sunriders.addUnits(UnitType.Horse_Archers, 8);
        sunriders.addAlliedUnits(UnitType.Horse_Archers, 3);
        sunriders.addUnits(UnitType.Archer, 4);
        sunriders.addAlliedUnits(UnitType.Archer, 1);

        regions.put("Horsethief Meadows",
                new Region("Horsethief Meadows", 4, 2, 1, 0, 0, 0, sunriders,
                        ConflictType.Warfare, 0));
        sunriders.getRegions().add(regions.get("Horsethief Meadows"));
        regions.put("Millet Plain",
                new Region("Millet Plain", 7, 2, 0, 0, 0, 0, sunriders,
                        ConflictType.Warfare, 0));
        sunriders.getRegions().add(regions.get("Millet Plain"));
        regions.put("Song Bluff",
                new Region("Song Bluff", 3, 1, 0, 2, 0, 0, sunriders,
                        ConflictType.Warfare, 0));
        sunriders.getRegions().add(regions.get("Song Bluff"));
        regions.put("Arrow Lake",
                new Region("Arrow Lake", 2, 1, 3, 0, 1, 0, sunriders,
                        ConflictType.Warfare, 0));
        sunriders.getRegions().add(regions.get("Arrow Lake"));
        regions.put("Boar's Peak",
                new Region("Boar's Peak", 2, 3, 1, 0, 1, 0, sunriders,
                        ConflictType.Warfare, 0));
        sunriders.getRegions().add(regions.get("Boar's Peak"));

        sunriders.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Food, 4, 1));
        sunriders.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Food, 8, 2));
        sunriders.getGiveTradeChart().add(
                new TradeEntry(TradeResource.BP, 1, 2));
        sunriders.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Food, 12, 3));
        sunriders.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Food, 16, 4));
        sunriders.getGiveTradeChart().add(
                new TradeEntry(TradeResource.BP, 2, 4));
        sunriders.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Food, 20, 5));

        sunriders.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Mana, 1, 1));
        sunriders.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Mana, 2, 2));
        sunriders.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Mana, 3, 3));
        sunriders.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Mana, 4, 4));
        sunriders.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Mana, 5, 5));

        factions.add(sunriders);

        /* Lily Manor */
        MinorFaction lilyManor = new MinorFaction("Lily Manor", 1, 13, 5,
                6, 3, 5, 0, 14, 8, 4, 0);

        lilyManor.getDevelopments().add(createDevelopment(DevelopmentType.Barracks));
        lilyManor.getDevelopments().add(createDevelopment(DevelopmentType.Fletcher));
        lilyManor.getDevelopments().add(createDevelopment(DevelopmentType.Improved_Barracks));
        lilyManor.getDevelopments().add(createDevelopment(DevelopmentType.Epic_Diplomats));
        lilyManor.getDevelopments().add(createDevelopment(DevelopmentType.Center_of_Culture));
        lilyManor.getDevelopments().add(createDevelopment(DevelopmentType.Center_of_Culture));
        lilyManor.getDevelopments().add(createDevelopment(DevelopmentType.Trade_Capital));

        lilyManor.addUnits(UnitType.Archer, 5);
        lilyManor.addAlliedUnits(UnitType.Archer, 2);
        lilyManor.addUnits(UnitType.Infantry, 5);
        lilyManor.addAlliedUnits(UnitType.Infantry, 2);
        lilyManor.addUnits(UnitType.Militia, 5);

        regions.put("Caster's Edge",
                new Region("Caster's Edge", 2, 3, 1, 3, 3, 0, lilyManor,
                        ConflictType.Warfare, 0));
        dev = createDevelopment(DevelopmentType.Castle, regions.get("Caster's Edge"));
        lilyManor.getDevelopments().add(dev);
        regions.get("Caster's Edge").getDevelopments().add(dev);
        lilyManor.getRegions().add(regions.get("Caster's Edge"));

        regions.put("Nalah Farms",
                new Region("Nalah Famrs", 3, 1, 0, 0, 1, 0, lilyManor,
                        ConflictType.Warfare, 0));
        lilyManor.getRegions().add(regions.get("Nalah Farms"));

        regions.put("Goldspike",
                new Region("Goldspike", 2, 2, 2, 0, 1, 0, lilyManor,
                        ConflictType.Warfare, 0));
        dev = createDevelopment(DevelopmentType.Keep, regions.get("Goldspike"));
        lilyManor.getDevelopments().add(dev);
        regions.get("Goldspike").getDevelopments().add(dev);
        lilyManor.getRegions().add(regions.get("Goldspike"));

        lilyManor.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 1, 1));
        lilyManor.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 2, 2));
        lilyManor.getGiveTradeChart().add(
                new TradeEntry(TradeResource.BP, 1, 2));
        lilyManor.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 3, 3));
        lilyManor.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 4, 4));
        lilyManor.getGiveTradeChart().add(
                new TradeEntry(TradeResource.BP, 2, 4));
        lilyManor.getGiveTradeChart().add(
                new TradeEntry(TradeResource.Luxuries, 5, 5));

        lilyManor.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Food, 4, 1));
        lilyManor.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Food, 8, 2));
        lilyManor.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Food, 12, 3));
        lilyManor.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Food, 16, 4));
        lilyManor.getReceiveTradeChart().add(
                new TradeEntry(TradeResource.Food, 20, 5));

        factions.add(lilyManor);

        /* Set deployment levels and clear any state. */
        clearState();
    }

    public MinorFaction getFaction(String factionName) {
        for (MinorFaction faction : factions) {
            if (faction.getName().equalsIgnoreCase(factionName)) {
                return faction;
            }
        }
        return null;
    }

    public ObservableList<MinorFaction> getFactions() {
        return factions;
    }

    public void setFactions(ObservableList<MinorFaction> minorFactions) {
        this.factions = minorFactions;
    }

    public ObservableList<MinorFaction> factionProperty() {
        return factions;
    }

    public Stronghold getStronghold() {
        return stronghold;
    }

    public Autarch getAutarch() {
        return autarch;
    }

    public SeasonalThreats getSeasonalThreats() {
        return seasonalThreats;
    }

    public ObservableList<Hero> getHeroes() {
        return stronghold.getHeroes();
    }

    public ThreatCost getThreatCost(ThreatType type) {
        return threatCosts.get(type);
    }

    public ObservableList<EspionageMissions> getEspionageMissions() {
        return espionageMissions;
    }

    public ObservableList<SabotageMissions> getSabotageMissions() {
        return sabotageMissions;
    }

    public ObservableList<ConflictType> getMiniGames() {
        return miniGames;
    }

    public ObservableList<Region> getNeutralRegions() {
        return neutralRegions;
    }

    public Region getRegion(String regionName) {
        return regions.get(regionName);
    }

    public ObservableList<Quest> getQuests() {
        return quests;
    }

    /* 
     Used by the main controller to show all the developments in the game.
     */
    private ObservableList<Development> developmentList
            = FXCollections.observableArrayList();

    public ObservableList getDevelopmentList() {
        return developmentList;
    }

    public void setDevelopmentList(ObservableList value) {
        developmentList = value;
    }

    public ObservableList developmentListProperty() {
        return developmentList;
    }

    public HashMap<DevelopmentType, Development> getDevelopments() {
        return developments;
    }

    /* Creates a new regional development. */
    public Development createDevelopment(DevelopmentType type,
            Region region) {
        Development dev = createDevelopment(type);
        dev.setRegion(region);
        return dev;
    }

    /* Creates a new development of the appropriate type. */
    public Development createDevelopment(DevelopmentType type) {
        Development baseDev = getDevelopments().get(type);
        Development dev = new Development(type,
                baseDev.getName(), baseDev.getTree(), baseDev.isRegional(),
                baseDev.getBp(), baseDev.getFood(), baseDev.getTimber(),
                baseDev.getOre(), baseDev.getMana(), baseDev.getLuxury(),
                baseDev.getPrerequisites(), baseDev.getDescription(),
                baseDev.getFunction());
        return dev;
    }

    public HashMap<UnitType, UnitCost> getUnits() {
        return units;
    }
    private final IntegerProperty season = new SimpleIntegerProperty();

    public int getSeason() {
        return season.get();
    }

    public void setSeason(int value) {
        season.set(value);
    }

    public IntegerProperty seasonProperty() {
        return season;
    }
    private final IntegerProperty year = new SimpleIntegerProperty();

    public int getYear() {
        return year.get();
    }

    public void setYear(int value) {
        year.set(value);
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    /*
     * Clear out all the state that accumulates during missions.
     */
    public void clearState() {
        stronghold.clearState();
        autarch.clearState();
        factions.stream().forEach((faction) -> {
            faction.clearState();
        });
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
