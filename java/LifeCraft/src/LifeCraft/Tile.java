package LifeCraft;

import LifeCraft.organisms.Organism;

import java.awt.*;

public class Tile
{
    private final Point position;
    private Organism organism;
    public Tile(Point position, Organism organism)
    {
        this.position = position;
        this.organism = organism;
    }
    public void setOrganism(Organism organism)
    {
        this.organism = organism;
    }
    public Organism getOrganism()
    {
        return organism;
    }
    public Point getPosition()
    {
        return position;
    }
}
