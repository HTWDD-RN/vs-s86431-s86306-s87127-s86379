import java.awt.Color;

public class MandelbrotThread extends Thread
{

    private double xstart; //x-Startkoordinate
    private double ystart; //y-Startkoordinate
    private double xend;   //x-Endkoordinate
    private double yend;   //y-Endkoordinate
    private int offset;   //x-Startpunkt im Array
    private int PIXELWIDTH;//Weite des Arrayabschnitts
    private int PIXELHEIGHT;//vertikale Pixelanzahl
    private int MAX_ITERATIONS;

    private Color[] blendedColors;

    private Color[][] colorarray;

    private int iter(double a, double b) {

        double x = 0.0;
        double y = 0.0;
        int iterations = 0;

        do {
            double xnew = x * x - y * y + a;
            double ynew = 2 * x * y + b;
            x = xnew;
            y = ynew;
            iterations++;
            if (iterations == MAX_ITERATIONS)
                return MAX_ITERATIONS;
        } while (x * x + y * y <= 4);
        return iterations;
    }

    public void run() {

        double coordinterval = (xend - xstart) / colorarray.length;

        for (int i = offset; i < offset + PIXELWIDTH; i++) {
            for (int o = 0; o < PIXELHEIGHT; o++) {
                colorarray[i][o] = blendedColors[(iter(xstart + i * coordinterval, yend + o * (ystart - yend) / PIXELHEIGHT))];
            }
        }
    }

    MandelbrotThread(double xstart, double xend, double ystart, double yend, int offset, int pixelWidthPerThread, Color[][] colorarray, Color[] blendedColors, int MAX_ITERATIONS) {
        this.offset = offset;
        this.xstart = xstart;
        this.xend = xend;
        this.ystart = ystart;
        this.yend = yend;
        this.PIXELWIDTH = pixelWidthPerThread;
        this.PIXELHEIGHT = colorarray[0].length;
        this.colorarray = colorarray;
        this.blendedColors = blendedColors;
        this.MAX_ITERATIONS = MAX_ITERATIONS;
    }
}
