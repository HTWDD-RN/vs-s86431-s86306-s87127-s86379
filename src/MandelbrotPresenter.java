import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class MandelbrotPresenter implements ActionListener, MandelbrotMasterRemote
{
    MandelbrotView view;
    MandelbrotModel model;

    int ZOOM_STEPS = 100;

    int pixelWidth = 1024;
    int pixelHeight = 768;

    double real;
    double im;
    double zoomFactor;

    int maxIterations = 1000;

    double xmin = -1.66666;
    double xmax = 1;
    double ymin = -1;
    double ymax = 1;

    ZoomThread thread;

    private final List<WorkerRemote> workers = new ArrayList<>();

    public MandelbrotPresenter(int maxIterations, int pixelWidth, int pixelHeight, int zoom_steps) {
        this.maxIterations = maxIterations;
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
        this.ZOOM_STEPS = zoom_steps;
    }

    public MandelbrotPresenter() {
    }

    @Override
    public synchronized void registerWorker(WorkerRemote worker) throws RemoteException {
        workers.add(worker);
        System.out.println("Worker registered: " + worker);
    }

    @Override
    public synchronized void unregisterWorker(WorkerRemote worker) throws RemoteException {
        workers.remove(worker);
        System.out.println("Worker unregistered: " + worker);
    }

    @Override
    public int getMaxIterations() throws RemoteException {
        return maxIterations;
    }

    public void setModelAndView(MandelbrotModel model, MandelbrotView view) {
        this.model = model;
        this.view = view;

        // View/Model-Initialisierung bleibt gleich
        view.setDim(pixelWidth, pixelHeight);
        model.setPIXELHEIGHT(pixelHeight);
        model.setPIXELWIDTH(pixelWidth);
        model.setMAX_ITERATIONS(maxIterations);
        real = Double.parseDouble(view.reField.getText());
        im = Double.parseDouble(view.imField.getText());
        zoomFactor = Double.parseDouble(view.zoomField.getText());

        // Presenter als Master registrieren
        try {

            MandelbrotMasterRemote stub = (MandelbrotMasterRemote) UnicastRemoteObject.exportObject(this, 0);

            //Registry auf Port 1099
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("Registry created on Port 1099.");
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(1099);
                System.out.println("Registry already exists on Port 1099.");
            }

            // MasterPresenter in der Registry binden
            registry.rebind("Master", stub);
            System.out.println("MandelbrotPresenter bound as 'Master'.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
            for (int i = 0; i < ZOOM_STEPS; i++) {
                // Anzahl der aktuell registrierten Worker
                int available = workers.size();

                // keine worker -> lokal
                if (available == 0) {
                    System.out.println("No workers available, calculating locally.");
                    Color[][] full = model.generateColors(xmin, xmax, ymin, ymax);
                    if (full != null) {
                        UpdateRequest update = new UpdateRequest(view, full);
                        EventQueue.invokeLater(update);
                    } else {
                        break;
                    }
                }
                // wenn worker, streifen
                else {
                    int height = pixelHeight;
                    int width = pixelWidth;

                    int rowsPerWorker = height / available;
                    Color[][] combined = new Color[width][height];

                    double imagStepPerPixel = (ymax - ymin) / (double) height;

                    List<StripeResult> results = Collections.synchronizedList(new ArrayList<>());
                    CountDownLatch latch = new CountDownLatch(available);

                    for (int wIdx = 0; wIdx < available; wIdx++) {
                        final int yStart = wIdx * rowsPerWorker;
                        final int yEnd = (wIdx == available - 1) ? height : (yStart + rowsPerWorker);
                        final int yChunkSize = yEnd - yStart;
                        final WorkerRemote w = workers.get(wIdx);

                        new Thread(() -> {
                            try {
                                Color[][] stripe = w.generateColors(
                                        xmin, xmax, ymin + yStart * imagStepPerPixel, ymin + yEnd * imagStepPerPixel, width, yChunkSize
                                );
                                results.add(new StripeResult(yStart, yChunkSize, stripe));
                            } catch (RemoteException e) {
                                System.err.println("RemoteException while generating colors from worker: " + e.getMessage());
                                e.printStackTrace();
                            } finally {
                                latch.countDown();
                            }
                        }).start();
                    }

                    // Warten bis alle Worker fertig sind
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    for (int j = 0; j < results.size(); j++) {
                        StripeResult result = results.get(j);
                        int targetYStart = height - result.yStart - result.yChunkSize;
                        for (int x = 0; x < width; x++) {
                            for (int yLocal = 0; yLocal < result.yChunkSize; yLocal++) {
                                combined[x][targetYStart + yLocal] = result.stripe[x][yLocal];
                            }
                        }
                    }

                    UpdateRequest update = new UpdateRequest(view, combined);
                    EventQueue.invokeLater(update);
                }

                double x = xmax - xmin;
                double y = ymax - ymin;
                xmin = real - (x / 2) * zoomFactor;
                xmax = real + (x / 2) * zoomFactor;
                ymin = im - (y / 2) * zoomFactor;
                ymax = im + (y / 2) * zoomFactor;
            }
        }

        class StripeResult
        {
            int yStart;
            int yChunkSize;
            Color[][] stripe;

            StripeResult(int yStart, int yChunkSize, Color[][] stripe) {
                this.yStart = yStart;
                this.yChunkSize = yChunkSize;
                this.stripe = stripe;
            }
        }
    }
}
