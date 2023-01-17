import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class ConexionServer implements Runnable {
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private static Vector<ConexionServer> chatThreads = new Vector<>();
    private String name = "";

    public ConexionServer(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                out.println("Melissa ese imbecil");
                name = in.nextLine();
                if (name == null) {
                    return;
                }
                synchronized (chatThreads) {
                    for (ConexionServer client : chatThreads) {
                        if (client.name.equals(name)) {
                            out.println("NAMEALREADYEXISTS");
                            return;
                        }
                    }
                    break;
                }
            }

            out.println("SUBMITNAME" + name);
            chatThreads.add(this);
            while (true) {
                String input = in.nextLine();
                if (input.startsWith("/quit")) {
                    return;
                }
                for (ConexionServer client : chatThreads) {
                    client.out.println("MESSAGE " + name + ": " + input);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
