package edu.oswego.cs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MetalAlloyView {

    private final JFrame metalAlloyFrame;
    private final int DEFAULT_REGION_SIZE = 3;

    DrawRegions regions;
    private final int ORIGINAL_HEIGHT;

    private final int ORIGINAL_WIDTH;

    double heightScale;

    double widthScale;


    public MetalAlloyView(int height, int width, MetalAlloy alloy) {
        metalAlloyFrame = new JFrame("Metal Alloy");
        int taskBarHeight = Toolkit.getDefaultToolkit().getScreenInsets(metalAlloyFrame.getGraphicsConfiguration()).top + 10;
        ORIGINAL_HEIGHT = DEFAULT_REGION_SIZE * height + taskBarHeight;
        ORIGINAL_WIDTH = DEFAULT_REGION_SIZE * width;
        heightScale = 1.0;
        widthScale = 1.0;
        metalAlloyFrame.setSize(ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
        metalAlloyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        metalAlloyFrame.setResizable(true);
        displayRegions(alloy);
        metalAlloyFrame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                Component c = event.getComponent();
                int height = c.getHeight();
                int width = c.getWidth();
                heightScale = ((double)height - taskBarHeight) / (ORIGINAL_HEIGHT - taskBarHeight);
                widthScale = (double)width / ORIGINAL_WIDTH;
                displayRegions(alloy);
            }
        });
    }

    public void displayRegions(MetalAlloy alloy) {
        metalAlloyFrame.getContentPane().removeAll();
        regions = new DrawRegions(alloy);
        metalAlloyFrame.getContentPane().add(regions);
        metalAlloyFrame.revalidate();
        metalAlloyFrame.repaint();
    }

    public void display() {
        metalAlloyFrame.setVisible(true);
    }


    private class DrawRegions extends JPanel {

        MetalAlloy alloy;

        public DrawRegions(MetalAlloy metalAlloy) {
            this.alloy = metalAlloy;
        }

        public void drawRegions(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            for (int row = 0; row < alloy.getHeight(); row++) {
                for (int col = 0; col < alloy.getWidth(); col++) {
                    MetalAlloyRegion region = alloy.getMetalAlloyRegion(row, col);
                    Color regionColor = new Color(region.getR(), region.getG(), region.getB());
                    graphics2D.setColor(regionColor);
                    graphics2D.fillRect(col * DEFAULT_REGION_SIZE, row * DEFAULT_REGION_SIZE, DEFAULT_REGION_SIZE, DEFAULT_REGION_SIZE);
                }
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D)graphics;
            g2.scale(widthScale, heightScale);
            super.paintComponent(g2);
            drawRegions(g2);
        }
    }
}
