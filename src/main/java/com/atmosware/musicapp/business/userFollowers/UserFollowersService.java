package com.atmosware.musicapp.business.userFollowers;

import com.atmosware.musicapp.business.user.UserResponse;
import com.atmosware.musicapp.entity.User;

import java.util.List;

public interface UserFollowersService {
   List<UserResponse > getAllByUserId(User user);
    void add(User user, User followUser);
    void delete(User user,User unFollowUser);
}
