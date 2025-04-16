package LifeCraft.organisms.plants;

import LifeCraft.World;
import LifeCraft.organisms.Organism;

import java.awt.*;

public class Sow_thistle extends Plant
{
    public Sow_thistle(World world, Point position)
    {
        super(world, position);
        sowingAttempts = 3;
    }

    public Sow_thistle(World world, Point position, int strength, int age)
    {
        super(world, position, strength, age);
    }

    @Override
    public Organism babyFactory(World world, Point position)
    {
        return new Sow_thistle(world, position);
    }

    @Override
    public String toString()
    {
        return "Sow_thistle";
    }
}