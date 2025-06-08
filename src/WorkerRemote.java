import java.awt.Color;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WorkerRemote extends Remote
{
    Color[][] generateColors(double xstart, double xend, double ystart, double yend, int Width, int Height
    ) throws RemoteException;
}
