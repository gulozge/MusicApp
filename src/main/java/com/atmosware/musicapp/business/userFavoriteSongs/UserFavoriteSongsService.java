package com.atmosware.musicapp.business.userFavoriteSongs;

import com.atmosware.musicapp.business.song.GetByUserIdFavoriteSongs;
import com.atmosware.musicapp.business.user.UserResponse;
import com.atmosware.musicapp.entity.Song;
import com.atmosware.musicapp.entity.User;

import java.util.List;

public interface UserFavoriteSongsService {
   List<GetByUserIdFavoriteSongs> getAllByUserId(User user);
    void add(User user, Song song);
    void delete(User user,Song song);
}
