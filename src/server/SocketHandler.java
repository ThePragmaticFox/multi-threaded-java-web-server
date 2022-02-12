import java.net.Socket;

public class SocketHandler implements Runnable {

    private final Socket clientSocket;

    public SocketHandler(final Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        
        
    }
    
}
