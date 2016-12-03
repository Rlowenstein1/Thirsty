package persistence.json.net;

import com.google.gson.annotations.Expose;
import model.Credential;

public class Command {
    
    public enum CommandType {
        SAVE_USER, LOAD_USER, DELETE_USER,
        SAVE_WATER_REPORT, LOAD_WATER_REPORT, DELETE_WATER_REPORT,
        SAVE_QUALITY_REPORT, LOAD_QUALITY_REPORT, DELETE_QUALITY_REPORT,
        AUTHENTICATE, DEAUTHENTICATE, UNKNOWN
    }

    @Expose
    private CommandType command;
    @Expose
    private boolean response;
    @Expose
    private String data;
    @Expose
    private Credential credential;

    public Command(CommandType type, boolean response, String data, Credential c) {
        this.command = type;
        this.response = response;
        this.data = data;
        this.credential = c;
    }

    public Command() {
        this(CommandType.UNKNOWN, false, null, null);
    }

    public void setCommandType(CommandType command) {
        this.command = command;
    }

    public CommandType getCommand() {
        return command;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public Credential getCredential() {
        return credential;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return (
                String.format(""
                        + "Command: [\n"
                        + "  type: %s\n"
                        + "  response: %b\n"
                        + "  data: %s\n"
                        + "  credential: %s\n"
                        + "]",
                        command,
                        response,
                        data,
                        credential
                )
        );
    }
}
