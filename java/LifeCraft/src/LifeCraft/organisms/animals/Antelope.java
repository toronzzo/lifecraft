package LifeCraft.organisms.animals;

import LifeCraft.World;
import LifeCraft.organisms.Organism;

import java.awt.*;

public class Antelope extends Animal
{
    public Antelope(World world, Point position)
    {
        super(world, position);
        strength = 4;
        initiative = 4;
        steps = 2;
        escape = 50;
    }

    public Antelope(World world, Point position, int strength, int age)
    {
        super(world, position, strength, age);
    }

    @Override
    public Organism babyFactory(World world, Point position)
    {
        return new Antelope(world, position);
    }
    @Override
    public String toString()
    {
        return "Antelope";
    }

}
