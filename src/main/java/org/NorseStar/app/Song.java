package org.NorseStar.app;

import java.nio.file.Path;

public class Song {
    private final Path path;

    private String album;
    private String albumArtist;
    private String artist;
    private String comment;
    private String discNumber;
    private String genre;
    private String recordLabel;
    private String title;
    private String trackNumber;
    private String year;

    public Song(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public String getAlbum() {
        return album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }
    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDiscNumber() {
        return discNumber;
    }
    public void setDiscNumber(String discNumber) {
        this.discNumber = discNumber;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRecordLabel() {
        return recordLabel;
    }
    public void setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrackNumber() {
        return trackNumber;
    }
    public void setTrackNumber(String trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
}
