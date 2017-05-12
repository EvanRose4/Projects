
import java.awt.Image;

public class SIinvader extends SIship{
    private int x,y,width,height,deathTimer = 0,type = 0;
    private Image hitImage;
    public SIinvader(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        hitImage = getImage("SIinvaderBlast.gif");
    }
    public Image hitPic() {
        return hitImage;
    }
}
