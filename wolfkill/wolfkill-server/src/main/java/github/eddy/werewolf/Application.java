package github.eddy.werewolf;

import github.eddy.werewolf.beans.Player;
import github.eddy.werewolf.beans.Room;
import github.eddy.werewolf.common.SkillEnum;

import github.eddy.werewolf.common.RoleEnum;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Application {

    public static void main(String[] a) throws IOException {
        Application app = new Application();

        Room room = new Room(0L);
        System.out.println("游戏开始");
        Integer integer = Integer.parseInt(app.getInput());
        room.initPlayer(integer);

        Integer day = 1;
        while (!room.getStop()) {
            System.out.println("天黑" + day);
            List<Player> p_wolf_list = room.getPlayersByRole(RoleEnum.wolf);
            System.out.println("狼人请杀人:" + room.getPlayerIdsByRole(RoleEnum.wolf).toString());
            Integer killNo = Integer.parseInt(app.getInput());

            Player p_prophet = room.getPlayersByRole(RoleEnum.prophet).get(0);
            System.out.println("预言家查验:" + p_prophet.getId());
            Integer checkNo = Integer.parseInt(app.getInput());
            if (p_prophet.getAlive()) {
                System.out.println("结果:" + checkNo + "是" + room.getPlayerById(checkNo).getRole().getGroup());
            } else {
                System.out.println("已经死亡,无法获取消息");
            }

            Player p_witch = room.getPlayersByRole(RoleEnum.witch).get(0);
            System.out.println("女巫是否使用解药/毒药:" + p_witch.getId());
            Integer killORHeal = Integer.parseInt(app.getInput());
            Integer chooseNo = -1;
            if (p_witch.getAlive()) {
                if (killORHeal == 1) {
                    Boolean[] skills = p_witch.getSkillStatus();
                    if (p_witch.canPosionORMedicine(SkillEnum.medicine)) {
                        System.out.println("你救了:" + killNo);
                        p_witch.setSkillStatus(new Boolean[]{false, skills[1]});
                    } else {
                        killORHeal = 0;
                        System.out.println("解药已经使用过了");
                    }
                } else if (killORHeal == -1) {
                    if (p_witch.canPosionORMedicine(SkillEnum.posion)) {
                        System.out.println("选择要毒的人");
                        chooseNo = Integer.parseInt(app.getInput());
                    } else {
                        killORHeal = 0;
                        System.out.println("毒药已经使用过了");
                    }
                }
            } else {
                System.out.println("已经死亡,无法获取消息");
            }

            System.out.println("天亮" + day);
            if (killORHeal == -1) {
                room.getPlayerById(chooseNo).killedBySkill(room.getPlayersByRole(RoleEnum.witch).get(0), SkillEnum.posion);
            } else if (killORHeal == 0) {
                if (room.getPlayerById(killNo).killedByKill()) {
                    System.out.println("猎人:" + killNo);
                    Integer gunNo = Integer.parseInt(app.getInput());
                    room.getPlayerById(gunNo).killedBySkill(room.getPlayerById(killNo), SkillEnum.gun);
                }
            }

            room.checkWin();
            if (room.getStop()) {
                System.out.println(room.getGameResult());
                break;
            }

            System.out.println("投票");
            Integer voteNo = Integer.parseInt(app.getInput());
            if (room.getPlayerById(voteNo).killedByVote()) {
                System.out.println("猎人:" + voteNo);
                Integer gunNo = Integer.parseInt(app.getInput());
                room.getPlayerById(gunNo).killedBySkill(room.getPlayerById(voteNo), SkillEnum.gun);
            }

            room.checkWin();
            if (room.getStop()) {
                System.out.println(room.getGameResult());
                break;
            }
            day++;
        }
        System.out.println("游戏结束");
    }


    public String getInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
}
