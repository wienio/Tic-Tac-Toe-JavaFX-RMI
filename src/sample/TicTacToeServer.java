package sample;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Wienio on 2017-05-18.
 */
public class TicTacToeServer extends UnicastRemoteObject implements ClientCallback, ServerInterface {

    private ClientCallbackListener clientCallbackListener;
    private GuiCellListener guiCellListener;

    public void setGuiCellListener(GuiCellListener guiCellListener) {
        this.guiCellListener = guiCellListener;
    }

    protected TicTacToeServer() throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind("LocalServer", this);
    }

    public void sendToClient (int status, int index) {
        if(clientCallbackListener != null) {
            try {
                clientCallbackListener.sendToClient(status, index);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setCellOnClientCallback (ClientCallbackListener clientCallbackListener) throws RemoteException {
        this.clientCallbackListener = clientCallbackListener;
    }

    @Override
    public ClientCallbackListener getCellOnClientCallback() throws RemoteException {
        return clientCallbackListener;
    }

    @Override
    public void sendCell(int status, int index) {
        if (guiCellListener != null) {
            guiCellListener.writeCell(new Cell(this,status, index));
        }
    }
}
