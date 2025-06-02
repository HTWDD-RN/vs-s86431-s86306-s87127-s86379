import java.awt.Color;

public class MandelbrotModel
{
    int MAX_ITERATIONS = 1000;
    int PIXELHEIGHT;
    int PIXELWIDTH;
    int THREADCUNT=1;
    private Color[] blendedColors = new Color[MAX_ITERATIONS+1];
    private static final Color[] colors = new Color[]{
            Color.BLUE,
            new Color(32, 107, 203),
            new Color(237, 255, 255),
            new Color(255, 153, 51)};

    public void setPIXELHEIGHT(int height){
        PIXELHEIGHT=height;
    }

    public void setPIXELWIDTH(int width){
        PIXELWIDTH=width;
    }

    public void setTHREADCOUNT(int cunt){
        this.THREADCUNT=cunt;
    }

    public void setMAX_ITERATIONS(int MAX_ITERATIONS) {
        this.MAX_ITERATIONS = MAX_ITERATIONS;
        generateBlendedColors();
    }

    private void generateBlendedColors() {
        for (int i = 0; i <= MAX_ITERATIONS; i++)
        {
            blendedColors[i] = intToColor(i);
        }
    }

    Color intToColor(int val){

        Color x;
        Color y;
        float blending;

        if (val < MAX_ITERATIONS * 0.025) {
            x = colors[0];
            y = colors[1];
            blending = (float) (val /(MAX_ITERATIONS * 0.025));
        } else if (val < MAX_ITERATIONS * 0.15) {
            x = colors[1];
            y = colors[2];
            blending = (float) (val / (MAX_ITERATIONS * 0.15 - MAX_ITERATIONS * 0.025));
        } else {
            x = colors[2];
            y = colors[3];
            blending = (float) (val / (MAX_ITERATIONS - MAX_ITERATIONS * 0.15));
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

        //START THREADS (EXCEPT LAST SLICE)
        for (int i=0;i<THREADCUNT-1;i++){
            threadarray[i]=new MandelbrotThread(xstart,xend,ystart,yend,i*pixelWidthPerThread,pixelWidthPerThread,colorarray, blendedColors);
            threadarray[i].start();
        }

        //START LAST THREAD (FOR LAST SLICE)
        threadarray[THREADCUNT-1]=new MandelbrotThread(xstart,xend,ystart,yend,(THREADCUNT-1)*pixelWidthPerThread,colorarray.length-(THREADCUNT-1)*pixelWidthPerThread,colorarray, blendedColors);
        threadarray[THREADCUNT-1].start();

        //WAIT FOR ALL THREADS TO TERMINATE
        try{
            for(int i=0;i<THREADCUNT-1;i++){
                threadarray[i].join();
            }
            threadarray[THREADCUNT-1].join();
        }
        catch(InterruptedException e){
            System.out.println("Starting anew...");
            return null;
        }

        return colorarray;
    }
}
