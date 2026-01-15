import org.NorseStar.app.Song;
import org.NorseStar.app.TagRepository;

import java.nio.file.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        final boolean backup = false;     // change whenever

        Scanner sc = new Scanner(System.in);

        java.util.logging.Logger.getLogger("org.jaudiotagger").setLevel(java.util.logging.Level.OFF);

        TagRepository tagRepo = new TagRepository(backup);

        Path currentDir = validDirectory(sc);
        while (true) {
            List<Path> entries = tagRepo.listPaths(currentDir);

            System.out.println("\nCurrent: " + currentDir);
            System.out.println("# (number)");
            System.out.println(".. (go up)");
            System.out.println("q (quit)");
            System.out.println();

            for (int i = 0; i < entries.size(); i++) {
                Path p = entries.get(i);
                String name = p.getFileName().toString();
                if (Files.isDirectory(p)) name = "[DIR] " + name;
                System.out.printf("%d) %s%n", i, name);
            }

            System.out.println("\nChoice (#, .., q): ");
            String cmd = sc.nextLine().trim();

            if (cmd.equalsIgnoreCase("q")) break;

            if (cmd.equals("..")) {
                Path parent = currentDir.getParent();
                if (parent != null) currentDir = parent;
                continue;
            }

            int index;
            try {
                index = Integer.parseInt(cmd);
            }  catch (NumberFormatException e) {
                System.out.println("Invalid command entered.");
                continue;
            }

            if (index < 0 || index >= entries.size()) {
                System.out.println("Out of range.");
                continue;
            }

            Path chosen = entries.get(index);

            if (Files.isDirectory(chosen)) {
                currentDir = chosen;
                continue;
            }

            if (!chosen.toString().toLowerCase().endsWith(".mp3")) {
                System.out.println("Not an MP3.");
                continue;
            }

            Song song = tagRepo.read(chosen);
            editSong(sc, tagRepo, song);

        }
    }

    private static Path validDirectory(Scanner sc) {
        while (true) {
            System.out.println("Enter music folder path (or q): ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("q")) System.exit(0);

            Path directory;
            try {
                directory = Paths.get(input);
            }  catch (InvalidPathException e) {
                System.out.println("Invalid path.");
                continue;
            }

            if (!Files.exists(directory)) {
                System.out.println("Path does not exist.");
                continue;
            }

            if (!Files.isDirectory(directory)) {
                System.out.println("Not a directory.");
                continue;
            }

            if (!Files.isReadable(directory)) {
                System.out.println("Directory is not readable.");
                continue;
            }
            return directory;
        }
    }

    private static void editSong(Scanner sc, TagRepository tagRepo, Song song) throws Exception {
        while (true) {
            System.out.println("\nEditing: " + song.getPath().getFileName());
            System.out.println("1) Title        : " + song.getTitle());
            System.out.println("2) Artist       : " + song.getArtist());
            System.out.println("3) Album        : " + song.getAlbum());
            System.out.println("4) Album Artist : " + song.getAlbumArtist());
            System.out.println("5) Track        : " + song.getTrackNumber());
            System.out.println("6) Disc Number  : " + song.getDiscNumber());
            System.out.println("7) Year         : " + song.getYear());
            System.out.println("8) Genre        : " + song.getGenre());
            System.out.println("9) Comment      : " + song.getComment());
            System.out.println("s) Save and back");
            System.out.println("b) Back (no save)");

            System.out.println("Choice: ");
            String choice = sc.nextLine().trim();

            if (choice.equalsIgnoreCase("b")) return;

            if (choice.equalsIgnoreCase("s")) {
                tagRepo.save(song);
                System.out.println("Song has been saved.");
                return;
            }

            int field;
            try {
                field = Integer.parseInt(choice);
            }  catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
                continue;
            }

            System.out.println("New value (blank to clear): ");
            String value = sc.nextLine().trim();

            switch (field) {
                case 1  -> song.setTitle(value);
                case 2  -> song.setArtist(value);
                case 3  -> song.setAlbum(value);
                case 4  -> song.setAlbumArtist(value);
                case 5  -> song.setTrackNumber(value);
                case 6  -> song.setDiscNumber(value);
                case 7  -> song.setYear(value);
                case 8  -> song.setGenre(value);
                case 9  -> song.setComment(value);
                default -> System.out.println("Out of range.");
            }
        }
    }
}