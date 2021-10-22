package hr.dhruza.utils.board;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WingsAndBombsBoardBuilder extends BoardBuilder {
  private final List<Integer> bombCellNumbs;
  private final List<ImageView> bombImages;

  private final List<Integer> wingsCellNumbs;
  private final List<ImageView> wingImages;

  public WingsAndBombsBoardBuilder(GridPane gp) {
    super(gp);
    this.bombImages = initBombImages();
    this.bombCellNumbs = initBombCellNumbs();
    this.wingImages = intiWingImages();
    this.wingsCellNumbs = initWingCellNumbs();
  }

  @Override
  void setCellLabel(VBox cell, int cellNum) {
    int cellBombIndex = bombCellNumbs.indexOf(cellNum);
    int cellWingIndex = wingsCellNumbs.indexOf(cellNum);

    if (cellBombIndex != -1) {
      cell.getChildren().add(bombImages.get(cellBombIndex));
      return;
    }

    if (cellWingIndex != -1) {
      cell.getChildren().add(wingImages.get(cellWingIndex));
      return;
    }

    Label fieldNumLabel = super.createCellLabel(cellNum);
    cell.getChildren().add(fieldNumLabel);
  }

  private List<Integer> initBombCellNumbs() {
    return Arrays.asList(10, 17, 62, 56, 45, 77, 70);
  }

  private List<Integer> initWingCellNumbs() {
    return Arrays.asList(8, 15, 30, 41, 50);
  }

  private List<ImageView> initBombImages() {
    List<ImageView> imageViews = new ArrayList<>();
    try {
      imageViews =
          Arrays.asList(
              createImageView(
                  getClass().getResource("/hr/dhruza/assets/icons/bomb_3.png").getPath()),
              createImageView(
                  getClass().getResource("/hr/dhruza/assets/icons/bomb_7.png").getPath()),
              createImageView(
                  getClass().getResource("/hr/dhruza/assets/icons/bomb_19.png").getPath()),
              createImageView(
                  getClass().getResource("/hr/dhruza/assets/icons/bomb_34.png").getPath()),
              createImageView(
                  getClass().getResource("/hr/dhruza/assets/icons/bomb_36.png").getPath()),
              createImageView(
                  getClass().getResource("/hr/dhruza/assets/icons/bomb_55.png").getPath()),
              createImageView(
                  getClass().getResource("/hr/dhruza/assets/icons/bomb_60.png").getPath()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return imageViews;
  }

  private List<ImageView> intiWingImages() {
    List<ImageView> imageViews = new ArrayList<>();
    try {
      imageViews =
          Arrays.asList(
              createImageView(
                  getClass().getResource("/hr/dhruza/assets/icons/wings_14.png").getPath()),
              createImageView(
                  getClass().getResource("/hr/dhruza/assets/icons/wings_31.png").getPath()),
              createImageView(
                  getClass().getResource("/hr/dhruza/assets/icons/wings_42.png").getPath()),
              createImageView(
                  getClass().getResource("/hr/dhruza/assets/icons/wings_65.png").getPath()),
              createImageView(
                  getClass().getResource("/hr/dhruza/assets/icons/wings_74.png").getPath()));

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return imageViews;
  }

  private ImageView createImageView(String path) throws FileNotFoundException {
    FileInputStream input = new FileInputStream(path);
    Image image = new Image(input);
    ImageView imageView = new ImageView(image);
    imageView.setFitHeight(50);
    imageView.setFitWidth(50);

    return imageView;
  }
}
