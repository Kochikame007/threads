package org.example.throughput;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HttpHandler {

    private static final String SOURCE_FILE = "./src/main/resources/war_and_peace.txt";
    private static final int NUMBER_OF_THREADS = 1;

    public static void main(String[] args) throws IOException {

        String text = new String(Files.readAllBytes(Paths.get(SOURCE_FILE)));

        System.out.println("text outpuot " + text);
        startServer(text);
    }

    public static void startServer(String text) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 1);
        server.createContext("/search" ,new WordHandler(text));
        Executor exe = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        server.setExecutor(exe);
        server.start();
    }

    public static class WordHandler implements com.sun.net.httpserver.HttpHandler {
        private String text;

        public WordHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("incoming request: " + exchange);
            String query = exchange.getRequestURI().getQuery();
            String[] keyValue = query.split("=");
            String action = keyValue[0];
            String word = keyValue[1];

            if (!action.equals("word")){
                exchange.sendResponseHeaders(400 , 0);
                return;
            }

            long count = countWord(word);
            byte[] response = Long.toString(count).getBytes();
            exchange.sendResponseHeaders(200, response.length);
            OutputStream out = exchange.getResponseBody();
            out.write(response);
            out.close();
        }

        public long countWord(String word){
            long count=0;
            long index=0;
            while(index>=0){
                index = text.indexOf(word , (int)index);
                if(index>=0){
                    count++;
                    index++;
                }
            }
            return count;
        }
    }

}
