package pigeon;

import java.util.UUID;

public class User {
    private int id;
    private int image;
    private int writeTime;

    private static int currentImage = 0;
    private static final int MAX_IMAGES = 5;

    public User(int writeTime) {
        this.writeTime = writeTime;
        this.id = UUID.randomUUID().hashCode();
        this.image = (currentImage++) % MAX_IMAGES;
    }
}
