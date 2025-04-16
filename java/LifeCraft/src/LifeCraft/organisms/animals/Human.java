package LifeCraft.organisms.animals;

import LifeCraft.World;
import LifeCraft.organisms.Organism;

import java.awt.*;
import java.util.ArrayList;

public class Human extends Animal
{
    private int ability_time = 0;
    private int cool_down = 0;
    private boolean ability_started = false;
    public Human(World world, Point position)
    {
        super(world, position);
        strength = 5;
        initiative = 4;
        humanish = true;
    }

    public Human(World world, Point position, int strength, int age, int ability_time, int ability_cool_down)
    {
        super(world, position, strength, age);
        this.ability_time = ability_time;
        this.cool_down = ability_cool_down;
    }

    @Override
    public void action()
    {
        super.action();
        this.specialAbility();
        this.decreaseTimes();
    }
    public void specialAbility()
    {
        setAbility_started(world.isMainCharacterPurge());
        if (hasAbility_started() && getCool_down() == 0)
        {
            setAbility_time(ABILITY_TIME);
            setCool_down(ABILITY_TIME + COOL_DOWN);
        }
        if (getAbility_time() != 0)
        {
            purge();
        }
    }
    public void decreaseTimes()
    {
        if (getAbility_time() != 0)
        {
            setAbility_time(getAbility_time() - 1);
        }
        if (getCool_down() != 0)
        {
            setCool_down(getCool_down() - 1);
        }
    }
    public void purge()
    {
        ArrayList<Point> toKill = world.getMap().getOrganismsAround(this.getPosition()); //get every organism around and kill it
        for (Point point : toKill)
        {
            world.kill(world.getMap().getTile(point).getOrganism());
        }
    }
    public int getAbility_time()
    {
        return ability_time;
    }
    public int getCool_down()
    {
        return cool_down;
    }
    public void setAbility_time(int ability_time)
    {
        this.ability_time = ability_time;
    }
    public void setCool_down(int cool_down)
    {
        this.cool_down = cool_down;
    }
    public void setAbility_started(boolean ability_started)
    {
        this.ability_started = ability_started;
    }
    public boolean hasAbility_started()
    {
        return ability_started;
    }

    @Override
    public Organism babyFactory(World world, Point position)
    {
        return new Human(world, position);
    }
    @Override
    public String toString()
    {
        return "Human";
    }
    @Override
    public String toSave()
    {
        String toSave = "";
        toSave += super.toSave();
        toSave += this.getAbility_time();
        toSave += "\n";
        toSave += this.getCool_down();
        toSave += "\n";
        return toSave;
    }
}