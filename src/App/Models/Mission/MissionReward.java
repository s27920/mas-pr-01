package App.Models.Mission;

import App.Models.Guild.GuildMember;
import App.Util.SuperObject;

public class MissionReward extends SuperObject {
    private final MissionRewardType rewardType;
    private long quantity;

    public MissionReward(MissionRewardType rewardType, long quantity) {
        this.rewardType = rewardType;
        this.quantity = quantity;
    }

    public MissionReward(MissionRewardType rewardType) {
        this.rewardType = rewardType;
    }

    public MissionRewardType getRewardType() {
        return rewardType;
    }

    public long getQuantity() {
        return quantity;
    }

    public void addQuantity(long earned){
        this.quantity += earned;
    }

    public void setOwner(GuildMember guildMember){

    }
}
