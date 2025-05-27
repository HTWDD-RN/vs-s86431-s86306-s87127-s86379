import java.awt.*;

public class UpdateRequest implements Runnable
{
    MandelbrotView view;
    Color[][] c;

    public UpdateRequest(MandelbrotView view, Color[][] c) {
        this.view = view;
        this.c = c;
    }

    @Override
    public void run() {
        view.updateMandelbrot(c);
    }
}
