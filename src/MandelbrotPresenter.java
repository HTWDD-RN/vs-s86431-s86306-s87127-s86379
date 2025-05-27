import java.awt.*;

public class MandelbrotPresenter
{
    MandelbrotView view;
    MandelbrotModel model;

    int pixelWidth = 1024;
    int pixelHeight = 768;

    double xmin = -1.66666;
    double xmax = 1;
    double ymin = -1;
    double ymax = 1;

    public void setModelAndView(MandelbrotModel model, MandelbrotView view) {
        this.model = model;
        this.view = view;
        view.setDim(pixelWidth, pixelHeight);
        model.setPIXELHEIGHT(pixelHeight);
        model.setPIXELWIDTH(pixelWidth);
    }

    public void calc() {
        Color[][] c;
        c = model.generateColors(xmin, xmax, ymin, ymax);
        view.updateMandelbrot(c);
    }
}
