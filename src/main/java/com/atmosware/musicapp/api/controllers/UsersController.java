package com.atmosware.musicapp.api.controllers;

import com.atmosware.musicapp.business.abstracts.UserService;
import com.atmosware.musicapp.business.dto.requests.UserLoginRequest;
import com.atmosware.musicapp.business.dto.requests.UserRequest;
import com.atmosware.musicapp.business.dto.responses.GetByIdFavoriteSongs;
import com.atmosware.musicapp.business.dto.responses.SongResponse;
import com.atmosware.musicapp.business.dto.responses.UserResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UsersController {
    private final UserService service;

    @GetMapping
    public List<UserResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping("/registion")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse add(@Valid @RequestBody UserRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable UUID id, @Valid @RequestBody UserRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody UserLoginRequest request) {
        service.login(request);
    }

    @PostMapping("/{userId}/follow/{followId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void follow(@PathVariable UUID userId, @PathVariable UUID followId) {
        service.follow(userId, followId);
    }

    @DeleteMapping("/{userId}/unfollow{unfollowedId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void unfollow(@PathVariable UUID userId, @PathVariable UUID unfollowedId) {
        service.unfollow(userId, unfollowedId);
    }

    @GetMapping("/{userId}/following")
    public List<UserResponse> getFollowing(@PathVariable UUID userId) {
        return service.getFollowing(userId);
    }

    @PostMapping("/{userId}/favorites/{songId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSongToFavorites(@PathVariable UUID userId, @PathVariable UUID songId) {
         service.addSongToFavorites(userId,songId);
    }

    @DeleteMapping("/{userId}/favorites/{songId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSongFromFavorites(@PathVariable UUID userId, @PathVariable UUID songId) {
        service.removeSongFromFavorites(userId, songId);
    }

    @GetMapping("/{userId}/favoriteSongs")
    public List<GetByIdFavoriteSongs> getFavoriteSongs(@PathVariable UUID userId) {
        return service.getFavoriteSongs(userId);
    }

    @GetMapping("/{userId}/commonFavorites/{followedId}")
    public List<GetByIdFavoriteSongs> getCommonFavorites(@PathVariable UUID userId, @PathVariable UUID followedId) {
        return service.getCommonFavoriteSongs(userId, followedId);
    }

}