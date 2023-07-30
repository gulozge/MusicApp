package com.atmosware.musicapp.common.constants;

public class Messages {
    public static class User {
        public static final String NotFound = "USER_NOT_FOUND";
        public static final String Exists = "USER_ALREADY_EXISTS";
        public static final String NotCorrect = "USERNAME_OR_PASSWORD_NOT_CORRECT";
    }
    public static class Admin {
        public static final String NotFound = "ADMIN_NOT_FOUND";
        public static final String Exists = "ADMIN_ALREADY_EXISTS";
        public static final String NotCorrect = "EMAIL_OR_PASSWORD_NOT_CORRECT";
    } public static class Follower {
        public static final String Exists = "FOLLOW_ALREADY_EXISTS";
        public static final String NotFollwed = "NOT_FOllWED_YOURSELF";
    }
}