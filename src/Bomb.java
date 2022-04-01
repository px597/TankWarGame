import java.awt.*;

/**
 * Created on 2022/3/31 16:51.
 * 将坦克爆炸功能视为一个类
 * @author Peng Xin
 * @version 1.0
 */

public class Bomb{
    private int x,y;
    private boolean isLive = true;
    private int life = 9;   // 爆炸效果的生命值
    // 用于显示爆炸效果的图像
    private Image image01 = Toolkit.getDefaultToolkit().getImage(TankPanel.class.getResource("/bomb_1.gif"));
    private Image image02 = Toolkit.getDefaultToolkit().getImage(TankPanel.class.getResource("/bomb_2.gif"));
    private Image image03 = Toolkit.getDefaultToolkit().getImage(TankPanel.class.getResource("/bomb_3.gif"));

    public Bomb(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Bomb(Tank tank){
        this.x = tank.getX();
        this.y = tank.getY();
    }
    void lifeDown(){
        if(this.life > 0) life--;
        else {
            this.isLive = false;
        }
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

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public Image getImage01() {
        return image01;
    }

    public void setImage01(Image image01) {
        this.image01 = image01;
    }

    public Image getImage02() {
        return image02;
    }

    public void setImage02(Image image02) {
        this.image02 = image02;
    }

    public Image getImage03() {
        return image03;
    }

    public void setImage03(Image image03) {
        this.image03 = image03;
    }
}
