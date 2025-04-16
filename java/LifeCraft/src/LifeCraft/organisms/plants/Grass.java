package LifeCraft.organisms.plants;

import LifeCraft.World;
import LifeCraft.organisms.Organism;

import java.awt.*;

public class Grass extends Plant
{
    public Grass(World world, Point position)
    {
        super(world, position);
    }

    public Grass(World world, Point position, int strength, int age)
    {
        super(world, position, strength, age);
    }

    @Override
    public Organism babyFactory(World world, Point position)
    {
        return new Grass(world, position);
    }

    @Override
    public String toString()
    {
        return "Grass";
    }
}
