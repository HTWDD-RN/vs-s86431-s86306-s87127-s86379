public class MandelbrotThread extends Thread {

    int xstart;
    int PIXELWIDTH;
    int PIXELHEIGHT;

    public void run(){
        System.out.println("AAAAAAAAAAAHHHHHHHHHHH");
    }


    MandelbrotThread(int xstart,int width,int PIXELHEIGHT){
        this.xstart=xstart;
        this.PIXELWIDTH=width;
        this.PIXELHEIGHT=PIXELHEIGHT;
    }
}
