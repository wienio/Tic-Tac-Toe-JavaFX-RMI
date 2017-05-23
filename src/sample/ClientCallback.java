package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Wienio on 2017-05-22.
 */
public interface ClientCallback extends Remote {
    void setCellOnClientCallback (ClientCallbackListener clientCallbackListener) throws RemoteException;
    ClientCallbackListener getCellOnClientCallback() throws RemoteException;
}
