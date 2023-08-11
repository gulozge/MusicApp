package com.atmosware.musicapp.business.userFavoriteSongs;

import com.atmosware.musicapp.business.song.GetByUserIdFavoriteSongs;
import com.atmosware.musicapp.constants.Messages;
import com.atmosware.musicapp.entity.Song;
import com.atmosware.musicapp.entity.User;
import com.atmosware.musicapp.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFavoriteSongsManager implements UserFavoriteSongsService {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public List<GetByUserIdFavoriteSongs> getAllByUserId(User user) {
        log.info("Successfully fetched favorite songs for User {}", user.getId());
        return user.getFavoriteSongs()
                .stream()
                .map(favoriteSong -> GetByUserIdFavoriteSongs
                        .builder()
                        .id(favoriteSong.getId())
                        .artistName(favoriteSong.getArtist().getFirstName())
                        .name(favoriteSong.getName())
                        .description(favoriteSong.getDescription())
                        .category(favoriteSong.getCategory())
                        .lyrics(favoriteSong.getLyrics())
                        .build())
                .toList();
    }

    @Override
    public void add(User user, Song song) {
        checkIfAlreadyFavoriteSong(user, song);
        user.getFavoriteSongs().add(song);

        String userFavoritesKey = "user_favorites";
        redisTemplate.opsForZSet().incrementScore(userFavoritesKey, user.getId().toString(), 1);
        String songFavoritesKey = "song_favorites";
        redisTemplate.opsForZSet().incrementScore(songFavoritesKey, song.getId().toString(), 1);
        log.info("Song {} successfully added to User {}'s favorites", song.getId(), user.getId());
    }

    @Override
    public void delete(User user, Song song) {
        user.getFavoriteSongs().remove(song);
        log.info("Song {} successfully removed from User {}'s favorites", song.getId(), user.getId());
    }
    private void checkIfAlreadyFavoriteSong(User user, Song song) {
        boolean isAlreadyFavorited = user.getFavoriteSongs().stream()
                .anyMatch(favoriteSong -> favoriteSong.getId().equals(song.getId()));
        if (isAlreadyFavorited) {
            log.error("Song {} is already a favorite for user {}", song.getId(), user.getId());
            throw new BusinessException(Messages.FavoriteSong.EXISTS);
        }
    }
}
