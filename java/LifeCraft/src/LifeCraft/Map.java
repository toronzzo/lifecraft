
package LifeCraft;

import LifeCraft.organisms.Organism;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Map implements Constants {
    private final int x_tiles;
    private final int y_tiles;
    private Tile[][] map;
    private final Point[] windRose;

    public Map(int x_tiles, int y_tiles) {
        this.windRose = new Point[]{NORTH, EAST, SOUTH, WEST};
        this.x_tiles = x_tiles;
        this.y_tiles = y_tiles;
        this.createMap();
    }

    private void createMap() {
        this.map = new Tile[this.x_tiles][this.y_tiles];

        for(int y = 0; y < this.y_tiles; ++y) {
            for(int x = 0; x < this.x_tiles; ++x) {
                this.map[x][y] = new Tile(new Point(x, y), (Organism)null);
            }
        }

    }

    public Tile getTile(Point point) {
        return this.map[point.x][point.y];
    }

    public ArrayList<Point> getTileInMap(Point point) {
        ArrayList<Point> validCoordinates = new ArrayList();
        Point[] var3 = this.windRose;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Point direction = var3[var5];
            Point newCoordinates = new Point(point.x + direction.x, point.y + direction.y);
            if (newCoordinates.x >= 0 && newCoordinates.x < this.x_tiles && newCoordinates.y >= 0 && newCoordinates.y < this.y_tiles) {
                validCoordinates.add(newCoordinates);
            }
        }

        return validCoordinates;
    }

    public Tile getExactTile(Point start, Point move) {
        ArrayList<Point> validCoordinates = this.getTileInMap(start);
        if (validCoordinates.isEmpty()) {
            return this.map[start.x][start.y];
        } else {
            Point newCoordinates = new Point(start.x + move.x, start.y + move.y);
            return newCoordinates.x >= 0 && newCoordinates.x < this.x_tiles && newCoordinates.y >= 0 && newCoordinates.y < this.y_tiles ? this.map[newCoordinates.x][newCoordinates.y] : this.map[start.x][start.y];
        }
    }

    public Tile getRandomTile(Point point) {
        ArrayList<Point> validCoordinates = this.getTileInMap(point);
        if (validCoordinates.isEmpty()) {
            return null;
        } else {
            Random rand = new Random();
            int randomIndex = rand.nextInt(validCoordinates.size());
            return this.map[((Point)validCoordinates.get(randomIndex)).x][((Point)validCoordinates.get(randomIndex)).y];
        }
    }

    public Tile getRandomTileForBaby(Point dad, Point mum) {
        ArrayList<Point> validCoordinates = this.getTileInMap(dad);
        ArrayList<Point> validCoordinatesSecond = this.getTileInMap(mum);
        validCoordinates.addAll(validCoordinatesSecond);
        if (dad.x == mum.x) {
            validCoordinates.removeIf((place) -> {
                return place.x == dad.x;
            });
        } else if (dad.y == mum.y) {
            validCoordinates.removeIf((place) -> {
                return place.y == dad.y;
            });
        }

        validCoordinates.removeIf((place) -> {
            return this.getTile(place).getOrganism() != null;
        });
        if (validCoordinates.isEmpty()) {
            return null;
        } else {
            Random rand = new Random();
            int randomIndex = rand.nextInt(validCoordinates.size());
            return this.map[((Point)validCoordinates.get(randomIndex)).x][((Point)validCoordinates.get(randomIndex)).y];
        }
    }

    public Tile getRandomEmptyTile(Point point) {
        ArrayList<Point> validCoordinates = this.getTileInMap(point);
        validCoordinates.removeIf((place) -> {
            return this.getTile(place).getOrganism() != null;
        });
        if (validCoordinates.isEmpty()) {
            return null;
        } else {
            Random rand = new Random();
            int randomIndex = rand.nextInt(validCoordinates.size());
            return this.map[((Point)validCoordinates.get(randomIndex)).x][((Point)validCoordinates.get(randomIndex)).y];
        }
    }

    public Tile getRandomWeakerTile(Point point) {
        ArrayList<Point> validCoordinates = this.getTileInMap(point);
        validCoordinates.removeIf((place) -> {
            return this.getTile(place).getOrganism() != null && this.getTile(point).getOrganism().getStrength() < this.getTile(place).getOrganism().getStrength();
        });
        if (validCoordinates.isEmpty()) {
            return this.map[point.x][point.y];
        } else {
            Random rand = new Random();
            int randomIndex = rand.nextInt(validCoordinates.size());
            return this.map[((Point)validCoordinates.get(randomIndex)).x][((Point)validCoordinates.get(randomIndex)).y];
        }
    }

    public ArrayList<Point> getAnimalsAround(Point point) {
        ArrayList<Point> validCoordinates = this.getTileInMap(point);
        validCoordinates.removeIf((place) -> {
            return this.getTile(place).getOrganism() == null || !this.getTile(place).getOrganism().isAnimal();
        });
        return validCoordinates;
    }

    public ArrayList<Point> getOrganismsAround(Point point) {
        ArrayList<Point> validCoordinates = this.getTileInMap(point);
        validCoordinates.removeIf((place) -> {
            return this.getTile(place).getOrganism() == null;
        });
        return validCoordinates;
    }

    public Point[] getWindRose() {
        return this.windRose;
    }

    public int getX_tiles() {
        return this.x_tiles;
    }

    public int getY_tiles() {
        return this.y_tiles;
    }
}
