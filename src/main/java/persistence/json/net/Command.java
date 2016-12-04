package persistence.json.net;

import com.google.gson.annotations.Expose;
import model.Credential;

public class Command {
    
    public enum CommandType {
        SAVE_USER, LOAD_USER, DELETE_USER,
        SAVE_CREDENTIAL,
        SAVE_WATER_REPORT, LOAD_WATER_REPORT, DELETE_WATER_REPORT,
        SAVE_QUALITY_REPORT, LOAD_QUALITY_REPORT, DELETE_QUALITY_REPORT,
        AUTHENTICATE, DEAUTHENTICATE, UNKNOWN
    }

    @Expose
    private CommandType command;
    @Expose
    private String data;
    @Expose
    private Credential credential;


    @Expose
    private boolean response;
    @Expose
    private boolean success;
    @Expose
    private String message;

    public Command(CommandType type, String data, Credential c, boolean response, boolean success, String message) {
        this.command = type;
        this.response = response;
        this.data = data;
        this.credential = c;
        this.success = success;
        this.message = message;
    }

    public Command(CommandType type, String data, Credential c) {
        this(type, data, c, false, false, null);
    }

    public Command() {
        this(CommandType.UNKNOWN, null, null);
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

    public boolean isSuccessful() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return (
                String.format(""
                        + "Command: [\n"
                        + "  type: %s\n"
                        + "  data: %s\n"
                        + "  credential: %s\n"
                        + "  response: %b\n"
                        + "  success: %b\n"
                        + "  message: %s\n"
                        + "]",
                        command,
                        data,
                        credential,
                        response,
                        success,
                        message
                )
        );
    }
}
