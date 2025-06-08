import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MandelbrotMasterRemote extends Remote
{
    void registerWorker(WorkerRemote worker) throws RemoteException;

    int getMaxIterations() throws RemoteException;

    void unregisterWorker(WorkerRemote worker) throws RemoteException;
}
