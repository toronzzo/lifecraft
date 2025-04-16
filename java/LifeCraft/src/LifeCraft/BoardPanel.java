package LifeCraft;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel implements Constants
{
    private final int x_tiles, y_tiles, tileSize;
    private final World world;
    private final JLabel[][] board;

    public BoardPanel(World world, int x_tiles, int y_tiles, int height)
    {
        this.world = world;
        this.x_tiles = x_tiles;
        this.y_tiles = y_tiles;
        this.tileSize = height / this.x_tiles;
        this.board = new JLabel[x_tiles][y_tiles];
        createBoard();
    }

    private void createBoard()
    {
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK , BORDER));
        GridLayout layout = new GridLayout(x_tiles, y_tiles);
        this.setLayout(layout);

        for (int row = 0; row < x_tiles; row++) {
            for (int column = 0; column < y_tiles; column++) {
                board[row][column] = new TileLabel(tileSize);
                add(board[row][column]);
            }
        }
    }
    private ImageIcon createIcon(ImageIcon icon)
    {
        Image image = icon.getImage();
        Image newImage = image.getScaledInstance(getWidth() / y_tiles, getHeight() / x_tiles, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newImage);
    }
    public void draw()
    {
        for (int column = 0; column < y_tiles; column++)
        {
            for (int row = 0; row < x_tiles; row++)
            {
                if (world.getMap().getTile(new Point(row, column)).getOrganism() != null)
                {
                    ImageIcon icon = createIcon(new ImageIcon(System.getProperty("user.dir") + "/src/" +
                            world.getMap().getTile(new Point(row,column)).getOrganism().toString() + ".png"));
                    board[row][column].setIcon(icon);
                }
                else
                {
                    board[row][column].setIcon(null);
                }
            }
        }
    }
    public int getTileSize()
    {
        return tileSize;
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
}
