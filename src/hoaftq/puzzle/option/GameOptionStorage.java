package hoaftq.puzzle.option;

import hoaftq.puzzle.common.PuzzleImage;
import hoaftq.puzzle.game.EmptyTilePosition;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class GameOptionStorage {
    private static final String PUZZLE_PROPERTIES_FILENAME_KEY = "Puzzle.properties";
    private static final String IMAGE_TYPE_KEY = "image-type";
    private static final String IMAGE_NAME_KEY = "image-name";
    private static final String ROW = "row";
    private static final String COLUMN = "column";

    private static final String IMAGE_TYPE_USE_NUMBER = "0";
    private static final String IMAGE_TYPE_USE_INTERNAL_IMAGE = "1";
    private static final String IMAGE_TYPE_USE_EXTERNAL_IMAGE = "2";

    private final GameOptionValidator validator;

    public GameOptionStorage(GameOptionValidator validator) {
        this.validator = validator;
    }

    /**
     * Save game information to properties file
     */
    public void save(GameOption gameOption) {
        var properties = createGameInfoProperties(gameOption);

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
    public GameOption get() {
        var properties = new Properties();
        try {
            properties.load(new FileInputStream(PUZZLE_PROPERTIES_FILENAME_KEY));
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get image type and image file name
        var imageType = properties.getProperty(IMAGE_TYPE_KEY, IMAGE_TYPE_USE_INTERNAL_IMAGE);
        var imageFileName = properties.getProperty(IMAGE_NAME_KEY, "default.jpg");
        if (isInvalidImageType(imageType)
            || (IMAGE_TYPE_USE_EXTERNAL_IMAGE.equals(imageType) && !new File(imageFileName).exists())) {
            imageType = IMAGE_TYPE_USE_INTERNAL_IMAGE;
            imageFileName = "default.jpg";
        }

        var usedImage = !IMAGE_TYPE_USE_NUMBER.equals(imageType);
        var puzzleImage = new PuzzleImage(imageFileName, IMAGE_TYPE_USE_INTERNAL_IMAGE.equals(imageType));

        var row = properties.getProperty(ROW);
        if (validator.isInvalidRowOrColumn(row)) {
            row = "4";
        }

        String column = properties.getProperty(COLUMN);
        if (validator.isInvalidRowOrColumn(column)) {
            column = "4";
        }

        return new GameOption(
                usedImage,
                puzzleImage,
                Byte.parseByte(row),
                Byte.parseByte(column),
                EmptyTilePosition.BOTTOM_RIGHT);
    }

    private static boolean isInvalidImageType(String imageType) {
        return !List.of(IMAGE_TYPE_USE_NUMBER, IMAGE_TYPE_USE_INTERNAL_IMAGE, IMAGE_TYPE_USE_EXTERNAL_IMAGE)
                .contains(imageType);
    }

    private static Properties createGameInfoProperties(GameOption gameOption) {
        var properties = new Properties();
        if (gameOption.usedImage()) {
            properties.put(IMAGE_NAME_KEY, gameOption.puzzleImage().fileName());
            var imageType = gameOption.puzzleImage().isInternalResource()
                    ? IMAGE_TYPE_USE_INTERNAL_IMAGE
                    : IMAGE_TYPE_USE_EXTERNAL_IMAGE;
            properties.put(IMAGE_TYPE_KEY, imageType);
        } else {
            properties.put(IMAGE_TYPE_KEY, IMAGE_TYPE_USE_NUMBER);
        }
        properties.put(ROW, String.valueOf(gameOption.row()));
        properties.put(COLUMN, String.valueOf(gameOption.column()));
        return properties;
    }

}
