/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.json.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lib.Debug;
import persistence.json.PersistentJsonInterface;

/**
 *
 * @author tybrown
 */
public abstract class PersistentJsonNetworkInterface extends PersistentJsonInterface implements AutoCloseable {
    private Socket sock;

    private String serverName;
    private int serverPort;

    private BufferedReader in = null;
    private PrintWriter out = null;

    private ReaderThread readerThread;

    protected BlockingQueue<String> inputMessages;

    @Override
    public void initialize() throws IOException {
        connect();
        readerThread = new ReaderThread();
        readerThread.start();
    }

    /**
     * Constructs a persistent json network interface
     * @param hostname The hostname of the server to connect to
     * @param port The port of the server to connect to
     */
    public PersistentJsonNetworkInterface(String hostname, int port) {
        super();
        this.serverName = hostname;
        this.serverPort = port;
        this.inputMessages = new LinkedBlockingQueue<>();
    }

    /**
     * Connects to the server. Should be called from the initialize() method. 
     * @throws IOException If the connection to the server failed, no host listening on that port, no internet connection, etc
     */
    protected void connect() throws IOException {
        this.sock = new Socket(serverName, serverPort);
    }

    /**
     * Disconnects from the server.
     * @throws IOException If there was a problem during disconnection
     */
    public void disconnect() throws IOException {
        if (sock != null && !sock.isClosed()) {
            sock.close();
        }
    }

    /**
     * Receives a single line (terminated by a \n or \r\n) from the server. Blocks until line is received
     * @return The line received
     * @throws IOException If there was a problem with the socket. Connection must be re-established
     */
    public String receiveLine() throws IOException {
        Debug.debug("Receiving line...");
        if (!sock.isConnected()) {
            throw (new IOException("Socket Not connected!"));
        }
        if (in == null) {
            this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        }
        String line = this.in.readLine();
        if (line == null) {
            Debug.debug("Client closed the connection!");
            sock.close();
        }
        return (line);
    }

    /**
     * Receives a message from the server. A message is defined as any continuous block of characters which is followed by a blank line (ending in \n\n or \r\n\r\n). Blocks until a full message is received
     * @return The String of the message received
     * @throws IOException If there was a problem with the socket. Connection must be re-established
     */
    public String receiveMessage() throws IOException {
        String line, text = "";
        int lines = 0;
        while ((line = receiveLine()) != null) {
            lines++;
            Debug.debug("Line[%d]: \"%s\"", lines, line);
            if (lines > 1 && line.length() == 0) {
                break;
            }
            text += line + "\n";
        }
        return (lines != 0 ? text : null);
    }
    
    /**
     * Sends the given text to the server
     * @param text The text to send
     * @throws IOException If there was a problem with the socket. Connection must be re-established
     */
    public void sendText(String text) throws IOException {
        if (text == null) {
            return;
        }
        if (sock.isClosed()) {
            throw (new IOException("Socket Not connected!"));
        }
        Debug.debug("Sending message:\n%s", text);
        if (out == null) {
            out = new PrintWriter(sock.getOutputStream(), false); //false = need to flush manually
        }
        synchronized (sock) {
            out.printf("%s", text);
            out.flush();
        }
    }

    /**
     * Gets the count of the messages which are waiting for processing. 0 means no messages, and that a subsequent call to getNextMessage() will (most likely) block
     * @return 
     */
    public int bufferedMessagesCount() {
        return (inputMessages.size());
    }

    /**
     * Gets the next (oldest) message on the queue. Blocks if no message is available
     * @return 
     */
    public String getNextMessage() throws InterruptedException {
        return (inputMessages.take());
    }

    /**
     * Override of autocloseable interface (lets the try-with-resources automatically close the socket when done)
     * @throws IOException if there was some problem during socket closing
     */
    @Override
    public void close() throws IOException {
        disconnect();
    }

    /**
     * Class that handles asynchronous network reading
     */
    private class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!sock.isClosed()) {
                try {
                    String in = receiveMessage();
                    inputMessages.put(in);
                } catch (IOException | InterruptedException e) {
                    Debug.debug("Exception: %s", e.toString());
                }
            }
        }
    }
}
