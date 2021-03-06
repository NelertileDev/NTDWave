package io.github.nottoxicdev;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import io.github.nottoxicdev.Game.STATE;

public class KeyInput extends KeyAdapter {

    private Handler handler;
    private Upgrades upgrades;
    private boolean[] keyDown = new boolean[4];

    public KeyInput(Handler handler, Upgrades upgrades) {
        this.handler = handler;
        this.upgrades = upgrades;

        keyDown[0] = false;
        keyDown[1] = false;
        keyDown[2] = false;
        keyDown[3] = false;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (Game.gameState == STATE.Game) {
                if (key == KeyEvent.VK_ESCAPE) {
                    if (Game.paused) {
                        Game.paused = false;
                    } else {
                        Game.paused = true;
                    }
                }
            }

            if (tempObject.getID() == ID.Player) {
                for (int j = 0; j < upgrades.object.size(); j++) {
                    Upgrade tempUpgrade = upgrades.object.get(j);

                    if (tempUpgrade.getID() == UpgradeID.Heal) {
                        if (key == KeyEvent.VK_E) {
                            Heal.healing = true;
                        }
                    }
                }
                if (key == KeyEvent.VK_F3) {
                    if (Game.showCollisionBoxes) {
                        Game.showCollisionBoxes = false;
                    } else {
                        Game.showCollisionBoxes = true;

                    }
                }
                // key events for player 1
                if (key == KeyEvent.VK_W) {
                    tempObject.setVelY(-6f);
                    keyDown[0] = true;
                } else if (key == KeyEvent.VK_S) {
                    tempObject.setVelY(6f);
                    keyDown[1] = true;

                } else if (key == KeyEvent.VK_D) {
                    tempObject.setVelX(6f);
                    keyDown[2] = true;

                } else if (key == KeyEvent.VK_A) {
                    tempObject.setVelX(-6f);
                    keyDown[3] = true;

                }
            }

        }

    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);

            if (tempObject.getID() == ID.Player) {
                // key events for player 1
                if (key == KeyEvent.VK_W) {
                    // tempObject.setVelY(0);
                    keyDown[0] = false;
                } else if (key == KeyEvent.VK_S) {
                    // tempObject.setVelY(0);
                    keyDown[1] = false;

                } else if (key == KeyEvent.VK_D) {
                    // tempObject.setVelX(0);
                    keyDown[2] = false;

                } else if (key == KeyEvent.VK_A) {
                    // tempObject.setVelX(0);
                    keyDown[3] = false;

                }

                if (!keyDown[0] && !keyDown[1])
                    tempObject.setVelY(0f);
                if (!keyDown[2] && !keyDown[3])
                    tempObject.setVelX(0f);
            }

        }
    }

}
