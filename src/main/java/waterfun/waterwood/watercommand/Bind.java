package waterfun.waterwood.watercommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bind {
    private String name;
    private Keys key;
    private String permission;
    private boolean cancelAction;
    private String commandMessage;
    public String getCommandMessage() {
        return commandMessage;
    }

    public void setCommandMessage(String commandMessage) {
        this.commandMessage = commandMessage;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public String getCommand() {
        return command;
    }

    private String command;
    private CommandType commandType;

    public void setCommand(String command) {
        this.command = command;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void Binds(String name, Keys key, String permission, String command, CommandType commandType, boolean cancelAction,String commandMessage) {
        this.name = name;
        this.key = key;
        this.permission = permission;
        this.cancelAction = cancelAction;
        this.command = command;
        this.commandType = commandType;
        this.commandMessage = commandMessage;
    }

    public Bind(String name, String key, String permission, String command, boolean cancelAction,String commandMessage) throws IllegalArgumentException{
        CommandType commandType = CommandType.player;
        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(command);
        String commandText;
        if(matcher.find()){
            commandType = CommandType.valueOf(matcher.group(1));
        }
        commandText = command.substring(command.indexOf("]")+1,command.length());
        if(permission.equals("none")){ permission = "";}
         this.Binds(name,Keys.valueOf(key),permission,commandText,commandType,cancelAction,commandMessage);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Keys getKey() {
        return key;
    }

    public void setKey(Keys key) {
        this.key = key;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isCancelAction() {
        return cancelAction;
    }

    public void setCancelAction(boolean cancelAction) {
        this.cancelAction = cancelAction;
    }
    public enum Keys {
        SHIFT_F,SHIFT_Q
    }

    public enum CommandType{
        player,console
    }
}
