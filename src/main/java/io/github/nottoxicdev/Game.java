package io.github.nottoxicdev;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.prefs.Preferences;

public class Game extends Canvas implements Runnable {
    // dev v: 15 | beta v: 1
    public static String v = GameMeta.ConstructGameMeta("NTDWave", 1, 0, GameMeta.TAG.dev, 15);
    public static String m = "";
    // Construct mod
    // m = GameMeta.ConstructModMeta("modName", 1, 0, GameMeta.TAG.dev, 1);

    public static final int WIDTH = 1080, HEIGHT = WIDTH / 12 * 9;

    private Thread thread;
    private boolean running = false;

    public static boolean paused = false;

    private Handler handler;
    private HUD hud;
    private Spawn spawner;
    private Menu menu;
    private Upgrades upgrades;
    private Random r;

    public static boolean showCollisionBoxes = false;

    public enum STATE {
        Menu,
        Game,
        End
    };

    public static STATE gameState = STATE.Menu;
    public static String title = "";

    public static BufferedImage sprite_sheet;

    public static boolean hasWon = false;

    public static Preferences prefs = Preferences.userRoot().node("/io/github/nottoxicdev");

    public Game() {
        BufferedImageLoader loader = new BufferedImageLoader();
        sprite_sheet = loader.loadImage("/res/NTDWave-Spritesheet.png");
        handler = new Handler();
        upgrades = new Upgrades();

        hud = new HUD();
        menu = new Menu(handler, hud);

        this.addKeyListener(new KeyInput(handler, upgrades));
        this.addMouseListener(menu);

        if (m == "") {
            title = v;
        } else {
            title = v + " | " + m;
        }
        new Window(WIDTH, HEIGHT, title, this);

        Menu.selectedSkin = prefs.getInt("skin", 0);

        spawner = new Spawn(handler, hud, upgrades);
        r = new Random();

        if (gameState == STATE.Game) {
            handler.addObject(new Player((float) WIDTH / 2 - 32, (float) HEIGHT / 2 - 32, ID.Player, handler));
        } else {
            for (int i = 0; i < 20; i++) {

                handler.addObject(new MenuParticle(r.nextFloat(Spawn.fixedWidth),
                        r.nextFloat(Spawn.fixedHeight), ID.MenuParticle, GroupID.Effect,
                        handler));
            }

        }
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() {
        // gameloop
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running)
                render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                // System.out.println(frames);
                frames = 0;
            }

        }
        stop();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.darkGray);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        handler.render(g);
        if (paused) {
            g.setFont(HUD.font);
            g.setColor(Color.RED);
            g.drawString("PAUSED", 100, 100);
        }
        if (gameState == STATE.Game) {
            hud.render(g);

        } else if (gameState == STATE.Menu || gameState == STATE.End) {
            menu.render(g);
        }
        g.dispose();
        bs.show();

    }

    private void tick() {
        if (!paused) {
            if (gameState == STATE.Game) {
                handler.tick();
                hud.tick();
                spawner.tick();
                upgrades.tick();

                if (HUD.HEALTH <= 0) {
                    HUD.HEALTH = 100;
                    upgrades.object.clear();
                    gameState = STATE.End;

                    handler.clearEnemies();
                    for (int i = 0; i < 20; i++) {

                        handler.addObject(new MenuParticle(r.nextFloat(Spawn.fixedWidth),
                                r.nextFloat(Spawn.fixedHeight), ID.MenuParticle, GroupID.Effect,
                                handler));
                    }

                }

                if (hasWon) {
                    HUD.HEALTH = 100;
                    gameState = STATE.End;

                    handler.clearEnemies();
                    for (int i = 0; i < 20; i++) {

                        handler.addObject(new MenuParticle(r.nextFloat(Spawn.fixedWidth),
                                r.nextFloat(Spawn.fixedHeight), ID.MenuParticle, GroupID.Effect,
                                handler));
                    }
                }
            } else if (gameState == STATE.Menu || gameState == STATE.End) {
                menu.tick();
                handler.tick();
            }
        }
    }

    public static float clamp(Float var, Float min, Float max) {
        if (var >= max) {
            return var = max;
        } else if (var <= min) {
            return var = min;
        } else {
            return var;
        }
    }

    public static void main(String[] args) {
        new Game();
    }
}