package persistence.json.net;

import model.Credential;

public class Command {
    
    public enum CommandType {
        SAVE_USER, LOAD_USER, DELETE_USER,
        SAVE_WATER_REPORT, LOAD_WATER_REPORT, DELETE_WATER_REPORT,
        SAVE_QUALITY_REPORT, LOAD_QUALITY_REPORT, DELETE_QUALITY_REPORT,
        AUTHENTICATE, DEAUTHENTICATE
    }

    private CommandType command;
    private String data;
    private Credential credential;

    public void setCommand(CommandType command) {
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
}
