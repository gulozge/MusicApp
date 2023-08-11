package com.atmosware.musicapp.constants;

public class Messages {
    public static class User {
        public static final String NOT_FOUND = "USER_NOT_FOUND";
        public static final String EXISTS = "USER_ALREADY_EXISTS";
        public static final String NOT_CORRECT = "USERNAME_OR_PASSWORD_NOT_CORRECT";
    }

    public static class Admin {
        public static final String NOT_FOUND = "ADMIN_NOT_FOUND";
        public static final String EXISTS = "ADMIN_ALREADY_EXISTS";
        public static final String NOT_CORRECT = "EMAIL_OR_PASSWORD_NOT_CORRECT";
    }

    public static class Follower {
        public static final String EXISTS = "FOLLOW_ALREADY_EXISTS";
        public static final String NOT_FOLLWED = "NOT_FOllWED_YOURSELF";
    }

    public static class Album {
        public static final String NOT_EXISTS = "ALBUM_NOT_EXISTS";
        public static final String EXISTS = "ALBUM_ALREADY_EXISTS";
    }

    public static class Artist {
        public static final String NOT_FOUND = "ARTIST_NOT_FOUND";
    }

    public static class Song {
        public static final String NOT_EXISTS = "ALBUM_NOT_EXISTS";
        public static final String EXISTS = "SONG_ALREADY_EXISTS";
    }

    public static class FavoriteSong {
        public static final String EXISTS = "FAVORITE_SONG_ALREADY_EXISTS";
    }
}