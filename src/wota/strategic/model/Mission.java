/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wota.strategic.model;

import java.util.Date;
import java.util.Random;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static wota.strategic.model.MissionType.Assassination;
import static wota.strategic.model.MissionType.Espionage;
import static wota.strategic.model.MissionType.Sabotage;
import static wota.strategic.model.MissionType.Trade;
import static wota.strategic.model.MissionType.Conquest;
import wota.strategic.ui.WotAStrategicModel;

/**
 * Holds details related to an ongoing mission. Created by the
 * MissionResolutionController, and destroyed at the end of the mission. Tracks
 * chances of success, type of mission, assigning consequences to heroes, and
 * casting of spells.
 *
 * @author plewis
 */
public class Mission {

    /*
     Create a new random number generator for use during the mission.
     */
    Random rand = new Random((new Date()).getTime());

    Stronghold stronghold;
    WotAStrategicModel wotaModel;

    public Mission(WotAStrategicModel wotaModel) {
        difficulty.set(0);
        baseDifficulty.set(0);
        strongholdHitChance.set(50);
        factionHitChance.set(50);
        strongholdOnDefense.set(false);
        skill.set(0);
        caught.set(0);
        lastRoll.set(0);
        type.set(MissionType.Exploration);

        this.wotaModel = wotaModel;
        this.stronghold = wotaModel.getStronghold();

        /* 
         Set up a binding for failure chance, so that modifications to
         skill or difficulty cause the calculation of a new chance 
         of failure.
         */
        IntegerBinding chanceBinding = new IntegerBinding() {
            {
                super.bind(skill, difficulty);
            }

            @Override
            protected int computeValue() {
                int cresult = 99;
                int diff = skill.get() - difficulty.get();
                switch (diff) {
                    case 5:
                        cresult = 99;
                        break;
                    case 4:
                        cresult = 99;
                        break;
                    case 3:
                        cresult = 98;
                        break;
                    case 2:
                        cresult = 90;
                        break;
                    case 1:
                        cresult = 50;
                        break;
                    case 0:
                        cresult = 33;
                        break;
                    case -1:
                        cresult = 16;
                        break;
                    case -2:
                        cresult = 8;
                        break;
                    case -3:
                        cresult = 4;
                        break;
                    case -4:
                        cresult = 2;
                        break;
                    case -5:
                        cresult = 1;
                        break;
                    case -6:
                        cresult = 1;
                        break;
                    case -7:
                        cresult = 1;
                        break;
                    default:
                        break;
                }
                return cresult;
            }
        };
        chance.bind(chanceBinding);

        /* 
         The more skill the Stronghold possesses in Warfare, the
         easier it is for troops to hit.
         */
        IntegerBinding strongholdHitChanceBinding = new IntegerBinding() {
            {
                super.bind(skill, difficulty);
            }

            @Override
            protected int computeValue() {
                int diff = skill.get() - difficulty.get();
                return 20 + (diff * 2);
            }
        };
        strongholdHitChance.bind(strongholdHitChanceBinding);

        IntegerBinding factionHitChanceBinding = new IntegerBinding() {
            {
                super.bind(skill, difficulty);
            }

            @Override
            protected int computeValue() {
                int diff = skill.get() - difficulty.get();
                return 20 - (diff * 2);
            }
        };
        factionHitChance.bind(factionHitChanceBinding);
    }

    public void resetMission() {
        difficulty.set(0);
        success.set(false);
        strongholdOnDefense.set(false);
        skill.set(0);
        caught.set(0);
        lastRoll.set(0);
        type.set(MissionType.Exploration);
        heroes.stream().forEach((hero) -> {
            hero.setOnMission(false);
        });
        heroes.clear();
    }

    private final IntegerProperty difficulty = new SimpleIntegerProperty();

    public int getDifficulty() {
        return difficulty.get();
    }

    public void setDifficulty(int value) {
        difficulty.set(value);
    }

    public IntegerProperty difficultyProperty() {
        return difficulty;
    }
    private final ObjectProperty<ConflictType> minigame
            = new SimpleObjectProperty<>();

    public ConflictType getMinigame() {
        return minigame.get();
    }

    public void setMinigame(ConflictType value) {
        if (value != null) {
            minigame.set(value);
            calculateSkill();
        }
    }

    public ObjectProperty minigameProperty() {
        return minigame;
    }

    private final IntegerProperty skill = new SimpleIntegerProperty();

    public int getSkill() {
        return skill.get();
    }

    public void setSkill(int value) {
        skill.set(value);
    }

    public IntegerProperty skillProperty() {
        return skill;
    }
    private ObservableList<Hero> heroes
            = FXCollections.observableArrayList();

    public ObservableList<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(ObservableList<Hero> value) {
        this.heroes = value;
    }

    public ObservableList<Hero> heroesProperty() {
        return heroes;
    }

    public void addHero(Hero newHero) {
        if (heroes.size() > 3) {
            heroes.get(0).setOnMission(false);
            heroes.remove(0);
        }
        newHero.setOnMission(true);
        heroes.add(newHero);
        calculateSkill();
    }

    public void removeHero(Hero removedHero) {
        heroes.stream().filter((hero) -> (hero.getName().equalsIgnoreCase(removedHero.getName()))).forEach((hero) -> {
            hero.setOnMission(false);
        });
        heroes.remove(removedHero);
        calculateSkill();
    }

    /*
     Each time the heroes are adjusted, the skill level is recalculated.
     */
    private void calculateSkill() {
        int tskill = 0;

        /* First determine the contribution from each hero. */
        for (Hero hero : heroes) {

            /* 
             If the hero is a spellcaster, their skill is lower, although
             developments can change that.
             */
            if (hero.isSpellcaster()) {
                if (stronghold.getDevelopment(DevelopmentType.Legendary_Casting) != null) {
                    tskill = tskill + 2;
                } else if (stronghold.getDevelopment(DevelopmentType.Advanced_Casting) != null) {
                    tskill = tskill + 1;
                } else if (stronghold.getDevelopment(DevelopmentType.Great_Casting) != null) {
                    tskill = tskill + 0;
                } else {
                    tskill = tskill - 1;
                }
            }

            if (hero.getMajorFocus() == getMinigame()) {
                tskill = tskill + hero.getRank();
            } else if (hero.getMinorFocus() == getMinigame()) {
                tskill = tskill + (hero.getRank() - 1);
            } else {
                tskill = tskill + (hero.getRank() - 2);
            }
        }
        if (tskill > 0) {
            tskill = (tskill / 4);
        }

        setSkill(tskill);
    }
    private final IntegerProperty season = new SimpleIntegerProperty();

    public int getSeason() {
        return season.get();
    }

    public void setSeason(int value) {
        season.set(value);
    }

    public IntegerProperty seasonProperty() {
        return season;
    }

    /* 
     * Used to model getting caught during infiltration missions.
     * For now, if the heroes fail four times, they are caught (in the
     * real game, only a particular hero is caught).
     */
    private final IntegerProperty caught = new SimpleIntegerProperty();

    public int getCaught() {
        return caught.get();
    }

    public void setCaught(int value) {
        caught.set(value);
    }

    public IntegerProperty caughtProperty() {
        return caught;
    }
    private final IntegerProperty lastRoll = new SimpleIntegerProperty();

    public int getLastRoll() {
        return lastRoll.get();
    }

    public void setLastRoll(int value) {
        lastRoll.set(value);
    }

    public IntegerProperty lastRollProperty() {
        return lastRoll;
    }

    /*
     * Will assign one consequence to a random hero on the mission. 
     */
    public void assignConsequences() {
        int numberOfHeroes = this.heroes.size();
        int hindex = rand.nextInt(numberOfHeroes);
        Hero nextHero = heroes.get(hindex);
        nextHero.setConsequences(nextHero.getConsequences() + 1);
        caught.set(caught.get() + 1);
    }
    /*
     * Used to hold the target faction for the mission.
     */
    private final ObjectProperty<MinorFaction> faction
            = new SimpleObjectProperty<>();

    public MinorFaction getFaction() {
        return faction.get();
    }

    public void setFaction(MinorFaction value) {
        /* Do any calculations of difficulty based on the mission type. */
        faction.set(value);
        updateDifficulties();
    }

    private void updateDifficulties() {
        if (getType() == null || getFaction() == null) {
            return;
        }
        switch (getType()) {
            case Conquest:
                this.difficulty.set(getFaction().getMilitaryDifficulty());
                this.baseDifficulty.set(getFaction().getMilitaryDifficulty());
                break;
            case Espionage:
                this.difficulty.set(getFaction().getInfiltrationDifficulty());
                this.baseDifficulty.set(getFaction().getInfiltrationDifficulty());
                break;
            case Assassination:
                this.difficulty.set(getFaction().getInfiltrationDifficulty() + 1);
                this.baseDifficulty.set(getFaction().getInfiltrationDifficulty());
                break;
            case Sabotage:
                this.difficulty.set(getFaction().getInfiltrationDifficulty() + 1);
                this.baseDifficulty.set(getFaction().getInfiltrationDifficulty());
                break;
            case Trade:
                this.difficulty.set(getFaction().getTradeDifficulty());
                this.baseDifficulty.set(getFaction().getTradeDifficulty());
                break;
            default:
                break;
        }
    }

    public ObjectProperty factionProperty() {
        return faction;
    }
    private final ObjectProperty<MissionType> type
            = new SimpleObjectProperty<>();

    public MissionType getType() {
        return type.get();
    }

    public void setType(MissionType value) {
        type.set(value);
        updateDifficulties();
    }

    public ObjectProperty typeProperty() {
        return type;
    }
    private final IntegerProperty baseDifficulty = new SimpleIntegerProperty();

    public int getBaseDifficulty() {
        return baseDifficulty.get();
    }

    public void setBaseDifficulty(int value) {
        baseDifficulty.set(value);
    }

    public IntegerProperty baseDifficultyProperty() {
        return baseDifficulty;
    }
    private final BooleanProperty success = new SimpleBooleanProperty();

    public boolean isSuccess() {
        return success.get();
    }

    public void setSuccess(boolean value) {
        success.set(value);
    }

    public BooleanProperty successProperty() {
        return success;
    }

    /*
     Chance of success for a mission.
     */
    private final IntegerProperty chance = new SimpleIntegerProperty();

    public int getChance() {
        return chance.get();
    }

    public void setChance(int value) {
        chance.set(value);
    }

    public IntegerProperty chanceProperty() {
        return chance;
    }

    /**
     * Attempt a mission with the currently selected four heroes against the
     * current mission success chance. A failure results in a consequence
     * assigned to one of the four heroes.
     *
     * @param stronghold
     * @return boolean Successful mission.
     */
    public boolean attemptMission(Stronghold stronghold) {
        
        /* Determine if any spellcasters take backlash. */
        for (Hero nextHero : heroes) {
            if (nextHero.isSpellcaster() && nextHero.getMana() > 0) {
                nextHero.setBacklash(nextHero.getBacklash()
                        + determineBacklash(nextHero, 1));
                nextHero.setMana(nextHero.getMana() - 1);
            }
        }
        
        /* 
        Offensive developments that match the mini-game offer
        extra re-rolls for success.
        */
        for(Development development : stronghold.getDevelopments()){
            if(development.getTree().name().equalsIgnoreCase(minigame.get().name())
                    && development.getFunction() == DevelopmentFunction.Offense){
                lastRoll.set(rand.nextInt(99));
                if(lastRoll.get() < getChance()){
                    return true;
                }
            }
        }
        
        /* Everyone gets at least one roll. */
        lastRoll.set(rand.nextInt(99));
        if (lastRoll.get() < getChance()) {
            return true;
        }

        assignConsequences();

        return false;
    }

    /**
     * Casting a spell lets the heroes have a chance at succeeding at the
     * mission without taking any consequences, at a cost of possibly losing
     * resources in the region the hero is bound to. Called by the
     * MissionResolutionController.
     *
     * @param spellCaster
     * @param manaSpent
     * @return
     */
    public String castSpell(Hero spellCaster, int manaSpent) {
        if (manaSpent < 5) {
            return "Must spend at least five mana";
        }

        /* Check to see if hero is capable of casting this spell. */
        if (manaSpent > 4 && spellCaster.getRank() < 5) {
            return "Must be rank 5 to use 5 or more mana";
        }

        if (manaSpent > 9 && spellCaster.getRank() < 6) {
            return "Must be rank 6 to use 10 or more mana";
        }

        if (manaSpent > 14
                && stronghold.getDevelopment(DevelopmentType.Mages_Guild) == null) {
            return "Need a Mages Guild in order to spend 15 or more mana";
        }

        /* Remove mana spent. */
        spellCaster.setMana(spellCaster.getMana() - manaSpent);

        int rerolls = rerollAmount(manaSpent);

        /* Was the spell successful? */
        boolean successful = false;
        for (int i = 0; i < rerolls; i++) {
            if (rand.nextInt(99) < getChance()) {
                successful = true;
                break;
            }
        }

        String successString = "Failed to cast spell! ";
        if (successful) {
            successString = "Succeeded at casting spell! ";
        }

        /* Determine backlash. */
        int backlash = determineBacklash(spellCaster, manaSpent);
        spellCaster.setBacklash(spellCaster.getBacklash() + backlash);

        String backlashString = "Backlash: " + backlash;
        return successString + backlashString;
    }

    /*
     Helper method to determine amount of backlash a hero caster may take.
     */
    private int determineBacklash(Hero caster, int manaSpent) {
        int firstDice = 6;
        if (stronghold.getDevelopment(DevelopmentType.Legendary_Channeling) != null) {
            firstDice = 12;
        } else if (stronghold.getDevelopment(DevelopmentType.Advanced_Channeling) != null) {
            firstDice = 10;
        } else if (stronghold.getDevelopment(DevelopmentType.Great_Channeling) != null) {
            firstDice = 8;
        }
        int backlash = manaSpent
                - (caster.getRank() + ((rand.nextInt(firstDice) + 1)
                - (rand.nextInt(6) + 1)));
        if (backlash < 0) {
            backlash = 0;
        }
        return backlash;
    }

    /**
     * Helper method to determine effectiveness of casting a spell.
     *
     * @param manaAmount
     * @return
     */
    public int rerollAmount(int manaAmount) {
        int multiples = manaAmount / 5;
        int rerolls;
        switch (multiples) {
            case 0:
                rerolls = 0;
                break;
            case 1:
                rerolls = 3;
                break;
            case 2:
                rerolls = 7;
                break;
            case 3:
                rerolls = 15;
                break;
            default:
                rerolls = 15;
                break;
        }
        return rerolls;
    }

    /* Details for Warfare missions. */
    /* Prior to any mission, both the Stronghold and the Autarch may
     benefit from allied military forces. */
    public void addAllies() {
        for (MinorFaction nextFaction : wotaModel.getFactions()) {
            if (nextFaction.getDisposition() > 4) {
                nextFaction.getAlliedUnits().stream().forEach((allyUnits) -> {
                    stronghold.addUnits(allyUnits);
                });
            }
            if (nextFaction.getDisposition() < -4) {
                nextFaction.getAlliedUnits().stream().forEach((allyUnits) -> {
                    wotaModel.getAutarch().addUnits(allyUnits);
                });
            }
        }
    }

    /* Only the Stronghold and the Autarch have allied units. */
    public void clearAllies() {
        stronghold.clearAlliedUnits();
        wotaModel.getAutarch().clearAlliedUnits();
    }

    private final IntegerProperty strongholdHitChance = new SimpleIntegerProperty();

    public int getStrongholdHitChance() {
        return strongholdHitChance.get();
    }

    public void setStrongholdHitChance(int value) {
        strongholdHitChance.set(value);
    }

    public IntegerProperty strongholdHitChanceProperty() {
        return strongholdHitChance;
    }
    private final IntegerProperty factionHitChance = new SimpleIntegerProperty();

    public int getFactionHitChance() {
        return factionHitChance.get();
    }

    public void setFactionHitChance(int value) {
        factionHitChance.set(value);
    }

    public IntegerProperty factionHitChanceProperty() {
        return factionHitChance;
    }

    private final BooleanProperty strongholdOnDefense = new SimpleBooleanProperty();

    public boolean isStrongholdOnDefense() {
        return strongholdOnDefense.get();
    }

    public void setStrongholdOnDefense(boolean value) {
        strongholdOnDefense.set(value);
    }

    public BooleanProperty strongholdOnDefenseProperty() {
        return strongholdOnDefense;
    }

    /* Play through a round of conflict, which corresponds to the
     units attacking each other in a pre-determined order. */
    public void roundOfWarfare(Stronghold stronghold) {

        /* 
         * Chance of heroes taking consequences is a little different
         * in Warfare.  Chance is equal to 1/2 the probability of the roll
         * failing.
         */
        if ((100 - getChance()) > 0
                && rand.nextInt(99) < ((100 - getChance()) / 2)) {
            assignConsequences();
        }

        /* Set up the attacker and defender. */
        Faction attacker = stronghold;
        int attackerHit = strongholdHitChance.get();
        Faction defender = faction.get();
        int defenderHit = factionHitChance.get();
        if (isStrongholdOnDefense()) {
            defender = stronghold;
            defenderHit = strongholdHitChance.get();
            attacker = faction.get();
            attackerHit = factionHitChance.get();
        }

        /* 
         * If a keep or castle is present, a different order of attacks
         * occurs until the structure is defeated.
         */
        if ((defender.getUnit(UnitType.Keep) != null)
                && defender.getUnit(UnitType.Keep).getDeployed() > 0) {
            siege(attacker, attackerHit, UnitType.Keep, defender, defenderHit);
            return;
        }
        if ((defender.getUnit(UnitType.Castle) != null)
                && defender.getUnit(UnitType.Castle).getDeployed() > 0) {
            siege(attacker, attackerHit, UnitType.Castle, defender, defenderHit);
            return;
        }

        /*
         * Order of regular attacks: defending sunriders, attacking sunriders,
         * defending dragons, attacking dragons, defending archers, 
         * attacking cavalry, defending cavalry, defending infantry, 
         * attacking infantry, defending militia, attacking militia, 
         * attacking archers.
         */
        generalUnitAttack(defender, UnitType.Horse_Archers, defenderHit,
                attacker, false);
        generalUnitAttack(attacker, UnitType.Horse_Archers, attackerHit,
                defender, true);
        generalUnitAttack(defender, UnitType.Dragon, defenderHit,
                attacker, false);
        generalUnitAttack(attacker, UnitType.Dragon, attackerHit,
                defender, true);
        generalUnitAttack(defender, UnitType.Battle_Mages, defenderHit,
                attacker, false);
        generalUnitAttack(defender, UnitType.Archer, defenderHit,
                attacker, false);
        generalUnitAttack(attacker, UnitType.Cavalry, attackerHit,
                defender, true);
        generalUnitAttack(attacker, UnitType.Battle_Mages, attackerHit,
                defender, true);
        generalUnitAttack(defender, UnitType.Cavalry, defenderHit,
                attacker, false);
        generalUnitAttack(defender, UnitType.Infantry, defenderHit,
                attacker, false);
        generalUnitAttack(attacker, UnitType.Infantry, attackerHit,
                defender, true);
        generalUnitAttack(defender, UnitType.Militia, defenderHit,
                attacker, false);
        generalUnitAttack(attacker, UnitType.Militia, attackerHit,
                defender, true);
        generalUnitAttack(attacker, UnitType.Archer, attackerHit,
                defender, true);
    }

    /* 
     * If a keep or castle is present, the defending archers attack the
     * attacking siege engines, next any attacking archers attack defending
     * archers (at a penalty), and finally the siege engines strike
     * the fortifications.
     */
    private void siege(Faction attacker,
            int attackerHit, UnitType defendingStructure,
            Faction defender, int defenderHit) {
        if (attacker.getUnit(UnitType.Catapult) != null
                && attacker.getUnit(UnitType.Catapult).getDeployed() > 0) {
            specificUnitAttack(defender, UnitType.Archer,
                    defenderHit, UnitType.Catapult,
                    attacker, false);
            specificUnitAttack(defender, UnitType.Dragon,
                    defenderHit, UnitType.Catapult,
                    attacker, false);
        } else {
            specificUnitAttack(defender, UnitType.Archer,
                    defenderHit, UnitType.Cannon,
                    attacker, false);
            specificUnitAttack(defender, UnitType.Dragon,
                    defenderHit, UnitType.Cannon,
                    attacker, false);
        }

        /* Penalty to hit defending archers. */
        specificUnitAttack(attacker, UnitType.Archer,
                attackerHit - 10, UnitType.Archer,
                defender, true);
        specificUnitAttack(attacker, UnitType.Catapult,
                attackerHit, defendingStructure,
                defender, true);
        specificUnitAttack(attacker, UnitType.Cannon,
                attackerHit, defendingStructure,
                defender, true);
        specificUnitAttack(attacker, UnitType.Dragon,
                attackerHit, defendingStructure,
                defender, true);
    }

    /* All the attacking units of a certin type attack
     all the defending units of a certain type.
     */
    private void specificUnitAttack(Faction attackingFaction,
            UnitType attackingUnitType, int attackerHitChance,
            UnitType defendingUnitType, Faction defendingFaction,
            boolean offense) {
        attackingFaction.getUnits().stream().filter((attackingUnit) -> (attackingUnit.getUtype() == attackingUnitType)).forEach((attackingUnit) -> {
            specificUnitAttack(attackingUnit, attackerHitChance,
                    defendingUnitType, defendingFaction, offense);
        });

    }

    /* Resolve attacks against one specific unit type. */
    private void specificUnitAttack(OrderOfBattle attackingUnit, int hitChance,
            UnitType defendingUnitType, Faction defendingFaction, boolean offense) {
        if (attackingUnit == null || defendingUnitType == null
                || attackingUnit.getDeployed() <= 0) {
            return;
        }
        int attacks = attackingUnit.getDefense();
        if (offense) {
            attacks = attackingUnit.getOffense();
        }
        for (int i = 0; i < attackingUnit.getDeployed(); i++) {
            for (int j = 0; j < attacks; j++) {
                if (rand.nextInt(99) < hitChance) {
                    applyDamage(defendingFaction, defendingUnitType);
                }
            }
        }
    }

    /* 
     Both the attacking faction, and every allied unit joined with
     them of the appropriate type may attack.
     */
    private void generalUnitAttack(Faction attackingFaction,
            UnitType attackingUnitType, int attackerHitChance,
            Faction defendingFaction, boolean offense) {

//        System.out.println();
//        System.out.println("generalUnitAttack");
//        System.out.println("defendingFaction: " + defendingFaction);
//        System.out.println("attackingUnitType: " + attackingUnitType);
//        System.out.println("attackingFaction.getUnits(): ");
//        for (OrderOfBattle unit : attackingFaction.getUnits()) {
//            System.out.println("  unit.getUtype(): " + unit.getUtype());
//        }
        attackingFaction.getUnits().stream().filter((attackingUnit) -> (attackingUnit.getUtype() == attackingUnitType)).forEach((attackingUnit) -> {
            generalUnitAttackResolution(attackingUnit, attackerHitChance,
                    defendingFaction, offense);
        });
    }

    /* Resolve all the attacks from one unit type. */
    private void generalUnitAttackResolution(OrderOfBattle attackingUnit, int hitChance,
            Faction target, boolean offense) {

        if (attackingUnit == null) {
            return;
        }
        int attacks = attackingUnit.getDefense();
        if (offense) {
            attacks = attackingUnit.getOffense();
        }
        for (int i = 0; i < attackingUnit.getDeployed(); i++) {
            for (int j = 0; j < attacks; j++) {
                if (rand.nextInt(99) < hitChance) {
                    applyDamage(target, null);
                }
            }
        }
    }

    /* Apply damage from attacks from one unit, any allied units could
     be hit as well. */
    private void applyDamage(Faction target, UnitType unitType) {
        int totalTargets = 0;
        for (OrderOfBattle targetUnit : target.getUnits()) {
            if (unitType != null) {
                if (targetUnit.getUtype() != unitType) {
                    continue;
                }
            }
            totalTargets = totalTargets + targetUnit.getDeployed();
        }
        if (totalTargets <= 0) {
            return;
        }
        int hitUnit = rand.nextInt(totalTargets);
        int currentUnit = 0;
        for (OrderOfBattle targetUnit : target.getUnits()) {
            if (unitType != null) {
                if (targetUnit.getUtype() != unitType) {
                    continue;
                }
            }
            currentUnit = currentUnit + targetUnit.getDeployed();
            if (hitUnit < currentUnit) {
                targetUnit.setDamage(targetUnit.getDamage() + 1);
                /* Unit has been killed */
                if (targetUnit.getMorale() <= targetUnit.getDamage()) {
                    if (targetUnit.getDeployed() > 0) {
                        if (targetUnit.getUtype() == UnitType.Militia) {
                            targetUnit.setCasualties(targetUnit.getCasualties() + 2);
                        } else {
                            targetUnit.setCasualties(targetUnit.getCasualties() + 1);
                        }
                        targetUnit.setDeployed(targetUnit.getDeployed() - 1);
                    }
                    if (targetUnit.getAvailable() > 0) {
                        targetUnit.setAvailable(targetUnit.getAvailable() - 1);
                        target.setTotalUnits(target.getTotalUnits() - 1);
                    }
                    targetUnit.setDamage(0);
                }
                return;
            }
        }
    }

    /* 
     Called after a Warfare mission to apply population loss to every
     faction that had casualties within their troop ranks.
     */
    public void applyCasualties() {
        applyCasualties(stronghold);
        wotaModel.getFactions().stream().forEach((nextFaction) -> {
            applyCasualties(nextFaction);
        });
    }

    /* 
     Each faction takes 1/4 the casualties in population loss,
     with militia counting for two.
     */
    private void applyCasualties(Faction faction) {
        int casualties = 0;
        for (OrderOfBattle unit : faction.getUnits()) {
            casualties = casualties + unit.getCasualties();
            unit.setCasualties(0);
        }
        int factionLoss = casualties / 4;
        faction.setPopulation(faction.getPopulation() - factionLoss);
    }

}
