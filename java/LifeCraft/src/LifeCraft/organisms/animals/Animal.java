package LifeCraft.organisms.animals;

import LifeCraft.Tile;
import LifeCraft.World;
import LifeCraft.organisms.Organism;

import java.awt.*;
import java.util.Random;

public abstract class Animal extends Organism
{
    public Animal(World world, Point position)
    {
        super(world, position);
    }

    public Animal(World world, Point position, int strength, int age)
    {
        super(world, position, strength, age);
    }

    public void action()
    {
        if (this.isAlive()) // if is still alive
        {
            this.setAge(this.getAge() + 1);
            world.moveToQueue(this);
            for (int step = 0; step < this.getSteps(); step++)
            {
                if (!tryMove())
                {
                    break;
                }
            }
        }
    }

    public boolean tryMove()
    {
        Point start = this.getPosition();
        Point end = world.getMap().getRandomTile(start).getPosition();
        if (this.isWary()) //will not go to stronger enemy
        {
            end = world.getMap().getRandomWeakerTile(start).getPosition();
        }
        if (this.isHumanish()) //is alike to a human
        {
            Point endTile = world.getKey();
            end = world.getMap().getExactTile(start, endTile).getPosition();
        }
        Organism org = world.getMap().getTile(end).getOrganism();
        if (start.x == end.x && start.y == end.y || this.hasChill()) //if it has the possibility not to move
        {
            System.out.println(this + " stayed at (" + start.x + "," + start.y + ")");
            return true;
        }
        if (org == null) //if the tile is empty
        {
            this.setPosition(end);
            world.swap(world.getMap().getTile(start), world.getMap().getTile(end));
            return true;
        }
        else if (this.getClass().equals(org.getClass())) //if both organisms are the same type
        {
            if (world.getMap().getRandomTileForBaby(start, end) != null)
            {
                this.breed(org, start, end);
            }
            return false;
        }
        else if (this.collision(org, start, end)) //if organisms are different types
        {
            this.setPosition(end);
            world.swap(world.getMap().getTile(start), world.getMap().getTile(end));
            return false;
        }
        else
        {
            return false;
        }
    }
    public void breed(Organism love, Point start, Point end)
    {
        Tile babyHome = world.getMap().getRandomTileForBaby(start, end); //find empty tile for baby
        Organism baby = this.babyFactory(world, babyHome.getPosition());
        babyHome.setOrganism(baby);
        world.bornToQueue(baby);
        System.out.println(this + " from (" + start.x + "," + start.y + ")"
                + " mated with " + love + " at (" + end.x + "," + end.y + ")"
                + " and had a baby " + baby + " at (" + baby.getPosition().x + "," + baby.getPosition().y + ")");
    }
    public boolean collision(Organism defender, Point start, Point end)
    {
        System.out.println(this + " from (" + start.x + "," + start.y + ")"
                + " attacked " + defender + " at (" + end.x + "," + end.y + ")");
        if (tryEscape(defender, start))
        {
            return false;
        }
        else if (!defender.hasShield(this))
        {
            if(defender.isPlant())
            {
                world.kill(defender);
                if(defender.isNutrient())
                {
                    this.setStrength(this.getStrength() + defender.getNutricity());
                }
                if (defender.isToxic())
                {
                    world.kill(this);
                    return false;
                }
                return true;
            }
            else if (this.getStrength() >= defender.getStrength())
            {
                this.setPosition(defender.getPosition());
                world.kill(defender);
                return true;
            }
            else if (this.getStrength() < defender.getStrength())
            {
                world.kill(this);
                return false;
            }
        }
        return false;
    }
    public boolean tryEscape(Organism defender, Point start)
    {
        Random random = new Random();
        int chanceToEscape = random.nextInt(100);
        boolean escaped = false;
        Tile escapeTile =  world.getMap().getRandomEmptyTile(defender.getPosition()); //determine an empty tile for the possibility of escape
        if (escapeTile != null)
        {
            if (chanceToEscape < this.getEscapeChance()) //attacker escapes
            {
                System.out.println(this + " from (" + this.getPosition().x + "," + this.getPosition().y + ")"
                        + " escaped from " + defender + " at (" + defender.getPosition().x + "," + defender.getPosition().y + ")"
                        + " to position (" + escapeTile.getPosition().x + "," + escapeTile.getPosition().y + ")");
                world.swap(world.getMap().getTile(this.getPosition()), world.getMap().getTile(escapeTile.getPosition()));
                this.setPosition(escapeTile.getPosition());
                escaped = true;
            }
            if (chanceToEscape < defender.getEscapeChance()) //defender escapes, attacker moves to his place
            {
                System.out.println(defender + " from (" + defender.getPosition().x + "," + defender.getPosition().y + ")"
                        + " escaped from " + this + " at (" + this.getPosition().x + "," + this.getPosition().y + ")"
                        + " to position (" + escapeTile.getPosition().x + "," + escapeTile.getPosition().y + ")");
                this.setPosition(defender.getPosition());
                world.swap(world.getMap().getTile(defender.getPosition()), world.getMap().getTile(escapeTile.getPosition()));
                world.swap(world.getMap().getTile(start), world.getMap().getTile(this.getPosition()));
                defender.setPosition(escapeTile.getPosition());
                escaped = true;
            }
        }
        return escaped;
    }
}
