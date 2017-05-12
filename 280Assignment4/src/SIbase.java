import java.applet.AudioClip;
import java.awt.Image;
//Evan  Rose 
public class SIbase extends SIship{
    private boolean alive = true;
    private AudioClip sound;
    private Image image;
    
    public SIbase(int x, int y,int width, int height) {
        super(x,y,width, height);
       
        this.image = getImage("SIbase.gif");
        this.sound = getSound("SIbaseShoot.wav");
        
    }
    public void hit() {
        alive = false;
        hitSound();
    }
    public Image getPic() {
        if(alive) {
            return image;
        }
        else {
            return hitPic();
        }
    }
    public void shoot() {
        sound.play();
    }
    public void destroy() {
        alive = true;
        this.sound = getSound("SIshipHit.wav");
    }
}
