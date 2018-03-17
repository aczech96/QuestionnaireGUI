import java.sql.Connection;

/**
 * Created by Anna on 21.01.2018.
 */

public class Server {
    public boolean flag, endGame = false;

    public void startRunning() {
        DataBase baza = new DataBase();
        NetworkingServer networkingServer = new NetworkingServer(baza, this);
        new Thread(networkingServer).start();
        String message = "";
        int i = 1;
        int end = baza.numberOfRows();
        while (true) {
            if (networkingServer.outputStream == null || flag) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                do {
                    if (!flag) {
                        message = baza.readDataBase(i);
                        networkingServer.sendMsg(message + "\n");
                        flag = true;
                        i++;
                    } else {
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } while (i <= end);
                break;
            }
        }
        endGame = true;
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.startRunning();
    }
}