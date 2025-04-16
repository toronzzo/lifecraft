package LifeCraft;

import javax.swing.*;
import java.awt.*;

public class TileLabel extends JLabel implements Constants
{
    int tile_size;
    public TileLabel(int tile_size)
    {
        this.tile_size = tile_size;
        createTile();
    }
    private void createTile()
    {
        setPreferredSize(new Dimension(tile_size, tile_size));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, BORDER));
    }
}
