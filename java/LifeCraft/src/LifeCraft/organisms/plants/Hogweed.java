package LifeCraft.organisms.plants;

import LifeCraft.World;
import LifeCraft.organisms.Organism;
import LifeCraft.organisms.animals.Animal;

import java.awt.*;
import java.util.ArrayList;

public class Hogweed extends Plant
{
    public Hogweed(World world, Point position)
    {
        super(world, position);
        strength = 99;
        toxic = true;
    }

    public Hogweed(World world, Point position, int strength, int age)
    {
        super(world, position, strength, age);
    }

    public void action()
    {
        if (this.isAlive())
        {
            ArrayList<Point> toKill = world.getMap().getAnimalsAround(this.getPosition()); //get all animals around and kill them
            for (Point point : toKill)
            {
                var organism = world.getMap().getTile(point).getOrganism();
                if (organism instanceof Animal) {
                    System.out.println(world.getMap().getTile(point).getOrganism().toString() + " from (" + point.x + "," + point.y + ") got intoxicated");
                    world.kill(organism);
                }

            }
            this.setAge(this.getAge() + 1);
            world.moveToQueue(this);
        }
    }
    @Override
    public Organism babyFactory(World world, Point position)
    {
        return new Hogweed(world, position);
    }

    @Override
    public String toString()
    {
        return "Hogweed";
    }
}
