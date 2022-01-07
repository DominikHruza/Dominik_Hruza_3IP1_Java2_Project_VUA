package hr.dhruza.networking.tcp;

import hr.dhruza.controller.MainScreenController;
import hr.dhruza.model.GameContextSerializable;
import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread {

    MainScreenController mainScreenController;
    private Socket clientSocket;

    public Client(MainScreenController msc) {
        mainScreenController = msc;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(Server.HOST, Server.PORT)) {
            clientSocket = socket;
            System.err.println("Client is connecting to: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            while(true){
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                GameContextSerializable contextSerializable = (GameContextSerializable) ois.readObject();
                Platform.runLater(() -> mainScreenController.refresh(contextSerializable) );
                System.out.println("Context data received");
            }
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendData(GameContextSerializable gameContextSerializable) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(gameContextSerializable);
            System.out.println("Context data sent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
