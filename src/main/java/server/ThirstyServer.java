/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import controller.MasterSingleton;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lib.Debug;
import model.Credential;
import model.QualityReport;
import model.ReportManager;
import model.User;
import model.WaterReport;
import persistence.PersistenceInterface;
import persistence.json.PersistentJsonFile;
import persistence.json.PersistentJsonInterface;
import persistence.json.net.Command;

/**
 *
 * @author tybrown
 */
public class ThirstyServer {

    public static final int PORT = 9988;
    public static final String DB_PATH = PersistentJsonFile.DEFAULT_PATH;

    private ServerSocket serverSocket;

    private BlockingQueue<WorkerCommand> inputCommands;
    private ConnectionHandler handler;

    public static void main(String[] args) {
        new ThirstyServer().doMain(args);
    }
        
    public void doMain(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            Debug.log("Listening for connections on TCP port %d...", serverSocket.getLocalPort());
        } catch (IOException e) {
            Debug.fatal("Failed to create new TCP ServerSocket! Reason: \"%s\"", e.toString());
            System.exit(1);
        }

        handler = new ConnectionHandler(serverSocket, new PersistentJsonFile(DB_PATH));
        handler.start();

    }

    private class ConnectionHandler extends Thread {
        private final ServerSocket serverSocket;
        private CommandThread commandThread;
        private Set<Worker> workers;
        private int workerCount = 0;
        private PersistentJsonInterface persist;

        public ConnectionHandler(ServerSocket sock, PersistentJsonInterface persist) {
            this.serverSocket = sock;
            this.persist = persist;
            this.workers = Collections.synchronizedSet(new HashSet<>());
            inputCommands = new LinkedBlockingQueue<>();
            this.commandThread = new CommandThread(persist, workers);
            this.commandThread.start();
            MasterSingleton.initialize(persist);
        }

        @Override
        public void run() {
            try {
                while (!serverSocket.isClosed()) {
                    try {
                        Debug.debug("Spawning new worker...");
                        Worker w = new Worker(serverSocket.accept(), ++workerCount, persist);
                        workers.add(w);
                        w.start();
                    } catch (IOException e) {
                        Debug.error("Failed to accept new connection on socket! Reason: \"%s\"", e.toString());
                    }
                }
            } finally {
                try {
                    if (!serverSocket.isClosed()) {
                        serverSocket.close();
                    }
                    for (Worker w : workers) {
                        w.close();
                    }
                    commandThread.interrupt();
                } catch (IOException e) {
                    Debug.error("Failed to close socket! Reason: \"%s\"", e.toString());
                }
            }
        }

        public void workerStopped(Worker w) {
            workers.remove(w);
        }

    }

    private class WorkerCommand {
        private Worker worker;
        private Command command;

        public WorkerCommand(Worker worker, Command command) {
            this.worker = worker;
            this.command = command;
        }

        public Worker getWorker() {
            return worker;
        }

        public Command getCommand() {
            return command;
        }
    }

    private class CommandThread extends Thread {
        private PersistentJsonInterface persist;
        private Set<Worker> workers;

        public CommandThread(PersistentJsonInterface persist, Set<Worker> workers) {
            this.persist = persist;
            this.workers = workers;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Debug.debug("Taking from inputCommands...");
                    WorkerCommand commandW = inputCommands.take();
                    Command commandIn = commandW.getCommand();
                    Debug.debug("Got a command from inputCommands: %s", commandIn);
                    //got a message from a client. push the message out to all other clients
                    switch (commandIn.getCommand()) {
                        case SAVE_USER:
                            User newUser = persist.fromJson(commandIn.getData(), User.class);
                            Debug.debug("User wants to save a user: %s", newUser);
                            if (newUser != null) {
                                newUser = persist.saveUser(newUser);
                                Worker w = commandW.getWorker();
                                String data = null;
                                if (newUser != null) {
                                    data = persist.toJson(newUser);
                                    w.sendCommand(new Command(Command.CommandType.SAVE_USER, data, w.getCredential(), true, true, null));
                                    Debug.debug("User saved! Now let's let all other clients connected know about this user...");
                                    List<Worker> removes = new ArrayList<>();
                                    for (Worker ww : workers) {
                                        try {
                                            ww.sendCommand(new Command(Command.CommandType.LOAD_USER, data, ww.getCredential()));
                                        } catch (IOException e) {
                                            Debug.debug("Client had an IOEXception: %s", e.toString());
                                            ww.close();
                                            removes.add(ww);
                                        }
                                    }
                                    for (Worker ww : removes) {
                                        workers.remove(ww);
                                    }
                                } else {
                                    w.sendCommand(new Command(Command.CommandType.SAVE_USER, data, w.getCredential(), true, false, "Failed to save user!"));
                                }
                            }
                            break;
                        case SAVE_WATER_REPORT:
                            WaterReport newReport = persist.fromJson(commandIn.getData(), WaterReport.class);
                            Debug.debug("User wants to save a water report: %s", newReport);
                            if (newReport != null) {
                                newReport = persist.saveWaterReport(newReport);
                                Worker w = commandW.getWorker();
                                String data = null;
                                if (newReport != null) {
                                    data = persist.toJson(newReport);
                                    w.sendCommand(new Command(Command.CommandType.SAVE_WATER_REPORT, data, w.getCredential(), true, true, null));
                                    Debug.debug("Report saved! Now let's let all other clients connected know about this user...");
                                    List<Worker> removes = new ArrayList<>();
                                    for (Worker ww : workers) {
                                        try {
                                            ww.sendCommand(new Command(Command.CommandType.LOAD_WATER_REPORT, data, ww.getCredential()));
                                        } catch (IOException e) {
                                            Debug.debug("Client had an IOEXception: %s", e.toString());
                                            ww.close();
                                            removes.add(ww);
                                        }
                                    }
                                    for (Worker ww : removes) {
                                        workers.remove(ww);
                                    }
                                } else {
                                    w.sendCommand(new Command(Command.CommandType.SAVE_WATER_REPORT, data, w.getCredential(), true, false, "Failed to save water report!"));
                                }
                            }
                            break;
                        case SAVE_QUALITY_REPORT:
                            QualityReport newQualityReport = persist.fromJson(commandIn.getData(), QualityReport.class);
                            Debug.debug("User wants to save a quality report: %s", newQualityReport);
                            if (newQualityReport != null) {
                                newQualityReport = persist.saveQualityReport(newQualityReport);
                                Worker w = commandW.getWorker();
                                String data = null;
                                if (newQualityReport != null) {
                                    data = persist.toJson(newQualityReport);
                                    w.sendCommand(new Command(Command.CommandType.SAVE_QUALITY_REPORT, data, w.getCredential(), true, true, null));
                                    Debug.debug("Report saved! Now let's let all other clients connected know about this user...");
                                    List<Worker> removes = new ArrayList<>();
                                    for (Worker ww : workers) {
                                        try {
                                            ww.sendCommand(new Command(Command.CommandType.LOAD_QUALITY_REPORT, data, ww.getCredential()));
                                        } catch (IOException e) {
                                            Debug.debug("Client had an IOEXception: %s", e.toString());
                                            ww.close();
                                            removes.add(ww);
                                        }
                                    }
                                    for (Worker ww : removes) {
                                        workers.remove(ww);
                                    }
                                } else {
                                    w.sendCommand(new Command(Command.CommandType.SAVE_QUALITY_REPORT, data, w.getCredential(), true, false, "Failed to save water report!"));
                                }
                            }
                            break;
                        case SAVE_CREDENTIAL:
                            Credential newCredential = persist.fromJson(commandIn.getData(), Credential.class);
                            Debug.debug("User wants to save a credential: %s", newCredential);
                            if (newCredential != null) {
                                persist.saveUserCredential(newCredential);
                                Debug.debug("Credential saved!");
                                Worker w = commandW.getWorker();
                                w.sendCommand(new Command(Command.CommandType.SAVE_CREDENTIAL, null, w.getCredential(), true, true, null));
                            }
                            break;
                        case DELETE_USER:
                            Debug.debug("User wants to delete a user: %s", persist.fromJson(commandIn.getData(), User.class));
                            break;
                        case DELETE_WATER_REPORT:
                            break;
                        case DELETE_QUALITY_REPORT:
                            break;
                    }
                } catch (IOException e) {
                    Debug.debug("IOException: %s", e.toString());
                } catch (InterruptedException e) {
                    Debug.debug("Interrupted: %s", e.toString());
                    break;
                }
            }
        }
    }

    private class Worker extends Thread implements AutoCloseable {
        private Socket sock;
        private BufferedReader in;
        private PrintWriter out;
        private int id;
        private boolean authenticated = false;
        private boolean creatingUser = false;
        private String creatingUserName = null;
        private PersistentJsonInterface persist;
        private Credential userCred;

        public Worker(Socket sock, int id, PersistentJsonInterface persist) {
            this.sock = sock;
            this.id = id;
            this.persist = persist;
        }
        
        @Override
        public void run() {
            Debug.debug("Client connected from %s", sock.getRemoteSocketAddress().toString());
            while (!sock.isClosed()) {
                try {
                    String mess = receiveMessage();
                    Debug.debug("client says: %s", mess);
                    if (mess == null) {
                        close();
                        continue;
                    }
                    Command command = persist.fromJson(mess, Command.class);
                    if (command.getCommand() == Command.CommandType.UNKNOWN) {
                        Debug.debug("Unknown command type!");
                        continue;
                    }
                    if (!authenticated) {
                        switch (command.getCommand()) {
                            case AUTHENTICATE:
                                if (creatingUser) {
                                    break;
                                }
                                Credential userCred = persist.fromJson(command.getData(), Credential.class);
                                Debug.debug("User wants to authenticate with credential: %s", userCred);
                                if (userCred != null) {
                                    //do authentication
                                    User authedUser = persist.authenticateUser(userCred);
                                    String data = null;
                                    String message = null;
                                    if (authedUser != null) {
                                        data = persist.toJson(authedUser);
                                        authenticated = true;
                                    } else {
                                        message = "Invalid username/password!";
                                        userCred = null;
                                    }
                                    this.userCred = userCred;
                                    sendCommand(new Command(Command.CommandType.AUTHENTICATE, data, getCredential(), true, authenticated, message));
                                    List<WaterReport> reports = ReportManager.getWaterReportList();
                                    if (authenticated) {
                                        for (WaterReport wr : reports) {
                                            /*
                                            WaterReport wrC = wr.cloneIt();
                                            for (QualityReport qrC : wrC.getQualityReportList()) {
                                                qrC.setParentReport(null);
                                            }
                                            */
                                            sendCommand(new Command(Command.CommandType.LOAD_WATER_REPORT, persist.toJson(wr), getCredential()));
                                        }
                                    }
                                }
                                break;
                            case SAVE_USER:
                                if (creatingUser) {
                                    break;
                                }
                                User newUser = persist.fromJson(command.getData(), User.class);
                                Debug.debug("User wants to create a new user: %s", newUser);
                                if (newUser != null) {
                                    String data = null;
                                    String message = null;
                                    String username = null;
                                    if (persist.userExists(newUser.getUsername())) {
                                        message = "User already exists!";
                                        Debug.debug("%s", message);
                                        sendCommand(new Command(Command.CommandType.SAVE_USER, data, null, true, creatingUser, message));
                                    } else {
                                        Debug.debug("Saving user...");
                                        newUser = persist.saveUser(newUser);
                                        if (newUser != null) {
                                            creatingUser = true;
                                            Debug.debug("notifying command thread of new user: %s", newUser);
                                            username = newUser.getUsername();
                                            data = persist.toJson(newUser);
                                            inputCommands.put(new WorkerCommand(this, new Command(Command.CommandType.SAVE_USER, data, getCredential())));
                                        } else {
                                            message = "Error while saving user!";
                                            Debug.debug("%s", message);
                                            sendCommand(new Command(Command.CommandType.SAVE_USER, data, null, true, creatingUser, message));
                                        }
                                    }
                                    creatingUserName = username;
                                }
                                break;
                            case SAVE_CREDENTIAL:
                                if (!creatingUser) {
                                    break;
                                }
                                Credential newCredential = persist.fromJson(command.getData(), Credential.class);
                                Debug.debug("User wants to create a new credential: %s", newCredential);
                                if (newCredential != null) {
                                    String data = null;
                                    String message = null;
                                    String username = null;
                                    if (creatingUserName.equals(newCredential.getUsername())) {
                                        Debug.debug("notifying command thread of new credential...");
                                        data = persist.toJson(newCredential);
                                        inputCommands.put(new WorkerCommand(this, new Command(Command.CommandType.SAVE_CREDENTIAL, data, null)));
                                    } else {
                                        Debug.debug("User did not match previous! (\"%s\" != \"%s\")", creatingUserName, newCredential.getUsername());
                                        message = "Username error!";
                                        sendCommand(new Command(Command.CommandType.SAVE_CREDENTIAL, data, null, true, false, message));
                                    }
                                    creatingUser = false;
                                    creatingUserName = null;
                                }
                                break;
                        }
                    } else {
                        if (command.getCommand() == Command.CommandType.DEAUTHENTICATE) {
                            authenticated = false;
                            sendCommand(new Command(Command.CommandType.DEAUTHENTICATE, null, null, true, true, null));
                        } else {
                            inputCommands.put(new WorkerCommand(this, command));
                        }
                    }
                } catch (JsonParseException e) {
                    Debug.debug("Failed to decode json: %s", e.toString());
                } catch (IOException e) {
                    Debug.debug("Worker encountered exception: %s", e.toString());
                    try {
                        close();
                    } catch (IOException ee) {
                    }
                } catch (InterruptedException e) {
                    Debug.debug("Worker encountered exception: %s", e.toString());
                }
            }
            Debug.debug("Client closed");
        }

        public Credential getCredential() {
            return (userCred);
        }

        /**
         * Disconnects from the server.
         * @throws IOException If there was a problem during disconnection
         */
        public void disconnect() throws IOException {
            if (sock != null && !sock.isClosed()) {
                sock.close();
            }
            this.interrupt();
        }

        /**
         * Receives a single line (terminated by a \n or \r\n) from the server. Blocks until line is received
         * @return The line received
         * @throws IOException If there was a problem with the socket. Connection must be re-established
         */
        public String receiveLine() throws IOException {
            if (!sock.isConnected() || sock.isClosed()) {
                throw (new IOException("Socket not connected!"));
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

        private void sendCommand(Command c) throws IOException {
            sendMessage(persist.toJson(c));
        }


        /**
         * Override of autocloseable interface (lets the try-with-resources automatically close the socket when done)
         * @throws IOException if there was some problem during socket closing
         */
        @Override
        public void close() throws IOException {
            disconnect();
        }
        
        public int getWorkerId() {
            return id;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 79 * hash + this.id;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Worker other = (Worker) obj;
            if (this.id != other.id) {
                return false;
            }
            return true;
        }
        
    }
}
