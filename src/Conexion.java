import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Conexion extends Socket {


    private static final int PORT = 6667;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started on port: " + PORT);

        while (true) {
            Socket socket = serverSocket.accept();
            ConexionServer server = new ConexionServer(socket);
            new Thread(server).start();
        }
    }

    public static void isOkay(){

    }
}
