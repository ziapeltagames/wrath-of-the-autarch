/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import wota.strategic.model.Development;
import wota.strategic.model.Threat;
import wota.strategic.model.Mission;

/**
 * FXML Controller class
 *
 * @author plewis
 */
public class ThreatDeterminationController implements Initializable {

    private WotAStrategicModel wotaModel;
    private Mission currentMission;

    @FXML
    private TableView<Threat> threatsTable;
    @FXML
    private TableColumn<Threat, String> threatNameTableColumn;
    @FXML
    private TableColumn<Threat, Object> threatFactionTableColumn;
    @FXML
    private TableColumn<Threat, Object> threatConflictTypeTableColumn;
    @FXML
    private TableColumn<Threat, Integer> threatDifficultyTableColumn;
    @FXML
    private TableColumn<Threat, Object> threatTargetRegionTableColumn;
    @FXML
    private TableColumn<Threat, Object> threatTargetFactionTableColumn;
    @FXML
    private TableColumn<Threat, Object> threatTargetHeroTableColumn;
    @FXML
    private TableColumn<Threat, Object> threatTargetDevelopmentTableColumn;

    @FXML
    private Label skirmishUses;
    @FXML
    private Label skirmishMaxDifficulty;
    @FXML
    private Label diplomacyUses;
    @FXML
    private Label diplomacyMaxDifficulty;
    @FXML
    private Label infiltrationUses;
    @FXML
    private Label infiltrationMaxDifficulty;
    @FXML
    private Label warfareUses;
    @FXML
    private Label warfareMaxDifficulty;
    
    @FXML
    private Button removeSkirmishThreatButton;
    @FXML
    private Button removeInfiltrationThreatButton;
    @FXML
    private Button removeWarfareThreatButton;
    @FXML
    private Button removeDiplomacyThreatButton;    

    @FXML
    private TextArea statusTextArea;
    @FXML
    private AnchorPane threatAnchorPane;

    @FXML
    private void finishThreats(ActionEvent event) throws Exception {

        /* Divvy up the various threats into their appropriate lists. */
        for(Threat threat : wotaModel.getSeasonalThreats().getPreSortedThreats()){
            switch(threat.getFactionThreatType()){
                case Diplomatic_Threat:
                    wotaModel.getSeasonalThreats().getDiplomaticThreats().add(threat);
                    break;
                case Regional_Threat:
                    wotaModel.getSeasonalThreats().getRegionalThreats().add(threat);
                    break;
                default:
                    wotaModel.getSeasonalThreats().getFactionThreats().add(threat);
                    break;
            }
        }
        
        wotaModel.getSeasonalThreats().getPreSortedThreats().clear();

        /* Calculate XP, and healing or training. Also manage food. */
        wotaModel.getStronghold().endSeason(currentMission);

        /* 
         Clear out all lingering state from missions before starting
         the next season.
         */
        wotaModel.clearState();

        wotaModel.hideThreatDetermination();
    }

    @FXML
    private void removeInfiltrationThreat(ActionEvent event) {
        Threat selectedThreat
                = threatsTable.getSelectionModel().getSelectedItem();
        if (selectedThreat != null) {
            boolean remove = checkInfiltrationThreat(selectedThreat);
            if(!remove){
                return;
            }
            wotaModel.getSeasonalThreats().getPreSortedThreats().remove(selectedThreat);
        }
        setupDefenseButtons();
    }

    private boolean checkInfiltrationThreat(Threat selectedThreat){
        int infLevel = Integer.parseInt(infiltrationMaxDifficulty.getText());
        if(infLevel > selectedThreat.getDifficulty()){
            return false;
        }
        /* Use the next infiltration development. */
        for(Development nextDev : wotaModel.getStronghold().getDevelopments()){
            switch(nextDev.getType()){
                case Great_Spies:
                    nextDev.setUsed(true);
                    return true;
                case Superb_Spies:
                    nextDev.setUsed(true);
                    return true;
                case Fantastic_Spies:
                    nextDev.setUsed(true);
                    return true;
            }
        }
        return false;
    }
    
    @FXML
    private void removeDiplomacyThreat(ActionEvent event) {
        Threat selectedThreat
                = threatsTable.getSelectionModel().getSelectedItem();
        if (selectedThreat != null) {
            boolean remove = checkDiplomacyThreat(selectedThreat);
            if(!remove){
                return;
            }
            wotaModel.getSeasonalThreats().getPreSortedThreats().remove(selectedThreat);
        }
        setupDefenseButtons();
    }
    
    private boolean checkDiplomacyThreat(Threat selectedThreat){
        int infLevel = Integer.parseInt(diplomacyMaxDifficulty.getText());
        if(infLevel > selectedThreat.getDifficulty()){
            return false;
        }
        /* Use the next infiltration development. */
        for(Development nextDev : wotaModel.getStronghold().getDevelopments()){
            switch(nextDev.getType()){
                case Great_Diplomats:
                    nextDev.setUsed(true);
                    return true;
                case Superb_Diplomats:
                    nextDev.setUsed(true);
                    return true;
                case Fantastic_Diplomats:
                    nextDev.setUsed(true);
                    return true;
            }
        }
        return false;
    }    

    @FXML
    private void removeWarfareThreat(ActionEvent event) {
        Threat selectedThreat
                = threatsTable.getSelectionModel().getSelectedItem();
        if (selectedThreat != null) {
            boolean remove = checkWarfareThreat(selectedThreat);
            if(!remove){
                return;
            }            
            wotaModel.getSeasonalThreats().getPreSortedThreats().remove(selectedThreat);
        }
        setupDefenseButtons();
    }
    
    private boolean checkWarfareThreat(Threat selectedThreat){
        int infLevel = Integer.parseInt(warfareMaxDifficulty.getText());
        if(infLevel > selectedThreat.getDifficulty()){
            return false;
        }
        /* Use the next infiltration development. */
        for(Development nextDev : wotaModel.getStronghold().getDevelopments()){
            switch(nextDev.getType()){
                case Barracks:
                    nextDev.setUsed(true);
                    return true;
                case Improved_Barracks:
                    nextDev.setUsed(true);
                    return true;
                case Advanced_Barracks:
                    nextDev.setUsed(true);
                    return true;
            }
        }
        return false;
    }    

    @FXML
    private void removeSkirmishThreat(ActionEvent event) {
        Threat selectedThreat
                = threatsTable.getSelectionModel().getSelectedItem();
        if (selectedThreat != null) {
            boolean remove = checkSkirmishThreat(selectedThreat);
            if(!remove){
                return;
            }            
            wotaModel.getSeasonalThreats().getPreSortedThreats().remove(selectedThreat);
        }
        setupDefenseButtons();
    }
    
    private boolean checkSkirmishThreat(Threat selectedThreat){
        int infLevel = Integer.parseInt(skirmishMaxDifficulty.getText());
        if(infLevel > selectedThreat.getDifficulty()){
            return false;
        }
        /* Use the next infiltration development. */
        for(Development nextDev : wotaModel.getStronghold().getDevelopments()){
            switch(nextDev.getType()){
                case Great_Guard_Force:
                    nextDev.setUsed(true);
                    return true;
                case Superb_Guard_Force:
                    nextDev.setUsed(true);
                    return true;
                case Fantastic_Guard_Force:
                    nextDev.setUsed(true);
                    return true;
            }
        }
        return false;
    }    

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        threatNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        threatConflictTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("minigame"));
        threatDifficultyTableColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        threatFactionTableColumn.setCellValueFactory(new PropertyValueFactory<>("minorFaction"));
        threatTargetDevelopmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("targetDevelopment"));
        threatTargetFactionTableColumn.setCellValueFactory(new PropertyValueFactory<>("targetFaction"));
        threatTargetHeroTableColumn.setCellValueFactory(new PropertyValueFactory<>("targetHero"));
        threatTargetRegionTableColumn.setCellValueFactory(new PropertyValueFactory<>("targetRegion"));
    }

    /* Called at the end of each season. */
    public void determineThreats(Mission currentMission) {
        this.currentMission = currentMission;
        statusTextArea.clear();
        wotaModel.getSeasonalThreats().determineThreats();
        threatsTable.setItems(wotaModel.getSeasonalThreats().getPreSortedThreats());
        setupDefenseButtons();
    }

    private void setupDefenseButtons() {
        setupDefenseUses();
        setupDefenseLevels();
    }

    private void setupDefenseUses() {
        int skirmishUsesInt = 0;
        int infUsesInt = 0;
        int dipUsesInt = 0;
        int warfareUsesInt = 0;

        for (Development development : wotaModel.getStronghold().getDevelopments()) {

            if (development.isUsed()) {
                continue;
            }

            switch (development.getType()) {
                case Great_Guard_Force:
                    skirmishUsesInt++;
                    break;
                case Superb_Guard_Force:
                    skirmishUsesInt++;
                    break;
                case Fantastic_Guard_Force:
                    skirmishUsesInt++;
                    break;
                case Great_Spies:
                    infUsesInt++;
                    break;
                case Superb_Spies:
                    infUsesInt++;
                    break;
                case Fantastic_Spies:
                    infUsesInt++;
                    break;
                case Great_Diplomats:
                    dipUsesInt++;
                    break;
                case Superb_Diplomats:
                    dipUsesInt++;
                    break;
                case Fantastic_Diplomats:
                    dipUsesInt++;
                    break;
                case Barracks:
                    warfareUsesInt++;
                    break;
                case Advanced_Barracks:
                    warfareUsesInt++;
                    break;
                case Improved_Barracks:
                    warfareUsesInt++;
                    break;
            }
        }

        skirmishUses.setText(Integer.toString(skirmishUsesInt));
        infiltrationUses.setText(Integer.toString(infUsesInt));
        diplomacyUses.setText(Integer.toString(dipUsesInt));
        warfareUses.setText(Integer.toString(warfareUsesInt));
        
        if(skirmishUsesInt > 0){
            removeSkirmishThreatButton.setDisable(false);
        }
        else{
            removeSkirmishThreatButton.setDisable(true);
        }
        
        if(dipUsesInt > 0){
            removeDiplomacyThreatButton.setDisable(false);
        }
        else{
            removeDiplomacyThreatButton.setDisable(true);
        }
        
        if(infUsesInt > 0){
            removeInfiltrationThreatButton.setDisable(false);
        }
        else{
            removeInfiltrationThreatButton.setDisable(true);
        }
        
        if(warfareUsesInt > 0){
            removeWarfareThreatButton.setDisable(false);
        }
        else{
            removeWarfareThreatButton.setDisable(true);
        }        
    }

    private void setupDefenseLevels() {
        int skirmishLevel = 4;
        int infiltrationLevel = 4;
        int diplomacyLevel = 4;
        int warfareLevel = 4;

        for (Development development : wotaModel.getStronghold().getDevelopments()) {

            switch (development.getType()) {
                case Great_Guard_Force:
                    skirmishLevel++;
                    break;
                case Superb_Guard_Force:
                    skirmishLevel++;
                    break;
                case Fantastic_Guard_Force:
                    skirmishLevel++;
                    break;
                case Great_Spies:
                    infiltrationLevel++;
                    break;
                case Superb_Spies:
                    infiltrationLevel++;
                    break;
                case Fantastic_Spies:
                    infiltrationLevel++;
                    break;
                case Great_Diplomats:
                    diplomacyLevel++;
                    break;
                case Superb_Diplomats:
                    diplomacyLevel++;
                    break;
                case Fantastic_Diplomats:
                    diplomacyLevel++;
                    break;
                case Barracks:
                    warfareLevel++;
                    break;
                case Advanced_Barracks:
                    warfareLevel++;
                    break;
                case Improved_Barracks:
                    warfareLevel++;
                    break;
            }
        }

        skirmishMaxDifficulty.setText(Integer.toString(skirmishLevel));
        infiltrationMaxDifficulty.setText(Integer.toString(infiltrationLevel));
        diplomacyMaxDifficulty.setText(Integer.toString(diplomacyLevel));
        warfareMaxDifficulty.setText(Integer.toString(warfareLevel));
    }

    public void setMain(WotAStrategicModel wotaModel) {
        this.wotaModel = wotaModel;
    }
}
