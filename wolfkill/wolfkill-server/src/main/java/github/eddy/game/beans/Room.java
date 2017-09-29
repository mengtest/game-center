package github.eddy.game.beans;

import github.eddy.game.common.GameResultEnum;
import github.eddy.game.common.RoleEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static github.eddy.game.common.GameResultEnum.bad;
import static github.eddy.game.common.GameResultEnum.good;
import static github.eddy.game.common.RoleEnum.*;

@Slf4j
@Getter
public class Room {
    Long id;
    Boolean stop = false;
    GameResultEnum gameResult;

    private List<Player> players = new ArrayList<>();
    private Map<RoleEnum, List<Integer>> playerMap = new EnumMap<>(RoleEnum.class);//角色:玩家编号

    public Room(Long id) {
        this.id = id;
        log.debug("创建房间:{}", id);
    }

    //--------------------------------------------------------------------------------------------------
    public List<Integer> getPlayerIdsByRole(RoleEnum role) {
        return playerMap.get(role);
    }

    public List<Player> getPlayersByRole(RoleEnum role) {
        return getPlayerIdsByRole(role).stream().map(this::getPlayerById).collect(Collectors.toList());
    }

    public Player getPlayerById(Integer id) {
        return players.get(id - 1);//id = index+1
    }


    public void print() {
        players.stream().map(player -> player.getId() + ":" + player.getRole() + ":" + player.getAlive()).reduce((s, s2) -> s + "\t" + s2).ifPresent(s -> log.debug("当前状况:\n{}", s));
    }

    public void checkWin() {
        print();

        Integer goodWin = 1, badWin = -1;
        for (Player player : players) {
            if (!player.getAlive()) continue;
            if (player.getRole().equals(wolf)) {
                goodWin = 0;
            } else {
                badWin = 0;
            }
            if (goodWin + badWin == 0) return;
        }

        stop = true;
        gameResult = goodWin + badWin > 0 ? good : bad;
    }

    //--------------------------------------------------------------------------------------------------
    public void initPlayer(Integer playerNum) {
        log.debug("初始化游戏 ,共{}人", playerNum);

        List<RoleEnum> roles = generateRoles(playerNum);
        for (int i = 0; i < roles.size(); i++) {
            RoleEnum role = roles.get(i);
            players.add(new Player(i + 1, role));
            if (!playerMap.containsKey(role)) {
                playerMap.put(role, new ArrayList<>());
            }
            playerMap.get(role).add(i + 1);
        }
    }

    public List<RoleEnum> generateRoles(Integer playerNum) {
        List<RoleEnum> list = new ArrayList<>();
        switch (playerNum) {
            case 9:
                addMany(list, citizen, 3);
                addMany(list, witch, 1);
                addMany(list, prophet, 1);
                addMany(list, hunter, 1);
                addMany(list, wolf, 3);
                break;
            case 12:
                addMany(list, citizen, 4);
                addMany(list, witch, 1);
                addMany(list, prophet, 1);
                addMany(list, hunter, 1);
                addMany(list, moron, 1);
                addMany(list, wolf, 4);
                break;
            default:
                throw new RuntimeException("不支持");
        }
        Collections.shuffle(list);
        log.debug("分配身份:{}", list.toString());
        return list;
    }

    public void addMany(List list, Object object, Integer num) {
        for (int i = 0; i < num; i++) {
            list.add(object);
        }
    }
}
