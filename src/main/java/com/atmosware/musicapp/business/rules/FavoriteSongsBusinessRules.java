package com.atmosware.musicapp.business.rules;

import com.atmosware.musicapp.common.constants.Messages;
import com.atmosware.musicapp.core.exceptions.BusinessException;
import com.atmosware.musicapp.entities.Song;
import com.atmosware.musicapp.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FavoriteSongsBusinessRules {


    public void checkIfAlreadyFavoriteSong(User user, Song song){
        boolean isAlreadyFavorited = user.getFavoriteSongs().stream()
                .anyMatch(favoriteSong -> favoriteSong.getId().equals(song.getId()));
        if (isAlreadyFavorited) {
            throw new BusinessException(Messages.FavoriteSong.Exists);
        }
    }
}
