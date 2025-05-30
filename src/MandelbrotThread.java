import java.awt.Color;

public class MandelbrotThread extends Thread {

    private double xstart; //x-Startkoordinate
    private double ystart; //y-Startkoordinate
    private double xend;   //x-Endkoordinate
    private double yend;   //y-Endkoordinate
    private int offset;   //x-Startpunkt im Array
    private int PIXELWIDTH;//Weite des Arrayabschnitts
    private int PIXELHEIGHT;//vertikale Pixelanzahl
    private int MAX_ITERATIONS=1000;

    private static Color[] colors = new Color[]{
            Color.BLUE,
            new Color(32, 107, 203),
            new Color(237, 255, 255),
            new Color(255, 153, 51)};

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
            } while (x*x+y*y<=4);
                return iterations;
    }

    Color intToColor(int val){
        
        Color x;
        Color y;
        float blending;

        if (val < 25) {
            x = colors[0];
            y = colors[1];
            blending = (float) val / 25;
        } else if (val < 150) {
            x = colors[1];
            y = colors[2];
            blending = (float) val / (150 - 25);
        } else {
            x = colors[2];
            y = colors[3];
            blending = (float) val / (1000 - 150);
        }

        float inverse_blending = 1 - blending;

        float red = x.getRed() * blending + y.getRed() * inverse_blending;
        float green = x.getGreen() * blending + y.getGreen() * inverse_blending;
        float blue = x.getBlue() * blending + y.getBlue() * inverse_blending;

        red = red > 255 ? 255 : red;
        green = green > 255 ? 255 : green;
        blue = blue > 255 ? 255 : blue;

        red = red < 0 ? 0 : red;
        green = green < 0 ? 0 : green;
        blue = blue < 0 ? 0 : blue;

        //note that if i pass float values they have to be in the range of 0.0-1.0 
        //and not in 0-255 like the ones i get returned by the getters.
        Color blended = new Color (red / 255, green / 255, blue / 255);
        if (val != MAX_ITERATIONS)
            return blended;
        return Color.BLACK;
    }

    public void run(){
        
        double coordinterval=(xend-xstart)/colorarray.length;

        for(int i=offset;i<offset+PIXELWIDTH;i++){
            for(int o=0;o<PIXELHEIGHT;o++){
                colorarray[i][o]=intToColor(iter(xstart+i*coordinterval,yend+o*(ystart-yend)/PIXELHEIGHT));
            }
        }
    }

    MandelbrotThread(double xstart, double xend, double ystart, double yend, int offset,int pixelWidthPerThread,Color[][] colorarray){
        this.offset=offset;
        this.xstart=xstart;
        this.xend=xend;
        this.ystart=ystart;
        this.yend=yend;
        this.PIXELWIDTH=pixelWidthPerThread;
        this.PIXELHEIGHT=colorarray[0].length;
        this.colorarray=colorarray;
    }
}
