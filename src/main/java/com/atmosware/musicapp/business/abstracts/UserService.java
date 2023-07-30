package com.atmosware.musicapp.business.abstracts;


import com.atmosware.musicapp.business.dto.requests.UserLoginRequest;
import com.atmosware.musicapp.business.dto.requests.UserRequest;
import com.atmosware.musicapp.business.dto.responses.GetByIdFavoriteSongs;
import com.atmosware.musicapp.business.dto.responses.SongResponse;
import com.atmosware.musicapp.business.dto.responses.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAll();

    UserResponse getById(UUID id);

    UserResponse add(UserRequest request);

    UserResponse update(UUID id, UserRequest request);

    void delete(UUID id);

    void login(UserLoginRequest request);

    void follow(UUID userId, UUID followedId);

    void unfollow(UUID userId, UUID followedId);

    List<UserResponse> getFollowing(UUID userId);
    void addSongToFavorites(UUID userId, UUID songId);

    void removeSongFromFavorites(UUID userId, UUID songId);
    public List<GetByIdFavoriteSongs> getFavoriteSongs(UUID userId);
    public List<GetByIdFavoriteSongs> getCommonFavoriteSongs(UUID userId, UUID followedId);
}