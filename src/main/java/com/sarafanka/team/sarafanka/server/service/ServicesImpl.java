package com.sarafanka.team.sarafanka.server.service;

import com.sarafanka.team.sarafanka.server.entity.*;
import com.sarafanka.team.sarafanka.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ServicesImpl implements Services {

    @Autowired
    private RunningActionsRepository repo;
    @Autowired
    private ActionRepository actRepo;
    @Autowired
    private FriendsRepository friendRepo;
    @Autowired
    private AccountRepository accRepo;
    @Autowired
    private InvitesRepositories invRepo;
    @Autowired
    private CompaniesRepository comRepo;
    @Autowired
    private CouponsRepository couponsRepo;
    @Autowired
    private MarketologRepository marketologRepo;
    @Autowired
    private ActionAccessRepository actionAccessRepository;
    @Autowired
    private MarketologWorkingPlaceRepository marketologWorkingPlaceRepository;
    @Autowired
    private EstablishmentRepository establishmentRepository;
    @Autowired
    private BarmenOperationHistoryRepository barmenOperationHistoryRepository;

    @Override
    public Integer addBarmenOperation(String lgn, String type, String clientLgn, Long actionID) {
        Long barmenID = accRepo.findBylogin(lgn).getId();
        Long clientID = accRepo.findBylogin(clientLgn).getId();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();

        BarmensOperationHistory newOperation = new BarmensOperationHistory();
        newOperation.setBarmenAccountID(barmenID);
        newOperation.setActionID(actionID);
        newOperation.setClientAccountID(clientID);
        newOperation.setOperationType(type);
        newOperation.setOperationDate(date);
        barmenOperationHistoryRepository.saveAndFlush(newOperation);
        return 1;
    }

    @Override
    public List<BarmensOperationHistoryForApp> getBarmenOperations(String lgn) {
        Long barmenID = accRepo.findBylogin(lgn).getId();
        List<BarmensOperationHistory> history = barmenOperationHistoryRepository.findByBarmenAccountID(barmenID);
        List<BarmensOperationHistoryForApp> historyForApp = new ArrayList<>();
        for (BarmensOperationHistory operation: history) {
            if (operation.getBarmenAccountID().equals(barmenID)) {
                BarmensOperationHistoryForApp operationForApp = new BarmensOperationHistoryForApp();
                operationForApp.setBarmenAccount(accRepo.findByid(operation.getBarmenAccountID()).getLogin());
                operationForApp.setClientAccount(accRepo.findByid(operation.getClientAccountID()).getLogin());
                operationForApp.setOperationType(operation.getOperationType());
                operationForApp.setOperationDate(operation.getOperationDate());
                operationForApp.setAction(actRepo.findById(operation.getActionID()).getDescription());
                historyForApp.add(operationForApp);
            }
        }
        return historyForApp;
    }

    @Override
    public Integer registrationMarketolog(String lgn, String pass, String firstname, String secondname, String estAdress) {

        // Удаляем лишние пробелы в конце и в начале логина и пароля
        String targetLogin = lgn.trim();
        String targetPass = pass.trim();
        Integer responseCode = 0;
        Boolean duplicate =false;

        //Получаем список всех аккаунтов
        List<Account> list = accRepo.findAll();
        //Поиск аккаунта в списке (Проверка на дублирование логинов)
        for (Account acc:list) {
            if (acc.getLogin().equals(targetLogin)){
                duplicate = true;
                break;
            }
        }
        //Если нет такого логина, созадаем новый аккаунт
        if(!duplicate){
            Calendar c = Calendar.getInstance();
            Long date = c.getTimeInMillis();
            Account newAcc = new Account();
            newAcc.setLogin(targetLogin);
            newAcc.setPassword(targetPass);
            newAcc.setAccountType("marketolog");
            newAcc.setFirstName(firstname);
            newAcc.setSecondName(secondname);
            newAcc.setCreatingDate(date);
            accRepo.saveAndFlush(newAcc);
            Marketolog marketolog = new Marketolog();
            marketolog.setAccountID(accRepo.findBylogin(targetLogin).getId());
            marketolog.setMain(0);
            marketolog.setCompanyID(establishmentRepository.findByFactAdress(estAdress).getCompanyID());
            marketologRepo.saveAndFlush(marketolog);
            MarketologWorkingPlace mwp = new MarketologWorkingPlace();
            mwp.setEstablishmentID(establishmentRepository.findByFactAdress(estAdress).getId());
            mwp.setMarketologID(marketologRepo.findByAccountID(accRepo.findBylogin(targetLogin).getId()).getId());
            marketologWorkingPlaceRepository.saveAndFlush(mwp);

            responseCode =1;
        }
        else{
            responseCode = -1;
        }
        return  responseCode;
    }

    @Override
    public Integer registrationMainMarketolog(String lgn, String pass, String firstname, String secondname, String companyName,String companyCategory, String companyType, String companyDescription, String companyAdress, String companyPhone, String companySite,String estAdress, String estPhone) {
        // Удаляем лишние пробелы в конце и в начале логина и пароля
        String targetLogin = lgn.trim();
        String targetPass = pass.trim();
        Integer responseCode = 0;
        Boolean duplicate =false;

        //Получаем список всех аккаунтов
        List<Account> list = accRepo.findAll();
        //Поиск аккаунта в списке (Проверка на дублирование логинов)
        for (Account acc:list) {
            if (acc.getLogin().equals(targetLogin)){
                duplicate = true;
                responseCode=-1;
                break;
            }
        }
        List<Company> list1 = comRepo.findAll();

        for (Company com:list1) {
            if (com.getTitle().equals(companyName)){
                duplicate = true;
                responseCode = -2;
                break;
            }
        }
        //Если нет такого логина, созадаем новый аккаунт
        if(!duplicate){
            Calendar c = Calendar.getInstance();
            Long date = c.getTimeInMillis();
            //Создание аккаунта

            Account newAcc = new Account();
            newAcc.setLogin(targetLogin);
            newAcc.setPassword(targetPass);
            newAcc.setAccountType("marketolog");
            newAcc.setFirstName(firstname);
            newAcc.setSecondName(secondname);
            newAcc.setCreatingDate(date);
            accRepo.saveAndFlush(newAcc);
            //Создание компании
            Company newCompany = new Company();
            newCompany.setTitle(companyName);
            newCompany.setCategory(companyCategory);
            newCompany.setCompanyType(companyType);
            newCompany.setDescription(companyDescription);
            newCompany.setAdress(companyAdress);
            newCompany.setPhone(companyPhone);
            newCompany.setSite(companySite);
            newCompany.setCreatingDate(date);
            comRepo.saveAndFlush(newCompany);
            //Создание маркетолога
            Marketolog marketolog = new Marketolog();
            marketolog.setAccountID(accRepo.findBylogin(targetLogin).getId());
            marketolog.setMain(1);
            marketolog.setCompanyID(comRepo.findByTitle(companyName).getId());
            marketologRepo.saveAndFlush(marketolog);
            //Создание заведения
            Establishment est = new Establishment();
            est.setCompanyID(comRepo.findByTitle(companyName).getId());
            est.setEstPhone(estPhone);
            est.setFactAdress(estAdress);
            establishmentRepository.saveAndFlush(est);

            responseCode =1;
        }

        return  responseCode;
    }

    public List<Action> getRunningActionsByLoginAndOrgId(String lgn, Long orgId, Integer ifComplited) {
        List<RunningActions> allRunningActions = repo.findAll();
        List<Long> targetActions = new ArrayList<Long>();
        for (RunningActions ract:allRunningActions) {
            if (accRepo.findOne(ract.getAccountLoginID()).getLogin().equals(lgn) && ract.getComplited().equals(ifComplited)){
                targetActions.add(actRepo.findOne(ract.getActionTitleID()).getId());
            }
        }
        List<Action> allActions= actRepo.findAll();
        List<Action> list = new ArrayList<Action>();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();
        for (Long str:targetActions) {
            for (Action act:allActions) {
                if (act.getId() == str && act.getOrganizationID().equals(orgId)&&date>act.getTimeStart()&&date<act.getTimeEnd()){
                    list.add(act);
                    continue;
                }
            }
        }
        return list;
    }

    @Override
    public Integer deleteFriend(String lgn, String friend) {
        for (Friends friend1: friendRepo.findAll()) {
            if ((friend1.getUser1_id().equals(accRepo.findBylogin(lgn).getId())&&friend1.getUser2_id().equals(accRepo.findBylogin(friend).getId()))||
                    (friend1.getUser2_id().equals(accRepo.findBylogin(lgn).getId())&&friend1.getUser1_id().equals(accRepo.findBylogin(friend).getId())) ){
                friendRepo.delete(friend1);
            }
        }
        return 1;
    }

    public Integer addNewRunningActions(String lgn, Long id) {

        actRepo.increasePeopleUser(id);

        RunningActions runAct = new RunningActions();
        runAct.setAccountLoginID(accRepo.findBylogin(lgn).getId());
        runAct.setActionTitleID(id);
        runAct.setPercentOfComplete(0);
        runAct.setComplited(0);
        repo.saveAndFlush(runAct);
        return 2;
    }

    public List<Account> getFriendsForInvite(String lgn, Long actionID) {
        List<Friends> allFriends = friendRepo.findAll();
        List<Invite> invites = invRepo.findAll();
        List<Account> list = new ArrayList<Account>();
        Long tempId = accRepo.findBylogin(lgn).getId();


        for (Friends fr:allFriends) {
            if (fr.getStatus().equals(1)) {
                if (fr.getUser1_id().equals(tempId)) {
                   // if (invites.size()== 0) {
                //        list.add(accRepo.findOne(fr.getUser2_id()));
                //    }
                    Boolean hasInvite = true;
                    for (Invite invite:invites) {
                        if ((invite.getAction_id()).equals(actionID)&&
                                (invite.getInit_user_id()).equals(tempId)&&
                                (invite.getTarget_user_id()).equals(fr.getUser2_id()
                                )){
                            hasInvite = false;
                        }
                    }
                    if (hasInvite){
                        list.add(accRepo.findOne(fr.getUser2_id()));
                    }
                }
                if (fr.getUser2_id().equals(tempId)) {
                  //  if (invites.size() == 0){
                  //      list.add(accRepo.findOne(fr.getUser1_id()));
                  //  }
                    Boolean hasInvite = true;
                    for (Invite invite:invites) {
                        if ((invite.getAction_id()).equals(actionID)&&
                                (invite.getInit_user_id()).equals(tempId)&&
                                (invite.getTarget_user_id()).equals(fr.getUser1_id())){
                            hasInvite = false;
                        }

                    }
                    if (hasInvite){
                        list.add(accRepo.findOne(fr.getUser1_id()));
                    }
                }
            }
        }
        return list;
    }

    public List<Account> getFriends(String lgn) {
        List<Friends> allFriends = friendRepo.findAll();
        List<Invite> invites = invRepo.findAll();
        List<Account> list = new ArrayList<Account>();
        Long tempId = accRepo.findBylogin(lgn).getId();


        for (Friends fr:allFriends) {
            if (fr.getStatus().equals(1)) {
                if (fr.getUser1_id().equals(tempId)) {
                    list.add(accRepo.findOne(fr.getUser2_id()));
                                   }
                if (fr.getUser2_id().equals(tempId)) {
                            list.add(accRepo.findOne(fr.getUser1_id()));
                }
            }
        }
        return list;
    }
    public List<Account> getRequestFriends(String lgn) {
        List<Friends> allFriends = friendRepo.findAll();
        List<Account> allAccounts = accRepo.findAll();
        List<Account> list = new ArrayList<Account>();
        Long tempId = Long.valueOf(0);
        for (Account acc:allAccounts) {
            if (acc.getLogin().equals(lgn)) {
                tempId=acc.getId();
                break;
            }
        }

        for (Friends fr:allFriends) {
            if (fr.getStatus().equals(0) && fr.getUser2_id().equals(tempId)) {
                list.add(accRepo.findOne(fr.getUser1_id()));
            }
        }
        return list;
    }

    @Override
    public Integer createNewFriendRequest(String initLgn, String targetLgn) {
        Long initAccountID= Long.valueOf(0);
        Long targetAccountID= Long.valueOf(0);
        for (Account acc:accRepo.findAll()) {
            if (acc.getLogin().equals(initLgn)){
                initAccountID = acc.getId();

            }
            if (acc.getLogin().equals(targetLgn)){
                targetAccountID = acc.getId();

            }
        }

        Friends newFriendRequest = new Friends();
        newFriendRequest.setUser1_id(initAccountID);
        newFriendRequest.setUser2_id(targetAccountID);
        newFriendRequest.setStatus(0);
        friendRepo.saveAndFlush(newFriendRequest);
        return 1;
    }

    @Override
    public Integer acceptFriendRequest(String accepterLgn, String inviterLgn) {
        Long accepterAccountID= Long.valueOf(0);
        Long inviterAccountID= Long.valueOf(0);
        for (Account acc:accRepo.findAll()) {
            if (acc.getLogin().equals(accepterLgn)){
                accepterAccountID = acc.getId();
                continue;
            }
            if (acc.getLogin().equals(inviterLgn)){
                inviterAccountID = acc.getId();
                continue;
            }
        }
       friendRepo.acceptFriendRequest(inviterAccountID,accepterAccountID,Integer.valueOf(1));
        return 1;
    }

    //Получить список людей, приглащающий человека с targetID
    public List<Account> getUsersByInviteTargetID(String targetLogin) {
        Long targetID = accRepo.findBylogin(targetLogin).getId();
        List<Invite> inviteList = invRepo.findAll();
        List<Account> accList = accRepo.findAll();
        List<Account> result = new ArrayList<Account>();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();


        for (Invite inv:inviteList) {
            if (inv.getTarget_user_id().equals(targetID) && inv.getResponseCode()==0) {
                Long initUserId = inv.getInit_user_id();
                Action action = actRepo.findById(inv.getAction_id());
                if (date > action.getTimeStart() && date < action.getTimeEnd()){
                    for (Account acc : accList) {
                        Boolean flag = false;
                        if (initUserId.equals(acc.getId())) {
                            for (Account acc1 : result) {
                                if (acc1.getLogin().equals(acc.getLogin())) {
                                    flag = true;
                                }
                            }
                            if (!flag) {
                                result.add(acc);
                                break;
                            }
                        }
                    }
            }
            }
        }
        return result;
    }

    //Получить список акций в которые InitID пригласил человека с targetID
    public List<Action> getInvitedAction(String initLogin, String targetLogin) {
        Long initID = Long.valueOf(0);
        Long targetID = Long.valueOf(0);
        List<Action> result = new ArrayList<Action>();

        List<Invite> inviteList = invRepo.findAll();
        List<Action> actList = actRepo.findAll();

        for (Account acc:accRepo.findAll()) {
            if (acc.getLogin().equals(initLogin)){
                initID = acc.getId();
                break;
            }
        }

        for (Account acc:accRepo.findAll()) {
            if (acc.getLogin().equals(targetLogin)){
                targetID = acc.getId();
                break;
            }
        }

        for (Invite inv: inviteList) {
            if(inv.getTarget_user_id().equals(targetID)&&inv.getInit_user_id().equals(initID) && inv.getResponseCode()==0) {
                for (Action act: actList) {
                    if(inv.getAction_id().equals(act.getId())){
                        result.add(act);
                        break;
                    }
                }
            }

        }
        return result;
    }

    public List<Action> getFriendsRunningActions(String lgn) {
        List<Action> targetActions = new ArrayList<Action>();
      //  for (Account acc:getFriends(lgn)) {
          //  List<Action> tempAction = getRunningActionsByLoginAndOrgId(acc.getLogin());
         //   for (Action action:tempAction) {
          //      targetActions.add(action);
          //  }
        //}
        return targetActions;
    }

    public List<Company> getCompanyWithRunningAction(String lgn, Integer ifComplited){

        List<Company> result = new ArrayList<Company>();
        List<RunningActions> runActs = repo.findAll();
        List<Action> actions = actRepo.findAll();
        List<Company> companies = comRepo.findAll();
        List<Account> accs = accRepo.findAll();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();

        //Получение id пользователя по логину
        Long id = Long.valueOf(0);
        for (Account acc: accs) {
            if (acc.getLogin().equals(lgn)){
                id = acc.getId();
                break;
            }
        }

        for (RunningActions runAct: runActs) {
            if (runAct.getAccountLoginID().equals(id) && runAct.getComplited().equals(ifComplited)){
                for (Action act:actions) {
                    if (runAct.getActionTitleID().equals(act.getId())){
                        for (Company company:companies) {
                            if (act.getOrganizationID().equals(company.getId()) &&date > act.getTimeStart() && date < act.getTimeEnd()){
                                Boolean flag = true;
                                for (Company cmp:result ) {
                                    if (cmp.equals(company)) flag =false;
                                }
                                if (flag) result.add(company);
                                break;
                            }
                        }
                     break;
                    }
                }
            }
        }

        return result;
    }

    public Integer ChangeProgress(String lgn,  Long id, String userLogin, String barmenLogin) {
        Long accountID= Long.valueOf(0), actionID= Long.valueOf(0),userAccountID= Long.valueOf(0);
        Action tempAct = new Action();
        Long barmenID = accRepo.findBylogin(barmenLogin).getId();

        accountID = accRepo.findBylogin(lgn).getId();
        actionID = id;
        tempAct = actRepo.findById(id);
        userAccountID = accRepo.findBylogin(userLogin).getId();

        for (Invite invite:invRepo.findAll()) {
            if (invite.getTarget_user_id().equals(userAccountID) &&invite.getInit_user_id().equals(accountID) && invite.getAction_id().equals(actionID)){
               invRepo.delete(invite.getId());
            }
        }

        for (RunningActions ract:repo.findAll() ) {
            if (ract.getAccountLoginID().equals(accountID) && ract.getActionTitleID().equals(actionID)) {
                repo.changeProgress(ract.getId(), ract.getPercentOfComplete() + 1);
                //Создаем запись в истории операций
                Calendar c = Calendar.getInstance();
                Long date = c.getTimeInMillis();
                BarmensOperationHistory newOperation = new BarmensOperationHistory();
                newOperation.setBarmenAccountID(barmenID);
                newOperation.setActionID(actionID);
                newOperation.setClientAccountID(accountID);
                newOperation.setOperationType("Отсканировал код и выдал награду для друга");
                newOperation.setOperationDate(date);
                if (ract.getPercentOfComplete() >= tempAct.getTarget()-1) {
                    //Выдать купон
                    Coupon newCoupon = new Coupon();
                    newCoupon.setAccountID(accountID);
                    newCoupon.setActionID(actionID);
                    newCoupon.setCompanyID(tempAct.getOrganizationID());
                    newCoupon.setReward(tempAct.getReward());
                    newOperation.setOperationType("Отсканировал код,выдал награду для друга, выдал купон");
                    couponsRepo.saveAndFlush(newCoupon);
                    repo.changeComplitedStatus(ract.getId());

                }

                barmenOperationHistoryRepository.saveAndFlush(newOperation);
                break;
            }
        }
        return 1;
    }

    public Integer addNewInvite(String initLogin, Long actionID, Long[] targetLogins) {
        Long accountID= accRepo.findBylogin(initLogin).getId();

        for (Long i: targetLogins) {
            if (i!=-1) {
                Calendar c = Calendar.getInstance();
                Long date = c.getTimeInMillis();
                Invite tempInvite = new Invite();
                tempInvite.setInit_user_id(accountID);
                tempInvite.setAction_id(actionID);
                tempInvite.setTarget_user_id(i);
                tempInvite.setInviteDate(date);
                tempInvite.setResponseCode(0);
                invRepo.saveAndFlush(tempInvite);
            }
        }
        return 1;
    }

    public List<Coupon> getCouponsByUserLogin(String lgn) {
        Long accountID= accRepo.findBylogin(lgn).getId();

        List<Coupon> result = new ArrayList<Coupon>();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();


        for (Coupon coupon:couponsRepo.findAll()) {
            Action action = actRepo.findById(coupon.getId());
            if (coupon.getAccountID().equals(accountID)&&date>action.getTimeStart()&&date<action.getTimeEnd()){
                result.add(coupon);
            }

        }
        return result;
    }

    public List<Company> getCompanyWithAction(String lgn) {
        Long accountID = accRepo.findBylogin(lgn).getId();
        List<Company> result = new ArrayList<Company>();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();

        //Флаг показывает, что пользователь участвует минимум в одной акции компании
        Boolean flag = false;

        for (Company company:comRepo.findAll()) {
            Integer countOfAction = 0;
            Integer countOfRunningAction = 0;
            for (Action action:actRepo.findAll()) {
                if (action.getOrganizationID().equals(company.getId())&&date > action.getTimeStart() && date < action.getTimeEnd() ){
                    countOfAction++;
                    for (RunningActions ract:repo.findAll()) {
                        if (ract.getAccountLoginID().equals(accountID) && ract.getActionTitleID().equals(action.getId())){
                            countOfRunningAction++;
                            flag = true;
                            break;
                        }
                    }
                }
            }

            if(!countOfRunningAction.equals(countOfAction) && countOfAction>0) {
                result.add(company);
            }
        }

        return result;
    }

    @Override
    public Integer deleteUsedCoupon(String Login, Long actionID,String barmenLogin) {
        Long accountID= Long.valueOf(0);
        Long barmenAccountID = accRepo.findBylogin(barmenLogin).getId();

        for (Account acc:accRepo.findAll()) {
            if (acc.getLogin().equals(Login)){
                accountID = acc.getId();
                break;
            }
        }


        //Удаляем купон
        for (Coupon coupon:couponsRepo.findAll()) {
            if (coupon.getAccountID().equals(accountID) &&coupon.getActionID().equals(actionID)){
                couponsRepo.delete(coupon.getId());

                //Создаем запись в историю операций
                Calendar c = Calendar.getInstance();
                Long date = c.getTimeInMillis();
                BarmensOperationHistory newOperation = new BarmensOperationHistory();
                newOperation.setBarmenAccountID(barmenAccountID);
                newOperation.setActionID(actionID);
                newOperation.setClientAccountID(accountID);
                newOperation.setOperationType("Отсканировал купон и выдал приз");
                newOperation.setOperationDate(date);
                barmenOperationHistoryRepository.saveAndFlush(newOperation);
                break;


            }
        }

        return 3 ;
    }

    @Override
    public List<Company> getCompanyByCategory(String category,String lgn) {
        Long accountID = accRepo.findBylogin(lgn).getId();
        List<Company> resultList = new ArrayList<Company>();
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();
        for (Company company:comRepo.findAll()) {
            if (company.getCategory().equals(category)){
                Integer countOfAction = 0;
                Integer countOfRunningAction = 0;
                for (Action action:actRepo.findAll()) {
                    if (action.getOrganizationID().equals(company.getId())&&date > action.getTimeStart() && date < action.getTimeEnd() ){
                        countOfAction++;
                        for (RunningActions ract:repo.findAll()) {
                            if (ract.getAccountLoginID().equals(accountID) && ract.getActionTitleID().equals(action.getId())){
                                countOfRunningAction++;
                                break;
                            }
                        }
                    }
                }

                if(!countOfRunningAction.equals(countOfAction) && countOfAction>0) {
                    resultList.add(company);
                }
            }
        }

        return resultList;
    }

    @Override
    public Company getMarketologCompany(String login) {
        Long accID = accRepo.findBylogin(login).getId();
        Long companyID = marketologRepo.findByAccountID(accID).getCompanyID();
        return comRepo.findById(companyID);
    }

    @Override
    public Integer getMarketologRole(String lgn) {
        Long accID = accRepo.findBylogin(lgn).getId();
        return marketologRepo.findByAccountID(accID).getMain();
    }

    @Override
    public Integer createEstablishment(String login, String adress, String estPhone) {
        Establishment newEst = new Establishment();
        newEst.setFactAdress(adress);
        newEst.setCompanyID(marketologRepo.findByAccountID(accRepo.findBylogin(login).getId()).getCompanyID());
        newEst.setEstPhone(estPhone);
        establishmentRepository.saveAndFlush(newEst);
        return 1;
    }

    @Override
    public Integer changeCompanyInfoByMain(String login, String newName,String newCategory, String newType,String newDescription, String newAdress,String newPhone,String newSite) {
        Long accID = accRepo.findBylogin(login).getId();
        Long companyID = marketologRepo.findByAccountID(accID).getCompanyID();
        comRepo.changeCompanyInfo(companyID,newName,newCategory,newType,newDescription,newAdress,newPhone,newSite);
        return 1;
    }

    @Override
    public Integer addNewActon(String login, String reward, String supportReward, Integer target,String description,Long timeStart,Long timeEnd,String est) {

        Long accID = accRepo.findBylogin(login).getId();

        Action newAction = new Action("Common title",description,marketologRepo.findByAccountID(accID).getCompanyID(),accID,"bringer",reward,supportReward,0,target,timeStart,timeEnd);
        actRepo.saveAndFlush(newAction);
        if (!est.equals("Все заведения")){
            ActionAccess actionAccess = new ActionAccess();
            actionAccess.setActionID(actRepo.findByRewardAndDescription(reward,description).getId());
            actionAccess.setEstablishmentID(establishmentRepository.findByFactAdress(est).getId());
            actionAccessRepository.saveAndFlush(actionAccess);
        }
        return 1;
    }

    @Override
    public List<Action> getActionsForMarketolog(String lgn) {
        List<Action> result = new ArrayList<Action>();
        Long accID = accRepo.findBylogin(lgn).getId();
        List<Long> estIDs = new ArrayList<Long>();
        List<Long> actionIDs = new ArrayList<Long>();

        Marketolog marketolog = marketologRepo.findByAccountID(accID);
        if(marketolog.getMain().equals(1)){
            result = actRepo.findActionsByOrganizationID(marketolog.getCompanyID());
        }
        else {
            for (MarketologWorkingPlace mwp:marketologWorkingPlaceRepository.findMarketologWorkingPlacesByMarketologID(marketolog.getId())) {
                for (ActionAccess aa:actionAccessRepository.findActionAccessesByEstablishmentID(mwp.getEstablishmentID())) {
                    actionIDs.add(aa.getActionID());
                }
            }
            for (Long i :actionIDs) {
                result.add(actRepo.findById(i));
            }
        }
         return result;
    }

    @Override
    public Integer deleteActionByID(Long actionID) {
        actRepo.delete(actionID);
        repo.deleteByActionTitleID(actionID);
        return 1;
    }

    @Override
    public Integer getCountOfFriendRequest(String lgn) {
        Integer result = 0;
        Long accountID = accRepo.findBylogin(lgn).getId();
        for (Friends friend:friendRepo.findAll() ) {
            if(friend.getStatus().equals(0) && friend.getUser2_id().equals(accountID)){
                result++;
            }
        }
        return result;
    }

    @Override
    public List<Integer> getCountOfAction(String lgn) {
        Long accountID = accRepo.findBylogin(lgn).getId();
        List<Company> companies = comRepo.findAll();
        List<Action> actions = actRepo.findAll();
        List<Integer> result = new ArrayList<Integer>();
        Integer number = 0;
        String[] categories = new String[5];
        categories[0]="smoke";
        categories[1]="drink";
        categories[2]="eat";
        categories[3]="movie";
        categories[4]="game";

        Integer[] countOfAction = new Integer[categories.length];
        for (int i = 0;i<categories.length;i++){
            countOfAction[i] = 0;
        }

        List<Long> companiesOfCategory = new ArrayList<Long>();
        Calendar c = Calendar.getInstance();
        for (String category:categories) {
            for (Company company:companies) {
                if (company.getCategory().equals(category)){
                    companiesOfCategory.add(company.getId());
                }
            }
            for (Action action:actions) {
                for (Long compId:companiesOfCategory) {
                    if (action.getOrganizationID().equals(compId)&&action.getTimeStart()<c.getTimeInMillis() && action.getTimeEnd()>c.getTimeInMillis() ){
                        Boolean flag = true;
                        for (RunningActions ract:repo.findAll()) {
                            if (ract.getAccountLoginID().equals(accountID) && ract.getActionTitleID().equals(action.getId()) &&!ract.getComplited().equals(-1)){

                                flag = false;
                                break;
                            }
                        }
                        if (flag)
                            countOfAction[number]++;
                        break;
                    }
                }
            }
            number++;
            companiesOfCategory.clear();
        }
        for (int i = 0;i<categories.length;i++){
            result.add(countOfAction[i]);
        }
/*
        //Получение списка категорий
        categories.add(companies.get(0).getCategory());
        for (Company company:companies) {
            Boolean flag = false;
            for (String category:categories) {
                if (!category.equals(company.getCategory())){
                    flag = true;
                    break;
                }
            }
            if (flag) categories.add(company.getCategory());
        }
*/

        return result;
    }

    @Override
    public Integer getCountPeopleToEnd(String lgn, Long actionID) {

        return (actRepo.findById(actionID).getTarget()-repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accRepo.findBylogin(lgn).getId(),0).getPercentOfComplete());
    }

    @Override
    public Integer deleteAccount(String email) {
        Long accountID = accRepo.findBylogin(email).getId();
        //Удаление аккаунта
        accRepo.delete(accountID);
        //Удаление запущенных им акций
        for (RunningActions ract: repo.findAll()) {
            if (ract.getAccountLoginID().equals(accountID)){
                repo.delete(ract.getId());
            }
        }
        //Удаление дружеских связей
        for (Friends friend: friendRepo.findAll()) {
            if (friend.getUser1_id().equals(accountID) ||friend.getUser2_id().equals(accountID)){
                friendRepo.delete(friend.getId());
            }
        }


        //Удаление неиспользованных купонов
        for (Coupon coupon: couponsRepo.findAll()) {
            if (coupon.getAccountID().equals(accountID)){
                couponsRepo.delete(coupon.getId());
            }
        }

        return 1;
    }

    @Override
    public Integer changeInfo(String oldEmail,String email, String firstname, String secondname) {
        accRepo.changeInfo(oldEmail,email,firstname,secondname);
        return 1;
    }

    @Override
    public Integer getCouponsCount(String lgn) {
        List<Coupon> coupons = couponsRepo.findAll();
        Long accountID = accRepo.findBylogin(lgn).getId();
        Integer result =0;
        Calendar c = Calendar.getInstance();
        Long date = c.getTimeInMillis();

        for (Coupon coupon: coupons) {
            Action action = actRepo.findById(coupon.getActionID());
            if (coupon.getAccountID().equals(accountID)&&date>action.getTimeStart()&&date<action.getTimeEnd()){
                result++;
            }
        }
        return result;
    }

    @Override
    public Integer deleteRAact(String lgn, Long actionID) {
        Long accountID = accRepo.findBylogin(lgn).getId();
        repo.deleteRActByUser(repo.findByActionTitleIDAndAccountLoginIDAndComplited(actionID,accountID,0).getId());
        return 1;
    }

    @Override
    public List<Account> getAvatarPathFriendsWithRAct(String login, Long actionID) {
        List<Account> friendList = getFriends(login);
        List<Account> friendWithRAct = new ArrayList<>();
        Integer friendCount = 5;                                //Максимальное требуемое количество друзей
        Integer counter = 0;
        for (Account friend:friendList ) {
            for (RunningActions ract:repo.findAll() ) {
                if (ract.getAccountLoginID().equals(friend.getId()) && ract.getActionTitleID().equals(actionID) &&ract.getComplited().equals(0)){
                    friendWithRAct.add(friend);
                    counter++;
                    break;
                }
                if (counter.equals(friendCount)) break;
            }
        }
        return friendWithRAct;
    }

    @Override
    public List<Account> getAvatarPathFriendsHelped(String login, Long actionID) {
        Long accountID = accRepo.findBylogin(login).getId();
        List<Account> friendList = getFriends(login);
        List<Account> friendHelped = new ArrayList<>();
        Integer friendCount = 5;                                //Максимальное требуемое количество друзей
        Integer counter = 0;
        for (Account friend:friendList ) {
            for (Invite invite:invRepo.findAll() ) {
                if (invite.getResponseCode() == 1 && invite.getInit_user_id().equals(accountID) && invite.getTarget_user_id().equals(friend.getId()) && invite.getAction_id().equals(actionID)){
                    friendHelped.add(friend);
                    counter++;
                    break;
                }
                if (counter.equals(friendCount)) break;
            }
        }
        return friendHelped;
    }

    @Override
    public List<Account> getAvatarPathFriendsWithRActInCompany(String login, String companyName) {
        List<Account> friendList = getFriends(login);
        List<Account> friendWithRActInCompany = new ArrayList<>();
        List<Action> companyActionList = actRepo.findActionsByOrganizationID(comRepo.findByTitle(companyName).getId());
        Integer friendCount = 5;                                //Максимальное требуемое количество друзей
        Integer counter = 0;
        for (Account friend:friendList ) {
            for (RunningActions ract:repo.findAll() ) {
                for (Action action: companyActionList) {
                    if (ract.getAccountLoginID().equals(friend.getId()) && ract.getActionTitleID().equals(action.getId()) &&ract.getComplited().equals(0)){
                        friendWithRActInCompany.add(friend);
                        counter++;
                        break;
                    }
                    if (counter.equals(friendCount)) break;
                }
                if (counter.equals(friendCount)) break;
            }
        }
        return friendWithRActInCompany;
    }

    @Override
    public List<Account> getAvatarPathCommonFriends(String login, String friendLogin) {
        List<Account> commonFriends = new ArrayList<>();
        List<Account> userFriendList = getFriends(login);
        List<Account> friendFriendList = getFriends(friendLogin);
        Integer friendCount = 5;                                //Максимальное требуемое количество друзей
        Integer counter = 0;
        for (Account userFriend:userFriendList) {
            for (Account friendFriend:friendFriendList) {
                    if (userFriend.getLogin().equals(friendFriend.getLogin()    )){
                        counter++;
                        commonFriends.add(userFriend);
                        friendFriendList.remove(friendFriend);
                        break;
                    }
            }
            if (counter.equals(friendCount)) break;
        }
        return commonFriends;
    }
}
