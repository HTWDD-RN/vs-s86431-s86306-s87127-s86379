import java.awt.Color;

public class MandelbrotModel
{
    int MAX_ITERATIONS = 1000;
    int PIXELHEIGHT;
    int PIXELWIDTH;

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
            } while (x <= 2 && y <= 2);
                return iterations;
    }

    public void setPIXELHEIGHT(int height){
        PIXELHEIGHT=height;
    }

    public void setPIXELWIDTH(int width){
        PIXELWIDTH=width;
    }

    Color intToColor(int val){
        //TODO write function
        return Color.BLACK;
    }

    Color[][] generateColors(double xstart, double xend, double ystart, double yend){
        Color[][] colorarray=new Color[PIXELWIDTH][PIXELHEIGHT];

        for(int i=0;i<PIXELWIDTH;i++){
            for(int o=0;o<PIXELHEIGHT;i++){
                colorarray[i][o]=intToColor(iter(xstart+i*(xend-xstart)/PIXELWIDTH,ystart+i*(yend-ystart)/PIXELHEIGHT));
            }


        }

        return colorarray;
    }

}
