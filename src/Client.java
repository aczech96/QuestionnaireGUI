import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Created by Anna on 21.01.2018.
 */
public class Client extends JFrame {
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String message = "";
    private String serverIP;
    private Socket socket;
    private JTextField userText;
    private JTextArea chatWindow;

    public Client(String host) {
        super("Klient");
        serverIP = host;
        userText = new JTextField();
        userText.setEnabled(false);
        userText.addActionListener(
                event -> {
                    sendMessage(event.getActionCommand());
                    userText.setText("");
                }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(1366 / 3, 768 / 2);
        setVisible(true);
    }

    public void startRunning() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();
        } catch (EOFException e) {
            showMessage("Ustawiono polaczenie\n");
        } catch (IOException ex) {
            showMessage("Rozlaczono");
        } finally {
            close();
        }
    }

    //laczenie z serverem
    private void connectToServer() throws IOException {
        showMessage("Proba polaczenia\n");
        socket = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Polaczony z: " + socket.getInetAddress().getHostName() + "\n");
    }

    //ustawia polaczenie w celu wysylania i odbierania info
    private void setupStreams() throws IOException {
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());
        showMessage("Mozlie jest przesylanie info miedzy serwerem a kientem\n");
    }

    private void whileChatting() throws IOException {
        ableToType(true);
        do {
            try {
                message = (String) inputStream.readObject();
                showMessage(message);
            } catch (ClassNotFoundException e) {
                showMessage("Nieznany typ obiektu\n");
            }

        } while (!message.equals("Dziekujemy za wypelnienie ankiety"));
    }

    private void close() {
        System.out.println("Czyszczenie polaczenia\n");
        ableToType(false);
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException ioeEeption) {
            ioeEeption.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
            showMessage("Klient - " + message + "\n");
        } catch (IOException e) {
            chatWindow.append("Blad przy wysylaniu\n");
        }
    }

    private void showMessage(final String text) {
        SwingUtilities.invokeLater(
                () -> chatWindow.append(text)
        );
    }
//tof - true or false
    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(
                () -> userText.setEnabled(tof)
        );
    }
}
