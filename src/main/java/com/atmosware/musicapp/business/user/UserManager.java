package com.atmosware.musicapp.business.user;


import com.atmosware.musicapp.business.admin.LoginResponse;
import com.atmosware.musicapp.business.song.GetByUserIdFavoriteSongs;
import com.atmosware.musicapp.business.song.SongResponse;
import com.atmosware.musicapp.business.song.SongService;
import com.atmosware.musicapp.business.userFavoriteSongs.UserFavoriteSongsService;
import com.atmosware.musicapp.business.userFollowers.UserFollowersService;
import com.atmosware.musicapp.constants.Messages;
import com.atmosware.musicapp.entity.Song;
import com.atmosware.musicapp.entity.User;
import com.atmosware.musicapp.entity.enums.Role;
import com.atmosware.musicapp.exception.BusinessException;
import com.atmosware.musicapp.repository.UserRepository;
import com.atmosware.musicapp.business.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManager implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final SongService songService;
    private final TokenService tokenService;
    private final UserFollowersService userFollowersService;
    private final UserFavoriteSongsService userFavoriteSongsService;
    private final AuthenticationManager authenticationManager;


    @Override
    public List<UserResponse> getAll() {
        List<UserResponse> users = repository.findAll()
                .stream()
                .map(user -> UserResponse
                        .builder()
                        .id(user.getId())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .userName(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .toList();
        log.info("Found {} albums", users.size());
        return users;
    }

    @Override
    public UserResponse getById(UUID id) {
        User user = repository.findById(id).orElseThrow();
        log.info("Found user with id: {}", id);
        return UserResponse
                .builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .userName(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    public UserResponse add(UserRequest request) {
        log.info("Adding new user with userName: {}...", request.getUserName());
        checkIfUserNameExists(request.getUserName());
        User user = new User();
        user.setId(null);
        return saveAndReturnResponse(request, user);
    }

    @Override
    public UserResponse update(UUID id, UserRequest request) {
        log.info("Updating user with id: {}...", id);
        checkIfUserExists(id);
        checkIfUserNameExists(request.getUserName());
        User user = new User();
        user.setId(id);
        return saveAndReturnResponse(request, user);
    }

    @Override
    public void delete(UUID id) {
        log.warn("Deleting user with id: {}...", id);
        checkIfUserExists(id);
        repository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
    }

    public LoginResponse login(UserLoginRequest request) {
        checkAuth(request.getUserName(), request.getPassword());
        log.info("User login successfully with userName: {}", request.getUserName());
        String token = generateToken(request.getUserName());
        tokenService.add(token);
        return new LoginResponse(token);
    }

    @Override
    public void logout(String token) {
      tokenService.delete(token);
    }

    @Override
    @Transactional
    public void follow(UUID userId, UUID followedId) {
        log.info("Attempting to follow: User {} -> User {}", userId, followedId);
        checkIfUserExists(userId);
        checkIfUserExists(followedId);
        checkIfUserAndFollowedSomePerson(userId, followedId);
        User user = repository.findById(userId).orElseThrow();
        User followUser = repository.findById(followedId).orElseThrow();
        userFollowersService.add(user, followUser);

    }

    @Override
    @Transactional
    public void unfollow(UUID userId, UUID followedId) {
        log.warn("Attempting to unfollow: User {} -> User {}", userId, followedId);
        checkIfUserExists(userId);
        checkIfUserExists(followedId);
        User user = repository.findById(userId).orElseThrow();
        User unfollowUser = repository.findById(followedId).orElseThrow();
        userFollowersService.delete(user, unfollowUser);
    }

    @Override
    public List<UserResponse> getFollowing(UUID userId) {
        checkIfUserExists(userId);
        User user = repository.findById(userId).orElseThrow();
        return userFollowersService.getAllByUserId(user);
    }

    @Override
    @Transactional
    public void addSongToFavorites(UUID userId, UUID songId) {
        log.info("User {} is adding Song {} to favorites", userId, songId);
        checkIfUserExists(userId);
        User user = repository.findById(userId).orElseThrow();
        SongResponse song = songService.getById(songId);
        Song convertSong = songService.mapToSong(song);
        userFavoriteSongsService.add(user, convertSong);
    }

    @Override
    @Transactional
    public void removeSongFromFavorites(UUID userId, UUID songId) {
        log.warn("User {} is removing Song {} from favorites", userId, songId);
        User user = repository.findById(userId).orElseThrow();
        SongResponse song = songService.getById(songId);
        Song convertSong = songService.mapToSong(song);
        userFavoriteSongsService.delete(user, convertSong);
    }

    @Override
    public List<GetByUserIdFavoriteSongs> getFavoriteSongs(UUID userId) {
        checkIfUserExists(userId);
        User user = repository.findById(userId).orElseThrow();
        return userFavoriteSongsService.getAllByUserId(user);
    }

    @Override
    public List<GetByUserIdFavoriteSongs> getCommonFavoriteSongs(UUID userId, UUID followedId) {
        checkIfUserExists(userId);
        checkIfUserExists(followedId);
        List<GetByUserIdFavoriteSongs> userFavoriteSongs = getFavoriteSongs(userId);
        List<GetByUserIdFavoriteSongs> followedUserFavoriteSongs = getFavoriteSongs(followedId);

        List<GetByUserIdFavoriteSongs> commonFavoriteSongs = userFavoriteSongs
                .stream()
                .filter(followedUserFavoriteSongs::contains)
                .toList();
        log.info("Successfully fetched common favorite songs between User {} and User {}", userId, followedId);
        return commonFavoriteSongs;
    }

    private UserResponse saveAndReturnResponse(UserRequest request, User user) {
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        repository.save(user);
        log.info("{} successfully with id: {}", user.getId() == null ? "User added" : "User updated", user.getId());
        return UserResponse
                .builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .userName(user.getUsername())
                .email(user.getEmail())
                .build();
    }


    private void checkIfUserAndFollowedSomePerson(UUID userId, UUID followedId) {
        if (userId.equals(followedId)) {
            log.error("User {} is trying to follow themselves.", userId);
            throw new BusinessException(Messages.Follower.NOT_FOLLWED);
        }
    }

    private void checkIfUserExists(UUID id) {
        if (!repository.existsById(id)) {
            log.error("User {} not found.", id);
            throw new BusinessException(Messages.User.NOT_FOUND);
        }
    }

    private void checkIfUserNameExists(String email) {
        if (repository.existsByUserName(email)) {
            log.error("Username {} already exists.", email);
            throw new BusinessException(Messages.User.EXISTS);
        }
    }

    private void checkAuth(String email, String pasword) {
        log.debug("Authenticating user with email {}.", email);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, pasword));
    }

    private String generateToken(String userName) {
        User user = repository.findByUserName(userName);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        log.info("Token generated for username {}.", userName);
        return tokenService.generateToken(extraClaims, user);
    }
}