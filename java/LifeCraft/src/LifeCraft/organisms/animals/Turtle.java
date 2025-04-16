package LifeCraft.organisms.animals;
import LifeCraft.World;
import LifeCraft.organisms.Organism;

import java.awt.*;

public class Turtle extends Animal
{
    public Turtle(World world, Point position)
    {
        super(world, position);
        strength = 2;
        initiative = 1;
        chill = 75;
    }

    public Turtle(World world, Point position, int strength, int age)
    {
        super(world, position, strength, age);
    }

    @Override
    public boolean hasShield(Organism attacker)
    {
        if (attacker.getStrength() < 5)
        {
            System.out.println(this + " from (" + this.getPosition().x + "," + this.getPosition().y + ")"
                    + " defended from " + attacker + " at (" + attacker.getPosition().x + "," + attacker.getPosition().y + ")");
            return true;
        }
        return false;
    }
    @Override
    public String toString()
    {
        return "Turtle";
    }
    @Override
    public Organism babyFactory(World world, Point position)
    {
        return new Turtle(world, position);
    }
}
