package command;

import java.io.Serializable;

/**
 *
 *
 * @author liusya@yonyou.com
 * @date 11/9/15.
 */
@lombok.Data public class Command implements Serializable{

    String commandName;

    String params;

    public Command(String name) {
        this.commandName = name;
    }
}
