import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.net.InetAddresses;

public class Server {

    private final static String ROOT = "/www";
    private final static InetAddress LOCAL_IP = InetAddresses.forString("127.0.0.1");
    private final static int PORT = 8080;
    private final static int QUEUE_SIZE = 100;
    private final static int NB_POOL_THREADS = Math.max(Runtime.getRuntime().availableProcessors(), 1);

    private final String root;
    private final InetAddress localIp;
    private final int port;
    private final int queueSize;
    private final int nbPoolThreads;

    private final SocketHandler[] socketHandlers;

    public Server(final String root,
            final InetAddress localIp,
            final int port,
            final int queueSize,
            final int nbPoolThreads) {
        this.root = root;
        this.localIp = localIp;
        this.port = port;
        this.queueSize = queueSize;
        this.nbPoolThreads = nbPoolThreads;
        this.socketHandlers = new SocketHandler[nbPoolThreads];
    }

    public static void main(String[] args) {

        for (String arg : args) {
            if (arg.contains("-h") || arg.contains("--help")) {
                System.out.println("-r=<root>\n-l=<localIp>\n-p=<port>\n-q=<queueSize>\n-n=<nbPoolThreads>");
                return;
            }
        }

        final String root = parseRoot(args);
        final InetAddress localIp = parseLocalIp(args);
        final int queueSize = parseQueueSize(args);
        final int port = parsePort(args);
        final int nbPoolThreads = parseNbPoolThreads(args);

        final Server server = new Server(root, localIp, port, queueSize, nbPoolThreads);

        Logger.getGlobal().log(Level.INFO,
                String.format(
                        "Server starts with the following parameters: root = %s, localIp = %s, port = %s, queueSize = %s, nbPoolThreads = %s",
                        root, localIp, port, queueSize, nbPoolThreads));

        server.run();

        Logger.getGlobal().log(Level.INFO, "Server interrupted. Shutting down.");
    }

    private static Optional<String> parseArgs(final String[] args, final String pattern) {
        return Arrays.stream(args)
                .filter(arg -> arg.startsWith(pattern))
                .map(arg -> arg.replace(pattern, ""))
                .findFirst();
    }

    private static String parseRoot(String[] args) {
        return parseArgs(args, "-r=|--root=").orElse(ROOT);
    }

    private static InetAddress parseLocalIp(String[] args) {
        return parseArgs(args, "-l=|--localIp=").map(InetAddresses::forString).orElse(LOCAL_IP);
    }

    private static int parsePort(String[] args) {
        return parseArgs(args, "-p=|--port=").map(Integer::valueOf).orElse(PORT);
    }

    private static int parseQueueSize(String[] args) {
        return parseArgs(args, "-q=|--queueSize=").map(Integer::valueOf).orElse(QUEUE_SIZE);
    }

    private static int parseNbPoolThreads(String[] args) {
        return parseArgs(args, "-n=|--nbPoolThreads=").map(Integer::valueOf).orElse(NB_POOL_THREADS);
    }

    private void run() {
        try (final ServerSocket serverSocket = new ServerSocket(this.port, this.queueSize, this.localIp)) {
            while (true) {  
                final Socket clientSocket = serverSocket.accept();
                clientSocket.
            }
        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE,
                    String.format("Unable to listen to port %s\n%s", this.port, e.getMessage()));
        }
    }
}
