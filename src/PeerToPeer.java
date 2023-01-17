import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PeerToPeer extends JFrame{

    private final JTextField inputField;
    private final JTextArea chatArea;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) {
//        if (args.length != 2) {
//            System.err.println("Usage: java ChatPeer <host name> <port number>");
//            System.exit(1);
//        }
//
//        String hostName = args[0];
//        int portNumber = Integer.parseInt(args[1]);

        PeerToPeer peerToPeer = new PeerToPeer("localhost", 6667);
    }

    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    chatArea.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public PeerToPeer(String hostName, int portNumber) {
        super("P2P Chat");

        try {
            Socket peerSocket = new Socket(hostName, portNumber);
            out = new PrintWriter(peerSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(peerSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }

        inputField = new JTextField();
        inputField.setEditable(false);
        inputField.addActionListener(e -> {
            out.println(inputField.getText());
            inputField.setText("");
        });
        add(inputField, BorderLayout.NORTH);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                out.println("END");
                System.exit(0);
            }
        });

        setSize(300, 150);
        setVisible(true);

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
    }
}