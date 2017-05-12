import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class SIthing{
    private int x, y, width, height;
    
    public SIthing(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public int getY() {
        return this.y;
    }
    public int getX() {
        return this.x;
    }
    public void setX(int newX) {
        x = newX;
    }
    public void setY(int newY) {
        y = newY;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public void setWidth(int newWidth) {
        width = newWidth;
    }
    public void setHeight(int newHeight) {
        height = newHeight;
    }
    public void setDimensions(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
    }
    public void moveXBy(int moveX) {
        this.x += moveX;
    }
    public void moveYBy(int moveY) {
        this.y += moveY;
    }
    public Image getImage(String name) {
        URL file = getClass().getResource(name);
        ImageIcon icon = new ImageIcon(file);
        return icon.getImage();
    }
    public AudioClip getSound(String name) {
        URL file = getClass().getResource(name);
        return Applet.newAudioClip(file);
    }
}
