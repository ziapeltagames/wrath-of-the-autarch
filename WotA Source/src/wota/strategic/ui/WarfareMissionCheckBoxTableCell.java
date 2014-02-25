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
import wota.strategic.model.Faction;
import wota.strategic.model.Mission;
import wota.strategic.model.OrderOfBattle;
import wota.strategic.model.Region;
import wota.strategic.model.UnitType;

/**
 *
 * @author plewis
 */
public class WarfareMissionCheckBoxTableCell extends TableCell<Region, Boolean> {

    private CheckBox checkBox;
    private final WotAStrategicModel wotaModel;
    private final Mission currentMission;

    public WarfareMissionCheckBoxTableCell(WotAStrategicModel wotaModel,
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
                Region selectedRegion = (Region) getTableRow().getItem();
                Faction faction = currentMission.getFaction();
                if(currentMission.isStrongholdOnDefense()){
                    faction = wotaModel.getStronghold();
                }
                if (faction == null) {
                    return;
                }
                ObservableList<Region> regions = faction.getRegions();
                for (Iterator<Region> it = regions.iterator(); it.hasNext();) {
                    Region region = it.next();
                    if (region == null || selectedRegion == null) {
                        continue;
                    }
                    if (region.getName().equalsIgnoreCase(selectedRegion.getName())) {
                        region.setSelected(true);
                        /* 
                         * Add any defensive structures on the region
                         * to the faction's order of battle.
                         */
                        for (Iterator<Development> devit = region.getDevelopments().iterator();
                                devit.hasNext();) {
                            Development dev = devit.next();
                            if (dev.getName().equalsIgnoreCase("castle")) {
                                OrderOfBattle castle =
                                        new OrderOfBattle(UnitType.Castle,
                                        1, faction);
                                castle.setDeployed(1);
                                faction.getUnits().add(castle);
                            }
                            if (dev.getName().equalsIgnoreCase("keep")) {
                                OrderOfBattle keep =
                                        new OrderOfBattle(UnitType.Keep,
                                        1, faction);
                                keep.setDeployed(1);
                                faction.getUnits().add(keep);
                            }
                        }
                        faction.updateUnitQuality();
                    } else {
                        region.setSelected(false);
                        faction.clearDefensiveFortifications();
                    }
                }
            }
        });

    }

    private boolean getBoolean() {
        return getItem() == null ? false : getItem().booleanValue();
    }
}
