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
import wota.strategic.model.Development;
import wota.strategic.model.Mission;
import wota.strategic.model.Threat;
import wota.strategic.model.ConflictType;
import wota.strategic.model.OrderOfBattle;
import wota.strategic.model.UnitType;

/**
 *
 * @author plewis
 */
public class FactionThreatMissionCheckBoxTableCell extends TableCell<Threat, Boolean> {

    private CheckBox checkBox;
    private final WotAStrategicModel wotaModel;
    private final Mission currentMission;
    private final MissionResolutionController controller;

    public FactionThreatMissionCheckBoxTableCell(MissionResolutionController controller,
            WotAStrategicModel wotaModel, Mission currentMission) {
        this.wotaModel = wotaModel;
        this.currentMission = currentMission;
        this.controller = controller;
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
                Threat selectedFactionThreat = (Threat) getTableRow().getItem();
                ObservableList<Threat> factionThreats = wotaModel.getSeasonalThreats().getFactionThreats();
                for (Iterator<Threat> it = factionThreats.iterator(); it.hasNext();) {
                    Threat factionThreat = it.next();
                    
                    /* Check if this is the faction threat that was selected. */
                    if (factionThreat.getName().equalsIgnoreCase(selectedFactionThreat.getName())) {
                        selectedFactionThreat.setSelected(true);
                        controller.setTargetFaction(factionThreat.getMinorFaction());
                        currentMission.setMinigame(selectedFactionThreat.getMinigame());
                        currentMission.setDifficulty(selectedFactionThreat.getDifficulty());
                        if (selectedFactionThreat.getMinigame() == ConflictType.Warfare) {
                            wotaModel.getStronghold().clearDefensiveFortifications();

                            for (Iterator<Development> devit = selectedFactionThreat.getTargetRegion().getDevelopments().iterator();
                                    devit.hasNext();) {
                                Development dev = devit.next();
                                if (dev.getName().equalsIgnoreCase("castle")) {
                                    OrderOfBattle castle =
                                            new OrderOfBattle(UnitType.Castle,
                                            1, wotaModel.getStronghold());
                                    castle.setDeployed(1);
                                    wotaModel.getStronghold().getUnits().add(castle);
                                }
                                if (dev.getName().equalsIgnoreCase("keep")) {
                                    OrderOfBattle keep =
                                            new OrderOfBattle(UnitType.Keep,
                                            1, wotaModel.getStronghold());
                                    keep.setDeployed(1);
                                    wotaModel.getStronghold().getUnits().add(keep);
                                }
                            }
                            wotaModel.getStronghold().updateUnitQuality();
                        }
                    } else {
                        factionThreat.setSelected(false);
                    }
                }
            }
        });

    }

    private boolean getBoolean() {
        return getItem() == null ? false : getItem().booleanValue();
    }
}
