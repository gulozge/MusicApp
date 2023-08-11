package com.atmosware.musicapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private String category;
    @Column(columnDefinition = "TEXT")
    private String lyrics;

    @ManyToOne
    private Album album;
    @ManyToOne
    private Artist artist;

    @ManyToMany(mappedBy = "favoriteSongs")
    private Set<User> users = new HashSet<>();
}
