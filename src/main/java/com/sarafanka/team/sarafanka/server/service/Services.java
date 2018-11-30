package com.sarafanka.team.sarafanka.server.service;

import com.google.zxing.WriterException;
import com.sarafanka.team.sarafanka.server.entity.*;

import java.io.IOException;
import java.util.List;

public interface Services {

    List<Action> getRunningActionsByLoginAndOrgId(String lgn, Long orgId, Integer ifComplited);
    Integer  addNewRunningActions(String lgn,Long id);
    Integer  addNewRunningActions(Account acc, Long id, String messageType, Long staffid) throws IOException, WriterException;

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

    List<Company> getCompanyWithAction(Long lgn);

    String deleteUsedCoupon (Long Login, Long actionID, Long barmenLogin);
    List<Company> getCompanyByCategory(String category, Long lgn);

    Company getMarketologCompany(Long login);

    Integer getMarketologRole(Long lgn);

    Integer changeCompanyInfoByMain(String login, String newName,String newCategory,String newType,String newDescription, String newAdress,String newPhone,String newSite);

    List<Action> getActionsForMarketolog(String lgn);

    Integer addNewActon(String login, String reward, String supportReward, Integer target,String description, Long timeStart,Long timeEnd,String est);

    Integer deleteActionByID(Long actionID);

    List<Integer> getCountOfAction(Long accountID);

    Integer getCouponsCount(String lgn);

    Integer createEstablishment (String login, String adress,String estPhone);

    Integer registrationMarketolog(Long creatorID, String lgn, String pass, String firstname, String secondname, String estAdress);

    Integer registrationMainMarketolog(String lgn, String pass, String firstname, String secondname, String companyName, String companyCategory, String companyType, String companyDescription, String companyAdress, String companyPhone, String companySite, Establishment newEst);

    Integer changeInfo(String oldEmail,String email, String firstname, String secondname);

    Integer deleteAccount(String email);

    Integer getCountPeopleToEnd(String lgn, Long actionID);

    Integer deleteFriend(String lgn, String friend);

    Integer getCountOfFriendRequest(String lgn);

    Integer addBarmenOperation(Long staffID, String type, Long clientID, Long actionID);

    List<StaffOperationHistoryForApp> getBarmenOperations(Long lgn);

    Integer deleteRAact(String lgn, Long actionID);

    List<Account> getAvatarPathFriendsWithRAct(String login, Long actionID);

    List<Account> getAvatarPathFriendsWithRActInCompany(String login, String companyName);

    List<Account> getAvatarPathFriendsHelped(String login, Long actionID);

    List<Account> getAvatarPathCommonFriends(String login, String friendLogin);

    List<StaffForApp> getMarketologs(Long mainMarketologLogin);
    List<StaffForApp> getBarmens(Long mainMarketologLogin);

    Integer deleteStaff(Long accID);

    List<Action> getActionsForStaff(Account account);

    String ChangeProgressForSocial(Long userID, Long actionID, Long barmenLogin) throws IOException, WriterException;

    Integer changeCompanyInfoByMain(Company newBrandInfo);

    Account getStaff(Long accID);

    Integer addNewActon2(Action action, Long creatorID, String est);

    Establishment getAccountsEst(Long accID);

    Integer changeStaffInfo(Long accID, String estname, Account account);

    List<ActionStatistic> getStatisticByMarketolog(Long marketologID);

    String getLink(String userID, Long actionID, Long barmenID) throws IOException;

    String getLinkAndBonus(Long userID, Long actionID, Long barmenID) throws IOException;
}
