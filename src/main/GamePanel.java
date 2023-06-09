package main;

import entity.Entity;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS
    final int originalTileSize = 16; // = 16x16 tile format
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 16x3 = 48x48 tile size
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // FPS
    int FPS = 60;

    // SYSTEM INITIALIZATION
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound se = new Sound();
    Sound music = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;

    // ENTITY AND OBJECT INITIALIZATION
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];
    public Entity npc[] = new Entity[10];

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;


    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    // PREPARE ALL OBJECTS, NPCs, MUSIC, GAMESTATE ETC.
    public void setupGame() {

        aSetter.setObject();
        aSetter.setNPC();
        playMusic(0);
        stopMusic();
        gameState = titleState;
    }

    // START THE GAME THREAD ON WHICH THE GAME WILL RUN
    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

//    public void run() WITH THE SLEEP METHOD {
//
//        double drawInterval = 1000__000__000/FPS; // 0.0166667 refresh time
//        double nextDrawTime = System.nanoTime() + drawInterval;
//
//        while(gameThread != null) {
//
//            // 1: update information such as character positions
//            update();
//            // 2: draw the screen with that updated information
//            repaint();
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime /= 1000_000; // convert to ms for the sleep function
//
//                if(remainingTime < 0) {
//                    remainingTime = 0;
//                }
//
//                Thread.sleep((long)remainingTime);
//
//                nextDrawTime += drawInterval;
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    // DELTA METHOD FOR THE GAME LOOP THAT CALCULATES HOW OFTEN WE SHOULD UPDATE THE GAME
    public void run() {

        double drawInterval = 1000__000__000/FPS; // 0.0166667 seconds refresh time
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval; // How much time has passed ?
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if(delta >= 1) { // One equals the drawInterval here, so when it exceeds that it is refreshing time
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer >= 1000_000_000) {
                // System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    // CHECK WHETHER GAME IS PAUSED OR NOT -> IF NO PAUSE, UPDATE GAME
    public void update() {

        if(gameState == playState){

            player.update();
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null){

                    npc[i].update();
                }
            }
        }
        if(gameState == pauseState){
            // nothing
        }

    }

    // THIS FUNCTION DOES THE ACTUAL DRAWING OF IMAGES
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // TITLE SCREEN
        if(gameState == titleState) {

            ui.draw(g2);

        } else {

            // DRAW TILES
            tileM.draw(g2);

            // DRAW OBJECTS
            for (int i = 0; i < obj.length; i++){
                if(obj[i] != null){
                    obj[i].draw(g2, this);
                }
            }

            // DRAW NPC
            for(int i = 0; i < npc.length; i++){
                if(npc[i] != null) {
                    npc[i].draw(g2);
                }
            }

            // DRAW PLAYER
            player.draw(g2);

            // UI
            ui.draw(g2);
        }

        g2.dispose(); // dispose of this context and release resources that were used
    }

    // MUSIC FUNCTIONS
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic(){
        music.stop();
    }
    public void playSE(int i){
        se.setFile(i);
        se.play();
    }
}
