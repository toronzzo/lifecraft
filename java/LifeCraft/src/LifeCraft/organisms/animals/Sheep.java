package LifeCraft.organisms.animals;
import LifeCraft.World;
import LifeCraft.organisms.Organism;

import java.awt.*;

public class Sheep extends Animal
{
    public Sheep(World world, Point position)
    {
        super(world, position);
        strength = 4;
        initiative = 4;
    }

    public Sheep(World world, Point position, int strength, int age)
    {
        super(world, position, strength, age);
    }

    @Override
    public String toString()
    {
        return "Sheep";
    }
    @Override
    public Organism babyFactory(World world, Point position)
    {
        return new Sheep(world, position);
    }
}