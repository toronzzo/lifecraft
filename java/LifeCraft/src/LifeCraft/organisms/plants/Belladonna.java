package LifeCraft.organisms.plants;

import LifeCraft.World;
import LifeCraft.organisms.Organism;

import java.awt.*;

public class Belladonna extends Plant
{
    public Belladonna(World world, Point position)
    {
        super(world, position);
        strength = 10;
        toxic = true;
    }

    public Belladonna(World world, Point position, int strength, int age)
    {
        super(world, position, strength, age);
    }

    @Override
    public Organism babyFactory(World world, Point position)
    {
        return new Belladonna(world, position);
    }

    @Override
    public String toString()
    {
        return "Belladonna";
    }
}

