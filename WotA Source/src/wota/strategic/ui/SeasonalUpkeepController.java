/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.ui;

import javafx.fxml.FXML;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import wota.strategic.model.Development;
import wota.strategic.model.DevelopmentType;
import wota.strategic.model.Mission;
import wota.strategic.model.Region;
import wota.strategic.model.TechTree;
import wota.strategic.model.UnitCost;
import wota.strategic.model.UnitType;

/**
 * FXML Controller class
 *
 * @author plewis
 */
public class SeasonalUpkeepController implements Initializable {

    private WotAStrategicModel wotaModel;
    
    @FXML
    private TextArea statusTextArea;
    
    private final StringBuffer purchasedDevelopments =
            new StringBuffer("Purchased: ");
            
    private Mission currentMission;
    @FXML
    private TextField totalFood;
    @FXML
    private TextField totalTimber;
    @FXML
    private TextField totalOre;
    @FXML
    private TextField totalMana;
    @FXML
    private TextField totalLuxuries;
    @FXML
    private TextField strongholdBP;
    @FXML
    private TextField burganValeBP;
    @FXML
    private TextField crescentHoldBP;
    @FXML
    private TextField lilyManorBP;
    @FXML
    private TextField gravewoodBP;
    @FXML
    private TextField sunridersBP;
    
    /* Developments */
    @FXML
    private ChoiceBox developmentsChoiceBox;
    @FXML
    private TextField devFoodCost;
    @FXML
    private TextField devTimberCost;
    @FXML
    private TextField devOreCost;
    @FXML
    private TextField devManaCost;
    @FXML
    private TextField devLuxuriesCost;
    @FXML
    private TextField devBPCost;
    @FXML
    private ChoiceBox devRegionSelect;
    
    /* Units */
    @FXML
    private ChoiceBox unitsChoiceBox;
    @FXML
    private TextField unitFoodCost;
    @FXML
    private TextField unitTimberCost;
    @FXML
    private TextField unitOreCost;
    @FXML
    private TextField unitManaCost;
    @FXML
    private TextField unitLuxuriesCost;
    @FXML
    private TextField unitBPCost;
    @FXML
    private TextField totalUnits;
    @FXML
    private TextField strongholdPopulation;
    
    @FXML
    private AnchorPane upkeepAnchorPane;
    
    @FXML
    private void finishUpkeep(ActionEvent event) throws Exception {
        wotaModel.hideSeasonalUpkeep();
        wotaModel.showThreatDetermination(currentMission);
    }

    @FXML
    private void purchaseDevelopment(ActionEvent event) {

        Development currentDevelopment =
                wotaModel.createDevelopment(((Development) developmentsChoiceBox.getValue()).getType());
        if(currentDevelopment == null){
            statusTextArea.setText("Choose a development!");
            return;
        }
        
        Region currentRegion = null;
        
        /* If the development is regional, add it to the current region. */
        if(currentDevelopment.isRegional()){
            currentRegion =
                    (Region) devRegionSelect.getValue();
            if(currentRegion == null){
                statusTextArea.setText("Choose region for this development!");
                return;
            }
            currentDevelopment.setRegion(currentRegion);
            currentRegion.getDevelopments().add(currentDevelopment);
        }
        wotaModel.getStronghold().getDevelopments().add(currentDevelopment);
        
        /* Some developments are easiest to track right when purchased. */
        switch(currentDevelopment.getType()){
            case Festival:
                wotaModel.getStronghold().setStability(wotaModel.getStronghold().getStability() + 1);
                break;
            default:
                break;
        }
        
        if(!purchasedDevelopments.toString().equalsIgnoreCase("Purchased: ")){
            purchasedDevelopments.append(", ");
        }
        purchasedDevelopments.append(currentDevelopment.getName());
        
        statusTextArea.setText(purchasedDevelopments.toString());
        
        int bp = Integer.parseInt(devBPCost.getText());
        if(currentDevelopment.getTree() == TechTree.Infiltration){
            int fbp = wotaModel.getStronghold().getGravewoodBP();
            if(fbp >= bp){
                wotaModel.getStronghold().setGravewoodBP(fbp - bp);
                bp = 0;
            }
            else{
                wotaModel.getStronghold().setGravewoodBP(0);
                bp = bp - fbp;
            }
        }
        else if(currentDevelopment.getTree() == TechTree.Arcane){
            int fbp = wotaModel.getStronghold().getBurganValeBP();
            if(fbp >= bp){
                wotaModel.getStronghold().setBurganValeBP(fbp - bp);
                bp = 0;
            }
            else{
                wotaModel.getStronghold().setBurganValeBP(0);
                bp = bp - fbp;
            }
        }
        else if(currentDevelopment.getTree() == TechTree.Diplomacy){
            int fbp = wotaModel.getStronghold().getLilyManorBP();
            if(fbp >= bp){
                wotaModel.getStronghold().setLilyManorBP(fbp - bp);
                bp = 0;
            }
            else{
                wotaModel.getStronghold().setLilyManorBP(0);
                bp = bp - fbp;
            }
        }
        else if(currentDevelopment.getTree() == TechTree.Warfare){
            int fbp = wotaModel.getStronghold().getSunridersBP();
            if(fbp >= bp){
                wotaModel.getStronghold().setSunridersBP(fbp - bp);
                bp = 0;
            }
            else{
                wotaModel.getStronghold().setSunridersBP(0);
                bp = bp - fbp;
            }
        }
        else if(currentDevelopment.getTree() == TechTree.Skirmish){
            int fbp = wotaModel.getStronghold().getCrescentHoldBP();
            if(fbp >= bp){
                wotaModel.getStronghold().setCrescentHoldBP(fbp - bp);
                bp = 0;
            }
            else{
                wotaModel.getStronghold().setCrescentHoldBP(0);
                bp = bp - fbp;
            }
        }
        
        wotaModel.getStronghold().setStrongholdBP(wotaModel.getStronghold().getStrongholdBP() - bp);

        /* Lower totals in Stronghold */
        int food = Integer.parseInt(devFoodCost.getText());
        wotaModel.getStronghold().setFood(wotaModel.getStronghold().getFood() - food);
        int timber = Integer.parseInt(devTimberCost.getText());
        wotaModel.getStronghold().setTimber(wotaModel.getStronghold().getTimber() - timber);
        int ore = Integer.parseInt(devOreCost.getText());
        wotaModel.getStronghold().setOre(wotaModel.getStronghold().getOre() - ore);
        int mana = Integer.parseInt(devManaCost.getText());
        wotaModel.getStronghold().setMana(wotaModel.getStronghold().getMana() - mana);
        int luxuries = Integer.parseInt(devLuxuriesCost.getText());
        wotaModel.getStronghold().setLuxuries(wotaModel.getStronghold().getLuxuries() - luxuries);        

        /* Re-calculate available developments. */
        wotaModel.getStronghold().calculateAvailableDevelopments(
                wotaModel.getDevelopments());
        developmentsChoiceBox.setItems(
                wotaModel.getStronghold().getAvailableDevelopments());
        wotaModel.getStronghold().calculateAvailableUnits( 
                wotaModel.getUnits());
        unitsChoiceBox.setItems(
                wotaModel.getStronghold().getAvailableUnits());
        
        /* May need to bump unit abilities. */
        wotaModel.getStronghold().updateUnitQuality();
    }

    @FXML
    private void purchaseUnit(ActionEvent event) {

        UnitCost currentUnit =
                (UnitCost) unitsChoiceBox.getValue();
        if(currentUnit == null){
            statusTextArea.setText("Please choose unit!");
            return;
        }
        
        /* Increment the number of this troop type. */
        wotaModel.getStronghold().addOneUnit(currentUnit.getUtype());
        
        int bp = Integer.parseInt(unitBPCost.getText());
        if(currentUnit.getUtype() == UnitType.Battle_Mages){
            int fbp = wotaModel.getStronghold().getBurganValeBP();
            if(fbp >= bp){
                wotaModel.getStronghold().setBurganValeBP(fbp - bp);
                bp = 0;
            }
            else{
                wotaModel.getStronghold().setBurganValeBP(0);
                bp = bp - fbp;
            }
        }
        else{
            int fbp = wotaModel.getStronghold().getSunridersBP();
            if(fbp >= bp){
                wotaModel.getStronghold().setSunridersBP(fbp - bp);
                bp = 0;
            }
            else{
                wotaModel.getStronghold().setSunridersBP(0);
                bp = bp - fbp;
            }
        }
        
        wotaModel.getStronghold().setStrongholdBP(wotaModel.getStronghold().getStrongholdBP() - bp);

        /* Lower totals in Stronghold */
        int food = Integer.parseInt(unitFoodCost.getText());
        wotaModel.getStronghold().setFood(wotaModel.getStronghold().getFood() - food);
        int timber = Integer.parseInt(unitTimberCost.getText());
        wotaModel.getStronghold().setTimber(wotaModel.getStronghold().getTimber() - timber);
        int ore = Integer.parseInt(unitOreCost.getText());
        wotaModel.getStronghold().setOre(wotaModel.getStronghold().getOre() - ore);
        int mana = Integer.parseInt(unitManaCost.getText());
        wotaModel.getStronghold().setMana(wotaModel.getStronghold().getMana() - mana);
        int luxuries = Integer.parseInt(unitLuxuriesCost.getText());
        wotaModel.getStronghold().setLuxuries(wotaModel.getStronghold().getLuxuries() - luxuries);        

        /* Re-calculate available developments. */
        wotaModel.getStronghold().calculateAvailableDevelopments(
                wotaModel.getDevelopments());
        developmentsChoiceBox.setItems(
                wotaModel.getStronghold().getAvailableDevelopments());        
        wotaModel.getStronghold().calculateAvailableUnits(
                wotaModel.getUnits());
        unitsChoiceBox.setItems(
                wotaModel.getStronghold().getAvailableUnits());
        
        if(!purchasedDevelopments.toString().equalsIgnoreCase("Purchased: ")){
            purchasedDevelopments.append(", ");
        }
        purchasedDevelopments.append(currentUnit.getName());
        statusTextArea.setText(purchasedDevelopments.toString());
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void adjustSeasonalResources(Mission currentMission){
        this.currentMission = currentMission;
        statusTextArea.clear();
        purchasedDevelopments.setLength(0);
        purchasedDevelopments.append("Purchased: ");
        
        /* 
         * Prior to buying developments, assign consequences,
         * harvest resources, and apply adjustments from ignored threats.
         */
        wotaModel.getSeasonalThreats().applyIgnoredThreats(wotaModel.getStronghold());
        wotaModel.getStronghold().harvestResources(currentMission);
        
        /* 
        Festivals cost an amount based on the number of regions the
        Stronghold has, may need to update that.
        */
        int totalRegions = wotaModel.getStronghold().getRegions().size();
        int totalPopulation = wotaModel.getStronghold().getPopulation();
        Development festival =
                 wotaModel.getDevelopments().get(DevelopmentType.Festival);
        festival.setFood(totalPopulation);
        festival.setLuxury(totalRegions);
        festival.setBp(totalRegions);
        
        /* Determine available developments. */
        wotaModel.getStronghold().calculateAvailableDevelopments(
                wotaModel.getDevelopments());
        developmentsChoiceBox.setItems(
                wotaModel.getStronghold().getAvailableDevelopments());
        wotaModel.getStronghold().calculateAvailableUnits(
                wotaModel.getUnits());
        unitsChoiceBox.setItems(
                wotaModel.getStronghold().getAvailableUnits());

        devRegionSelect.setItems(wotaModel.getStronghold().getRegions());
    }

    public void setMain(WotAStrategicModel wotaModel) {
        this.wotaModel = wotaModel;
      
        NumberFormat integerConverter = NumberFormat.getIntegerInstance();

        totalFood.textProperty().bindBidirectional(
                wotaModel.getStronghold().foodProperty(),
                integerConverter);
        totalTimber.textProperty().bindBidirectional(
                wotaModel.getStronghold().timberProperty(),
                integerConverter);
        totalOre.textProperty().bindBidirectional(
                wotaModel.getStronghold().oreProperty(),
                integerConverter);
        totalMana.textProperty().bindBidirectional(
                wotaModel.getStronghold().manaProperty(),
                integerConverter);
        totalLuxuries.textProperty().bindBidirectional(
                wotaModel.getStronghold().luxuriesProperty(),
                integerConverter);

        strongholdBP.textProperty().bindBidirectional(
                wotaModel.getStronghold().strongholdBPProperty(),
                integerConverter);
        burganValeBP.textProperty().bindBidirectional(
                wotaModel.getStronghold().burganValeBPProperty(),
                integerConverter);
        crescentHoldBP.textProperty().bindBidirectional(
                wotaModel.getStronghold().crescentHoldBPProperty(),
                integerConverter);
        lilyManorBP.textProperty().bindBidirectional(
                wotaModel.getStronghold().lilyManorBPProperty(),
                integerConverter);
        gravewoodBP.textProperty().bindBidirectional(
                wotaModel.getStronghold().gravewoodBPProperty(),
                integerConverter);
        sunridersBP.textProperty().bindBidirectional(
                wotaModel.getStronghold().sunridersBPProperty(),
                integerConverter);
        
        totalUnits.textProperty().bindBidirectional(
                wotaModel.getStronghold().totalUnitsProperty(),
                integerConverter);
        strongholdPopulation.textProperty().bindBidirectional(
                wotaModel.getStronghold().populationProperty(),
                integerConverter);

        /* 
         * Set up listener to populate text boxes when a particular
         * development is selected for purchase.
         */
        developmentsChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Development>() {
            @Override
            public void changed(ObservableValue ov,
                    Development oldValue, Development newValue) {
                if(newValue == null){
                    devFoodCost.setText("0");
                    devTimberCost.setText("0");
                    devOreCost.setText("0");
                    devManaCost.setText("0");
                    devLuxuriesCost.setText("0");
                    devBPCost.setText("0");
                    return;
                }
                devFoodCost.setText(Integer.toString(newValue.getFood()));
                devTimberCost.setText(Integer.toString(newValue.getTimber()));
                devOreCost.setText(Integer.toString(newValue.getOre()));
                devManaCost.setText(Integer.toString(newValue.getMana()));
                devLuxuriesCost.setText(Integer.toString(newValue.getLuxury()));
                devBPCost.setText(Integer.toString(newValue.getBp()));
                statusTextArea.setText(newValue.getDescription());
            }
        });
        
          unitsChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<UnitCost>() {
            @Override
            public void changed(ObservableValue ov,
                    UnitCost oldValue, UnitCost newValue) {
                if(newValue == null){
                    unitFoodCost.setText("0");
                    unitTimberCost.setText("0");
                    unitOreCost.setText("0");
                    unitManaCost.setText("0");
                    unitLuxuriesCost.setText("0");
                    unitBPCost.setText("0");
                    return;
                }
                unitFoodCost.setText(Integer.toString(newValue.getFood()));
                unitTimberCost.setText(Integer.toString(newValue.getTimber()));
                unitOreCost.setText(Integer.toString(newValue.getOre()));
                unitManaCost.setText(Integer.toString(newValue.getMana()));
                unitLuxuriesCost.setText(Integer.toString(newValue.getLuxury()));
                unitBPCost.setText(Integer.toString(newValue.getBp()));
                statusTextArea.setText(newValue.getDescription());
            }
        });
    }
}
