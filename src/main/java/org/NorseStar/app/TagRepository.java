package org.NorseStar.app;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public final class TagRepository {
    private final boolean backup;

    public TagRepository(boolean backup) {
        this.backup = backup;
    }

    public List<Path> listPaths(Path directoryPath) throws IOException {
        List<Path> paths = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {
            for (Path path : stream) {
                if (Files.isDirectory(path) || path.toString().toLowerCase().endsWith(".mp3")) paths.add(path);
            }
        }
        paths.sort((a, b) -> a.getFileName().toString().compareToIgnoreCase(b.getFileName().toString()));
        return paths;
    }

    public Song read(Path mp3Path) {
        Song song = new Song(mp3Path);
        try {
            MP3File mp3 = (MP3File) AudioFileIO.read(mp3Path.toFile());
            Tag tag = mp3.getTag();
            if (tag == null) return song;

            song.setAlbum(notNull(tag.getFirst(FieldKey.ALBUM)));
            song.setAlbumArtist(notNull(tag.getFirst(FieldKey.ALBUM_ARTIST)));
            song.setArtist(notNull(tag.getFirst(FieldKey.ARTIST)));
            song.setComment(notNull(tag.getFirst(FieldKey.COMMENT)));
            song.setDiscNumber(notNull(tag.getFirst(FieldKey.DISC_NO)));
            song.setGenre(notNull(tag.getFirst(FieldKey.GENRE)));
            song.setRecordLabel(notNull(tag.getFirst(FieldKey.RECORD_LABEL)));
            song.setTitle(notNull(tag.getFirst(FieldKey.TITLE)));
            song.setTrackNumber(notNull(tag.getFirst(FieldKey.TRACK)));
            song.setYear(notNull(tag.getFirst(FieldKey.YEAR)));
            return song;

        } catch (Exception e) { // don't care
            return song;
        }
    }

    public void save(Song song) throws Exception {
        if (backup) backup(song.getPath());

        MP3File mp3 = (MP3File) AudioFileIO.read(song.getPath().toFile());
        Tag tag = mp3.getTagOrCreateAndSetDefault();

        set(tag, FieldKey.ALBUM, song.getAlbum());
        set(tag, FieldKey.ALBUM_ARTIST, song.getAlbumArtist());
        set(tag, FieldKey.ARTIST, song.getArtist());
        set(tag, FieldKey.COMMENT, song.getComment());
        set(tag, FieldKey.DISC_NO, song.getDiscNumber());
        set(tag, FieldKey.GENRE, song.getGenre());
        set(tag, FieldKey.RECORD_LABEL, song.getRecordLabel());
        set(tag, FieldKey.TITLE, song.getTitle());
        set(tag, FieldKey.TRACK, song.getYear());
        set(tag, FieldKey.YEAR, song.getYear());

        mp3.commit();
    }

    private static void set(Tag tag, FieldKey key, String value) throws Exception {
        if (value == null) return;
        String v = value.trim();
        if (v.isEmpty()) return;
        tag.setField(key, v);
    }

    public void backup(Path path) throws IOException {
        Path bak = path.resolveSibling(path.getFileName() + ".bak");
        Files.copy(path, bak, StandardCopyOption.REPLACE_EXISTING);
    }

    private static String notNull(String s) {
        return s == null ? "" : s;
    }
}
