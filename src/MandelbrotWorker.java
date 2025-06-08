import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MandelbrotWorker implements WorkerRemote
{
    static int maxIterations = 1000;

    public MandelbrotWorker() {
    }

    @Override
    public Color[][] generateColors(double xstart, double xend, double ystart, double yend, int Width, int Height) throws RemoteException {
        MandelbrotModel model = new MandelbrotModel();
        model.setMAX_ITERATIONS(maxIterations);
        model.setPIXELWIDTH(Width);
        model.setPIXELHEIGHT(Height);


        Color[][] colorArray = model.generateColors(xstart, xend, ystart, yend);
        return colorArray;
    }

    public static void main(String[] args) {
        try {

            MandelbrotWorker worker = new MandelbrotWorker();
            WorkerRemote stub = (WorkerRemote) UnicastRemoteObject.exportObject(worker, 0);

            Registry registry = LocateRegistry.getRegistry(args[0], 1099);

            MandelbrotMasterRemote master = (MandelbrotMasterRemote) registry.lookup("Master");

            master.registerWorker(stub);
            maxIterations = master.getMaxIterations();
            System.out.println("Worker registriert und bereit.");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    master.unregisterWorker(stub);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
