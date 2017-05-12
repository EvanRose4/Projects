import java.awt.Image;

public class SImiddle extends SIinvader{
    private int x,y,width,height;
    private Image image;
    private boolean firstPic, hit = false;
    public SImiddle(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        firstPic = true;
        image = getImage("SImiddle0.gif");
    }
    public Image getPic() {
        if(!hit) {
        return this.image;
        }
        else {
            return this.hitPic();
        }
    }
    public void changePic() {
        if(firstPic) {
            image = getImage("SImiddle1.gif");
            firstPic = false;
        }
        else {
            image = getImage("SImiddle0.gif");
            firstPic = true;
        }
    }
    public void hit() {
        hit = true;
        hitSound();
    }
}
