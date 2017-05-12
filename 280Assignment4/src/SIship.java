import java.applet.AudioClip;
import java.awt.Image;

public class SIship extends SIthing{
    private boolean isHit;
    private Image hitImage,image;
    private AudioClip hitSound;
    public SIship(int x, int y,int width, int height) {
        super(x, y,width,height);
        isHit = false;
        hitImage = getImage("SIbaseBlast.gif");
        hitSound = getSound("SIshipHit.wav");
    }
    public Image hitPic() {
        return hitImage;
    }
    public void hitSound() {
        hitSound.play();
    }
}
