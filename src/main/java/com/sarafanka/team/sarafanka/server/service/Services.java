package com.sarafanka.team.sarafanka.server.service;

import com.sarafanka.team.sarafanka.server.entity.*;

import java.util.List;

public interface Services {

    List<Action> getRunningActionsByLoginAndOrgId(String lgn, Long orgId, Integer ifComplited);
    Integer  addNewRunningActions(String lgn,Long id);

    List<Account> getFriendsForInvite(String lgn, Long actionID);
    List<Account> getFriends(String lgn);
    Integer createNewFriendRequest(String initLgn,String targetLgn);
    Integer acceptFriendRequest(String accepterLgn,String inviterLgn);
    List<Account> getRequestFriends(String lgn);

    List<Account> getUsersByInviteTargetID(String targetLogin);
    List<Action> getInvitedAction(String initLogin, String targetLogin);

    List<Action> getFriendsRunningActions(String lgn);
    List<Company> getCompanyWithRunningAction(String lgn, Integer ifComplited);
    Integer ChangeProgress (String lgn, Long id,String userLogin, String barmenLogin);
    Integer addNewInvite (String initLogin, Long actionID,Long[] targetLogins);
    List<Coupon> getCouponsByUserLogin(String lgn);

    List<Company> getCompanyWithAction(String lgn);

    Integer deleteUsedCoupon (String Login, Long actionID,String barmenLogin);
    List<Company> getCompanyByCategory(String category,String lgn);

    Company getMarketologCompany(String login);

    Integer getMarketologRole(String lgn);

    Integer changeCompanyInfoByMain(String login, String newName,String newCategory,String newType,String newDescription, String newAdress,String newPhone,String newSite);

    List<Action> getActionsForMarketolog(String lgn);

    Integer addNewActon(String login, String reward, String supportReward, Integer target,String description, Long timeStart,Long timeEnd,String est);

    Integer deleteActionByID(Long actionID);

    List<Integer> getCountOfAction(String lgn);

    Integer getCouponsCount(String lgn);

    Integer createEstablishment (String login, String adress,String estPhone);

    Integer registrationMarketolog(String lgn, String pass, String firstname, String secondname, String estAdress);

    Integer registrationMainMarketolog(String lgn, String pass, String firstname, String secondname, String companyName,String companyCategory, String companyType, String companyDescription, String companyAdress, String companyPhone, String companySite,String estAdress, String estPhone);

    Integer changeInfo(String oldEmail,String email, String firstname, String secondname);

    Integer deleteAccount(String email);

    Integer getCountPeopleToEnd(String lgn, Long actionID);

    Integer deleteFriend(String lgn, String friend);

    Integer getCountOfFriendRequest(String lgn);

    Integer addBarmenOperation(String lgn, String type, String clientLgn, Long actionID);

    List<BarmensOperationHistoryForApp> getBarmenOperations(String lgn);
}
