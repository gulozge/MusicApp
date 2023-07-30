package com.atmosware.musicapp.business.rules;


import com.atmosware.musicapp.common.constants.Messages;
import com.atmosware.musicapp.core.exceptions.BusinessException;
import com.atmosware.musicapp.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FollowerBusinessRules {
    public void checkIfAlreadyFollow(User user, User followUser) {
        if (user.getFollowing().contains(followUser)) {
            throw new BusinessException(Messages.Follower.Exists);
        }
    }

    public void checkIfUserAndFollowedSomePerson(UUID userId, UUID followedId) {
        if (userId.equals(followedId)) {
            throw new BusinessException(Messages.Follower.NotFollwed);
        }
    }
}