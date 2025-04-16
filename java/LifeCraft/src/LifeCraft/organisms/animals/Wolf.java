package LifeCraft.organisms.animals;
import LifeCraft.World;
import LifeCraft.organisms.Organism;

import java.awt.*;

public class Wolf extends Animal
{
    public Wolf(World world, Point position)
    {
        super(world, position);
        strength = 9;
        initiative = 5;
    }
    public Wolf(World world, Point position, int strength, int age)
    {
        super(world, position, strength, age);
    }

    @Override
    public String toString()
    {
        return "Wolf";
    }
    @Override
    public Organism babyFactory(World world, Point position)
    {
        return new Wolf(world, position);
    }
}
