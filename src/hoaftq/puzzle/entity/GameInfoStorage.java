package hoaftq.puzzle.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class GameInfoStorage {
    private static final String PUZZLE_PROPERTIES_FILENAME_KEY = "Puzzle.properties";
    private static final String IMAGE_TYPE_KEY = "image-type";
    private static final String IMAGE_NAME_KEY = "image-name";
    private static final String ROW = "row";
    private static final String COLUMN = "column";

    private static final String IMAGE_TYPE_USE_NUMBER = "0";
    private static final String IMAGE_TYPE_USE_INTERNAL_IMAGE = "1";
    private static final String IMAGE_TYPE_USE_EXTERNAL_IMAGE = "2";

    private final GameInfoValidator validator;

    public GameInfoStorage(GameInfoValidator validator) {
        this.validator = validator;
    }

    /**
     * Save game information to properties file
     */
    public void save(GameInfo gameInfo) {
        var properties = createGameInfoProperties(gameInfo);

        // Save information to properties file
        try {
            properties.store(new FileOutputStream(PUZZLE_PROPERTIES_FILENAME_KEY),
                    IMAGE_NAME_KEY + ": image file name" + System.lineSeparator()
                    + IMAGE_TYPE_KEY + ": "
                    + IMAGE_TYPE_USE_NUMBER + "-use numbers, "
                    + IMAGE_TYPE_USE_INTERNAL_IMAGE + "-use default image, "
                    + IMAGE_TYPE_USE_EXTERNAL_IMAGE + "-use customized image" + System.lineSeparator()
                    + ROW + ": integer from 2 to 10" + System.lineSeparator()
                    + COLUMN + ": integer from 2 to 10");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load game information from properties file
     *
     * @return game information
     */
    public GameInfo get() {
        var properties = new Properties();
        try {
            properties.load(new FileInputStream(PUZZLE_PROPERTIES_FILENAME_KEY));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get image type and image file name
        var imageType = properties.getProperty(IMAGE_TYPE_KEY, IMAGE_TYPE_USE_INTERNAL_IMAGE);
        var imageFileName = properties.getProperty(IMAGE_NAME_KEY, "default.jpg");
        if (!List.of(IMAGE_TYPE_USE_NUMBER, IMAGE_TYPE_USE_INTERNAL_IMAGE, IMAGE_TYPE_USE_EXTERNAL_IMAGE)
                .contains(imageType)) {
            imageType = IMAGE_TYPE_USE_INTERNAL_IMAGE;
            imageFileName = "default.jpg";
        } else {
            if (IMAGE_TYPE_USE_EXTERNAL_IMAGE.equals(imageType) && !new File(imageFileName).exists()) {
                imageType = IMAGE_TYPE_USE_NUMBER;
            }
        }
        var usedImage = !IMAGE_TYPE_USE_NUMBER.equals(imageType);
        var puzzleImage = new PuzzleImage(imageFileName, IMAGE_TYPE_USE_INTERNAL_IMAGE.equals(imageType));

        var row = properties.getProperty(ROW);
        if (!validator.validateRowOrColumn(row)) {
            row = "4";
        }

        String column = properties.getProperty(COLUMN);
        if (!validator.validateRowOrColumn(column)) {
            column = "4";
        }

        return new GameInfo(
                usedImage,
                puzzleImage,
                Byte.parseByte(row),
                Byte.parseByte(column),
                EmptyTilePosition.BOTTOM_RIGHT);
    }

    private static Properties createGameInfoProperties(GameInfo gameInfo) {
        var properties = new Properties();
        String imageType;
        if (gameInfo.usedImage()) {
            properties.put(IMAGE_NAME_KEY, gameInfo.puzzleImage().getFileName());
            imageType = gameInfo.puzzleImage().isInternalResource()
                    ? IMAGE_TYPE_USE_INTERNAL_IMAGE
                    : IMAGE_TYPE_USE_EXTERNAL_IMAGE;
        } else {
            imageType = IMAGE_TYPE_USE_NUMBER;
        }
        properties.put(IMAGE_TYPE_KEY, imageType);
        properties.put(ROW, String.valueOf(gameInfo.row()));
        properties.put(COLUMN, String.valueOf(gameInfo.column()));
        return properties;
    }

}
