package hoaftq.puzzle.option;

import hoaftq.puzzle.common.PuzzleImage;

import java.io.*;
import java.util.List;

public class ImageListStorage {

    /**
     * Name of file containing image file names that user added
     */
    private static final String PUZZLE_DATA_FILENAME = "Puzzle.dat";

    /**
     * Save puzzle image list from list box to data file
     */
    public void savePuzzleImages(List<PuzzleImage> puzzleImageList) throws IOException {
        try (var stream = new ObjectOutputStream(new FileOutputStream(PUZZLE_DATA_FILENAME))) {
            stream.writeObject(puzzleImageList);
        }
    }

    /**
     * Load puzzle image list from saved data file
     *
     * @return puzzle image list
     */
    public List<PuzzleImage> loadPuzzleImages() {
        try (var ots = new ObjectInputStream(new FileInputStream(PUZZLE_DATA_FILENAME))) {

            //noinspection unchecked
            return (List<PuzzleImage>) ots.readObject();
        } catch (FileNotFoundException ignored) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return List.of();
    }
}
