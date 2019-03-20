package br.edu.ifce.pigeon.ui;

import br.edu.ifce.pigeon.presenters.MainPresenter;
import br.edu.ifce.pigeon.views.IMainWindow;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class MainWindow implements IMainWindow {
    private final Parent root;
    private final MainPresenter presenter = new MainPresenter(this);

    //Pigeon Frames
    private final ArrayList<Image> left = new ArrayList<>();
    private final ArrayList<Image> right = new ArrayList<>();

    //Components
    private final ImageView imageView;
    private final JFXDrawer navigationDrawer;
    private final HamburgerSlideCloseTransition transition;
    private final JFXButton hirePigeonBtn;
    private final JFXButton firePigeonBtn;

    public MainWindow() throws IOException {
        root = Component.load("main_screen.fxml");
        Node menu = Component.load("navigation_menu.fxml");

        JFXHamburger hamburgerBtn = (JFXHamburger) root.lookup("#menu-btn");
        JFXButton addUserBtn = (JFXButton) menu.lookup("#add-user-btn");
        imageView = (ImageView) root.lookup("#image-view-pigeon");
        navigationDrawer = (JFXDrawer) root.lookup("#navigation-drawer");
        hirePigeonBtn = (JFXButton) menu.lookup("#hire-pigeon-btn");
        firePigeonBtn = (JFXButton) menu.lookup("#fire-pigeon-btn");
        transition = new HamburgerSlideCloseTransition(hamburgerBtn);

        transition.setRate(-1);
        navigationDrawer.setSidePane(menu);
        hamburgerBtn.setOnMouseClicked(e -> presenter.onClickMenuBtn());
        hirePigeonBtn.setOnMouseClicked(e -> presenter.onClickHirePigeonBtn());
        firePigeonBtn.setOnMouseClicked(e -> presenter.onClickFirePigeonBtn());
        addUserBtn.setOnMouseClicked(e -> presenter.onClickAddUserBtn());

        presenter.onLoadView();
    }

    @Override
    public void toggleMenu() {
        transition.setRate(transition.getRate() * -1);
        transition.play();

        if (navigationDrawer.isClosed()) {
            navigationDrawer.open();
        } else {
            navigationDrawer.close();
        }
    }

    @Override
    public void loadPigeonFrames(int framesCount) {
        for (int i = 1; i <= framesCount; i++) {
            try {
                BufferedImage image = Component.loadImageBuffer(String.format("pigeon/pigeon_frame0%d.png", i));
                right.add(Component.loadImage(image));
                left.add(Component.loadImage(flipImage(image)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setPigeonFrame(int frame, PigeonFacingDirection direction) {
        Image pigeonFrame;
        switch (direction) {
            default:
            case LEFT:
                pigeonFrame = left.get(frame);
                break;
            case RIGHT:
                pigeonFrame = right.get(frame);
                break;
        }

        this.imageView.setImage(pigeonFrame);
    }

    private static BufferedImage flipImage(BufferedImage image) {
        BufferedImage mirrored = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pivot = image.getWidth() - x - 1;
                int pixel = image.getRGB(x, y);
                mirrored.setRGB(pivot, y, pixel);
            }
        }
        return mirrored;
    }

    @Override
    public void openCreatePigeonModal() {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(Component.load("pigeon_constructor.fxml")));
            stage.setTitle("Contratar pombo");
            stage.setResizable(false);
            stage.setIconified(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openCreateUserModal() {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(Component.load("add_user.fxml")));
            stage.setTitle("Adicionar pombo");
            stage.setResizable(false);
            stage.setIconified(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void firePigeon() {
        //TODO
    }

    @Override
    public void setHirePigeonDisable(boolean disabled) {
        this.hirePigeonBtn.setDisable(disabled);
    }

    @Override
    public void setFirePigeonDisable(boolean disabled) {
        this.firePigeonBtn.setDisable(disabled);
    }

    public Parent getRoot() {
        return this.root;
    }
}