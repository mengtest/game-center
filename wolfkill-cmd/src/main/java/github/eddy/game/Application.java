package github.eddy.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Application {

    public static void main(String[] a) throws IOException {
        Application app = new Application();
        System.out.println("游戏开始");
        Integer integer = Integer.parseInt(app.getInput());

        List<String> playerRoles = app.assignRoles(integer);
        System.out.println("分配身份:" + playerRoles.toString());

        System.out.println("天黑1");
        System.out.println("狼人");
        Integer killNo = Integer.parseInt(app.getInput());
        System.out.println(":" + killNo);

        System.out.println("预言家");
        Integer checkNo = Integer.parseInt(app.getInput());
        System.out.println(":" + checkNo);
        System.out.println(":" + playerRoles.get(checkNo));

        /**
         * TODO 按照主流程 完成单机版
         * TODO 使用消息分发的方式 ,发送消息给需要听的角色
         */
    }

    public String getInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public void addMany(List list, Object object, Integer num) {
        for (int i = 0; i < num; i++) {
            list.add(object);
        }
    }

    public List<String> assignRoles(int playerNum) {
        List list = new ArrayList();
        switch (playerNum) {
            case 9:
                addMany(list, "村民", 3);
                addMany(list, "女巫", 1);
                addMany(list, "预言家", 1);
                addMany(list, "猎人", 1);
                addMany(list, "狼", 3);
                break;
            case 12:
                addMany(list, "村民", 4);
                addMany(list, "女巫", 1);
                addMany(list, "预言家", 1);
                addMany(list, "猎人", 1);
                addMany(list, "白痴", 1);
                addMany(list, "狼", 4);
                break;
            default:
                throw new RuntimeException("不支持");
        }
        Collections.shuffle(list);
        return list;
    }
}
