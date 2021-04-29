import java.io.*;
import java.net.Socket;

public class Client extends Thread  {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8187;

    public static void main(String[] args) {
        try(Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream()))
        {
            while (!socket.isOutputShutdown()) {
                new Thread(() -> {
                    try {
                        String entryMsg = in.readUTF();
                        System.out.println("Сервер прислал сообщение: " + entryMsg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                if (reader.ready()) {
                    String clientCommand = reader.readLine();
                    out.writeUTF(clientCommand);
                    out.flush();

                    if(clientCommand.equalsIgnoreCase("end")) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
