import java.awt.*;

/**
 * Created on 2022/2/13 17:08.
 *
 * @author Peng Xin
 * @version 1.0
 */
// 小球类
public class Ball {
    private int x, y;
    private int speed;              //移动速度
    private Color color;            //颜色

    public final int WIDTH = 10, HEIGHT = 10;

    public Ball(int x, int y, int speed, Color color) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
