// Player.java
package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

public class Player extends Entity {


    GamePanel gamePanel;
    KeyHandler keyHandler;


    private int spriteCounter = 0;
    private int spriteNum = 1;
    private boolean restrictMovement = true;  // New field to control movement restriction

    private BufferedImage up1, up2, up3, down1, down2, down3;
    private BufferedImage left1, left2, left3, right1, right2, right3;
    private BufferedImage hpImage, speedImage;
    public final int screenX;
    public final int screenY;
    private Set<Integer> visitableTiles;
    private int hp;
    private int speed;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

        hp = 2;    // Set default HP to 5
        speed = 5; // Set default speed to 5

        setDefaultValues();
        getPlayerImage();
        initializeVisitableTiles();
        setRestrictMovement(true);
    }
    // Method to increase HP
    public void increaseHp(int amount) {
        int maxHp = 5; // Nilai maksimum HP
        while (amount > 0 && hp < maxHp) {
            hp += 1;
            amount -= 1;
        }
    }


    public void setDefaultValues() {
        worldX = gamePanel.tileSize * 23; // 23
        worldY = gamePanel.tileSize * 91; // 91
        speed = 5;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = loadImage("/res/player/tile004.png");
            up2 = loadImage("/res/player/tile005.png");
            up3 = loadImage("/res/player/tile006.png");
            down1 = loadImage("/res/player/tile000.png");
            down2 = loadImage("/res/player/tile001.png");
            down3 = loadImage("/res/player/tile002.png");
            left1 = loadImage("/res/player/tile012.png");
            left2 = loadImage("/res/player/tile013.png");
            left3 = loadImage("/res/player/tile014.png");
            right1 = loadImage("/res/player/tile008.png");
            right2 = loadImage("/res/player/tile009.png");
            right3 = loadImage("/res/player/tile010.png");
            hpImage = loadImage("/res/player/hp.png"); // Gambar untuk HP
            speedImage = loadImage("/res/player/speed.png"); // Gambar untuk kecepatan
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1); // Exit if images are not found
        }
    }

    private BufferedImage loadImage(String path) throws IOException {
        BufferedImage img = ImageIO.read(getClass().getResourceAsStream(path));
        if (img == null) {
            throw new IOException("Image not found: " + path);
        }
        return img;
    }

    private void initializeVisitableTiles() {
        visitableTiles = Set.of(
                2051, 2096, 2135, 2136, 2137, 2139, 2140, 2141, 2143, 2144,
                2147, 2148, 2150, 2151, 2183, 2184, 2188, 2189, 2192,
                2193, 2194, 2228, 2229, 2230, 2233, 2234, 2236, 2237, 2238, 2269, 2273,
                2274, 2275, 2276, 2277, 2280, 2284, 2307, 2308, 2309, 2310, 2311, 2312, 2314,
                2315, 2316, 2319, 2320, 2321, 2322, 2323, 2324, 2325, 2326,
                2327, 2328, 2329, 2330, 2331, 2332, 2335, 2336, 2337, 2352, 2356, 2369,
                2372, 2376, 2380, 2381, 2397, 2398, 2399,
                2401, 2402, 2403, 2405, 2406,
                2417, 2421, 2422, 2425, 2426, 2427, 2442, 2443,
                2444, 2445, 2460, 2486, 2487, 2488, 2489, 2494, 2495, 2532,
                2542, 2577, 2587, 2900, 2901, 2902, 2903, 2904, 2905, 2906,
                2907, 2908, 2909, 2910, 2911, 2946, 2948, 2949, 3206,
                3209, 3251, 3252, 3253, 3297, 3298, 3311, 3312, 3342, 3343,
                3398, 3443, 3482, 3483, 3484, 3485, 3486, 3487, 3488, 3716,
                3749, 3750, 3751, 3752, 3753, 3754, 3761, 3798, 3799, 3800,
                3801, 3802, 3803, 3804, 3805, 3806, 3851, 3852
        );
    }

    public void update() {

        boolean moving = false;

        if (keyHandler.upPressed) {
            if (canMove(worldX, worldY - speed)) {
                direction = "up";
                worldY -= speed;
                moving = true;
            }
        }
        if (keyHandler.downPressed) {
            if (canMove(worldX, worldY + speed)) {
                direction = "down";
                worldY += speed;
                moving = true;
            }
        }
        if (keyHandler.leftPressed) {
            if (canMove(worldX - speed, worldY)) {
                direction = "left";
                worldX -= speed;
                moving = true;
            }
        }
        if (keyHandler.rightPressed) {
            if (canMove(worldX + speed, worldY)) {
                direction = "right";
                worldX += speed;
                moving = true;
            }
        }

        if (moving) {
            // Check for HP increasing tile
            int currentTileCode = gamePanel.getTileManager().getPlayerTileCode();
            if (gamePanel.getTileManager().isHpIncreasingTile(currentTileCode)) {
                increaseHp(10); // Increase HP by a specified amount
            }
            spriteCounter++;
            if (spriteCounter > 10) {
                spriteNum++;
                if (spriteNum > 3) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        } else {
            spriteNum = 1; // Reset to default position when not moving
        }
    }

    private boolean canMove(int x, int y) {
        if (!restrictMovement) {
            return true;
        }
        int tileX = x / gamePanel.tileSize;
        int tileY = y / gamePanel.tileSize;
        int tileCode = gamePanel.getTileManager().mapTileNum[tileX][tileY];
        return gamePanel.getTileManager().isTileVisitable(tileCode);
    }
    // Method to decrease HP
    public void decreaseHp(int amount) {
        hp -= amount;
        if (hp < 0) hp = 0;
        // Add logic to handle player death if needed
    }

    // Method to decrease speed
    public void decreaseSpeed(int amount) {
        speed -= amount;
        if (speed < 1) speed = 1; // Minimum speed
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                } else if (spriteNum == 2) {
                    image = up2;
                } else if (spriteNum == 3) {
                    image = up3;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                } else if (spriteNum == 2) {
                    image = down2;
                } else if (spriteNum == 3) {
                    image = down3;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                } else if (spriteNum == 2) {
                    image = left2;
                } else if (spriteNum == 3) {
                    image = left3;
                }
                break;
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                } else if (spriteNum == 2) {
                    image = right2;
                } else if (spriteNum == 3) {
                    image = right3;
                }
                break;
        }

        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        float aspectRatio = (float) imgWidth / imgHeight;

        int newWidth = gamePanel.tileSize;
        int newHeight = (int) (gamePanel.tileSize / aspectRatio);

        g2.drawImage(image, screenX, screenY, newWidth, newHeight, null);
        // Draw HP and Speed representations
        drawStatus(g2);
    }

    private void drawStatus(Graphics2D g2) {
        int smallerTileSize = gamePanel.tileSize / 2; // Ukuran gambar yang lebih kecil dua kali lipat

        // Draw HP representation
        int hpX = 10;
        int hpY = 10;
        for (int i = 0; i < hp; i++) {
            g2.drawImage(hpImage, hpX + (i * smallerTileSize), hpY, smallerTileSize, smallerTileSize, null);
        }

        // Draw Speed representation
        int speedX = 10;
        int speedY = 10 + smallerTileSize + 10;
        for (int i = 0; i < speed; i++) {
            g2.drawImage(speedImage, speedX + (i * smallerTileSize), speedY, smallerTileSize, smallerTileSize, null);
        }
    }

    public void setRestrictMovement(boolean restrictMovement) {  // New method to enable/disable movement restriction
        this.restrictMovement = restrictMovement;
    }

    public boolean isRestrictMovement() {  // New method to check the current state of movement restriction
        return restrictMovement;
    }
}
