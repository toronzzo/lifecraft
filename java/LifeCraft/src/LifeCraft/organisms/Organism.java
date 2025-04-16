package LifeCraft.organisms;

import LifeCraft.Constants;
import LifeCraft.Tile;
import LifeCraft.World;
import LifeCraft.organisms.animals.Animal;
import LifeCraft.organisms.animals.Antelope;
import LifeCraft.organisms.animals.Fox;
import LifeCraft.organisms.animals.Human;
import LifeCraft.organisms.animals.Sheep;
import LifeCraft.organisms.animals.Turtle;
import LifeCraft.organisms.animals.Wolf;
import LifeCraft.organisms.plants.Belladonna;
import LifeCraft.organisms.plants.Grass;
import LifeCraft.organisms.plants.Guarana;
import LifeCraft.organisms.plants.Hogweed;
import LifeCraft.organisms.plants.Plant;
import LifeCraft.organisms.plants.Sow_thistle;
import java.awt.Point;
import java.util.Random;

public abstract class Organism implements Constants {
    protected static World world;
    protected Point position;
    protected int strength;
    protected int initiative;
    protected boolean alive = true;
    protected int age = 1;
    protected int steps = 1;
    protected int escape = 0;
    protected int chill = 0;
    protected int nutrient = 0;
    protected boolean wary = false;
    protected boolean shield = false;
    protected boolean toxic = false;
    protected boolean humanish = false;

    public Organism(World world, Point position) {
        Organism.world = world;
        this.position = position;
    }

    public Organism(World world, Point position, int strength, int age) {
        Organism.world = world;
        this.position = position;
        this.strength = strength;
        this.age = age;
    }

    public int getStrength() {
        return this.strength;
    }

    public int getInitiative() {
        return this.initiative;
    }

    public int getAge() {
        return this.age;
    }

    public int getSteps() {
        return this.steps;
    }

    public int getEscapeChance() {
        return this.escape;
    }

    public Point getPosition() {
        return this.position;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public boolean hasShield(Organism attacker) {
        return this.shield;
    }

    public boolean isWary() {
        return this.wary;
    }

    public boolean hasChill() {
        Random random = new Random();
        return random.nextInt(100) < this.chill;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public boolean isPlant() {
        return this instanceof Plant;
    }

    public boolean isAnimal() {
        return this instanceof Animal;
    }

    public boolean isToxic() {
        return this.toxic;
    }

    public boolean isNutrient() {
        return this.nutrient != 0;
    }

    public boolean isHumanish() {
        return this.humanish;
    }

    public int getNutricity() {
        return this.nutrient;
    }

    public abstract void action();

    public abstract Organism babyFactory(World var1, Point var2);

    public abstract String toString();

    public String toSave() {
        String toSave = "";
        toSave = toSave + this.toString();
        toSave = toSave + "\n";
        toSave = toSave + this.getAge();
        toSave = toSave + "\n";
        toSave = toSave + this.getPosition().x;
        toSave = toSave + "\n";
        toSave = toSave + this.getPosition().y;
        toSave = toSave + "\n";
        toSave = toSave + this.getStrength();
        toSave = toSave + "\n";
        return toSave;
    }

    public static void load(String name, int age, Point position, int strength, int ability_time, int ability_cool_down) {
        Organism organism = null;
        Tile tile = world.getMap().getTile(position);
        switch (name) {
            case "Human" -> organism = new Human(world, position, strength, age, ability_time, ability_cool_down);
            case "Wolf" -> organism = new Wolf(world, position, strength, age);
            case "Sheep" -> organism = new Sheep(world, position, strength, age);
            case "Fox" -> organism = new Fox(world, position, strength, age);
            case "Turtle" -> organism = new Turtle(world, position, strength, age);
            case "Antelope" -> organism = new Antelope(world, position, strength, age);
            case "Grass" -> organism = new Grass(world, position, strength, age);
            case "Guarana" -> organism = new Guarana(world, position, strength, age);
            case "Sow_thistle" -> organism = new Sow_thistle(world, position, strength, age);
            case "Belladonna" -> organism = new Belladonna(world, position, strength, age);
            case "Hogweed" -> organism = new Hogweed(world, position, strength, age);
        }

        world.addOrganism(tile, (Organism)organism);
    }
}
