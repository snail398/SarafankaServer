package com.sarafanka.team.sarafanka.server;

public class Constants {

    public static class URL{
        public static final String HOST ="http://80.250.211.138:8090/com.sarafanka.team.sarafanka.server-2.0/";
        //public static final String HOST ="http://192.168.1.64:8080";
        public static final String LOG_IN ="";
        public static final String GET_ALL_ACTIONS=HOST+"actions/getall";

        public static final String GET_ALL_COMPANIES=HOST+"companies/getall";

    }
    public static class PathsToFiles{
        public static final String pathToAvatars ="c:\\sarafun\\avatars\\";
        public static final String pathToCompaniesPhotos ="c:\\sarafun\\companiesPhotos\\";
        public static final String pathToQRCodes ="c:\\sarafun\\qrcodes\\";
    }

    public static class Security{

        public static final String SECRET_APP_INDETIFICATOR ="sarafanka398";

    }

    public static class Social {
        public static final String smsSignature = "TEST-SMS";
        public static final String bitlyAccessToken = "5f7e8da06a708a622c58ed9d65070c0de52ce7f8";

    }

}
