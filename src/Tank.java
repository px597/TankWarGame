
import java.awt.*;
import java.util.Vector;

import static java.lang.Thread.sleep;

/**
 * Created on 2022/2/2 20:02.
 * 2022.3.31 实现坦克被击中时的消亡功能
 * @author Peng Xin
 * @version 1.0
 */
// tank基类
public class Tank implements Runnable{

    private int x, y;               // 设置坦克基点
    private final int Width = 40, Height = 50;  // 坦克的长宽高。
    private Orientation orientation;// 朝向
    private int speed = 5;         //移动速度, 初始化为10
    private Color color;            //颜色
    private int survive;            //生命值
    private Bullet bullet = null;   //子弹 敌人的坦克可以有多颗子弹
    private Vector<Bullet> bullets = new Vector<>();   //子弹 敌人的坦克可以有多颗子弹
    public boolean isLive = true;   // 坦克是否存活
    private Bomb bomb = null;       // 自己坦克的爆炸场景，初始化为空

    public Tank(int x, int y, Orientation orientation, Color color, int survive) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.color = color;
        this.survive = survive;
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

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public int getSurvive() {
        return survive;
    }

    public void setSurvive(int survive) {
        this.survive = survive;
    }

    public Color getColor() {
        return color;
    }

    public int getSpeed() {
        return speed;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public Vector<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(Vector<Bullet> bullets) {
        this.bullets = bullets;
    }

    public int getWidth(){
        return this.Width;
    }
    public int getHeight(){
        return this.Height;
    }
    // 移动，且改变朝向
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void moveUp(){
        this.orientation = Orientation.UP;
        if(this.y > 0){
            this.y -= this.speed;
        }
    }
    public void moveDown(){
        this.orientation = Orientation.DOWN;
        if(this.y + this.Height < TankWarGame.HEIGHT) {
            this.y += this.speed;
        }
    }
    public void moveLeft(){
        this.orientation = Orientation.LEFT;
        if(this.x > 0) {
            this.x -= this.speed;
        }
    }
    public void moveRight(){
        this.orientation = Orientation.RIGHT;
        if(this.x + this.Height < TankWarGame.WIDTH) {
            this.x += this.speed;
        }
    }

    // 炸弹
    public void setBomb() {
        bomb = new Bomb(this);
    }

    public Bomb getBomb() {
        return bomb;
    }

    //射击行为，生成子弹并移动
    public void shot(){
        // 若当前坦克没有子弹
        if(this.getBullet() != null && this.getBullet().isLive) return;
        //根据坦克当前的朝向生成子弹，并让子弹飞行
        // if(bullet != null ) return; // 子弹存在时，不管。
        switch (orientation){
            case UP:
                //g.fill3DRect(x + 18,y,4,25,true);      //坦克炮筒
                bullet = new Bullet(x + 18, y, orientation);
                break;
            case DOWN:
                //g.fill3DRect(x + 18,y + 25,4,25,true);      //坦克炮筒
                bullet = new Bullet(x + 18, y + 50, orientation);
                break;
            case LEFT:
                //g.fill3DRect(x,y + 18,25,4,true);      //坦克炮筒
                bullet = new Bullet(x, y + 18, orientation);
                break;
            case RIGHT:
                //g.fill3DRect(x + 25,y + 18,25,4,true);      //坦克炮筒
                bullet = new Bullet(x + 50, y + 18, orientation);
                break;
            default:
                break;
        }
        bullets.add(bullet);
        // 启动子线程 进入run方法中去 本子线程只用来计算一颗子弹的位置
        // shot方法其实可以避免将子弹作为vector管理，因为不管是敌方坦克还是我方坦克，shot时都会启动一个子线程。
        new Thread(bullet).start();
    }

    public void randomMove(){
        switch (this.orientation){
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
        }
    }

    public void randomDirection(){
        // 随机改变方向
        switch ((int)(Math.random() * 4)){
            case 0:
                setOrientation(Orientation.UP);
                break;
            case 1:
                setOrientation(Orientation.DOWN);
                break;
            case 2:
                setOrientation(Orientation.LEFT);
                break;
            case 3:
                setOrientation(Orientation.RIGHT);
                break;
        }
    }
    public boolean inRange(){
        return (getX() > 0 && x < TankWarGame.WIDTH && y > 0 && y < TankWarGame.HEIGHT);
    }
    // 敌方坦克随机移动的子线程，坦克被击中则退出线程
    @Override
    public void run() {
        while(this.isLive) {
            // 在一定概率下随机发射子弹，再随机移动10 - 20步,
            // 因为坦克发射子弹时根据当前的方向发射的，所以每次先发射再移动，看起来会和坦克朝向连贯
            if(Math.random() > 0.8)
                this.shot();
            for (int i = 0; i < Math.max(10, Math.random() * 20); i++) {
                randomMove();
                try {
                    sleep(50);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            randomDirection();
        }
        System.out.println("敌方坦克随机移动线退出：" + Thread.currentThread().getName());
    }
}
