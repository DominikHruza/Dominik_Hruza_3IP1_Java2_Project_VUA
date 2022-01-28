package hr.dhruza.rmi;

import javafx.scene.paint.Color;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatService extends Remote {
    Color getColor() throws RemoteException;
    String getName() throws RemoteException;
    void send(String message) throws RemoteException;
}
