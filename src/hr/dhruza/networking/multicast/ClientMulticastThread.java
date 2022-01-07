package hr.dhruza.networking.multicast;

import hr.dhruza.controller.MainScreenClientController;
import hr.dhruza.model.GameContextSerializable;
import hr.dhruza.utils.ByteUtils;
import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientMulticastThread extends Thread {
    public static final int CLIENT_PORT = 4446;
    public static final String GROUP = "230.0.0.1";
    private final LinkedBlockingDeque<GameContextSerializable> gameStates = new LinkedBlockingDeque<>();
    private final MainScreenClientController mainScreenController;

    public ClientMulticastThread(MainScreenClientController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    @Override
    public void run() {
        try (MulticastSocket client = new MulticastSocket(CLIENT_PORT)) {
            InetAddress groupAdress = InetAddress.getByName(GROUP);
            Integer clientPort = Integer.valueOf(CLIENT_PORT);

            client.joinGroup(groupAdress);
            System.out.println(mainScreenController.hashCode() + " joined group " + GROUP);

            while (true){
                byte[] numberOfContextBytes = new byte[4];
                DatagramPacket packet = new DatagramPacket(numberOfContextBytes, numberOfContextBytes.length);
                client.receive(packet);
                int length = ByteUtils.byteArrayToInt(numberOfContextBytes);

                byte[] contextBytes = new byte[length];
                packet = new DatagramPacket(contextBytes, contextBytes.length);
                client.receive(packet);

                try(ByteArrayInputStream bais = new ByteArrayInputStream(contextBytes);
                    ObjectInputStream ois = new ObjectInputStream(bais)){
                    GameContextSerializable contextSerializable = (GameContextSerializable) ois.readObject();
                    Platform.runLater(() -> mainScreenController.refresh(contextSerializable));
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(ServerMulticastThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void trigger(GameContextSerializable gameContext){
        gameStates.add(gameContext);
    }
}
