
public class SImissile extends SIthing{
    private int width,height,x,y;
    private boolean loaded = true;
    public SImissile(int x, int y, int width, int height) {
        super(x,y,width,height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public boolean isLoaded() {
        return this.loaded;
    }
    public void load() {
        loaded = true;
    }
    public void unload() {
        loaded = false;
    }
}
