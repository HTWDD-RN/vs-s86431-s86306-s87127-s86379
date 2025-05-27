import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MandelbrotPresenter implements ActionListener
{
    MandelbrotView view;
    MandelbrotModel model;

    int ZOOM_STEPS = 100;

    int pixelWidth = 1024;
    int pixelHeight = 768;

    double real;
    double im;
    double zoomFactor;

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
        real = Double.parseDouble(view.reField.getText());
        im = Double.parseDouble(view.imField.getText());
        zoomFactor = Double.parseDouble(view.zoomField.getText());
    }

    public void calc() {
        Color[][] c;
        for (int i = 0; i < ZOOM_STEPS; i++) {
            c = model.generateColors(xmin, xmax, ymin, ymax);
            view.updateMandelbrot(c);
            double x = xmax - xmin;
            double y = ymax - ymin;

            xmin = real - (x/2)*zoomFactor;
            xmax = real + (x/2)*zoomFactor;
            ymin = im - (y/2)*zoomFactor;
            ymax = im + (y/2)*zoomFactor;
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        real = Double.parseDouble(view.reField.getText());
        im = Double.parseDouble(view.reField.getText());
        zoomFactor = Double.parseDouble(view.zoomField.getText());
        xmin = -1.66666;
        xmax = 1;
        ymin = -1;
        ymax = 1;
    }
}
