import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Server {

    private final static String HOSTNAME = "127.0.0.1";
    private final static int PORT = 8080;
    private final static int NB_POOL_THREADS = Math.max(Runtime.getRuntime().availableProcessors(), 2);
    private final static String ROOT = "/www";

    private final String hostname;
    private final int port;
    private final int nbPoolThreads;
    private final String root;

    public Server(final String hostname, final int port, final int nbPoolThreads, final String root) {
        this.hostname = hostname;
        this.port = port;
        this.nbPoolThreads = nbPoolThreads;
        this.root = root;
    }

    public static void main(String[] args) {

        for (String arg : args) {
            if (arg.contains("-h") || arg.contains("--help")) {
                System.out.println("-hn=<hostname>\n-p=<port>\n-n=<nbPoolThreads>\n-r=<root>");
                return;
            }
        }

        final String hostname = parseHostname(args);
        final int port = parsePort(args);
        final int nbPoolThreads = parseNbPoolThreads(args);
        final String root = parseRoot(args);
        final Server server = new Server(hostname, port, nbPoolThreads, root);

        Logger.getGlobal().log(Level.INFO,
                String.format(
                        "Server starts with the following parameters: hostname = %s, port = %s, nbPoolThreads = %s, root = %s",
                        hostname, port, nbPoolThreads, root));

        server.run();
        Logger.getGlobal().log(Level.INFO, "Server interrupted. Shutting down.");
    }

    private static Stream<String> parseArgs(final String[] args, final String pattern) {
        return Arrays.stream(args).filter(arg -> arg.startsWith(pattern)).map(arg -> arg.replace(pattern, ""));
    }

    private static String parseHostname(String[] args) {
        return parseArgs(args, "-hn=|--hostname=").findFirst().orElse(HOSTNAME);
    }

    private static int parsePort(String[] args) {
        return parseArgs(args, "-p=|--port=").map(Integer::valueOf).findFirst().orElse(PORT);
    }

    private static int parseNbPoolThreads(String[] args) {
        return parseArgs(args, "-n=|--nbPoolThreads=").map(Integer::valueOf).findFirst().orElse(NB_POOL_THREADS);
    }

    private static String parseRoot(String[] args) {
        return parseArgs(args, "-r=|--root=").findFirst().orElse(ROOT);
    }

    private void run() {
        while(true) {
            break;
        }
        System.out.println("lol");
    }
}