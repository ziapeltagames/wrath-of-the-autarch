/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.ui;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.IntegerStringConverter;
import wota.strategic.model.Condition;
import wota.strategic.model.Development;
import wota.strategic.model.Hero;
import wota.strategic.model.OrderOfBattle;
import wota.strategic.model.MinorFaction;
import wota.strategic.model.Region;
import wota.strategic.model.Trade;
import wota.strategic.model.TradeResource;

/**
 *
 * @author plewis
 */
public class TheRealmController implements Initializable {

    /* Reference to the main strategic model. */
    private WotAStrategicModel wotaModel;
    @FXML
    private TextField currentSeason;
    @FXML
    private TextField currentYear;

    private Stage mainStage;
    private File saveDirectory;

    /* Miscellaneous stronghold fields. */
    @FXML
    private TextField strongholdName;
    @FXML
    private TextField strongholdStability;
    @FXML
    private TextField strongholdPopulation;
    @FXML
    private TextField strongholdBPTextField;
    @FXML
    private TextField burganValeBPTextField;
    @FXML
    private TextField crescentHoldBPTextField;
    @FXML
    private TextField gravewoodBPTextField;
    @FXML
    private TextField lilyManorBPTextField;
    @FXML
    private TextField sunridersBPTextField;
    @FXML
    private TextField strongholdFoodTextField;
    @FXML
    private TextField strongholdManaTextField;
    @FXML
    private TextField strongholdLuxuriesTextField;
    @FXML
    private TextField strongholdOreTextField;
    @FXML
    private TextField strongholdTimberTextField;

    /* Minor faction fields. */
    @FXML
    private TextField burganValeDisposition;
    @FXML
    private TextField burganValeStability;
    @FXML
    private TextField burganValePopulation;
    @FXML
    private TextField crescentHoldDisposition;
    @FXML
    private TextField crescentHoldStability;
    @FXML
    private TextField crescentHoldPopulation;
    @FXML
    private TextField gravewoodDisposition;
    @FXML
    private TextField gravewoodStability;
    @FXML
    private TextField gravewoodPopulation;
    @FXML
    private TextField lilyManorDisposition;
    @FXML
    private TextField lilyManorStability;
    @FXML
    private TextField lilyManorPopulation;
    @FXML
    private TextField sunridersDisposition;
    @FXML
    private TextField sunridersStability;
    @FXML
    private TextField sunridersPopulation;
    @FXML
    private TextField autarchDisposition;
    @FXML
    private TextField autarchStability;
    @FXML
    private TextField autarchPopulation;
    @FXML
    private TextField autarchThreatTextField;
    @FXML
    private TextField autarchDiplomacyTextField;
    @FXML
    private TextField autarchInfiltrationTextField;
    @FXML
    private TextField autarchSkirmishTextField;
    @FXML
    private TextField autarchWarfareTextField;

    /* Fields for hero table. */
    @FXML
    private TableView<Hero> heroTable;
    @FXML
    private TableColumn<Hero, String> heroNameColumn;
    @FXML
    private TableColumn<Hero, Integer> heroRankColumn;
    @FXML
    private TableColumn<Hero, Integer> heroConsequencesColumn;
    @FXML
    private TableColumn<Hero, Integer> heroXPColumn;
    @FXML
    private TableColumn<Hero, Boolean> heroSpellcasterColumn;
    @FXML
    private TableColumn<Hero, Integer> heroBacklashColumn;
    @FXML
    private TableColumn<Hero, Object> heroMajorColumn;
    @FXML
    private TableColumn<Hero, Object> heroMinorColumn;
    @FXML
    private TableColumn<Hero, Object> heroRegionColumn;

    /* Fields for regions table. */
    @FXML
    private TableView<Region> regionTable;
    @FXML
    private TableColumn<Region, String> regionNameColumn;
    @FXML
    private TableColumn<Region, Integer> regionFoodColumn;
    @FXML
    private TableColumn<Region, Integer> regionTimberColumn;
    @FXML
    private TableColumn<Region, Integer> regionOreColumn;
    @FXML
    private TableColumn<Region, Integer> regionManaColumn;
    @FXML
    private TableColumn<Region, Integer> regionLuxuriesColumn;
    @FXML
    private TableColumn<Region, Integer> regionConsequencesColumn;
    @FXML
    private TableColumn<Region, Boolean> regionThreatColumn;
    @FXML
    private TableColumn<Region, String> regionDevelopmentsColumn;

    /* Fields for developments table. */
    @FXML
    private TableView<Development> developmentsTable;
    @FXML
    private TableColumn<Development, String> developmentsNameColumn;
    @FXML
    private TableColumn<Development, Object> developmentsRegionColumn;
    @FXML
    private TableColumn<Development, String> developmentsEffectColumn;

    /* Units table. */
    @FXML
    private TableView<OrderOfBattle> unitsTableView;
    @FXML
    private TableColumn<OrderOfBattle, Object> unitsTypeColumn;
    @FXML
    private TableColumn<OrderOfBattle, Object> unitsQualityColumn;    
    @FXML
    private TableColumn<OrderOfBattle, Integer> unitsAvailableColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> unitsOffenseColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> unitsDefenseColumn;
    @FXML
    private TableColumn<OrderOfBattle, Integer> unitsMoraleColumn;

    /* Fields for trade table. */
    @FXML
    private TableView<Trade> tradesTable;
    @FXML
    private TableColumn<Trade, MinorFaction> tradesFactionTableColumn;
    @FXML
    private TableColumn<Trade, TradeResource> tradesGiveTableColumn;
    @FXML
    private TableColumn<Trade, Integer> tradesGiveAmountTableColumn;
    @FXML
    private TableColumn<Trade, TradeResource> tradesReceiveTableColumn;
    @FXML
    private TableColumn<Trade, Integer> tradesReceiveAmountTableColumn;
    @FXML
    private TableColumn<Trade, Integer> tradesSeasonsRemainingTableColumn;

    /* Conditions table. */
    @FXML
    private TableView<Condition> conditionsTableView;
    @FXML
    private TableColumn<Condition, String> conditionsTypeTableColumn;
    @FXML
    private TableColumn<Condition, Integer> conditionsSeasonTableColumn;
    @FXML
    private TableColumn<Condition, Object> conditionsFactionTableColumn;
    @FXML
    private TableColumn<Condition, Object> conditionsHeroTableColumn;
    @FXML
    private TableColumn<Condition, Object> conditionsRegionTableColumn;
    @FXML
    private TableColumn<Condition, Object> conditionsDevelopmentTableColumn;
    
    /* Listings of all available developments */
    @FXML
    private TableView<Development> listingsTableView;
    @FXML
    private TableColumn<Development, String> listingsNameColumn;
    @FXML
    private TableColumn<Development, Object> listingsTypeColumn;    
    @FXML
    private TableColumn<Development, Integer> listingsBPColumn;    
    @FXML
    private TableColumn<Development, Integer> listingsTimberColumn;    
    @FXML
    private TableColumn<Development, Integer> listingsOreColumn;    
    @FXML
    private TableColumn<Development, Integer> listingsManaColumn;    
    @FXML
    private TableColumn<Development, Integer> listingsLuxColumn;  
    @FXML
    private TableColumn<Development, Object> listingsPrerequisitesColumn;       
    @FXML
    private TableColumn<Development, String> listingsEffectColumn;       

    public TheRealmController() {
    }

    /*
     * Advance the controller one turn.
     */
    @FXML
    private void takeTurn(ActionEvent event) throws Exception {
        wotaModel.showMissionResolution();
    }

    /*
     The user selects a save file to use. The game is saved once when the
     file is chosen, and then automatically at the start of every year.
     */
    @FXML
    private void saveFileAs(ActionEvent event) {    
        /* Check to see if the save file exists. */
        File selectedFile = selectFile(false);
        if (selectedFile != null) {
            wotaModel.setSaveFile(selectedFile);
            wotaModel.saveGame();
        }
    }
    
    @FXML
    private void saveFile(ActionEvent event){
        /* If a save file already exists, just use that one. */
        if(wotaModel.hasSaveFile()){
            wotaModel.saveGame();
            return;
        }
        
        /* Check to see if the save file exists. */
        File selectedFile = selectFile(false);
        if (selectedFile != null) {
            wotaModel.setSaveFile(selectedFile);
            wotaModel.saveGame();
        }
    }

    @FXML
    private void loadFile(ActionEvent event) {
        File selectedFile = selectFile(true);
        if (selectedFile != null) {
            wotaModel.loadGame(selectedFile);
        }
    }

    /* Choose a file for loading or for saving. */
    private File selectFile(boolean open) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select WotA Save File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("XML Files", "*.xml"));
        fileChooser.setInitialDirectory(saveDirectory);
        File selectedFile;
        if (open) {
            selectedFile = fileChooser.showOpenDialog(mainStage);
        } else {
            selectedFile = fileChooser.showSaveDialog(mainStage);
        }
        return selectedFile;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /*
         Map hero table to the Hero objects within the Stronghold.
         */
        heroNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        heroNameColumn.setCellFactory((TableColumn<Hero, String> p) -> new TextFieldTableCell(new DefaultStringConverter()));

        heroRankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        heroRankColumn.setCellFactory((TableColumn<Hero, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        heroConsequencesColumn.setCellValueFactory(new PropertyValueFactory<>("consequences"));
        heroConsequencesColumn.setCellFactory((TableColumn<Hero, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        heroXPColumn.setCellValueFactory(new PropertyValueFactory<>("xp"));
        heroXPColumn.setCellFactory((TableColumn<Hero, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        heroSpellcasterColumn.setCellValueFactory(new PropertyValueFactory<>("spellcaster"));
        heroSpellcasterColumn.setCellFactory((TableColumn<Hero, Boolean> p) -> new CheckBoxTableCell<>());

        heroBacklashColumn.setCellValueFactory(new PropertyValueFactory<>("backlash"));
        heroBacklashColumn.setCellFactory((TableColumn<Hero, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        heroMajorColumn.setCellValueFactory(new PropertyValueFactory<>("majorFocus"));
        heroMajorColumn.setCellFactory((TableColumn<Hero, Object> p) -> new ChoiceBoxTableCell(wotaModel.getMiniGames()));

        heroMinorColumn.setCellValueFactory(new PropertyValueFactory<>("minorFocus"));
        heroMinorColumn.setCellFactory((TableColumn<Hero, Object> p) -> new ChoiceBoxTableCell(wotaModel.getMiniGames()));

        heroRegionColumn.setCellValueFactory(new PropertyValueFactory<>("manaRegion"));
        heroRegionColumn.setCellFactory((TableColumn<Hero, Object> p) -> new ChoiceBoxTableCell(wotaModel.getStronghold().getRegions()));

        /* 
         Map regions table to regions within the Stronghold.
         */
        regionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        regionNameColumn.setCellFactory((TableColumn<Region, String> p) -> new TextFieldTableCell(new DefaultStringConverter()));

        regionFoodColumn.setCellValueFactory(new PropertyValueFactory<>("food"));
        regionFoodColumn.setCellFactory((TableColumn<Region, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        regionTimberColumn.setCellValueFactory(new PropertyValueFactory<>("timber"));
        regionTimberColumn.setCellFactory((TableColumn<Region, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        regionOreColumn.setCellValueFactory(new PropertyValueFactory<>("ore"));
        regionOreColumn.setCellFactory((TableColumn<Region, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        regionManaColumn.setCellValueFactory(new PropertyValueFactory<>("mana"));
        regionManaColumn.setCellFactory((TableColumn<Region, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        regionLuxuriesColumn.setCellValueFactory(new PropertyValueFactory<>("luxuries"));
        regionLuxuriesColumn.setCellFactory((TableColumn<Region, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        regionConsequencesColumn.setCellValueFactory(new PropertyValueFactory<>("consequences"));
        regionConsequencesColumn.setCellFactory((TableColumn<Region, Integer> p) -> new TextFieldTableCell(new IntegerStringConverter()));

        regionDevelopmentsColumn.setCellValueFactory(new PropertyValueFactory<>("developmentsString"));

        regionThreatColumn.setCellValueFactory(new PropertyValueFactory<>("threat"));

        /*
         Map developments table to developments within the Stronghold.
         */
        developmentsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        developmentsRegionColumn.setCellValueFactory(new PropertyValueFactory<>("region"));
        developmentsEffectColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        /*
         Map units table to units within the Stronghold.
         */
        unitsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("utype"));
        unitsQualityColumn.setCellValueFactory(new PropertyValueFactory<>("quality"));
        unitsAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        unitsOffenseColumn.setCellValueFactory(new PropertyValueFactory<>("offense"));
        unitsDefenseColumn.setCellValueFactory(new PropertyValueFactory<>("defense"));
        unitsMoraleColumn.setCellValueFactory(new PropertyValueFactory<>("morale"));

        /*
         Map trades table to trades within the Stronghold.
         */
        tradesFactionTableColumn.setCellValueFactory(new PropertyValueFactory<>("receiveFaction"));
        tradesGiveTableColumn.setCellValueFactory(new PropertyValueFactory<>("giveResource"));
        tradesGiveAmountTableColumn.setCellValueFactory(new PropertyValueFactory<>("giveAmount"));
        tradesReceiveTableColumn.setCellValueFactory(new PropertyValueFactory<>("receiveResource"));
        tradesReceiveAmountTableColumn.setCellValueFactory(new PropertyValueFactory<>("receiveAmount"));
        tradesSeasonsRemainingTableColumn.setCellValueFactory(new PropertyValueFactory<>("seasonsRemaining"));

        /*
         Map to active conditions within the Stronghold.
         */
        conditionsTypeTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        conditionsSeasonTableColumn.setCellValueFactory(new PropertyValueFactory<>("seasons"));
        conditionsFactionTableColumn.setCellValueFactory(new PropertyValueFactory<>("faction"));
        conditionsHeroTableColumn.setCellValueFactory(new PropertyValueFactory<>("hero"));
        conditionsDevelopmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("development"));
        conditionsRegionTableColumn.setCellValueFactory(new PropertyValueFactory<>("region"));
        
        /* Listings of all developments */
        listingsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        listingsTypeColumn.setCellValueFactory(new PropertyValueFactory<>("tree"));
        listingsBPColumn.setCellValueFactory(new PropertyValueFactory<>("bp"));
        listingsTimberColumn.setCellValueFactory(new PropertyValueFactory<>("timber"));
        listingsOreColumn.setCellValueFactory(new PropertyValueFactory<>("ore"));
        listingsManaColumn.setCellValueFactory(new PropertyValueFactory<>("mana"));
        listingsLuxColumn.setCellValueFactory(new PropertyValueFactory<>("luxury"));
        listingsPrerequisitesColumn.setCellValueFactory(new PropertyValueFactory<>("prerequisites"));
        listingsEffectColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    /*
     Reference to the main model which holds the state of the game.
     */
    public void setMain(WotAStrategicModel wotaModel, Stage mainStage) {
        this.wotaModel = wotaModel;

        this.mainStage = mainStage;
        saveDirectory = Paths.get(System.getProperty("user.home"),
                "Documents", "WotA").toFile();
        if(!saveDirectory.exists()){
            saveDirectory.mkdir();
        }

        heroTable.setItems(wotaModel.getHeroes());
        regionTable.setItems(wotaModel.getStronghold().getRegions());
        developmentsTable.setItems(wotaModel.getStronghold().getDevelopments());
        listingsTableView.setItems(wotaModel.getDevelopmentList());
        unitsTableView.setItems(wotaModel.getStronghold().getUnits());
        tradesTable.setItems(wotaModel.getStronghold().getActiveTrades());
        conditionsTableView.setItems(wotaModel.getStronghold().getConditions());

        /* Create a formatter to go between Integer and String. */
        NumberFormat integerConverter = NumberFormat.getIntegerInstance();

        /* Bind the miscellaneous fields to the backing model. */
        strongholdName.textProperty().bindBidirectional(wotaModel.getStronghold().nameProperty());
        strongholdStability.textProperty().bindBidirectional(
                wotaModel.getStronghold().stabilityProperty(),
                integerConverter);
        strongholdPopulation.textProperty().bindBidirectional(
                wotaModel.getStronghold().populationProperty(),
                integerConverter);
        currentSeason.textProperty().bindBidirectional(
                wotaModel.seasonProperty(), integerConverter);
        currentYear.textProperty().bindBidirectional(
                wotaModel.yearProperty(), integerConverter);

        /* Bind the total build points and resources. */
        strongholdBPTextField.textProperty().bindBidirectional(
                wotaModel.getStronghold().strongholdBPProperty(),
                integerConverter);
        burganValeBPTextField.textProperty().bindBidirectional(
                wotaModel.getStronghold().burganValeBPProperty(),
                integerConverter);
        crescentHoldBPTextField.textProperty().bindBidirectional(
                wotaModel.getStronghold().crescentHoldBPProperty(),
                integerConverter);
        lilyManorBPTextField.textProperty().bindBidirectional(
                wotaModel.getStronghold().lilyManorBPProperty(),
                integerConverter);
        gravewoodBPTextField.textProperty().bindBidirectional(
                wotaModel.getStronghold().gravewoodBPProperty(),
                integerConverter);
        sunridersBPTextField.textProperty().bindBidirectional(
                wotaModel.getStronghold().sunridersBPProperty(),
                integerConverter);
        strongholdFoodTextField.textProperty().bindBidirectional(
                wotaModel.getStronghold().foodProperty(),
                integerConverter);
        strongholdTimberTextField.textProperty().bindBidirectional(
                wotaModel.getStronghold().timberProperty(),
                integerConverter);
        strongholdOreTextField.textProperty().bindBidirectional(
                wotaModel.getStronghold().oreProperty(),
                integerConverter);
        strongholdManaTextField.textProperty().bindBidirectional(
                wotaModel.getStronghold().manaProperty(),
                integerConverter);
        strongholdLuxuriesTextField.textProperty().bindBidirectional(
                wotaModel.getStronghold().luxuriesProperty(),
                integerConverter);

        /* Bind the minor faction attributes. */
        burganValeDisposition.textProperty().bindBidirectional(
                wotaModel.getFaction("Burgan Vale").dispositionProperty(),
                integerConverter);
        burganValePopulation.textProperty().bindBidirectional(
                wotaModel.getFaction("Burgan Vale").populationProperty(),
                integerConverter);
        burganValeStability.textProperty().bindBidirectional(
                wotaModel.getFaction("Burgan Vale").stabilityProperty(),
                integerConverter);
        crescentHoldDisposition.textProperty().bindBidirectional(
                wotaModel.getFaction("Crescent Hold").dispositionProperty(),
                integerConverter);
        crescentHoldPopulation.textProperty().bindBidirectional(
                wotaModel.getFaction("Crescent Hold").populationProperty(),
                integerConverter);
        crescentHoldStability.textProperty().bindBidirectional(
                wotaModel.getFaction("Crescent Hold").stabilityProperty(),
                integerConverter);
        lilyManorDisposition.textProperty().bindBidirectional(
                wotaModel.getFaction("Lily Manor").dispositionProperty(),
                integerConverter);
        lilyManorPopulation.textProperty().bindBidirectional(
                wotaModel.getFaction("Lily Manor").populationProperty(),
                integerConverter);
        lilyManorStability.textProperty().bindBidirectional(
                wotaModel.getFaction("Lily Manor").stabilityProperty(),
                integerConverter);
        gravewoodDisposition.textProperty().bindBidirectional(
                wotaModel.getFaction("Gravewood").dispositionProperty(),
                integerConverter);
        gravewoodPopulation.textProperty().bindBidirectional(
                wotaModel.getFaction("Gravewood").populationProperty(),
                integerConverter);
        gravewoodStability.textProperty().bindBidirectional(
                wotaModel.getFaction("Gravewood").stabilityProperty(),
                integerConverter);
        sunridersDisposition.textProperty().bindBidirectional(
                wotaModel.getFaction("Sunriders").dispositionProperty(),
                integerConverter);
        sunridersPopulation.textProperty().bindBidirectional(
                wotaModel.getFaction("Sunriders").populationProperty(),
                integerConverter);
        sunridersStability.textProperty().bindBidirectional(
                wotaModel.getFaction("Sunriders").stabilityProperty(),
                integerConverter);

        /* Autarch attributes */
        autarchDisposition.textProperty().bindBidirectional(
                wotaModel.getAutarch().dispositionProperty(),
                integerConverter);
        autarchPopulation.textProperty().bindBidirectional(
                wotaModel.getAutarch().populationProperty(),
                integerConverter);
        autarchStability.textProperty().bindBidirectional(
                wotaModel.getAutarch().stabilityProperty(),
                integerConverter);

        autarchThreatTextField.textProperty().bindBidirectional(
                wotaModel.getAutarch().unallocatedThreatPoolProperty(),
                integerConverter);
        autarchDiplomacyTextField.textProperty().bindBidirectional(
                wotaModel.getAutarch().diplomacyPoolProperty(),
                integerConverter);
        autarchInfiltrationTextField.textProperty().bindBidirectional(
                wotaModel.getAutarch().infiltrationPoolProperty(),
                integerConverter);
        autarchSkirmishTextField.textProperty().bindBidirectional(
                wotaModel.getAutarch().skirmishPoolProperty(),
                integerConverter);
        autarchWarfareTextField.textProperty().bindBidirectional(
                wotaModel.getAutarch().warfarePoolProperty(),
                integerConverter);
    }
}
