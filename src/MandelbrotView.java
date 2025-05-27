import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MandelbrotView
{
    private MandelbrotPresenter presenter;
    private MandelbrotPanel p;
    JTextField reField;
    JTextField imField;
    JTextField zoomField;
    private int width;
    private int height;
    BufferedImage image;

    public MandelbrotView(MandelbrotPresenter presenter) {
        this.presenter = presenter;
    }

    public void setDim(int width, int height) {
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        initView();
    }

    private void initView() {
        JFrame frame = new JFrame("Mandelbrot");
        JPanel panel = new JPanel(new FlowLayout());
        p = new MandelbrotPanel();

        reField = new JTextField("-0.34837308755059104");
        imField = new JTextField("-0.6065038451823017");
        zoomField = new JTextField("0.8");
        panel.add(reField);
        panel.add(imField);
        panel.add(zoomField);

        frame.add(p, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        frame.setSize(width, height+50); // text fields -> +50
        frame.setVisible(true);
    }

    public void updateMandelbrot(Color[][] color) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, color[x][y].getRGB());
            }
        }
        p.repaint();
    }

    class MandelbrotPanel extends JPanel
    {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, width, height, null);
        }
    }

}
