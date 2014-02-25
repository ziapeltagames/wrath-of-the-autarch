/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.ui;

import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import wota.strategic.model.Mission;
import wota.strategic.model.Region;

/**
 *
 * @author plewis
 */
public class ExplorationMissionCheckBoxTableCell extends TableCell<Region, Boolean> {

    private CheckBox checkBox;
    private final WotAStrategicModel wotaModel;
    private final Mission currentMission;

    public ExplorationMissionCheckBoxTableCell(WotAStrategicModel wotaModel,
            Mission currentMission) {
        this.wotaModel = wotaModel;
        this.currentMission = currentMission;
        setText(null);
        createCheckbox();
        setGraphic(checkBox);
    }

    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            checkBox.setSelected(item);
            setGraphic(checkBox);
        }
    }

    private void createCheckbox() {
        checkBox = new CheckBox();
        checkBox.setOnMouseClicked((MouseEvent event) -> {
            Region selectedRegion = (Region) getTableRow().getItem();
            ObservableList<Region> regions = wotaModel.getNeutralRegions();
            regions.stream().forEach((region) -> {
                if(region.getName().equalsIgnoreCase(selectedRegion.getName())){
                    selectedRegion.setSelected(true);
                    currentMission.setDifficulty(selectedRegion.getDifficulty());
                    currentMission.setMinigame(selectedRegion.getMinigame());
                }
                else{
                    region.setSelected(false);
                }
            });
        });
        
    }

    private boolean getBoolean() {
        return getItem() == null ? false : getItem().booleanValue();
    }
}
