package com.sarafanka.team.sarafanka.server.entity;

public class ActionStatistic {


    private Long actionID;
    private String description;
    private String reward;
    private String supportReward;
    private Integer target;
    private Long timeStart;
    private Long timeEnd;
    private Long countRAct;
    private Long countComplitedRAct;
    private Long countMainBonus;
    private Long countMiniBonus;

    public ActionStatistic() {
    }

    public Long getActionID() {
        return actionID;
    }

    public void setActionID(Long actionID) {
        this.actionID = actionID;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getSupportReward() {
        return supportReward;
    }

    public void setSupportReward(String supportReward) {
        this.supportReward = supportReward;
    }


    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public Long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Long timeStart) {
        this.timeStart = timeStart;
    }

    public Long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Long getCountRAct() {
        return countRAct;
    }

    public void setCountRAct(Long countRAct) {
        this.countRAct = countRAct;
    }

    public Long getCountComplitedRAct() {
        return countComplitedRAct;
    }

    public void setCountComplitedRAct(Long countComplitedRAct) {
        this.countComplitedRAct = countComplitedRAct;
    }

    public Long getCountMainBonus() {
        return countMainBonus;
    }

    public void setCountMainBonus(Long countMainBonus) {
        this.countMainBonus = countMainBonus;
    }

    public Long getCountMiniBonus() {
        return countMiniBonus;
    }

    public void setCountMiniBonus(Long countMiniBonus) {
        this.countMiniBonus = countMiniBonus;
    }
}
