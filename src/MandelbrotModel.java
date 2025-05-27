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
            } while (x*x+y*y<=4);
                return iterations;
    }

    public void setPIXELHEIGHT(int height){
        PIXELHEIGHT=height;
    }

    public void setPIXELWIDTH(int width){
        PIXELWIDTH=width;
    }

    Color intToColor(int val){
        Color x=Color.BLUE;
        Color y=Color.ORANGE; //set by you
        float blending=(float)val/MAX_ITERATIONS;

        //if(blending>0.5 && blending!=1){
        //    System.out.println(blending);
        //}
        

        float inverse_blending = 1 - blending;

        float red =   x.getRed()   * blending   +   y.getRed()   * inverse_blending;
        float green = x.getGreen() * blending   +   y.getGreen() * inverse_blending;
        float blue =  x.getBlue()  * blending   +   y.getBlue()  * inverse_blending;

        //note that if i pass float values they have to be in the range of 0.0-1.0 
        //and not in 0-255 like the ones i get returned by the getters.
        Color blended = new Color (red / 255, green / 255, blue / 255);
        if (val != MAX_ITERATIONS)
            return blended;
        return Color.BLACK;
    }

    Color[][] generateColors(double xstart, double xend, double ystart, double yend){
        Color[][] colorarray=new Color[PIXELWIDTH][PIXELHEIGHT];

        for(int i=0;i<PIXELWIDTH;i++){
            for(int o=0;o<PIXELHEIGHT;o++){
                colorarray[i][o]=intToColor(iter(xstart+i*(xend-xstart)/PIXELWIDTH,ystart+o*(yend-ystart)/PIXELHEIGHT));
            }


        }

        return colorarray;
    }

}
