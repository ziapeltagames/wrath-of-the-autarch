/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.ui;

import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import wota.strategic.model.Hero;
import wota.strategic.model.Mission;

/**
 *
 * @author plewis
 */
public class HeroMissionCheckBoxTableCell extends TableCell<Hero, Boolean> {

    private CheckBox checkBox;
    private final Mission currentMission;
    private final MissionResolutionController controller;

    public HeroMissionCheckBoxTableCell(MissionResolutionController controller,
            Mission currentMission) {
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
                Hero selectedHero = (Hero) getTableRow().getItem();
                if (checkBox.isSelected()) {
                    selectedHero.setOnMission(true);
                    currentMission.addHero(selectedHero);
                } else {
                    selectedHero.setOnMission(false);
                    currentMission.removeHero(selectedHero);
                }
                controller.updateActiveHeroes();
            }
        });

    }

    private boolean getBoolean() {
        return getItem() == null ? false : getItem().booleanValue();
    }
}
