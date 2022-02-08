import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private final static String HOSTNAME = "127.0.0.1";
    private final static int PORT = 8080;
    private final static int NB_POOL_THREADS = Runtime.getRuntime().availableProcessors();

    private final String hostname;
    private final int port;
    private final int nbPoolThreads;

    public Server(final String hostname, final int port, final int nbPoolThreads) {
        this.hostname = hostname;
        this.port = port;
        this.nbPoolThreads = nbPoolThreads;
    }

    public static void main(String[] args) {

        for (String arg : args) {
            if (arg.contains("-h") || arg.contains("--help")) {
                System.out.println("-h=<hostname>\n-p=<port>\n-n=<nbPoolThreads>");
                return;
            }
        }

        final String hostname = parseHostname(args);
        final int port = parsePort(args);
        final int nbPoolThreads = parseNbPoolThreads(args);
        final Server server = new Server(hostname, port, nbPoolThreads);

        Logger.getGlobal().log(Level.INFO,
                String.format(
                        "Server starts with the following parameters: hostname = %s, port = %s, nbPoolThreads = %s",
                        hostname, port, nbPoolThreads));

        server.run();
        Logger.getGlobal().log(Level.INFO, "Server interrupted. Shutting down.");
    }

    private static String parseHostname(String[] args) {
        return HOSTNAME;
    }

    private static int parsePort(String[] args) {
        return PORT;
    }

    private static int parseNbPoolThreads(String[] args) {
        return NB_POOL_THREADS;
    }

    private void run() {
        System.out.println("lol");
    }
}