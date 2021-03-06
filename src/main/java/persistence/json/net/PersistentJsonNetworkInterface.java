/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.json.net;

import com.google.gson.JsonSyntaxException;
import controller.MasterSingleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.application.Platform;
import lib.Debug;
import model.Credential;
import model.QualityReport;
import model.User;
import model.WaterReport;
import persistence.json.PersistentJsonInterface;
import persistence.json.net.Command.CommandType;

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
    private CommandThread commandThread;

    protected BlockingQueue<String> inputMessages;
    protected BlockingQueue<Command> inputCommands;

    @Override
    public void initialize() throws IOException {
        connect();
        readerThread = new ReaderThread();
        readerThread.start();
        commandThread = new CommandThread();
        commandThread.start();
    }

    /**
     * Constructs a persistent json network interface
     * @param hostname The hostname of the server to connect to
     * @param port The port of the server to connect to
     */
    public PersistentJsonNetworkInterface(String hostname, int port) {
        this.serverName = hostname;
        this.serverPort = port;
        this.inputMessages = new LinkedBlockingQueue<>();
        this.inputCommands = new LinkedBlockingQueue<>();
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
        if (readerThread != null) {
            readerThread.interrupt();
        }
        if (commandThread != null) {
            commandThread.interrupt();
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
        String line;
        String text = "";
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
    public void sendRaw(String text) throws IOException {
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


    public void sendMessage(String text) throws IOException {
        sendRaw(text + "\n\n");
    }

    /**
     * Gets the count of the messages which are waiting for processing. 0 means no messages, and that a subsequent call to getNextMessage() will (most likely) block
     * @return 
     */
    public int bufferedMessagesCount() {
        return (inputMessages.size());
    }

    /**
     * Gets the next (oldest) response command on the queue. Blocks if no command is available
     * @return 
     */
    public Command getNextCommand() throws InterruptedException {
        return (inputCommands.take());
    }

    /**
     * Gets the next (oldest) message on the queue. Blocks if no message is available
     * @return 
     */
    private String getNextMessage() throws InterruptedException {
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

    public Command sendCommandAndAwaitResponse(CommandType type, String data, Credential cred) throws IOException {
        Command command = new Command(type, data, cred);
        Debug.debug("Sending command:\n%s", command);
        sendMessage(toJson(command));
        Command next = null;
        try {
            while (next == null || (!next.isResponse() && next.getCommand() != type)) {
                next = getNextCommand();
                Debug.debug("Got a command: %s", next.toString());
            }
            Debug.debug("Command was a response to our query for %s", type);
        } catch (InterruptedException e) {
            Debug.debug("getting response was interrupted!");
            next = null;
        }
        return (next);
    }

    public abstract void addUser(User u);

    public abstract void addWaterReport(WaterReport wr);

    public abstract void addQualityReport(QualityReport qr);

    /**
     * Class that handles asynchronous network reading
     */
    private class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!sock.isClosed()) {
                try {
                    String in = receiveMessage();
                    if (in == null) {
                        Debug.debug("Server closed the connection!");
                        close();
                        continue;
                    }
                    inputMessages.put(in);
                    Debug.debug("Put into inputMessages: %s", in);
                } catch (IOException | InterruptedException e) {
                    Debug.debug("Exception: %s", e.toString());
                }
            }
        }
    }

    /**
     */
    private class CommandThread extends Thread {
        @Override
        public void run() {
            while (!sock.isClosed()) {
                try {
                    Debug.debug("Taking from inputMessages...");
                    String mess = inputMessages.take();
                    Debug.debug("Got a string from inputMessages: %s", mess);
                    try {
                        Command commandIn = fromJson(mess, Command.class);
                        Debug.debug("Got a command:\n%s", commandIn);
                        if (commandIn.getCommand() == Command.CommandType.UNKNOWN) {
                            Debug.debug("Unknown command type!");
                            continue;
                        }
                        if (commandIn.isResponse()) {
                            inputCommands.put(commandIn);
                        } else {
                            //this is a new message (push notification), like loading a new user/report. handle adding it to the model by calling the overrideable functions
                            switch (commandIn.getCommand()) {
                                case LOAD_USER:
                                    try {
                                        User u = fromJson(commandIn.getData(), User.class);
                                        addUser(u.cloneIt());
                                    } catch (JsonSyntaxException e) {
                                        Debug.debug("Failed to cast incoming data to user: %s", e.toString());
                                    }
                                    break;
                                case LOAD_WATER_REPORT:
                                    try {
                                        WaterReport wr = fromJson(commandIn.getData(), WaterReport.class);
                                        addWaterReport(wr.cloneIt());
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                MasterSingleton.updateReportScreen(); //can't do this from a non-FX thread
                                            }
                                        });      
                                    } catch (JsonSyntaxException e) {
                                        Debug.debug("Failed to cast incoming data to water report: %s", e.toString());
                                    }
                                    break;
                                case LOAD_QUALITY_REPORT:
                                    try {
                                        QualityReport qr = fromJson(commandIn.getData(), QualityReport.class);
                                        addQualityReport(qr.cloneIt());
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                MasterSingleton.updateReportScreen(); //can't do this from a non-FX thread
                                            }
                                        });
                                    } catch (JsonSyntaxException e) {
                                        Debug.debug("Failed to cast incoming data to quality report: %s", e.toString());
                                    }
                                    break;
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        Debug.debug("Failed to cast incoming message to command: %s", e.toString());
                    }
                } catch (InterruptedException e) {
                    Debug.debug("Interrupted: %s", e.toString());
                }
            }
        }
    }
}
