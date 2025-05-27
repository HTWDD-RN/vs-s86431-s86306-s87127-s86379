import java.awt.Color;

public class MandelbrotModel
{
    int MAX_ITERATIONS = 1000;
    int PIXELHEIGHT;
    int PIXELWIDTH;
    int THREADCUNT=4;

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

    public void setTHREADCOUNT(int cunt){
        this.THREADCUNT=cunt;
    }

    Color intToColor(int val){
        //TODO: geringe Iterationszahl manuell mit Farben bestücken
        Color x;
        Color y;
        float blending;

        if (val < 25) {
            x = Color.BLUE;
            y = new Color(32, 107, 203);
            blending = (float) val / 25;
        } else if (val < 150) {
            x = new Color(32, 107, 203);
            y = new Color(237, 255, 255);
            blending = (float) val / (150 - 25);
        } else {
            x = new Color(237, 255, 255);
            y = new Color(255, 153, 51);
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

    Color[][] generateColors(double xstart, double xend, double ystart, double yend){

        int pixelWidthPerThread=PIXELWIDTH/THREADCUNT;
        MandelbrotThread[] threadarray = new MandelbrotThread[THREADCUNT];

        Color[][] colorarray=new Color[PIXELWIDTH][PIXELHEIGHT];

        //threads starten
        for (int i=0;i<THREADCUNT-1;i++){
            threadarray[i]=new MandelbrotThread(i*pixelWidthPerThread,pixelWidthPerThread,PIXELHEIGHT);
            threadarray[i].start();
        }        
        threadarray[THREADCUNT-1]=new MandelbrotThread((THREADCUNT-1)*pixelWidthPerThread,(PIXELWIDTH-pixelWidthPerThread*(THREADCUNT-1)),PIXELHEIGHT);
        threadarray[THREADCUNT-1].start();

        //auf threadterminierung warten
        try{
            for(int i=0;i<THREADCUNT-1;i++){
                threadarray[i].join();
            }
        }
        catch(InterruptedException e){
            System.out.println("juckt");
        }

        for(int i=0;i<PIXELWIDTH;i++){
            for(int o=0;o<PIXELHEIGHT;o++){
                colorarray[i][o]=intToColor(iter(xstart+i*(xend-xstart)/PIXELWIDTH,yend+o*(ystart-yend)/PIXELHEIGHT));
            }
        }

        return colorarray;
    }

}
