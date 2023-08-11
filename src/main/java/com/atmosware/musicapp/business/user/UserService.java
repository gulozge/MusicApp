package com.atmosware.musicapp.business.user;


import com.atmosware.musicapp.business.admin.LoginResponse;
import com.atmosware.musicapp.business.song.GetByUserIdFavoriteSongs;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAll();

    UserResponse getById(UUID id);

    UserResponse add(UserRequest request);

    UserResponse update(UUID id, UserRequest request);

    void delete(UUID id);

    LoginResponse login(UserLoginRequest request);
    void logout(String token);

    void follow(UUID userId, UUID followedId);

    void unfollow(UUID userId, UUID followedId);

    List<UserResponse> getFollowing(UUID userId);
    void addSongToFavorites(UUID userId, UUID songId);

    void removeSongFromFavorites(UUID userId, UUID songId);
    public List<GetByUserIdFavoriteSongs> getFavoriteSongs(UUID userId);
    public List<GetByUserIdFavoriteSongs> getCommonFavoriteSongs(UUID userId, UUID followedId);
}