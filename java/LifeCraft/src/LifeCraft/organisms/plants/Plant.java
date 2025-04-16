package LifeCraft.organisms.plants;

import LifeCraft.Tile;
import LifeCraft.World;
import LifeCraft.organisms.Organism;

import java.awt.*;
import java.util.Random;

public abstract class Plant extends Organism {
    protected static final Random random = new Random();
    protected int sowingChance = 10;
    protected int sowingAttempts = 1;

    public Plant(World world, Point position) {
        super(world, position);
        this.strength = 0;
        this.initiative = 0;
    }

    public Plant(World world, Point position, int strength, int age) {
        super(world, position, strength, age);
    }

    public void action() {
        if (this.isAlive()) {
            this.setAge(this.getAge() + 1);
            world.moveToQueue(this);
            int sowingProbability;
            for (int sow = 0; sow < this.getSowingAttempts(); sow++) {
                sowingProbability = random.nextInt(100);
                Tile seedlingHome = world.getMap().getRandomEmptyTile(this.getPosition());
                if (sowingProbability < this.getSowingChance() && seedlingHome != null) {
                    this.sow(seedlingHome);
                }
            }
        }
    }

    protected void sow(Tile seedlingHome) {
        Organism seedling = this.babyFactory(world, seedlingHome.getPosition());
        seedlingHome.setOrganism(seedling);
        world.bornToQueue(seedling);
        System.out.println(this + " sowed " + seedling + " at " + seedling.getPosition());
    }

    public int getSowingAttempts() {
        return sowingAttempts;
    }

    public int getSowingChance() {
        return sowingChance;
    }
}
