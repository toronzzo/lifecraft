package LifeCraft;

import LifeCraft.organisms.Organism;
import LifeCraft.organisms.OrganismList;
import LifeCraft.organisms.animals.Antelope;
import LifeCraft.organisms.animals.Fox;
import LifeCraft.organisms.animals.Human;
import LifeCraft.organisms.animals.Sheep;
import LifeCraft.organisms.animals.Turtle;
import LifeCraft.organisms.animals.Wolf;
import LifeCraft.organisms.plants.Belladonna;
import LifeCraft.organisms.plants.Grass;
import LifeCraft.organisms.plants.Guarana;
import LifeCraft.organisms.plants.Hogweed;
import LifeCraft.organisms.plants.Sow_thistle;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class World implements Constants {
    private final int tilesX;
    private final int tilesY;
    private final Random random = new Random();
    private Map map;
    private int turn = 0;
    private final ArrayList<Organism> moveQueue = new ArrayList();
    private ArrayList<Organism> turnQueue = new ArrayList();
    private final ArrayList<Organism> dieQueue = new ArrayList();
    private final ArrayList<Organism> bornQueue = new ArrayList();
    private Point key;
    private boolean mainCharacterAlive = false;
    private boolean mainCharacterPurge = false;

    public World(int width, int height) {
        this.tilesX = width;
        this.tilesY = height;
        this.createWorld(this.tilesX, this.tilesY, this.turn, false);
    }

    private void createWorld(int tilesX, int tilesY, int turn, boolean load) {
        this.turnQueue.clear();
        this.moveQueue.clear();
        this.bornQueue.clear();
        this.dieQueue.clear();
        this.turn = turn;
        this.map = new Map(tilesX, tilesY);
        if (!load) {
            Point point = new Point(this.random.nextInt(tilesX), this.random.nextInt(tilesY));
            Tile tile = this.map.getTile(point);
            int occupation = (int)((double)(tilesX * tilesY) * 0.5);
            int number_of_organisms = 1;
            this.addOrganism(tile, new Human(this, point));
            this.mainCharacterAlive = true;

            while(number_of_organisms < occupation) {
                point = new Point(this.random.nextInt(tilesX), this.random.nextInt(tilesY));
                tile = this.map.getTile(point);
                if (tile.getOrganism() == null) {
                    this.addOrganism(tile, (Organism)null);
                    ++number_of_organisms;
                }
            }

        }
    }

    public void addOrganism(Tile tile, Organism organism) {
        if (organism == null) {
            switch (OrganismList.getRandomOrganism()) {
                case WOLF -> organism = new Wolf(this, tile.getPosition());
                case SHEEP -> organism = new Sheep(this, tile.getPosition());
                case ANTELOPE -> organism = new Antelope(this, tile.getPosition());
                case FOX -> organism = new Fox(this, tile.getPosition());
                case TURTLE -> organism = new Turtle(this, tile.getPosition());
                case GRASS -> organism = new Grass(this, tile.getPosition());
                case SOW_THISTLE -> organism = new Sow_thistle(this, tile.getPosition());
                case GUARANA -> organism = new Guarana(this, tile.getPosition());
                case BELLADONNA -> organism = new Belladonna(this, tile.getPosition());
                case HOGWEED -> organism = new Hogweed(this, tile.getPosition());
            }
        }

        tile.setOrganism((Organism)organism);
        this.bornQueue.add(organism);
    }

    public void makeTurn() {
        System.out.println("---Turn " + this.getTurn() + "---");
        Iterator var1 = this.turnQueue.iterator();

        while(var1.hasNext()) {
            Organism organism = (Organism)var1.next();
            organism.action();
        }

        this.prepareNewTurn();
        this.setTurn(this.getTurn() + 1);
    }

    private void prepareNewTurn() {
        this.turnQueue.clear();
        this.turnQueue.addAll(this.moveQueue);
        this.turnQueue.addAll(this.bornQueue);
        this.turnQueue.removeAll(Collections.singletonList((Object)null));
        this.turnQueue = sort(this.turnQueue);
        this.moveQueue.clear();
        this.bornQueue.clear();
        this.dieQueue.clear();
        this.setMainCharacterPurge(false);
        System.out.print("\n");
    }

    private static ArrayList<Organism> sort(ArrayList<Organism> toSort) {
        Comparator<Organism> compareInitiative = Comparator.comparing(Organism::getInitiative);
        Comparator<Organism> compareAge = Comparator.comparing(Organism::getAge);
        Comparator<Organism> compare = compareInitiative.thenComparing(compareAge);
        List<Organism> sortedList = toSort.stream().sorted(compare.reversed()).toList();
        return new ArrayList(sortedList);
    }

    public Map getMap() {
        return this.map;
    }

    public void moveToQueue(Organism toMove) {
        this.moveQueue.add(toMove);
    }

    public void bornToQueue(Organism toBorn) {
        this.bornQueue.add(toBorn);
    }

    public void swap(Tile from, Tile to) {
        Organism temp = from.getOrganism();
        from.setOrganism(to.getOrganism());
        to.setOrganism(temp);
    }

    public void kill(Organism defender) {
        System.out.println("" + defender + " lies dead at (" + defender.getPosition().x + "," + defender.getPosition().y + ")");
        defender.setAlive(false);
        this.getMap().getTile(defender.getPosition()).setOrganism((Organism)null);
        if (defender instanceof Human) {
            this.mainCharacterAlive = false;
        }

    }

    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case 32:
                this.setKey(STAY);
                break;
            case 37:
                this.setKey(WEST);
                break;
            case 38:
                this.setKey(NORTH);
                break;
            case 39:
                this.setKey(EAST);
                break;
            case 40:
                this.setKey(SOUTH);
                break;
            case 80:
                this.setKey(STAY);
                System.out.println("Special ability time!");
                this.setMainCharacterPurge(true);
        }

    }

    public boolean isMainCharacterAlive() {
        return this.mainCharacterAlive;
    }

    public boolean isMainCharacterPurge() {
        boolean isPurgeTime = this.mainCharacterPurge;
        this.setMainCharacterPurge(false);
        return isPurgeTime;
    }

    public void setMainCharacterPurge(boolean mainCharacterPurge) {
        this.mainCharacterPurge = mainCharacterPurge;
    }

    public void setKey(Point key) {
        this.key = key;
    }

    public Point getKey() {
        return this.key;
    }

    public int getTilesX() {
        return this.tilesX;
    }

    public int getTilesY() {
        return this.tilesY;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return this.turn;
    }

    public void save(File file) throws IOException {
        StringBuilder str = new StringBuilder();
        str.append(this.getTurn());
        str.append("\n");
        str.append(this.getMap().getX_tiles());
        str.append("\n");
        str.append(this.getMap().getY_tiles());
        str.append("\n");
        Iterator var3 = this.turnQueue.iterator();

        while(var3.hasNext()) {
            Organism organism = (Organism)var3.next();
            if (organism.isAlive()) {
                str.append(organism.toSave());
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(str.toString());
        writer.close();
    }

    public void open(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int ability_time = 0;
        int ability_cool_down = 0;
        int turn = Integer.parseInt(reader.readLine());
        int tilesX = Integer.parseInt(reader.readLine());
        int tilesY = Integer.parseInt(reader.readLine());
        this.createWorld(tilesX, tilesY, turn, true);

        String line;
        while((line = reader.readLine()) != null) {
            String name = line;
            int age = Integer.parseInt(reader.readLine());
            int x = Integer.parseInt(reader.readLine());
            int y = Integer.parseInt(reader.readLine());
            int strength = Integer.parseInt(reader.readLine());
            if (name.equals("Human")) {
                ability_time = Integer.parseInt(reader.readLine());
                ability_cool_down = Integer.parseInt(reader.readLine());
            }

            Point position = new Point(x, y);
            Organism.load(name, age, position, strength, ability_time, ability_cool_down);
        }

        reader.close();
    }
}
