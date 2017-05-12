import java.awt.Image;

public class SIbottom extends SIinvader {
    private int x, y, width, height;
    private Image image;
    private boolean firstPic;
    private boolean hit;

    public SIbottom(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        firstPic = true;
        image = getImage("SIbottom0.gif");
    }
    public void hit() {
        hit = true;
        hitSound();
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
        if (firstPic) {
            image = getImage("SIbottom1.gif");
            firstPic = false;
        }
        else {
            image = getImage("SIbottom0.gif");
            firstPic = true;
        }
    }
}
