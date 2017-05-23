package sample;

import javafx.scene.control.TextField;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Wienio on 2017-05-18.
 */
public class TicTacToeClient extends UnicastRemoteObject implements ClientCallbackListener {
    Remote remoteService = null;
    private GuiCellListener guiCellListener;

    protected TicTacToeClient(TextField textField) throws RemoteException {
        super();
        try {
            remoteService = Naming.lookup(textField.getText());

            ClientCallback callback = (ClientCallback) remoteService;
            callback.setCellOnClientCallback(this);
        } catch (NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void sendToServer(int status, int index) {
        try {
            ServerInterface server = (ServerInterface) remoteService;
            server.sendCell(status, index);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendToClient(int status, int index) throws RemoteException {
        if (guiCellListener != null) {
            guiCellListener.writeCell(new Cell(this, status, index));
        }
    }

    public void setGuiCellListener(GuiCellListener guiCellListener) {
        this.guiCellListener = guiCellListener;
    }
}
