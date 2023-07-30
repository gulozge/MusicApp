package com.atmosware.musicapp.common.constants;

import com.atmosware.musicapp.api.controllers.ArtistsController;
import com.atmosware.musicapp.api.controllers.SongsController;

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
    public static class Album {
        public static final String NotExists = "ALBUM_NOT_EXISTS";
    }

    public static class Artist {
        public static final String NotFound = "ARTIST_NOT_FOUND";
    }

    public static class Song {
        public static final String NotExists = "ALBUM_NOT_EXISTS";
    }
    public static class FavoriteSong {
        public static final String Exists = "FAVORITE-SONG-ALREADY_EXISTS";
    }
}