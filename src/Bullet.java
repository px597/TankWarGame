import java.awt.*;

import static java.lang.Thread.sleep;

/**
 * Created on 2022/2/28 19:58.
 *
 * @author Peng Xin
 * @version 1.0
 */

public class Bullet implements Runnable{
    private int x, y;
    private int speed = 20; // 子弹初始速度设置为20
    private Orientation orientation;
    private final int radian = 5;
    private final Color color = Color.BLACK;

    // 直接的构造函数，应该有一个根据坦克当前的位置和姿态直接生成的构造器
    public Bullet(int x, int y,  Orientation orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    /** java 中构造器调用另一个构造器只能写在第一行，所以这里只能自己写
     * 根据坦克直接生成子弹
     * @param tank 坦克
     */
    public Bullet(Tank tank){
        switch (tank.getOrientation()){
            case UP:
                //g.fill3DRect(x + 18,y,4,25,true);      //坦克炮筒
                this.x = tank.getX() + 18;
                this.y = tank.getY();
                this.orientation = tank.getOrientation();
                break;
            case DOWN:
                //g.fill3DRect(x + 18,y + 25,4,25,true);      //坦克炮筒
                this.x = tank.getX() + 18;
                this.y = tank.getY() + 50;
                this.orientation = tank.getOrientation();
                break;
            case LEFT:
                //g.fill3DRect(x,y + 18,25,4,true);      //坦克炮筒
                this.x = tank.getX();
                this.y = tank.getY() + 18;
                this.orientation = tank.getOrientation();
                break;
            case RIGHT:
                //g.fill3DRect(x + 25,y + 18,25,4,true);      //坦克炮筒
                this.x = tank.getX() + 50;
                this.y = tank.getY() + 18;
                this.orientation = tank.getOrientation();
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        while(this.inRange()){
            try {
                sleep(50);
            }catch(Exception e){
                e.printStackTrace();
            }
            this.fly();
            System.out.println("子弹 " + x + " " + y);
        }
        System.out.println("子弹出界，线程退出："+ Thread.currentThread().getName());
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

    public int getRadian() {
        return radian;
    }
    public boolean inRange(){
        return (getX() > 0 && x < TankWarGame.WIDTH && y > 0 && y < TankWarGame.HEIGHT);
    }
    public void fly(){
        switch (orientation){
            case UP:
                y -= speed;
                break;
            case DOWN:
                y += speed;
                break;
            case LEFT:
                x -= speed;
                break;
            case RIGHT:
                x += speed;
                break;
            default:
                break;
        }
    }
}