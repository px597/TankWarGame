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
    public boolean isLive = true;  // 子弹是否还存在

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
        // 子弹击中敌方坦克或者超出边界就退出，并设为消亡状态
        while( this.isLive && this.inRange()){
            try {
                sleep(50);
            }catch(Exception e){
                e.printStackTrace();
            }
            this.fly();
            //System.out.println("子弹 " +getX() + " " + getY());
        }
        this.isLive = false;
        //System.out.println("子弹出界，线程退出："+ Thread.currentThread().getName());
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

    // 判断子弹是否击中坦克，感觉应该放在子弹类下
    // 子弹在创建后通过子线程执行，计算当前位置，同时判断当前子弹是否与对方坦克发生碰撞，发生碰撞对方就消失。
    public void hitTank(Tank tank){
        switch (tank.getOrientation()){
            case UP:
            case DOWN:
                // 朝上与朝下都可以按照此规则判断。
                if(this.getX() >= tank.getX() && this.getX() <= tank.getX() + tank.getWidth() &&
                        this.getY() >= tank.getY() && this.getY() <= tank.getY() + tank.getHeight()){
                    // 击中就是让坦克消失。
                    // 坦克被击中后还要从敌方坦克的数组中删除，否则只是没有绘制
                    // 但是还是会留在原地，有新的子弹进入时仍然会判定击中。
                    this.isLive = false;
                    tank.isLive = false;
                }
                break;
            case LEFT:
            case RIGHT:
                //朝左与朝右按此规则判断。
                if(this.getX() >= tank.getX() && this.getX() <= tank.getX() + tank.getHeight() &&
                        this.getY() >= tank.getY() && this.getY() <= tank.getY() + tank.getWidth()){
                    // 击中就是让坦克消失。
                    this.isLive = false;
                    tank.isLive = false;
                }
                break;
        }
    }
}