import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class SIPanel extends JPanel {
    /**
     * Evan Rose
     */
    private static final long serialVersionUID = 1L;
    private static final int panelWidth = 494, panelHeight = 398;
    private int baseX = 240, baseY = 380, score, invaderPace = 40, tick,
            iMCount = 0, iDir = 1, deathTimer, lastBottom, mDir = 1;

    private boolean right, left, queueNewWave = false, startText = true,
            gameOver = false, resetQueue = false, mystery = false;
    private SIbase base;
    private SImissile bMissile, iM1, iM2, iM3;
    private ArrayList<SItop> tops;
    private ArrayList<SImiddle> mids;
    private ArrayList<SIbottom> bottoms;
    private ArrayList<SIinvader> bottomRow;
    private SIinvader bottomShip, blownUp;
    private SImystery mysteryShip;

    private Timer timer;

    public SIPanel() {
        setBackground(Color.BLACK);
        setLayout(new FlowLayout());
        right = left = false;
        base = new SIbase(baseX, baseY, 26, 20);
        mysteryShip = new SImystery(-40, 50, 40, 20);
        
        bMissile = new SImissile(-2, -10, 2, 10);
        iM1 = new SImissile(-2, -10, 2, 10);
        iM2 = new SImissile(-2, -10, 2, 10);
        iM3 = new SImissile(-2, -10, 2, 10);

        setFocusable(true);

        tops = new ArrayList<SItop>();
        mids = new ArrayList<SImiddle>();
        bottoms = new ArrayList<SIbottom>();
        bottomRow = new ArrayList<SIinvader>();
        
        blownUp = null;

        makeWave();
        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (right) {
                    if (base.getX() < (494 - base.getWidth())) {
                        base.moveXBy(5);
                    }
                }
                if (left) {
                    if (base.getX() > 0) {
                        base.moveXBy(-5);
                    }
                }
                step();
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        left = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        right = true;
                        break;
                    case KeyEvent.VK_ENTER:
                        if(!gameOver) {
                        startText = false;
                        timer.start();
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        if (bMissile.isLoaded() == true) {
                            bMissile.setX(base.getX() + (base.getWidth() / 2));
                            bMissile.setY(base.getY() - 10);
                            base.shoot();
                            bMissile.unload();
                        }
                }
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        left = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        right = false;
                        break;
                }
            }
        });
    }

    // Pulse---------------------------------------------------------------------------------------------------------------------------------------------------------------Pulse
    private void step() {
        tick++;
        if (deathTimer > 0) {
            deathTimer--;
            if(deathTimer == 0 && blownUp != null) {
                if(bottoms.contains(blownUp)) {
                    bottoms.remove(bottoms.indexOf(blownUp));
                    blownUp = null;
                }
                if(mids.contains(blownUp)) {
                    mids.remove(mids.indexOf(blownUp));
                    blownUp = null;
                }
                if(tops.contains(blownUp)) {
                    tops.remove(tops.indexOf(blownUp));
                    blownUp = null;
                }
            }
        }
        if (tick % invaderPace == 0) {
            checkInvader();
            moveInvaders();
        }
        if (bMissile.getY() > -10 && !bMissile.isLoaded()) {
            bMissile.moveYBy(-5);
            checkInvaderCollision();
        }
        else {
            bMissile.setY(-100);
            bMissile.load();
        }
        if (iMCount > 0 && tick % 2 == 0) {
            checkMissile(iM1);
            checkMissile(iM2);
            checkMissile(iM3);
        }
        if (tick % 100 == 0) {
            if (iMCount == 0) {
                if (queueNewWave && !mystery) {
                    
                    tops.clear();
                    mids.clear();
                    bottoms.clear();
                    bottomRow.clear();
                    invaderPace = 40;
                    makeWave();
                    queueNewWave = false;

                }
            }
            if (resetQueue) {
                reset();
            }
        }
        if (!mystery) {
            double i = Math.random();
            if (i > 0.998) {
                mystery = true;
                
                mysteryShip.sound();
            }
        }
        else if (tick % 2 == 0) {
            mysteryShip.moveXBy(5 * mDir);
            if (mysteryShip.getX() > panelWidth || mysteryShip.getX() < mysteryShip.getWidth() * -1 -50) {
                mystery = false;
                mysteryShip.stopSound();
                mysteryShip.respawn();
                if (Math.random() < .5) {
                    mysteryShip.setX(-49);
                    mDir = 1;
                }
                else {
                    mysteryShip.setX(panelWidth + 1);
                    mDir = -1;
                }
            }
        }
    }

    // INVADERS-------------------------------------------------------------------------INVADERS
    private void checkInvaderCollision() {
        for (int i = 0; i < bottomRow.size(); i++) {
            // top
            if (bottomRow.get(i) != null) {
                SIinvader tempT = bottomRow.get(i);
                if (tempT.getY() + tempT.getHeight() >= bMissile.getY()
                        && tempT.getY() <= bMissile.getY()) {
                    if (tempT.getX() < bMissile.getX() - 4 && tempT.getX()
                            + tempT.getWidth() + 4 > bMissile.getX()) {
                        if (tops.contains(tempT)) {
                            SItop temp = tops.get(tops.indexOf(tempT));
                            changeBottom(temp);
                            temp.hit();
                            blownUp = temp;
                            deathTimer = 30;
                            score += 30;
                        }
                        else if (mids.contains(tempT)) {
                            SImiddle temp = mids.get(mids.indexOf(tempT));
                            changeBottom(temp);
                            temp.hit();
                            blownUp = temp;
                            deathTimer = 30;
                            score += 20;
                        }
                        else if (bottoms.contains(tempT)) {
                            SIbottom temp = bottoms
                                    .get(bottoms.indexOf(tempT));
                            changeBottom(temp);
                            temp.hit();
                            blownUp = temp;
                            deathTimer = 30;
                            score += 10;
                        }
                        bMissile.load();
                        bMissile.setY(-10);
                        if (bottomRow.size() == 0) {
                            queueNewWave = true;
                        }
                    }
                }
            }
        }
        if (mystery && bMissile.getY() <= 50 && bMissile.getY() >= 30) {
            if (bMissile.getX() >= mysteryShip.getX() && bMissile
                    .getX() <= mysteryShip.getX() + mysteryShip.getWidth()) {
                mysteryShip.hit();
                mysteryShip.stopSound();
                score += mysteryShip.getPoints();
                
                bMissile.setY(-100);
                bMissile.load();
            }
        }
    }

    private void checkInvader() {
        if (tops.size() > 0) {
            SItop temp = tops.get(tops.size() - 1);
            if (temp.getX() > 494 - temp.getWidth()) {
                iDir *= -1;
                invaderPace -= invaderPace * .2;
                if (invaderPace < 1) {
                    invaderPace = 1;
                }
                dropInvaders();
            }
            else {
                temp = tops.get(0);
                if (temp.getX() < 0) {
                    iDir *= -1;
                    dropInvaders();
                }
            }
        }
    }

    private void moveInvaders() {
        for (int i = 0; i < tops.size(); i++) {
            // top
            SItop tempT = tops.get(i);
            tempT.moveXBy(5 * iDir);

            tempT.changePic();

        }
        for (int i = 0; i < mids.size(); i++) {
            // mid
            SImiddle tempM = mids.get(i);
            tempM.moveXBy(5 * iDir);
            tempM.changePic();
        }
        for (int i = 0; i < bottoms.size(); i++) {
            // bottom
            SIbottom tempB = bottoms.get(i);
            tempB.moveXBy(5 * iDir);
            tempB.changePic();
        }
        if (iMCount < 3) {
            invaderAttack();
        }
    }

    private void invaderAttack() {
        int i = (int) Math.round(Math.random() * (bottomRow.size() - 1));
        if (iMCount < 3) {
            if (Math.random() * 100 > 40 && bottomRow.size() > 0) {
                if (iM1.isLoaded() == true) {
                    iM1.unload();
                    iM1.setX(bottomRow.get(i).getX()
                            + bottomRow.get(i).getWidth() / 2);
                    iM1.setY(bottomRow.get(i).getY());
                    iMCount++;
                    return;
                }
                else if (iM2.isLoaded() == true) {
                    iM2.unload();
                    iM2.setX(bottomRow.get(i).getX()
                            + bottomRow.get(i).getWidth() / 2);
                    iM2.setY(bottomRow.get(i).getY());
                    iMCount++;
                    return;
                }
                else if (iM3.isLoaded() == true) {
                    iM3.unload();
                    iM3.setX(bottomRow.get(i).getX()
                            + bottomRow.get(i).getWidth() / 2);
                    iM3.setY(bottomRow.get(i).getY());
                    iMCount++;
                    return;
                }
            }
        }

    }

    private void checkMissile(SImissile iM) {
        if (iM.isLoaded() == false && iM.getY() < base.getY() + 20) {
            iM.moveYBy(5);
            if (iM.getY() >= base.getY()
                    && iM.getY() <= base.getY() + base.getWidth()) {
                if (iM.getX() <= base.getX() + base.getWidth()
                        && iM.getX() >= base.getX()) {

                    
                    base.hit();
                    timer.stop();
                    gameOver = true;
                    iM.setY(-10);
                    iM.load();
                    iMCount--;
                }
            }
        }
        else if (!iM.isLoaded()) {
            iM.setY(-10);
            iM.load();
            iMCount--;
        }
    }

    private void changeBottom(SIinvader destroyed) {// Changes whenever an
                                                    // invader is destroyed,
                                                    // takes in the destroyed
                                                    // ship
        bottomShip = bottomRow.get(0);
        if (bottomRow.contains(destroyed)
                && bottomRow.indexOf(destroyed) != -1) {
            boolean done = false;
            for (int i = (bottoms.size() - 1); i >= 0; i--) {
                if (bottoms.get(i).getX() == destroyed.getX()
                        && bottoms.get(i) != destroyed) {
                    done = true;
                    bottomRow.add(bottomRow.indexOf(destroyed),
                            bottoms.get(i));
                    bottomRow.remove(bottomRow.indexOf(destroyed));
                    return;
                }
            }
            for (int i = (mids.size() - 1); i >= 0; i--) {
                if (mids.get(i).getX() == destroyed.getX()
                        && mids.get(i) != destroyed) {
                    bottomRow.add(bottomRow.indexOf(destroyed), mids.get(i));
                    bottomRow.remove(bottomRow.indexOf(destroyed));
                    return;
                }
            }
            for (int i = (tops.size() - 1); i >= 0; i--) {
                if (tops.get(i).getX() == destroyed.getX()
                        && tops.get(i) != destroyed) {
                    bottomRow.add(bottomRow.indexOf(destroyed), tops.get(i));
                    bottomRow.remove(bottomRow.indexOf(destroyed));
                    return;
                }
            }
            bottomRow.remove(bottomRow.indexOf(destroyed));
        }
    }

    private void makeWave() {
        for (int i = 0; i < 10; i++) {
            SItop temp = new SItop(30 * i + 100, 80, 30, 24);
            tops.add(temp);

            SImiddle tempM = new SImiddle(30 * i + 100, 100, 30, 24);
            mids.add(tempM);
            tempM = new SImiddle(30 * i + 100, 120, 30, 24);
            mids.add(tempM);

            SIbottom tempB = new SIbottom(30 * i + 100, 140, 30, 24);
            bottoms.add(tempB);
            tempB = new SIbottom(30 * i + 100, 160, 30, 24);
            bottoms.add(tempB);

            bottomRow.add(tempB);
        }
        bottomShip = bottomRow.get(0);
        iDir = 1;
    }

    private void paintInvaders(Graphics2D g) {
        for (int i = 0; i < tops.size(); i++) {
            SItop temp = tops.get(i);
            g.drawImage(temp.getPic(), temp.getX(), temp.getY(),
                    temp.getWidth(), temp.getHeight(), null);
        }
        for (int i = 0; i < mids.size(); i++) {
            SImiddle temp = mids.get(i);
            g.drawImage(temp.getPic(), temp.getX(), temp.getY(),
                    temp.getWidth(), temp.getHeight(), null);
        }
        for (int i = 0; i < bottoms.size(); i++) {
            SIbottom temp = bottoms.get(i);
            g.drawImage(temp.getPic(), temp.getX(), temp.getY(),
                    temp.getWidth(), temp.getHeight(), null);
        }
    }

    private void dropInvaders() {
        lastBottom = bottomShip.getY();
        for (int i = 0; i < tops.size(); i++) {
            // top
            SItop tempT = tops.get(i);
            tempT.moveYBy(12);
        }
        for (int i = 0; i < mids.size(); i++) {
            // mid
            SImiddle tempM = mids.get(i);
            tempM.moveYBy(12);
        }
        for (int i = 0; i < bottoms.size(); i++) {
            // bottom
            SIbottom tempB = bottoms.get(i);
            tempB.moveYBy(12);
        }
        if (lastBottom == bottomShip.getY()) {
            bottomShip.moveYBy(12);
        }
        
        if (bottomShip.getY() + bottomShip.getHeight() > panelHeight) {
            timer.stop();
            gameOver = true;
        }
    }

    // PAINT
    // COMPONENT---------------------------------------------------------------------PAINT
    // COMPONENT
    //gjhg
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(base.getPic(), base.getX(), base.getY(), base.getWidth(),
                base.getHeight(), null);
        paintInvaders(g2);
        g2.setColor(Color.white);
        if (!bMissile.isLoaded()) {
            g2.fillRect(bMissile.getX(), bMissile.getY(), bMissile.getWidth(),
                    bMissile.getHeight());
        }
        if (!iM1.isLoaded()) {
            g2.fillRect(iM1.getX(), iM1.getY(), iM1.getWidth(),
                    iM1.getHeight());
        }
        if (!iM2.isLoaded()) {
            g2.fillRect(iM2.getX(), iM2.getY(), iM2.getWidth(),
                    iM2.getHeight());
        }
        if (!iM3.isLoaded()) {
            g2.fillRect(iM3.getX(), iM3.getY(), iM3.getWidth(),
                    iM3.getHeight());
        }
        g2.setColor(Color.green);

        g2.setFont(new Font("Arial", 15, 15));
        String s = "Score: " + score;
        g2.drawString(s, panelWidth - s.length() * 8, 15);
        g2.setFont(new Font("Arial", 20, 20));
        if (startText) {
            g2.drawString("Press \"ENTER\" to Start", panelWidth / 2 - 90,
                    panelHeight / 2 + 20);
        }
        else if (gameOver) {
            g2.drawString("GAME OVER", panelWidth / 2 - 50, panelHeight / 2);
        }
        if (mystery) {
            g2.drawImage(mysteryShip.getPic(), mysteryShip.getX(),
                    mysteryShip.getY(), mysteryShip.getWidth(),
                    mysteryShip.getHeight(), null);
        }
        
    }

    private void reset() {
        score = 0;
        tick = 0;
        base = new SIbase(baseX, baseY, 30, 20);
        mysteryShip = new SImystery(-40, 50, 40, 20);
        
        mDir = 1;
        tops.clear();
        mids.clear();
        bottoms.clear();
        bottomRow.clear();
        makeWave();
        invaderPace = 40;
        resetQueue = false;
        gameOver = false;
        startText = true;
        timer.stop();
        resetMissiles(iM1);
        resetMissiles(iM2);
        resetMissiles(iM3);
        bMissile.load();
        bMissile.setY(-100);
        if (mystery) {
            mysteryShip.stopSound();
            mystery= false;
            mysteryShip = new SImystery(-40, 50, 40, 20);
            
            mDir = 1;
        }
        repaint();
    }

    private void resetMissiles(SImissile iM) {
        if (!iM.isLoaded()) {
            iM.setY(-10);
            iM.load();
            iMCount--;
        }
    }

    public boolean isRunning() {
        return (tick != 0);
    }

    public void resetQueue() {
        resetQueue = true;
        if (gameOver) {
            reset();
        }
    }

    public void pause() {
        timer.stop();
        mysteryShip.stopSound();
    }

    public void resume() {
        timer.start();
    }
}
