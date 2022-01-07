package hr.dhruza.networking.multicast;

import hr.dhruza.model.GameContextSerializable;
import hr.dhruza.utils.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMulticastThread extends Thread {

    public static final int CLIENT_PORT = 4446;
    public static final String GROUP = "230.0.0.1";
    private final LinkedBlockingDeque<GameContextSerializable> gameStates = new LinkedBlockingDeque<>();

    @Override
    public void run() {
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            System.err.println("Server multicasting on port: " + serverSocket.getLocalPort());
            InetAddress groupAdress = InetAddress.getByName(GROUP);
            Integer clientPort = Integer.valueOf(CLIENT_PORT);
            while (true) {
                  if(!gameStates.isEmpty()){
                      byte[] contextBytes;
                      try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
                          ObjectOutputStream oos = new ObjectOutputStream(baos)){
                          oos.writeObject(gameStates.getFirst());
                          gameStates.clear();
                          oos.flush();
                          contextBytes = baos.toByteArray();
                      }

                      byte[] contextByteSize = ByteUtils.intToByteArray(contextBytes.length);

                      DatagramPacket datagramPacket = new DatagramPacket(
                              contextByteSize,
                              contextByteSize.length,
                              groupAdress,
                              clientPort);
                      serverSocket.send(datagramPacket);

                      DatagramPacket contextPacket = new DatagramPacket(
                              contextBytes,
                              contextBytes.length,
                              groupAdress,
                              clientPort);
                      serverSocket.send(contextPacket);

                    System.out.println("sent");
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
