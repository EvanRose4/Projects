import java.applet.AudioClip;
import java.awt.Image;

public class SImystery extends SIinvader{
    private int x,y,width,height;
    private Image image;
    private boolean hit;
    private int points;
    private AudioClip sound;
    
    public SImystery(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        hit = false;
        sound = getSound("SImystery.wav");
        image = getImage("SImystery.gif");
    }
    public Image getPic() {
        if(!hit) {
        return this.image;
        }
        else {
            return this.hitPic();
        }
    }
    public void sound() {
        sound.play();
    }
    public void stopSound() {
        sound.stop();
    }
    public int getPoints() {
        int [] array = {50, 100, 150, 300};
        int i = (int) (Math.random()*4);
        return array[i]; 
    }
    public void hit() {
        hit = true;
        hitSound();
    }
    public void respawn() {
        hit = false;
    }
    
}