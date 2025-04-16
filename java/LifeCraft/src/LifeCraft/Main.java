package LifeCraft;

import javax.swing.*;

public class Main implements Constants
{
    public static void main(String[] args)
    {
        String width_s = JOptionPane.showInputDialog("Enter width");
        String height_s = JOptionPane.showInputDialog("Enter height");
        int width = Integer.parseInt(width_s);
        int height = Integer.parseInt(height_s);
        World world = new World(width, height);
        new FramePanel(world);
    }
}
