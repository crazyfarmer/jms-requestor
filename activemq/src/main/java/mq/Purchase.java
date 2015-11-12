package mq;

import command.Command;
import lombok.Getter;

/**
 * 设置命令
 *
 * @author liusya@yonyou.com
 * @date 11/9/15.
 */
public class Purchase extends Command {

    @Getter private int money;

    public Purchase(int money) {
        super("purchase");
        this.money = money;
    }
}
