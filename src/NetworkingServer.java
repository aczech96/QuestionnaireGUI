import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Anna on 21.01.2018.
 */
public class NetworkingServer implements Runnable {
    ServerSocket serverSocket;
    Socket socket;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    DataBase baza;
    Server server;
    Wykres wykres;

    public NetworkingServer(DataBase baza, Server server) {
        this.baza = baza;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(6789, 100);
            try {
                waitForConection();
                setupStream();
                receiveMsg();
                close();

            } catch (EOFException e) {
                System.out.println("Serwer zakonczyl polaczenie");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //serwer czeka na klienta az wysle ze chce sie polaczyc, pozniej akceptuje polaczenie na gniazdo i wypisuje z kim jest polaczony
    private void waitForConection() throws IOException {
        System.out.println("Czekanie na klienta...");
        socket = serverSocket.accept();
        System.out.println("Polaczono z" + socket.getInetAddress().getHostName());
    }

    // ustawianie polaczenia wejsciowego i wyjsciowego
    private void setupStream() throws IOException {
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());
        System.out.println("Mozliwe jest przesylanie pomiedzy klientem a serwerem");
    }

    public void sendMsg(String message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMsg() {
        System.out.println("Polaczono");
        String message = "";
        try {
            while (true) {
                if (!server.flag) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    message = (String)inputStream.readObject();
                    baza.saveInDB(message);
                    server.flag = false;
                }
                if (server.endGame && !server.flag) {
                    sendMsg("Dziekujemy za wypelnienie ankiety");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void close() {
        System.out.println("Zamykanie strumienia");
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
