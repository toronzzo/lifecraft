package LifeCraft.organisms.animals;

import LifeCraft.World;
import LifeCraft.organisms.Organism;

import java.awt.*;

public class Fox extends Animal
{
    public Fox(World world, Point position)
    {
        super(world, position);
        strength = 3;
        initiative = 7;
        wary = true;
    }

    public Fox(World world, Point position, int strength, int age)
    {
        super(world, position, strength, age);
    }

    @Override
    public Organism babyFactory(World world, Point position)
    {
        return new Fox(world, position);
    }
    @Override
    public String toString()
    {
        return "Fox";
    }
}
