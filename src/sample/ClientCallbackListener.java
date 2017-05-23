package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Wienio on 2017-05-18.
 */
public interface ClientCallbackListener extends Remote {
    void sendToClient(int status, int index) throws RemoteException;
}
