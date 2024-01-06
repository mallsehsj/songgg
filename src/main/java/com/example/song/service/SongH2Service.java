/*
 * 
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here
package com.example.song.service;

import com.example.song.model.Song;

import com.example.song.model.SongRowMapper;
import com.example.song.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class SongH2Service implements SongRepository {

    @Autowired
    private JdbcTemplate db;

    @Override
    public ArrayList<Song> getAllSong() {
        List<Song> list = db.query("select * from playlist", new SongRowMapper());
        ArrayList<Song> arraylist = new ArrayList<>(list);
        return arraylist;
    }

    @Override
    public Song getById(int songId) {
        try {
            Song songh = db.queryForObject("select * from playlist where songId = ?", new SongRowMapper(), songId);
            return songh;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Song addSong(Song song) {
        db.update("insert into playlist(songName, lyricist, singer, musicDirector) values(?,?,?,?)",
                song.getSongName(), song.getLyricist(), song.getSinger(), song.getMusicDirector());
        return db.queryForObject(
                "select * from playlist where songName = ? and lyricist = ?",
                new SongRowMapper(), song.getSongName(), song.getLyricist());
    }

    @Override

    public void deleteSong(int songId) {
        db.update("delete from playlist where songId = ?", songId);

    }

    @Override
    public Song updateSong(int songId, Song song) {
        if (song.getSongName() != null) {
            db.update("update playlist set songName = ? where songId =?", song.getSongName(), songId);
        }
        if (song.getLyricist() != null) {
            db.update("update playlist set lyricist = ? where songId =?", song.getLyricist(), songId);
        }
        if (song.getSinger() != null) {
            db.update("update playlist set singer = ? where songId =?", song.getSinger(), songId);
        }
        if (song.getMusicDirector() != null) {
            db.update("update playlist set musicDirector = ? where songId =?", song.getMusicDirector(), songId);
        }
        try {
            return db.queryForObject(
                    "select * from playlist where songName = ? and lyricist = ?",
                    new SongRowMapper(), song.getSongName(), song.getLyricist());

        } catch (Exception s) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

}