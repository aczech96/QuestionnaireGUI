import javax.swing.*;

/**
 * Created by Anna on 21.01.2018.
 */
public class ClientTest {
    public static void main(String[] args) {
    Client client= new Client("127.0.0.1");
    client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    client.startRunning();
    }
}
