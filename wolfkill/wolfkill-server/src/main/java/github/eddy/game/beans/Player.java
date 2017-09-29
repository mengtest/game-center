package github.eddy.game.beans;


import github.eddy.game.common.RoleEnum;
import github.eddy.game.common.SkillEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Player {
    Integer id;
    RoleEnum role;

    Boolean alive = true;
    SkillEnum deathReason;

    Boolean[] skillStatus;

    public Player(Integer id, RoleEnum role) {
        this.id = id;
        this.role = role;
        switch (role) {
            case witch:
                this.skillStatus = new Boolean[]{true, true};//00 无技能 10能救人 01能杀人 11能救能杀
                break;
            default:
                this.skillStatus = new Boolean[]{true};//0 无 1有一次技能
        }
    }


    public Boolean canPosionORMedicine(SkillEnum skillEnum) {
        switch (skillEnum) {
            case posion:
                return skillStatus[1];
            case medicine:
                return skillStatus[0];
            default:
                return false;
        }
    }


    public Boolean killedByVote() {
        System.out.println(id + "被处决了");
        return killed(SkillEnum.vote);
    }

    public Boolean killedByKill() {
        System.out.println(id + "被狼人杀死了");
        return killed(SkillEnum.kill);
    }

    public Boolean killedBySkill(Player player, SkillEnum skill) {
        switch (skill) {
            case posion:
                player.setSkillStatus(new Boolean[]{player.getSkillStatus()[0], false});
                break;
            case gun:
                player.setSkillStatus(new Boolean[]{false});
                break;
        }
        System.out.println(id + "被" + player.id + "用" + skill + "杀死了");
        return killed(skill);
    }

    public Boolean killed(SkillEnum skillEnum) {
        setDeathReason(skillEnum);
        setAlive(false);
        return !skillEnum.equals(SkillEnum.posion) && role.equals(RoleEnum.hunter);
    }
}
