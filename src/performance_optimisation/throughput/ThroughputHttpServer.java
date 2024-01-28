package performance_optimisation.throughput;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThroughputHttpServer {
    private static final String INPUT_FILE = "./resources/war_and_peace.txt";
    private static final int NUMBER_OF_THREAD = 1;

    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
        startServer(text);
    }

    public static void startServer(String text) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/search", new WordCountHandler(text));
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);
        server.setExecutor(executor);
        server.start();
    }

    private record WordCountHandler(String text) implements HttpHandler {
        @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                String[] keyValue = query.split("=");
                String action = keyValue[0];
                String word = keyValue[1];
                if (!action.equals("word")) {
                    exchange.sendResponseHeaders(400, 0);
                    return;
                }

                long count = countWord(word);

                byte[] response = Long.toString(count).getBytes();
                exchange.sendResponseHeaders(200, response.length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(response);
                outputStream.close();
            }

            private long countWord(String word) {
                long count = 0;
                int index = 0;
                while (index >= 0 && index < text.length()) {
                    index = text.indexOf(word, index);
                    if (index >= 0) {
                        if (index > 0 && text.charAt(index - 1) != ' ') {
                            index++;
                            continue;
                        } else if (index + word.length() < text.length() - 1 && text.charAt(index + word.length()) != ' ') {
                            index++;
                            continue;
                        }
                        count++;
                        index += word.length();
                    }
                }
                return count;
            }
        }
}
