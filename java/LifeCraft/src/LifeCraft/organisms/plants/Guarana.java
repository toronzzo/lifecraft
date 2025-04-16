package LifeCraft.organisms.plants;

import LifeCraft.World;
import LifeCraft.organisms.Organism;
import java.awt.Point;

public class Guarana extends Plant {
    public Guarana(World world, Point position) {
        super(world, position);
        this.nutrient = 3;
    }

    public Guarana(World world, Point position, int strength, int age) {
        super(world, position, strength, age);
    }

    public Organism babyFactory(World world, Point position) {
        return new Guarana(world, position);
    }

    public String toString() {
        return "Guarana";
    }
}
