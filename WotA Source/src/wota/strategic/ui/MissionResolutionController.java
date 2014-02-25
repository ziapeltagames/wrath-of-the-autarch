/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.ui;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import wota.strategic.model.Condition;
import wota.strategic.model.ConditionType;
import wota.strategic.model.Development;
import wota.strategic.model.DevelopmentType;
import wota.strategic.model.EspionageMissions;
import wota.strategic.model.EspionageType;
import wota.strategic.model.Threat;
import wota.strategic.model.Hero;
import wota.strategic.model.OrderOfBattle;
import wota.strategic.model.ConflictType;
import wota.strategic.model.MinorFaction;
import wota.strategic.model.Mission;
import wota.strategic.model.MissionType;
import wota.strategic.model.Quest;
import wota.strategic.model.Region;
import wota.strategic.model.SabotageMissions;
import wota.strategic.model.SabotageType;
import wota.strategic.model.TechTree;
import wota.strategic.model.Trade;
import wota.strategic.model.TradeEntry;
import wota.strategic.model.TradeResource;

/**
 * FXML Controller class
 *
 * @author plewis
 */
public class MissionResolutionController implements Initializable {

    /*
     * Reference to global model, used throughout the controller.
     */
    private WotAStrategicModel wotaModel;
    /* 
     * This is passed along to the upkeep phase to track all the various
     * changes in state that the Stronghold incurs from the current mission.
     */
    private Mission currentMission;

    /* 
     * General UI Fields. 
     */
    @FXML
    private Label currentMissionLabel;
    @FXML
    private TextArea statusTextArea;
    /* 
     * Fields for tracking the mission results. 
     */
    @FXML
    private TextField averageSkillTextField;
    @FXML
    private TextField difficultyTextField;
    @FXML
    private ChoiceBox minigameChoiceBox;
    @FXML
    private ChoiceBox targetFactionChoiceBox;
    @FXML
    private TextField baseDifficultyTextField;
    @FXML
    private TextField chanceSuccessTextField;
    @FXML
    private Label heroOneLabel;
    @FXML
    private Label heroOneConsequencesLabel;
    @FXML
    private Label heroOneBacklashLabel;
    @FXML
    private Label heroTwoLabel;
    @FXML
    private Label heroTwoConsequencesLabel;
    @FXML
    private Label heroTwoBacklashLabel;
    @FXML
    private Label heroThreeLabel;
    @FXML
    private Label heroThreeConsequencesLabel;
    @FXML
    private Label heroThreeBacklashLabel;
    @FXML
    private Label heroFourLabel;
    @FXML
    private Label heroFourConsequencesLabel;
    @FXML
    private Label heroFourBacklashLabel;
    @FXML
    private ChoiceBox casterChoiceBox;
    @FXML
    private Label backlashLabel;
    @FXML
    private TextField manaSpentTextField;
    @FXML
    private Label successRollsLabel;

    ObservableList<Hero> availableCasters = FXCollections.observableArrayList();
    /* 
     * Fields for exploration missions. 
     */
    @FXML
    private Tab explorationMissionTab;
    @FXML
    private TableView<Region> explorationRegionTable;
    @FXML
    private TableColumn<Region, ConflictType> conquestMiniGameColumn;
    @FXML
    private TableColumn<Region, Integer> conquestDifficultyColumn;
    @FXML
    private TableColumn<Region, String> conquestNameColumn;
    @FXML
    private TableColumn<Region, Integer> conquestFoodColumn;
    @FXML
    private TableColumn<Region, Integer> conquestTimberColumn;
    @FXML
    private TableColumn<Region, Integer> conquestPopulationColumn;
    @FXML
    private TableColumn<Region, Integer> conquestOreColumn;
    @FXML
    private TableColumn<Region, Integer> conquestManaColumn;
    @FXML
    private TableColumn<Region, Integer> conquestLuxuriesColumn;
    @FXML
    private TableColumn<Region, Boolean> conquestConquerColumn;
    /*
     * Fields for trade missions. 
     */
    @FXML
    private Tab tradeMissionTab;
    @FXML
    private Label diplomacyDispositionLabel;
    @FXML
    private TextField diplomacySeasonsTextField;
    @FXML
    private TableView<TradeEntry> diplomacyAvailableTableView;
    @FXML
    private TableColumn<TradeEntry, TradeResource> diplomacyAvailableResourceTableColumn;
    @FXML
    private TableColumn<TradeEntry, Integer> diplomacyAvailableAmountTableColumn;
    @FXML
    private TableColumn<TradeEntry, Integer> diplomacyAvailableValueTableColumn;
    @FXML
    private TableView<TradeEntry> diplomacyDesiredTableView;
    @FXML
    private TableColumn<TradeEntry, TradeResource> diplomacyDesiredResourceTableColumn;
    @FXML
    private TableColumn<TradeEntry, Integer> diplomacyDesiredAmountTableColumn;
    @FXML
    private TableColumn<TradeEntry, Integer> diplomacyDesiredValueTableColumn;
    /* 
     * Fields for conquest missions. 
     */
    @FXML
    private Tab warfareMissionTab;
    @FXML
    private TextField strongholdHitChanceTextField;
    @FXML
    private TextField factionHitChanceTextField;
    @FXML
    private TableView<Region> warfareTableView;
    @FXML
    private TableColumn<Region, String> warfareNameTableColumn;
    @FXML
    private TableColumn<Region, Integer> warfareFoodTableColumn;
    @FXML
    private TableColumn<Region, Integer> warfareTimberTableColumn;
    @FXML
    private TableColumn<Region, Integer> warfareOreTableColumn;
    @FXML
    private TableColumn<Region, Integer> warfareLuxuriesTableColumn;
    @FXML
    private TableColumn<Region, Integer> warfareManaTableColumn;
    @FXML
    private TableColumn<Region, String> warfareDevelopmentsTableColumn;
    @FXML
    private TableColumn<Region, Boolean> warfareSelectTableColumn;
    /* The forces the Stronghold musters during conquest. */
    @FXML
    private TableView<OrderOfBattle> strongholdForcesTableView;
    @FXML
    private TableColumn<OrderOfBattle, Object> strongholdForcesFactionColumn;
    @FXML
    private TableColumn<OrderOfBattle, Object> strongholdForcesTypeColumn;
    @FXML
    private TableColumn<OrderOfBattle, Object> strongholdForcesQualityColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> strongholdForcesAvailableColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> strongholdForcesOffenseColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> strongholdForcesDefenseColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> strongholdForcesDeployedColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> strongholdForcesFoodColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> strongholdForcesDamageColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> strongholdForcesCasualtiesColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> strongholdForcesMoraleColumn;
    /* The forces the faction defends with during Conquest missions. */
    @FXML
    private TableView<OrderOfBattle> factionForcesTableView;
    @FXML
    private TableColumn<OrderOfBattle, Object> factionForcesFactionColumn;
    @FXML
    private TableColumn<OrderOfBattle, Object> factionForcesTypeColumn;
    @FXML
    private TableColumn<OrderOfBattle, Object> factionForcesQualityColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> factionForcesAvailableColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> factionForcesOffenseColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> factionForcesDefenseColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> factionForcesDeployedColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> factionForcesFoodColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> factionForcesDamageColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> factionForcesCasualtiesColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> factionForcesMoraleColumn;
    /* 
     * Fields for regional threats. 
     */
    @FXML
    private Tab regionThreatMissionTab;
    @FXML
    private TableView<Threat> regionalThreatTable;
    @FXML
    private TableColumn<Threat, String> regionalThreatRegionTableColumn;
    @FXML
    private TableColumn<Threat, ConflictType> regionalThreatMiniGameTableColumn;
    @FXML
    private TableColumn<Threat, Integer> regionalThreatDifficultyTableColumn;
    @FXML
    private TableColumn<Threat, Boolean> regionalThreatSelectTableColumn;
    /* 
     * Fields for diplomatic threats. 
     */
    @FXML
    private Tab diplomaticThreatMissionTab;
    @FXML
    private TableView<Threat> diplomaticThreatTable;
    @FXML
    private TableColumn<Threat, String> diplomaticThreatFactionTableColumn;
    @FXML
    private TableColumn<Threat, ConflictType> diplomaticThreatMiniGameTableColumn;
    @FXML
    private TableColumn<Threat, Integer> diplomaticThreatDifficultyTableColumn;
    @FXML
    private TableColumn<Threat, Boolean> diplomaticThreatSelectTableColumn;
    /*
     * Fields for espionage misssions.
     */
    @FXML
    private Tab espionageMissionTab;
    @FXML
    private ChoiceBox espionageTypeChoiceBox;
    @FXML
    private ChoiceBox espionageTargetDevelopmentsChoiceBox;
    ObservableList<Development> filteredEspionageDevelopments
            = FXCollections.observableArrayList();
    /*
     * Fields for sabotage missions.
     */
    @FXML
    private Tab sabotageMissionTab;
    @FXML
    private ChoiceBox sabotageTypeChoiceBox;
    @FXML
    private ChoiceBox sabotageTargetDevelopmentsChoiceBox;

    /*
     * Fields for assassination missions.
     */
    @FXML
    private Tab assassinationMissionTab;
    @FXML
    private ChoiceBox assassinationMinigameChoiceBox;
    /*
     Fields for quests.
     */
    @FXML
    private Tab questMissionTab;
    @FXML
    private ChoiceBox questItemChoiceBox;
    ObservableList<Quest> availableQuests = FXCollections.observableArrayList();
    /*
     * Fields for faction threats.
     */
    @FXML
    private Tab factionThreatTab;
    @FXML
    private TextField threatStrongholdHitChanceTextField;
    @FXML
    private TextField threatFactionHitChanceTextField;
    @FXML
    private TableView<Threat> factionThreatsTable;
    @FXML
    private TableColumn<Threat, Object> factionThreatFactionColumn;
    @FXML
    private TableColumn<Threat, String> factionThreatNameColumn;
    @FXML
    private TableColumn<Threat, Object> factionThreatMinigameColumn;
    @FXML
    private TableColumn<Threat, Integer> factionThreatDifficultyColumn;
    @FXML
    private TableColumn<Threat, Boolean> factionThreatSelectedColumn;
    @FXML
    private TableColumn<Threat, Object> factionThreatTargetFactionColumn;
    @FXML
    private TableColumn<Threat, Object> factionThreatTargetHeroColumn;
    @FXML
    private TableColumn<Threat, Object> factionThreatTargetRegionColumn;
    @FXML
    private TableColumn<Threat, Object> factionThreatTargetDevelopmentColumn;
    /* The military forces the Stronghold defends with in a threat. */
    @FXML
    private TableView<OrderOfBattle> threatStrongholdForcesTableView;
    @FXML
    private TableColumn<OrderOfBattle, Object> threatStrongholdForcesFactionColumn;
    @FXML
    private TableColumn<OrderOfBattle, Object> threatStrongholdForcesTypeColumn;
    @FXML
    private TableColumn<OrderOfBattle, Object> threatStrongholdForcesQualityColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatStrongholdForcesAvailableColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatStrongholdForcesOffenseColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatStrongholdForcesDefenseColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatStrongholdForcesDeployedColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatStrongholdForcesFoodColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatStrongholdForcesDamageColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatStrongholdForcesCasualtiesColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatStrongholdForcesMoraleColumn;
    /* The forces the faction attacks with during a threat. */
    @FXML
    private TableView<OrderOfBattle> threatFactionForcesTableView;
    @FXML
    private TableColumn<OrderOfBattle, Object> threatFactionForcesFactionColumn;
    @FXML
    private TableColumn<OrderOfBattle, Object> threatFactionForcesTypeColumn;
    @FXML
    private TableColumn<OrderOfBattle, Object> threatFactionForcesQualityColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatFactionForcesAvailableColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatFactionForcesOffenseColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatFactionForcesDefenseColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatFactionForcesDeployedColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatFactionForcesFoodColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatFactionForcesDamageColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatFactionForcesCasualtiesColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> threatFactionForcesMoraleColumn;
    /* 
     * Fields for heroes. 
     */
    @FXML
    private Tab heroesTab;
    @FXML
    private TableView<Hero> heroTable;
    @FXML
    private TableColumn<Hero, String> heroNameColumn;
    @FXML
    private TableColumn<Hero, Integer> heroRankColumn;
    @FXML
    private TableColumn<Hero, ConflictType> heroMajorFocusColumn;
    @FXML
    private TableColumn<Hero, ConflictType> heroMinorFocusColumn;
    @FXML
    private TableColumn<Hero, Integer> heroConsequencesColumn;
    @FXML
    private TableColumn<Hero, Boolean> heroSelectColumn;
    @FXML
    private TableColumn<Hero, Integer> heroManaColumn;
    @FXML
    private TableColumn<Hero, Boolean> heroCasterColumn;
    @FXML
    private TableColumn<Hero, Object> heroRegionColumn;

    /*
     * If the mission uses the Warfare mini-game, need to inflict damage
     * on the troops for each side, until one side or the other yields.
     * There is no automatic check for victory.
     */
    @FXML
    private void continueMission(ActionEvent event) throws Exception {

        if (currentMission.getHeroes().size() != 4) {
            statusTextArea.setText("Choose four heroes first!");
            return;
        }

        /*
         Make sure the necessary fields are filled in for the mission.
         */
        if (!validateFields()) {
            return;
        }

        if (currentMission.getMinigame() == ConflictType.Warfare
                && currentMission.getType() != MissionType.Exploration
                && currentMission.getType() != MissionType.Diplomatic_Threat
                && currentMission.getType() != MissionType.Regional_Threat) {
            currentMission.roundOfWarfare(wotaModel.getStronghold());
            return;
        }

        if (currentMission.attemptMission(wotaModel.getStronghold())) {
            currentMission.setSuccess(true);
            finishMission();
            return;
        }
        currentMission.setSuccess(false);
        updateActiveHeroes();

        String resultString
                = "Last roll: " + currentMission.getLastRoll() + " ";
        if (currentMission.getMinigame() == ConflictType.Infiltration) {
            int moreFailures = 4 - currentMission.getCaught();
            if (moreFailures <= 0) {
                statusTextArea.setText(resultString + ", you're caught!");
            } else {
                statusTextArea.setText(resultString
                        + ", " + moreFailures + " more failures and you're caught!");
            }
        } else {
            statusTextArea.setText(resultString);
        }

    }

    @FXML
    private void castSpell(ActionEvent event) {
        Hero caster = (Hero) casterChoiceBox.getSelectionModel().getSelectedItem();
        if (caster == null) {
            statusTextArea.setText("Select a caster to cast a spell.");
            return;
        }
        if (caster.getManaRegion() == null) {
            statusTextArea.setText("The caster doesn't have a region to draw mana from.");
            return;
        }
        int manaSpent;
        try {
            manaSpent = Integer.parseInt(manaSpentTextField.getText());
        } catch (NumberFormatException exc) {
            statusTextArea.setText("Invalid mana spent value.");
            return;
        }
        if (manaSpent > caster.getMana()) {
            statusTextArea.setText("Spending more mana than the caster has.");
            return;
        }
        
        String results
                = currentMission.castSpell(caster, manaSpent);
        backlashLabel.setText(Integer.toString(caster.getBacklash()));

        /* Update backlash amounts. */
        updateActiveHeroes();
        statusTextArea.setText(results);
    }

    @FXML
    private void updateSuccessRolls(ActionEvent event) {
        Hero caster = (Hero) casterChoiceBox.getSelectionModel().getSelectedItem();
        if (caster == null) {
            return;
        }
        int manaSpent = 0;
        try {
            manaSpent = Integer.parseInt(manaSpentTextField.getText());
        } catch (NumberFormatException exc) {
            statusTextArea.setText("Invalid mana spent value.");
            return;
        }
        successRollsLabel.setText(Integer.toString(currentMission.rerollAmount(manaSpent)));
    }

    private boolean validateFields() {
        boolean valid = true;
        MissionType missionType = currentMission.getType();
        switch (missionType) {
            case Exploration:
                for (int i = 0; i < wotaModel.getNeutralRegions().size(); i++) {
                    Region nextRegion = wotaModel.getNeutralRegions().get(i);
                    if (nextRegion.isSelected()) {
                        return true;
                    }
                }
                statusTextArea.setText("Select a neutral region for Exploration!");
                valid = false;
                break;
            case Trade:
                TradeEntry give
                        = diplomacyDesiredTableView.getSelectionModel().getSelectedItem();
                if (give == null) {
                    statusTextArea.setText("Select something to give on a Trade mission!");
                    valid = false;
                }
                TradeEntry receive
                        = diplomacyAvailableTableView.getSelectionModel().getSelectedItem();
                if (receive == null) {
                    statusTextArea.setText("Select something to receive on a Trade mission!");
                    valid = false;
                }
                int seasons = 0;
                try {
                    seasons
                            = Integer.parseInt(diplomacySeasonsTextField.getText());
                } catch (NumberFormatException exc) {
                    statusTextArea.setText("Select a valid number of seasons for Trade mission!");
                    valid = false;
                }
                if (seasons < 1) {
                    statusTextArea.setText("Select a valid number of seasons for Trade mission!");
                    valid = false;
                }
                break;
            case Espionage:
                EspionageMissions espMission
                        = (EspionageMissions) espionageTypeChoiceBox.getSelectionModel().getSelectedItem();
                if (espMission == null) {
                    statusTextArea.setText("Pick a type of Espionage mission!");
                    valid = false;
                    break;
                }
                if (espMission.getType() == EspionageType.Tech_Secrets) {
                    Development techDev
                            = (Development) espionageTargetDevelopmentsChoiceBox.getSelectionModel().getSelectedItem();
                    if (techDev == null) {
                        statusTextArea.setText("Pick a development as the target for stealing secrets!");
                        valid = false;
                        break;
                    }
                }
                break;
            case Conquest:
                if (currentMission.getFaction().getRegions().stream().anyMatch((nextRegion) -> (nextRegion.isSelected()))) {
                    return true;
                }
                valid = false;
                statusTextArea.setText("Choose one of the faction's regions to attack!");
                break;
            case Assassination:
                ConflictType aminiGame
                        = (ConflictType) assassinationMinigameChoiceBox.getSelectionModel().getSelectedItem();
                if (aminiGame == null) {
                    statusTextArea.setText("Choose a leader focus for Assassination!");
                    valid = false;
                }
                break;
            case Quest:
                if (questItemChoiceBox.getSelectionModel().getSelectedItem() == null) {
                    statusTextArea.setText("Choose a quest to go on");
                    valid = false;
                }
                break;
            case Sabotage:
                SabotageMissions sabMission
                        = (SabotageMissions) sabotageTypeChoiceBox.getSelectionModel().getSelectedItem();
                if (sabMission == null) {
                    statusTextArea.setText("Pick a type of Sabotage mission!");
                    valid = false;
                    break;
                }
                if (sabMission.getType() == SabotageType.Target_Development) {
                    Development techDev
                            = (Development) sabotageTargetDevelopmentsChoiceBox.getSelectionModel().getSelectedItem();
                    if (techDev == null) {
                        statusTextArea.setText("Pick a development as the target for sabotage!");
                        valid = false;
                        break;
                    }
                }
                break;
            case Regional_Threat:
                if (wotaModel.getSeasonalThreats().getRegionalThreats().stream().anyMatch((nextRegionalThreat) -> (nextRegionalThreat.isSelected()))) {
                    return true;
                }
                valid = false;
                statusTextArea.setText("Choose a regional threat!");
                break;
            case Diplomatic_Threat:
                if (wotaModel.getSeasonalThreats().getDiplomaticThreats().stream().anyMatch((diplomaticThreat) -> (diplomaticThreat.isSelected()))) {
                    return true;
                }
                valid = false;
                statusTextArea.setText("Choose a diplomatic threat!");
                break;
            case Faction_Threat:
                if (wotaModel.getSeasonalThreats().getFactionThreats().stream().anyMatch((factionThreat) -> (factionThreat.isSelected()))) {
                    return true;
                }
                valid = false;
                statusTextArea.setText("Choose a target faction threat!");
                break;
            default:
                valid = false;
                statusTextArea.setText("Unknown mission type!");
                break;
        }
        return valid;
    }

    @FXML
    private void giveUpMission(ActionEvent event) throws Exception {
        currentMission.setSuccess(false);
        finishMission();
    }

    @FXML
    private void succeedAtMission(ActionEvent event) throws Exception {
        currentMission.setSuccess(true);
        finishMission();
    }

    @FXML
    private void finishMission() throws Exception {

        /*
         * Get the mission types, and apply results as appropriate.
         */
        MissionType missionType = currentMission.getType();
        boolean success = currentMission.isSuccess();
        MinorFaction targetFaction = currentMission.getFaction();

        switch (missionType) {
            case Exploration:
                for (int i = 0; i < wotaModel.getNeutralRegions().size(); i++) {
                    Region nextRegion = wotaModel.getNeutralRegions().get(i);
                    if (nextRegion.isSelected()) {
                        nextRegion.setSelected(false);
                        wotaModel.getNeutralRegions().remove(nextRegion);
                        if (!success) {
                            nextRegion.setConsequences(2);
                        }
                        wotaModel.getStronghold().getRegions().add(nextRegion);
                        break;
                    }
                }
                /* It costs two food for every region the Stronghold has to explore. */
                int foodCost = wotaModel.getStronghold().getRegions().size() * 2;
                wotaModel.getStronghold().setFood(wotaModel.getStronghold().getFood() - foodCost);
                break;
            case Trade:
                if (success) {
                    TradeEntry give
                            = diplomacyDesiredTableView.getSelectionModel().getSelectedItem();
                    TradeEntry receive
                            = diplomacyAvailableTableView.getSelectionModel().getSelectedItem();
                    int seasons = 0;
                    try {
                        seasons
                                = Integer.parseInt(diplomacySeasonsTextField.getText());
                    } catch (NumberFormatException exc) {
                        statusTextArea.setText("Select a valid number of seasons for Trade mission!");
                        return;
                    }
                    int dispositionBonus = 1;
                    targetFaction.setDisposition(targetFaction.getDisposition() + dispositionBonus);
                    Trade newTrade = new Trade(give.getResource(), give.getAmount(),
                            receive.getResource(), receive.getAmount(),
                            targetFaction, seasons);
                    wotaModel.getStronghold().getActiveTrades().add(newTrade);
                } else {
                    targetFaction.setDisposition(targetFaction.getDisposition() - 1);
                }
                break;
            case Espionage:
                EspionageMissions espMission
                        = (EspionageMissions) espionageTypeChoiceBox.getSelectionModel().getSelectedItem();
                if (espMission == null) {
                    statusTextArea.setText("Pick a type of Espionage mission!");
                    return;
                }
                if (espMission.getType() == EspionageType.Tech_Secrets
                        && success) {
                    Development techDev
                            = (Development) espionageTargetDevelopmentsChoiceBox.getSelectionModel().getSelectedItem();
                    if (techDev == null) {
                        statusTextArea.setText("Pick a development as the target for stealing secrets!");
                        return;
                    }
                    if (techDev.getBp() > 0) {
                        techDev.setBp(techDev.getBp() / 2);
                    }
                    if (techDev.getFood() > 0) {
                        techDev.setFood(techDev.getFood() / 2);
                    }
                    if (techDev.getOre() > 0) {
                        techDev.setOre(techDev.getOre() / 2);
                    }
                    if (techDev.getTimber() > 0) {
                        techDev.setTimber(techDev.getTimber() / 2);
                    }
                    if (techDev.getMana() > 0) {
                        techDev.setMana(techDev.getMana() / 2);
                    }
                    if (techDev.getLuxury() > 0) {
                        techDev.setLuxury(techDev.getLuxury() / 2);
                    }
                } else if (espMission.getType() == EspionageType.Diplomatic_Secrets
                        && success) {
                    Condition tradeMod = new Condition(ConditionType.Trade_Modifier, 5);
                    tradeMod.setMagnitude(-2);
                    wotaModel.getStronghold().getConditions().add(new Condition(ConditionType.Trade_Modifier, 5));
                    targetFaction.setTradeDifficulty(targetFaction.getTradeDifficulty() - 2);
                }
                if (currentMission.getCaught() > 3) {
                    int dispositionLoss = 2;
                    if (wotaModel.getStronghold().hasDevelopment(DevelopmentType.Center_of_Culture)) {
                        dispositionLoss = 1;
                    }
                    currentMission.getFaction().setDisposition(currentMission.getFaction().getDisposition() - dispositionLoss);
                }
                break;
            case Quest:
                if (success) {
                    Quest currentQuest
                            = (Quest) questItemChoiceBox.getSelectionModel().getSelectedItem();
                    wotaModel.getStronghold().getDevelopments().add(currentQuest.getArtifact());
                }
            case Sabotage:
                SabotageMissions sabMission
                        = (SabotageMissions) sabotageTypeChoiceBox.getSelectionModel().getSelectedItem();
                if (sabMission.getType() == SabotageType.Foment_Rebellion && success) {
                    targetFaction.setStability(targetFaction.getStability() - 1);
                }
                else {
                    /* Sabotaged a development. Remove the development from the faction and create a Condition. */
                    Development sabotagedDevelopment =
                            (Development) sabotageTargetDevelopmentsChoiceBox.getSelectionModel().getSelectedItem();
                    targetFaction.removeDevelopment(sabotagedDevelopment);
                    Condition sabotagedCondition = new Condition(ConditionType.Sabotaged_Development, 5);
                    sabotagedCondition.setDevelopment(sabotagedDevelopment);
                    sabotagedCondition.setFaction(targetFaction);
                    wotaModel.getStronghold().getConditions().add(sabotagedCondition);
                }
                if (currentMission.getCaught() > 3) {
                    int dispositionLoss = 5;
                    if (wotaModel.getStronghold().hasDevelopment(DevelopmentType.Center_of_Culture)) {
                        dispositionLoss = 4;
                    }
                    currentMission.getFaction().setDisposition(currentMission.getFaction().getDisposition() - dispositionLoss);
                }
                break;
            case Assassination:
                ConflictType aminiGame
                        = (ConflictType) assassinationMinigameChoiceBox.getSelectionModel().getSelectedItem();
                switch (aminiGame) {
                    case Infiltration:
                        targetFaction.setInfiltrationDifficulty(targetFaction.getInfiltrationDifficulty() - 1);
                        break;
                    case Warfare:
                        targetFaction.setMilitaryDifficulty(targetFaction.getMilitaryDifficulty() - 1);
                        break;
                    case Diplomacy:
                        targetFaction.setTradeDifficulty(targetFaction.getTradeDifficulty() - 1);
                        break;
                    case Skirmish:
                        targetFaction.setSkirmishDifficulty(targetFaction.getSkirmishDifficulty() - 1);
                        break;
                    default:
                        break;
                }
                if (currentMission.getCaught() > 3) {
                    int dispositionLoss = 5;
                    if (wotaModel.getStronghold().hasDevelopment(DevelopmentType.Center_of_Culture)) {
                        dispositionLoss = 4;
                    }
                    currentMission.getFaction().setDisposition(currentMission.getFaction().getDisposition() - dispositionLoss);
                }
                break;
            case Conquest:
                if (success) {
                    Region oldRegion = null;
                    for (Region nextRegion : currentMission.getFaction().getRegions()) {
                        if (nextRegion.isSelected()) {
                            wotaModel.getStronghold().getRegions().add(nextRegion);
                            nextRegion.setSelected(false);
                            oldRegion = nextRegion;
                            break;
                        }
                    }
                    int stabilityLoss = 2;
                    currentMission.getFaction().setStability(currentMission.getFaction().getStability() - stabilityLoss);
                    currentMission.getFaction().getRegions().remove(oldRegion);
                }

                /* Apply casualties from troop loss, which could result in a population hit. */
                currentMission.applyCasualties();

                /* The defending faction loses disposition (they don't like being attacked). */
                currentMission.getFaction().setDisposition(currentMission.getFaction().getDisposition() - 5);

                /* The Stronghold loses food to supply attacking troops. */
                if (!currentMission.isStrongholdOnDefense()) {
                    int totalFood = 0;
                    for (OrderOfBattle unit : wotaModel.getStronghold().getUnits()) {
                        totalFood = totalFood + unit.getFood();
                    }

                    wotaModel.getStronghold().setFood(wotaModel.getStronghold().getFood() - totalFood);
                }
                break;
            case Regional_Threat:
                if (success) {
                    for (Iterator<Threat> it = wotaModel.getSeasonalThreats().getRegionalThreats().iterator(); it.hasNext();) {
                        Threat nextRegionalThreat = it.next();
                        if (nextRegionalThreat.isSelected()) {
                            nextRegionalThreat.getTargetRegion().setThreat(false);
                            it.remove();
                            break;
                        }
                    }
                }
                /* 
                 * On a failure, the stability is increased by one, because it's
                 * going to be dropped by one later.
                 */
                wotaModel.getStronghold().setStability(wotaModel.getStronghold().getStability() + 1);
                break;
            case Diplomatic_Threat:
                for (Iterator<Threat> it = wotaModel.getSeasonalThreats().getDiplomaticThreats().iterator(); it.hasNext();) {
                    Threat diplomaticThreat = it.next();
                    if (diplomaticThreat.isSelected()) {
                        MinorFaction selectFaction = diplomaticThreat.getTargetFaction();
                        if (success) {
                            selectFaction.setDisposition(selectFaction.getDisposition() + 1);
                        }
                        it.remove();
                        break;
                    }
                }
                break;
            case Faction_Threat:
                for (Iterator<Threat> it
                        = wotaModel.getSeasonalThreats().getFactionThreats().iterator(); it.hasNext();) {
                    Threat factionThreat = it.next();
                    if (factionThreat.isSelected()) {
                        if (factionThreat.getMinigame() == ConflictType.Warfare) {
                            /* All sides suffer population loss from the battle. */
                            currentMission.applyCasualties();
                        }
                        if (success) {
                            it.remove();
                        }
                        break;
                    }
                }
            default:
                break;
        }

        /* 
         * Load the upkeep page, passing in all the details from
         * this mission.
         */
        currentMission.clearAllies();
        wotaModel.hideMissionResolution();
        wotaModel.showSeasonalUpkeep();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /* Heroes tab. */
        heroesTab.setOnSelectionChanged((Event t) -> {
            if(heroesTab.isSelected()){
                statusTextArea.setText("Select four heroes to go on the mission. Their skill is averaged together to determine a chance of success.");
            }
            
        });
        
        /* Exploration mission. */
        explorationMissionTab.setOnSelectionChanged((Event t) -> {
            if (explorationMissionTab.isSelected()) {
                currentMission.setType(MissionType.Exploration);
                currentMissionLabel.setText(explorationMissionTab.getText());
                statusTextArea.setText("Exploration costs 2 food for each region the Stronghold currently controls.");
            }
        });

        conquestNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        conquestFoodColumn.setCellValueFactory(new PropertyValueFactory<>("food"));
        conquestTimberColumn.setCellValueFactory(new PropertyValueFactory<>("timber"));
        conquestOreColumn.setCellValueFactory(new PropertyValueFactory<>("ore"));
        conquestManaColumn.setCellValueFactory(new PropertyValueFactory<>("mana"));
        conquestLuxuriesColumn.setCellValueFactory(new PropertyValueFactory<>("luxuries"));
        conquestPopulationColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        conquestDifficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        conquestMiniGameColumn.setCellValueFactory(new PropertyValueFactory<>("minigame"));
        conquestConquerColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
        conquestConquerColumn.setCellFactory((TableColumn<Region, Boolean> p) -> new ExplorationMissionCheckBoxTableCell(wotaModel, currentMission));

        /* Diplomatic mission. */
        tradeMissionTab.setOnSelectionChanged((Event t) -> {
            if (tradeMissionTab.isSelected()) {
                minigameChoiceBox.getSelectionModel().select(ConflictType.Diplomacy);
                currentMission.setType(MissionType.Trade);
                currentMissionLabel.setText(tradeMissionTab.getText());
                statusTextArea.setText("Trade options limited by faction disposition.");
            }
        });
        diplomacyAvailableAmountTableColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        diplomacyAvailableResourceTableColumn.setCellValueFactory(new PropertyValueFactory<>("resource"));
        diplomacyAvailableValueTableColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        diplomacyAvailableTableView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends TradeEntry> ov, TradeEntry oldEntry, TradeEntry newEntry) -> {
                    if (newEntry == null) {
                        currentMission.setDifficulty(0);
                        return;
                    }
                    int valueAvailable = newEntry.getValue();
                    TradeEntry desiredEntry = diplomacyDesiredTableView.getSelectionModel().getSelectedItem();
                    int valueDesired = 1;
                    if (desiredEntry != null) {
                        valueDesired = desiredEntry.getValue();
                    }
                    currentMission.setDifficulty(currentMission.getBaseDifficulty()
                            + (valueAvailable - valueDesired));
                });

        diplomacyDesiredAmountTableColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        diplomacyDesiredResourceTableColumn.setCellValueFactory(new PropertyValueFactory<>("resource"));
        diplomacyDesiredValueTableColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        diplomacyDesiredTableView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends TradeEntry> ov, TradeEntry oldEntry, TradeEntry newEntry) -> {
                    if (newEntry == null) {
                        currentMission.setDifficulty(0);
                        return;
                    }
                    int valueDesired = newEntry.getValue();
                    TradeEntry desiredEntry = diplomacyAvailableTableView.getSelectionModel().getSelectedItem();
                    int valueAvailable = 1;
                    if (desiredEntry != null) {
                        valueAvailable = desiredEntry.getValue();
                    }
                    currentMission.setDifficulty(currentMission.getBaseDifficulty()
                            + (valueAvailable - valueDesired));
                });

        /* General setup for warfare mission. */
        warfareMissionTab.setOnSelectionChanged((Event t) -> {
            if (warfareMissionTab.isSelected()) {
                minigameChoiceBox.getSelectionModel().select(ConflictType.Warfare);
                currentMission.setType(MissionType.Conquest);
                currentMissionLabel.setText(warfareMissionTab.getText());
                currentMission.setStrongholdOnDefense(false);
                wotaModel.getStronghold().clearDefensiveFortifications();
                statusTextArea.setText("Select units to take on warfare mission.");
            }
        });

        warfareNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        warfareFoodTableColumn.setCellValueFactory(new PropertyValueFactory<>("food"));
        warfareOreTableColumn.setCellValueFactory(new PropertyValueFactory<>("ore"));
        warfareTimberTableColumn.setCellValueFactory(new PropertyValueFactory<>("timber"));
        warfareLuxuriesTableColumn.setCellValueFactory(new PropertyValueFactory<>("luxuries"));
        warfareManaTableColumn.setCellValueFactory(new PropertyValueFactory<>("mana"));
        warfareDevelopmentsTableColumn.setCellValueFactory(new PropertyValueFactory<>("developmentsString"));
        warfareSelectTableColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
        warfareSelectTableColumn.setCellFactory((TableColumn<Region, Boolean> p) -> new WarfareMissionCheckBoxTableCell(wotaModel, currentMission));

        /* Military units from the Stronghold. */
        strongholdForcesFactionColumn.setCellValueFactory(new PropertyValueFactory<>("owningFaction"));
        strongholdForcesTypeColumn.setCellValueFactory(new PropertyValueFactory<>("utype"));
        strongholdForcesQualityColumn.setCellValueFactory(new PropertyValueFactory<>("quality"));
        strongholdForcesAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        strongholdForcesAvailableColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        strongholdForcesOffenseColumn.setCellValueFactory(new PropertyValueFactory<>("offense"));
        strongholdForcesOffenseColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        strongholdForcesDefenseColumn.setCellValueFactory(new PropertyValueFactory<>("defense"));
        strongholdForcesDefenseColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        strongholdForcesDeployedColumn.setCellValueFactory(new PropertyValueFactory<>("deployed"));
        strongholdForcesDeployedColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        strongholdForcesFoodColumn.setCellValueFactory(new PropertyValueFactory<>("food"));
        strongholdForcesFoodColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        strongholdForcesDamageColumn.setCellValueFactory(new PropertyValueFactory<>("damage"));
        strongholdForcesDamageColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        strongholdForcesCasualtiesColumn.setCellValueFactory(new PropertyValueFactory<>("casualties"));
        strongholdForcesCasualtiesColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        strongholdForcesMoraleColumn.setCellValueFactory(new PropertyValueFactory<>("morale"));
        strongholdForcesMoraleColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        factionForcesFactionColumn.setCellValueFactory(new PropertyValueFactory<>("owningFaction"));
        factionForcesTypeColumn.setCellValueFactory(new PropertyValueFactory<>("utype"));
        factionForcesQualityColumn.setCellValueFactory(new PropertyValueFactory<>("quality"));
        factionForcesAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        factionForcesAvailableColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        factionForcesOffenseColumn.setCellValueFactory(new PropertyValueFactory<>("offense"));
        factionForcesOffenseColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        factionForcesDefenseColumn.setCellValueFactory(new PropertyValueFactory<>("defense"));
        factionForcesDefenseColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        factionForcesDeployedColumn.setCellValueFactory(new PropertyValueFactory<>("deployed"));
        factionForcesDeployedColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        factionForcesFoodColumn.setCellValueFactory(new PropertyValueFactory<>("food"));
        factionForcesFoodColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        factionForcesDamageColumn.setCellValueFactory(new PropertyValueFactory<>("damage"));
        factionForcesDamageColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        factionForcesCasualtiesColumn.setCellValueFactory(new PropertyValueFactory<>("casualties"));
        factionForcesCasualtiesColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        factionForcesMoraleColumn.setCellValueFactory(new PropertyValueFactory<>("morale"));
        factionForcesMoraleColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        /* Regional Threat. */
        regionThreatMissionTab.setOnSelectionChanged((Event t) -> {
            if (regionThreatMissionTab.isSelected()) {
                currentMission.setType(MissionType.Regional_Threat);
                currentMissionLabel.setText(regionThreatMissionTab.getText());
                statusTextArea.setText("Ignoring a regional threat causes a loss of two stability");
            }
        });

        regionalThreatRegionTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        regionalThreatMiniGameTableColumn.setCellValueFactory(new PropertyValueFactory<>("minigame"));
        regionalThreatDifficultyTableColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        regionalThreatSelectTableColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
        regionalThreatSelectTableColumn.setCellFactory((TableColumn<Threat, Boolean> p) -> new RegionalThreatMissionCheckBoxTableCell(wotaModel, currentMission));

        /* Diplomatic Threat. */
        diplomaticThreatMissionTab.setOnSelectionChanged((Event t) -> {
            if (diplomaticThreatMissionTab.isSelected()) {
                currentMission.setType(MissionType.Diplomatic_Threat);
                currentMissionLabel.setText(diplomaticThreatMissionTab.getText());
                statusTextArea.setText("Ignoring a diplomatic threat causes a loss of two disposition with the faction");
            }
        });

        diplomaticThreatFactionTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        diplomaticThreatMiniGameTableColumn.setCellValueFactory(new PropertyValueFactory<>("minigame"));
        diplomaticThreatDifficultyTableColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        diplomaticThreatSelectTableColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
        diplomaticThreatSelectTableColumn.setCellFactory((TableColumn<Threat, Boolean> p) -> new DiplomaticThreatMissionCheckBoxTableCell(wotaModel, currentMission));

        /* Espionage. */
        espionageMissionTab.setOnSelectionChanged((Event t) -> {
            if (espionageMissionTab.isSelected()) {
                currentMission.setType(MissionType.Espionage);
                minigameChoiceBox.getSelectionModel().select(ConflictType.Infiltration);
                currentMissionLabel.setText(espionageMissionTab.getText());
                statusTextArea.setText("Targeting Tech or Arcane secrets halves the development's cost. "+
                        "Diplomatic secrets subtracts two from difficulty of next trade mission. "+
                        " Strategic plans forces the Autarch to allocate threat points.");
            }
        });

        /* Sabotage. */
        sabotageMissionTab.setOnSelectionChanged((Event t) -> {
            if (sabotageMissionTab.isSelected()) {
                currentMission.setType(MissionType.Sabotage);
                minigameChoiceBox.getSelectionModel().select(ConflictType.Infiltration);
                currentMissionLabel.setText(sabotageMissionTab.getText());
                statusTextArea.setText("Fomenting rebellion lowers the target faction's stability by one. "
                        + "Sabotaging a development disables a development for a year (choose development).");
            }
        });

        /* Assassination. */
        assassinationMissionTab.setOnSelectionChanged((Event t) -> {
            if (assassinationMissionTab.isSelected()) {
                currentMission.setType(MissionType.Assassination);
                minigameChoiceBox.getSelectionModel().select(ConflictType.Infiltration);
                currentMissionLabel.setText(assassinationMissionTab.getText());
                statusTextArea.setText("Assassination lowers the effectiveness of the target "
                        + " faction to handle a certain minigame type. Can't target the same faction's "
                        + " minigame more than once.");
            }
        });

        /* Quests. */
        questMissionTab.setOnSelectionChanged((Event t) -> {
            if (questMissionTab.isSelected()) {
                currentMission.setType(MissionType.Quest);
            }
        });


        /* Faction threat. */
        factionThreatTab.setOnSelectionChanged((Event t) -> {
            if (factionThreatTab.isSelected()) {
                currentMission.setType(MissionType.Faction_Threat);
                currentMissionLabel.setText(factionThreatTab.getText());
                currentMission.setStrongholdOnDefense(true);
            }
        });

        factionThreatFactionColumn.setCellValueFactory(new PropertyValueFactory<>("minorFaction"));
        factionThreatNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        factionThreatMinigameColumn.setCellValueFactory(new PropertyValueFactory<>("minigame"));
        factionThreatDifficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        factionThreatSelectedColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
        factionThreatTargetFactionColumn.setCellValueFactory(new PropertyValueFactory<>("targetFaction"));
        factionThreatTargetHeroColumn.setCellValueFactory(new PropertyValueFactory<>("targetHero"));
        factionThreatTargetRegionColumn.setCellValueFactory(new PropertyValueFactory<>("targetRegion"));
        factionThreatTargetDevelopmentColumn.setCellValueFactory(new PropertyValueFactory<>("targetDevelopment"));
        factionThreatSelectedColumn.setCellFactory((TableColumn<Threat, Boolean> p) -> new FactionThreatMissionCheckBoxTableCell(MissionResolutionController.this, wotaModel, currentMission));

        threatStrongholdForcesFactionColumn.setCellValueFactory(new PropertyValueFactory<>("owningFaction"));
        threatStrongholdForcesTypeColumn.setCellValueFactory(new PropertyValueFactory<>("utype"));
        threatStrongholdForcesQualityColumn.setCellValueFactory(new PropertyValueFactory<>("quality"));
        threatStrongholdForcesAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        threatStrongholdForcesAvailableColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        threatStrongholdForcesOffenseColumn.setCellValueFactory(new PropertyValueFactory<>("offense"));
        threatStrongholdForcesOffenseColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        threatStrongholdForcesDefenseColumn.setCellValueFactory(new PropertyValueFactory<>("defense"));
        threatStrongholdForcesDefenseColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        threatStrongholdForcesDeployedColumn.setCellValueFactory(new PropertyValueFactory<>("deployed"));
        threatStrongholdForcesDeployedColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        threatStrongholdForcesFoodColumn.setCellValueFactory(new PropertyValueFactory<>("food"));

        threatStrongholdForcesDamageColumn.setCellValueFactory(new PropertyValueFactory<>("damage"));
        threatStrongholdForcesDamageColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        threatStrongholdForcesCasualtiesColumn.setCellValueFactory(new PropertyValueFactory<>("casualties"));
        threatStrongholdForcesCasualtiesColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        threatStrongholdForcesMoraleColumn.setCellValueFactory(new PropertyValueFactory<>("morale"));
        threatStrongholdForcesMoraleColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        threatFactionForcesFactionColumn.setCellValueFactory(new PropertyValueFactory<>("owningFaction"));
        threatFactionForcesTypeColumn.setCellValueFactory(new PropertyValueFactory<>("utype"));
        threatFactionForcesQualityColumn.setCellValueFactory(new PropertyValueFactory<>("quality"));
        threatFactionForcesAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        threatFactionForcesAvailableColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        threatFactionForcesOffenseColumn.setCellValueFactory(new PropertyValueFactory<>("offense"));
        threatFactionForcesOffenseColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        threatFactionForcesDefenseColumn.setCellValueFactory(new PropertyValueFactory<>("defense"));
        threatFactionForcesDefenseColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        threatFactionForcesDeployedColumn.setCellValueFactory(new PropertyValueFactory<>("deployed"));
        threatFactionForcesDeployedColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        threatFactionForcesFoodColumn.setCellValueFactory(new PropertyValueFactory<>("food"));

        threatFactionForcesDamageColumn.setCellValueFactory(new PropertyValueFactory<>("damage"));
        threatFactionForcesDamageColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        threatFactionForcesCasualtiesColumn.setCellValueFactory(new PropertyValueFactory<>("casualties"));
        threatFactionForcesCasualtiesColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));
        threatFactionForcesMoraleColumn.setCellValueFactory(new PropertyValueFactory<>("morale"));
        threatFactionForcesMoraleColumn.setCellFactory((TableColumn<OrderOfBattle, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        /* Hero selection. */
        heroNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        heroMajorFocusColumn.setCellValueFactory(new PropertyValueFactory<>("majorFocus"));
        heroMinorFocusColumn.setCellValueFactory(new PropertyValueFactory<>("minorFocus"));
        heroRankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        heroConsequencesColumn.setCellValueFactory(new PropertyValueFactory<>("consequences"));
        heroCasterColumn.setCellValueFactory(new PropertyValueFactory<>("spellcaster"));
        heroManaColumn.setCellValueFactory(new PropertyValueFactory<>("mana"));
        heroRegionColumn.setCellValueFactory(new PropertyValueFactory<>("manaRegion"));
        heroSelectColumn.setCellValueFactory(new PropertyValueFactory<>("onMission"));
        heroSelectColumn.setCellFactory((TableColumn<Hero, Boolean> p) -> new HeroMissionCheckBoxTableCell(MissionResolutionController.this, currentMission));
    }

    /* This is for Espionage missions, they can only target certain developments. */
    private void filterDevelopmentsForEspionage(ObservableList<Development> inDevs) {
        filteredEspionageDevelopments.clear();
        inDevs.stream().filter((development) -> 
                ((development.getTree() == TechTree.Arcane
                || development.getTree() == TechTree.Infiltration)
                && !development.isRegional())).forEach((development) -> {
                    filteredEspionageDevelopments.add(development);
                });
    }

    public void setTargetFaction(MinorFaction minorFaction) {
        targetFactionChoiceBox.setValue(minorFaction);
    }

    public void setMiniGame(ConflictType miniGame) {
        minigameChoiceBox.setValue(miniGame);
    }

    public void setThreatFactionForces(ObservableList<OrderOfBattle> units) {
        threatFactionForcesTableView.setItems(units);
    }

    public void updateActiveHeroes() {
        ObservableList<Hero> activeHeroes
                = currentMission.getHeroes();
        for (int i = 0; i < 4; i++) {
            Hero nextHero = null;
            if (activeHeroes.size() > i) {
                nextHero = activeHeroes.get(i);
            }
            switch (i) {
                case 0:
                    if (nextHero != null) {
                        heroOneLabel.setText(nextHero.getName());
                        heroOneConsequencesLabel.setText(Integer.toString(nextHero.getConsequences()));
                        heroOneBacklashLabel.setText(Integer.toString(nextHero.getBacklash()));
                    } else {
                        heroOneLabel.setText("Hero One");
                        heroOneConsequencesLabel.setText("0");
                        heroOneBacklashLabel.setText("0");
                    }
                    break;
                case 1:
                    if (nextHero != null) {
                        heroTwoLabel.setText(nextHero.getName());
                        heroTwoBacklashLabel.setText(Integer.toString(nextHero.getBacklash()));
                        heroTwoConsequencesLabel.setText(Integer.toString(nextHero.getConsequences()));
                    } else {
                        heroTwoLabel.setText("Hero Two");
                        heroTwoConsequencesLabel.setText("0");
                        heroTwoBacklashLabel.setText("0");
                    }
                    break;
                case 2:
                    if (nextHero != null) {
                        heroThreeLabel.setText(nextHero.getName());
                        heroThreeBacklashLabel.setText(Integer.toString(nextHero.getBacklash()));
                        heroThreeConsequencesLabel.setText(Integer.toString(nextHero.getConsequences()));
                    } else {
                        heroThreeLabel.setText("Hero Three");
                        heroThreeConsequencesLabel.setText("0");
                        heroThreeBacklashLabel.setText("0");
                    }
                    break;
                case 3:
                    if (nextHero != null) {
                        heroFourLabel.setText(nextHero.getName());
                        heroFourBacklashLabel.setText(Integer.toString(nextHero.getBacklash()));
                        heroFourConsequencesLabel.setText(Integer.toString(nextHero.getConsequences()));
                    } else {
                        heroFourLabel.setText("Hero Four");
                        heroFourConsequencesLabel.setText("0");
                        heroFourBacklashLabel.setText("0");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Called before showing the mission resolution panel. Resets the current
     * mission details and populates lists which may have changed.
     */
    public void newMission() {

        /* Set up the general mission details. */
        currentMission.resetMission();
        currentMissionLabel.setText(explorationMissionTab.getText());
        minigameChoiceBox.setValue(ConflictType.Skirmish);
        targetFactionChoiceBox.getSelectionModel().selectFirst();
        statusTextArea.clear();
        updateActiveHeroes();

        /* Set up casters. */
        wotaModel.getStronghold().getHeroes().stream().filter((hero) -> (hero.isSpellcaster())).map((hero) -> {
            hero.updateMana(wotaModel.getStronghold());
            return hero;
        }).forEach((hero) -> {
            availableCasters.add(hero);
        });
        casterChoiceBox.setItems(availableCasters);

        /* Quests. */
        wotaModel.getQuests().stream().filter((nextQuest) -> (nextQuest.getAlly().getDisposition() > 4)).forEach((nextQuest) -> {
            availableQuests.add(nextQuest);
        });
        questItemChoiceBox.setItems(availableQuests);

        /* Add allies to factions. */
        currentMission.addAllies();

    }

    public Mission getMission() {
        return currentMission;
    }

    public void setMain(WotAStrategicModel wotaModel) {
        this.wotaModel = wotaModel;
        currentMission = new Mission(wotaModel);
        currentMission.setFaction(wotaModel.getAutarch());

        /* Table for choosing heroes. */
        heroTable.setItems(wotaModel.getHeroes());

        /* Bindings with current mission general difficulty and
         success chance. */
        NumberFormat integerConverter = NumberFormat.getIntegerInstance();
        difficultyTextField.textProperty().bindBidirectional(
                currentMission.difficultyProperty(), integerConverter);
        averageSkillTextField.textProperty().bindBidirectional(
                currentMission.skillProperty(), integerConverter);
        baseDifficultyTextField.textProperty().bindBidirectional(
                currentMission.baseDifficultyProperty(), integerConverter);
        chanceSuccessTextField.textProperty().bindBidirectional(
                currentMission.chanceProperty(), integerConverter);

        /* Construct the target faction choice box. */
        targetFactionChoiceBox.setItems(wotaModel.getFactions());
        targetFactionChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<MinorFaction>() {
                    @Override
                    public void changed(ObservableValue<? extends MinorFaction> ov,
                            MinorFaction oldFaction, MinorFaction newFaction) {
                        if (newFaction == null) {
                            return;
                        }
                        currentMission.setFaction(newFaction);

                        /* Warfare. */
                        factionForcesTableView.setItems(newFaction.getUnits());
                        warfareTableView.setItems(newFaction.getRegions());

                        /* Diplomacy. */
                        diplomacyAvailableTableView.setItems(newFaction.getCurrentGiveTradeChart());
                        diplomacyDesiredTableView.setItems(newFaction.getReceiveTradeChart());
                        diplomacyDispositionLabel.setText(Integer.toString(newFaction.getDisposition()));

                        /* Espionage. */
                        filterDevelopmentsForEspionage(newFaction.getDevelopments());
                        espionageTargetDevelopmentsChoiceBox.setItems(filteredEspionageDevelopments);

                        /* Sabotage. */
                        sabotageTargetDevelopmentsChoiceBox.setItems(newFaction.getDevelopments());

                        /* Faction threats. */
                        threatFactionForcesTableView.setItems(newFaction.getUnits());
                    }
                });

        /* Minigame choice box. */
        minigameChoiceBox.setItems(wotaModel.getMiniGames());
        minigameChoiceBox.valueProperty().bindBidirectional(
                currentMission.minigameProperty());
        minigameChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<ConflictType>() {
                    @Override
                    public void changed(ObservableValue ov,
                            ConflictType oldValue, ConflictType newValue) {
                        currentMission.setMinigame(newValue);
                    }
                });

        /* Assassination. */
        assassinationMinigameChoiceBox.setItems(wotaModel.getMiniGames());

        /* Espionage. */
        espionageTypeChoiceBox.setItems(wotaModel.getEspionageMissions());
        espionageTypeChoiceBox.getSelectionModel().selectFirst();

        /* Sabotage. */
        sabotageTypeChoiceBox.setItems(wotaModel.getSabotageMissions());
        sabotageTypeChoiceBox.getSelectionModel().selectFirst();

        /* Exploration. */
        explorationRegionTable.setItems(wotaModel.getNeutralRegions());

        /* Quests. */
        questItemChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Quest>() {
                    @Override
                    public void changed(ObservableValue ov,
                            Quest oldValue, Quest newValue) {
                        currentMission.setMinigame(newValue.getMinigame());
                        currentMission.setDifficulty(newValue.getDifficulty());
                    }
                });

        /* Warfare. */
        strongholdForcesTableView.setItems(wotaModel.getStronghold().getUnits());

        /* Regional Threat. */
        regionalThreatTable.setItems(wotaModel.getSeasonalThreats().getRegionalThreats());

        /* Diplomatic Threats. */
        diplomaticThreatTable.setItems(wotaModel.getSeasonalThreats().getDiplomaticThreats());

        /* Conquest. */
        strongholdHitChanceTextField.textProperty().bindBidirectional(
                currentMission.strongholdHitChanceProperty(), integerConverter);
        factionHitChanceTextField.textProperty().bindBidirectional(
                currentMission.factionHitChanceProperty(), integerConverter);

        /* Faction Threats. */
        threatStrongholdHitChanceTextField.textProperty().bindBidirectional(
                currentMission.strongholdHitChanceProperty(), integerConverter);
        threatFactionHitChanceTextField.textProperty().bindBidirectional(
                currentMission.factionHitChanceProperty(), integerConverter);
        factionThreatsTable.setItems(wotaModel.getSeasonalThreats().getFactionThreats());
        threatStrongholdForcesTableView.setItems(wotaModel.getStronghold().getUnits());
    }
}
