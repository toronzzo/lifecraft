package LifeCraft;

import LifeCraft.organisms.animals.*;
import LifeCraft.organisms.plants.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class FramePanel extends JFrame implements ActionListener, Constants, MouseListener, KeyListener
{
    private final int x_tiles, y_tiles;
    private boolean isListening;
    private JButton bNextTurn;
    private JPopupMenu popupMenu;
    private JMenuItem mOpen, mSave, mExit, mHowToPlay, mAbout;
    private final World world;
    private BoardPanel board;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int screenHeight = (int) screenSize.getHeight();
    private Tile clickedTile;

    public FramePanel(World world)
    {
        this.world = world;
        this.x_tiles = world.getTilesX();
        this.y_tiles = world.getTilesY();
        this.setFocusable(true);
        this.addKeyListener(this);
        this.isListening = false;
        this.addMouseListener(this);
        createFrame();
        this.setResizable(false);
        this.setVisible(true);
        world.makeTurn();
        board.draw();
    }
    private void createFrame()
    {
        this.setPreferredSize(new Dimension((int) (screenHeight * 0.9), (int) (screenHeight * 0.9)));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(BORDER, BORDER));
        this.board = new BoardPanel(world, x_tiles, y_tiles, (int) (screenHeight * 0.9 - MENU_SIZE - BUTTON_SIZE));
        mainPanel.add(board, BorderLayout.CENTER);
        mainPanel.add(buttonPanel(), BorderLayout.PAGE_END);
        this.add(mainPanel);
        this.menuPanel();
        this.setTitle("Temirlan Ospanov s201639 - LifeCraft");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.popup();
        this.pack();
    }
    private void popup() //add organism on board by clicking on a empty tile
    {
        this.popupMenu = new JPopupMenu();

        JMenuItem wolf = new JMenuItem("Wolf");
        wolf.addActionListener((ActionEvent event) -> {
            world.addOrganism(clickedTile, new Wolf(world, new Point(clickedTile.getPosition().x, clickedTile.getPosition().y)));
            board.draw();
        });
        JMenuItem sheep = new JMenuItem("Sheep");
        sheep.addActionListener((ActionEvent event) -> {
            world.addOrganism(clickedTile, new Sheep(world, new Point(clickedTile.getPosition().x, clickedTile.getPosition().y)));
            board.draw();
        });
        JMenuItem antelope = new JMenuItem("Antelope");
        antelope.addActionListener((ActionEvent event) -> {
            world.addOrganism(clickedTile, new Antelope(world, new Point(clickedTile.getPosition().x, clickedTile.getPosition().y)));
            board.draw();
        });
        JMenuItem fox = new JMenuItem("Fox");
        fox.addActionListener((ActionEvent event) -> {
            world.addOrganism(clickedTile, new Fox(world, new Point(clickedTile.getPosition().x, clickedTile.getPosition().y)));
            board.draw();
        });
        JMenuItem turtle = new JMenuItem("Turtle");
        turtle.addActionListener((ActionEvent event) -> {
            world.addOrganism(clickedTile, new Turtle(world, new Point(clickedTile.getPosition().x, clickedTile.getPosition().y)));
            board.draw();
        });
        JMenuItem grass = new JMenuItem("Grass");
        grass.addActionListener((ActionEvent event) -> {
            world.addOrganism(clickedTile, new Grass(world, new Point(clickedTile.getPosition().x, clickedTile.getPosition().y)));
            board.draw();
        });
        JMenuItem sow_thistle = new JMenuItem("Sow_thistle");
        sow_thistle.addActionListener((ActionEvent event) -> {
            world.addOrganism(clickedTile, new Sow_thistle(world, new Point(clickedTile.getPosition().x, clickedTile.getPosition().y)));
            board.draw();
        });
        JMenuItem guarana = new JMenuItem("Guarana");
        guarana.addActionListener((ActionEvent event) -> {
            world.addOrganism(clickedTile, new Guarana(world, new Point(clickedTile.getPosition().x, clickedTile.getPosition().y)));
            board.draw();
        });
        JMenuItem belladonna = new JMenuItem("Belladonna");
        belladonna.addActionListener((ActionEvent event) -> {
            world.addOrganism(clickedTile, new Belladonna(world, new Point(clickedTile.getPosition().x, clickedTile.getPosition().y)));
            board.draw();
        });
        JMenuItem hogweed = new JMenuItem("Hogweed");
        hogweed.addActionListener((ActionEvent event) -> {
            world.addOrganism(clickedTile, new Hogweed(world, new Point(clickedTile.getPosition().x, clickedTile.getPosition().y)));
            board.draw();
        });
        popupMenu.add(wolf);
        popupMenu.add(sheep);
        popupMenu.add(antelope);
        popupMenu.add(fox);
        popupMenu.add(turtle);
        popupMenu.add(grass);
        popupMenu.add(sow_thistle);
        popupMenu.add(guarana);
        popupMenu.add(belladonna);
        popupMenu.add(hogweed);
    }
    private void menuPanel()
    {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuFile = new JMenu("Game");

        mOpen = new JMenuItem("Open");
        mOpen.addActionListener(this);

        mSave = new JMenuItem("Save");
        mSave.addActionListener(this);

        mExit = new JMenuItem("Exit");
        menuFile.add(mSave);
        menuFile.add(mOpen);
        menuFile.addSeparator();
        menuFile.add(mExit);

        mExit.addActionListener(this);

        mOpen.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        mSave.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        mExit.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));

        JMenu menuHelp = new JMenu("Help");
        mHowToPlay = new JMenuItem("How to play");
        mHowToPlay.addActionListener(this);
        menuHelp.add(mHowToPlay);
        mAbout = new JMenuItem("About");
        mAbout.addActionListener(this);
        menuHelp.add(mAbout);

        menuBar.add(menuFile);
        menuBar.add(menuHelp);
    }
    private JPanel buttonPanel()
    {
        JPanel buttonPanel = new JPanel();
        bNextTurn = new JButton("Next turn");
        bNextTurn.addActionListener(this);
        bNextTurn.setFocusable(false);
        buttonPanel.add(bNextTurn, BorderLayout.PAGE_END);
        return buttonPanel;
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object action = e.getSource();
        if (action == bNextTurn)
        {
            if (!world.isMainCharacterAlive())
            {
                world.makeTurn();
                board.draw();
                this.isListening = false;
                return;
            }
            this.isListening = true;
        }
        if (action == mOpen)
        {
            JFileChooser toOpen = new JFileChooser();
            if (toOpen.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                File file = toOpen.getSelectedFile();
                try
                {
                    world.open(file);
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "Opened file " + file.getAbsolutePath());
            }
        }
        if (action == mSave)
        {
            JFileChooser toSave = new JFileChooser();
            if (toSave.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                File file = toSave.getSelectedFile();
                try
                {
                    world.save(file);
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(null, "You saved game at " + file.getAbsolutePath());
            }
        }
        else if (action == mExit)
        {
            int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION)
            {
                dispose();
            }
        }
        if (action == mHowToPlay)
        {
            JOptionPane.showMessageDialog(this,
                    """
                            Hi, happy that you are searching for answers.\s
                            In the left upper corner you can find all the option you will need: saving, opening and exiting from program.\s
                            About the moving. It is also very simple. You just click on the big button with 'Next turn' written on it.\s
                            Then you choose what to do with your player (remember, dead player cannot move!).\s
                            Arrows - moving, \t\t\t p - special ability, \t\t\t space - stay in place.\s
                            Enjoy!!!\s""");
        }
        if (action == mAbout)
        {
            JOptionPane.showMessageDialog(this, "Oskar Koloszko s188941 JavaMeadowGame \n Version 1.0");
        }
    }
    @Override
    public void keyPressed(KeyEvent k)
    {
        int keyCode = k.getKeyCode();

        if(this.isListening)
        {
            world.keyPressed(keyCode);
            this.isListening = false;
            world.makeTurn();
            board.draw();
        }
    }
    @Override
    public void keyTyped(KeyEvent e)
    {

    }
    @Override
    public void keyReleased(KeyEvent k)
    {

    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() != MouseEvent.BUTTON3)
            return;

        int x = e.getX() - BORDER_SIZE;
        int y = e.getY() - MENU_SIZE;
        if (x < 0 || x > y_tiles * this.board.getTileSize() || y < 0 || y > x_tiles * this.board.getTileSize())
        {
            return;
        }
        int tileX = x / this.board.getTileSize();
        int tileY = y / this.board.getTileSize();
        System.out.println(tileX + " " + tileY);
        clickedTile = world.getMap().getTile(new Point(tileY, tileX));
        if(clickedTile.getOrganism() == null)
        {
            this.popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }

    }
    @Override
    public void mousePressed(MouseEvent e)
    {

    }
    @Override
    public void mouseReleased(MouseEvent e)
    {

    }
    @Override
    public void mouseEntered(MouseEvent e)
    {

    }
    @Override
    public void mouseExited(MouseEvent e)
    {

    }
}
