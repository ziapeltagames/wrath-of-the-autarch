/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wota.strategic.model;

/**
 *
 * @author plewis
 */
public enum DevelopmentType {
    /* Arcane - 9 */
    Arcane_Academy, Expert_Casting, Expert_Channeling,
    Improved_Casting, Improved_Channeling, Advanced_Casting, 
    Advanced_Channeling, Mages_Guild, Mana_Forge, 
    
    /* Diplomacy - 9 */
    Superb_Diplomats, Fantastic_Diplomats, Epic_Diplomats,
    Trade_Guild, Trade_Relationships, Trade_Capital,
    Arts_and_Entertainment, Center_of_Culture, Festival,
    
    /* Infiltration - 9 */
    Superb_Spies, Fantastic_Spies, Epic_Spies, Improved_Thieves_Tools,
    Advanced_Thieves_Tools, Expert_Thieves_Tools, Local_Contacts,
    Influential_Contacts, Floor_Plans,
    
    /* Skirmish - 9 */
    Superb_Guard_Force, Fantastic_Guard_Force, Epic_Guard_Force,
    Improved_Swordsmith, Improved_Armorer, Advanced_Swordsmith,
    Advanced_Armorer, Improved_Bowyer, Advanced_Bowyer,
    
    /* Warfare - 9 */
    Barracks, Improved_Barracks, Advanced_Barracks, Stables,
    Fletcher, Siegecraft, Gunpowder, Keep, Castle,
    
    /* For units. */
    Military_Unit,
    
    /* Artifacts. */
    Theddas_Palimpsest, Arens_Forge, Gossamer_Spirit, Serum_of_Allure,
    Pelakhars_Loyalty
}
