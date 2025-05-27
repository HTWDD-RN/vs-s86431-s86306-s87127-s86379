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

    ZoomThread thread;

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
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        thread = new ZoomThread();
        thread.start();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        real = Double.parseDouble(view.reField.getText());
        im = Double.parseDouble(view.imField.getText());
        zoomFactor = Double.parseDouble(view.zoomField.getText());
        xmin = -1.66666;
        xmax = 1;
        ymin = -1;
        ymax = 1;
        calc();
    }

    public class ZoomThread extends Thread
    {
        @Override
        public void run() {
            Color[][] c;
            for (int i = 0; i < ZOOM_STEPS && !isInterrupted(); i++) {
                c = model.generateColors(xmin, xmax, ymin, ymax);
                UpdateRequest update = new UpdateRequest(view, c);
                EventQueue.invokeLater(update);
                double x = xmax - xmin;
                double y = ymax - ymin;

                xmin = real - (x / 2) * zoomFactor;
                xmax = real + (x / 2) * zoomFactor;
                ymin = im - (y / 2) * zoomFactor;
                ymax = im + (y / 2) * zoomFactor;
            }
        }
    }
}
