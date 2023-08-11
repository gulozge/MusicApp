package com.atmosware.musicapp.api;

import com.atmosware.musicapp.business.admin.LoginResponse;
import com.atmosware.musicapp.business.user.UserService;
import com.atmosware.musicapp.business.user.UserLoginRequest;
import com.atmosware.musicapp.business.user.UserRequest;
import com.atmosware.musicapp.business.song.GetByUserIdFavoriteSongs;
import com.atmosware.musicapp.business.user.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    @GetMapping("/getAll")
    public List<UserResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/getById/{id}")
    public UserResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping("/registion")
    public UserResponse add(@Valid @RequestBody UserRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id.equals(authentication.principal.id)")
    public UserResponse update(@PathVariable UUID id, @Valid @RequestBody UserRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#id.equals(authentication.principal.id)")
    void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody UserLoginRequest request) {
      return   service.login(request);
    }
    @PostMapping("/logout")
    void logout(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization").substring(7);
        service.logout(authorizationHeader);
    }
    @PostMapping("/{userId}/follow/{followId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void follow(@PathVariable UUID userId, @PathVariable UUID followId) {
        service.follow(userId, followId);
    }

    @DeleteMapping("/{userId}/unfollow/{unfollowedId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    void unfollow(@PathVariable UUID userId, @PathVariable UUID unfollowedId) {
        service.unfollow(userId, unfollowedId);
    }

    @GetMapping("/{userId}/following")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public List<UserResponse> getFollowing(@PathVariable UUID userId) {
        return service.getFollowing(userId);
    }

    @PostMapping("/{userId}/favorites/{songId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void addSongToFavorites(@PathVariable UUID userId, @PathVariable UUID songId) {
         service.addSongToFavorites(userId,songId);
    }

    @DeleteMapping("/{userId}/removeSongFromFavorites/{songId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public void removeSongFromFavorites(@PathVariable UUID userId, @PathVariable UUID songId) {
        service.removeSongFromFavorites(userId, songId);
    }

    @GetMapping("/{userId}/favoriteSongs")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public List<GetByUserIdFavoriteSongs> getFavoriteSongs(@PathVariable UUID userId) {
        return service.getFavoriteSongs(userId);
    }

    @GetMapping("/{userId}/commonFavorites/{followedId}")
    @PreAuthorize("#userId.equals(authentication.principal.id)")
    public List<GetByUserIdFavoriteSongs> getCommonFavorites(@PathVariable UUID userId, @PathVariable UUID followedId) {
        return service.getCommonFavoriteSongs(userId, followedId);
    }
}