/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.ui;

import java.util.Iterator;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import wota.strategic.model.Mission;
import wota.strategic.model.Threat;

/**
 *
 * @author plewis
 */
public class RegionalThreatMissionCheckBoxTableCell extends TableCell<Threat, Boolean> {

    private CheckBox checkBox;
    private final WotAStrategicModel wotaModel;
    private final Mission currentMission;

    public RegionalThreatMissionCheckBoxTableCell(WotAStrategicModel wotaModel,
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
        checkBox.setOnMouseClicked(new EventHandler<MouseEvent>() {            
            @Override
            public void handle(MouseEvent event) {
                Threat selectedRegionalThreat = (Threat) getTableRow().getItem();
                ObservableList<Threat> regionalThreats = wotaModel.getSeasonalThreats().getRegionalThreats();
                for (Iterator<Threat> it = regionalThreats.iterator(); it.hasNext();) {
                    Threat regionalThreat = it.next();
                    if(regionalThreat.getName().equalsIgnoreCase(selectedRegionalThreat.getName())){
                        selectedRegionalThreat.setSelected(true);
                        currentMission.setDifficulty(selectedRegionalThreat.getDifficulty());
                        currentMission.setMinigame(selectedRegionalThreat.getMinigame());
                    }
                    else{
                        regionalThreat.setSelected(false);
                    }
                }
            }
        });
        
    }

    private boolean getBoolean() {
        return getItem() == null ? false : getItem().booleanValue();
    }
}
