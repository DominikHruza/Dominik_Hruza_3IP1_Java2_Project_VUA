package hr.dhruza.networking.tcp;

import hr.dhruza.controller.MainScreenController;
import hr.dhruza.model.GameContextSerializable;
import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {
    public static final String HOST = "localhost";
    public static final int PORT = 8888;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    MainScreenController mainScreenController;

    public Server(MainScreenController msc) {
        mainScreenController = msc;
    }

    @Override
    public void run() {
        try (ServerSocket socket = new ServerSocket(PORT)) {
            serverSocket = socket;
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            clientSocket = serverSocket.accept();
            while(true){
                System.err.println("Client connected from port: " + clientSocket.getPort());
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                GameContextSerializable contextSerializable = (GameContextSerializable) ois.readObject();
                Platform.runLater(() -> mainScreenController.refresh(contextSerializable));
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
