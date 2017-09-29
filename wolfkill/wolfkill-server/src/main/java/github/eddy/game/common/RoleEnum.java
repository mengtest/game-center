package github.eddy.game.common;

import lombok.Getter;

@Getter
public enum RoleEnum {
    citizen("村民", "好人"),
    witch("女巫", "好人"),
    prophet("预言家", "好人"),
    hunter("猎人", "好人"),
    moron("白痴", "好人"),
    wolf("狼", "坏人");

    String description;
    String group;

    RoleEnum(String des, String group) {
        this.description = des;
        this.group = group;
    }
}
