package com.atmosware.musicapp.business.userFollowers;

import com.atmosware.musicapp.business.user.UserResponse;
import com.atmosware.musicapp.constants.Messages;
import com.atmosware.musicapp.entity.User;
import com.atmosware.musicapp.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserFollowersManager implements UserFollowersService {

    @Override
    public List<UserResponse> getAllByUserId(User user) {
        log.info("Successfully fetched the following list for User {}", user.getId());
        return user.getFollowing()
                .stream()
                .map(followedUser -> UserResponse
                        .builder()
                        .id(followedUser.getId())
                        .firstname(followedUser.getFirstname())
                        .lastname(followedUser.getLastname())
                        .userName(followedUser.getUsername())
                        .email(followedUser.getEmail())
                        .build())
                .toList();
    }

    @Override
    public void add(User user, User followUser) {
        checkIfAlreadyFollow(user, followUser);
        user.getFollowing().add(followUser);
        log.info("User {} successfully followed User {}", user.getId(), followUser.getId());
    }

    @Override
    public void delete(User user, User unFollowUser) {
        user.getFollowing().remove(unFollowUser);
        log.info("User {} successfully unfollowed User {}", user.getId(), unFollowUser.getId());
    }

    private void checkIfAlreadyFollow(User user, User followUser) {
        if (user.getFollowing().contains(followUser)) {
            log.warn("User {} is already following user {}", user.getId(), followUser.getId());
            throw new BusinessException(Messages.Follower.EXISTS);
        }
    }

}
