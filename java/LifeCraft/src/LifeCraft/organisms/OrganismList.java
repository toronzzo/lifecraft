
package LifeCraft.organisms;

import java.util.Random;

public class OrganismList {
    public OrganismList() {
    }

    public static organisms getRandomOrganism() {
        Random random = new Random();
        return OrganismList.organisms.values()[random.nextInt(OrganismList.organisms.values().length)];
    }

    public static enum organisms {
        HUMAN,
        WOLF,
        SHEEP,
        ANTELOPE,
        FOX,
        TURTLE,
        GRASS,
        SOW_THISTLE,
        GUARANA,
        BELLADONNA,
        HOGWEED;

        private organisms() {
        }
    }
}
