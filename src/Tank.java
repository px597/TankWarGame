
import javax.print.attribute.standard.MediaSize;
import java.awt.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Vector;

import static java.lang.Thread.sleep;

/**
 * Created on 2022/2/2 20:02.
 * 2022.3.31 实现坦克被击中时的消亡功能
 * 2022.4.6 实现坦克不重叠的功能
 * @author Peng Xin
 * @version 1.0
 */
// tank基类
public class Tank implements Runnable, Serializable {

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
    private Vector<Orientation> canNotMoveOrientations;   // 坦克不可以移动的方向
    private Vector<Tank> tanks;     // 所有坦克

    public String name;

    public Tank(int x, int y, Orientation orientation, Color color, int survive) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.color = color;
        this.survive = survive;
        this.canNotMoveOrientations = new Vector<>();
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

    public void setName(String name){
        this.name = name;
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
        if(this.y > 0 &&
                !this.canNotMoveOrientations.contains(Orientation.UP)){
            this.y -= this.speed;
        }
    }
    public void moveDown(){
        this.orientation = Orientation.DOWN;
        if(this.y + this.Height < TankWarGame.HEIGHT &&
                !this.canNotMoveOrientations.contains(Orientation.DOWN)) {
            //System.out.println(name + " 向下走 ");
            this.y += this.speed;
        }
    }
    public void moveLeft(){
        this.orientation = Orientation.LEFT;
        if(this.x > 0 &&
                !this.canNotMoveOrientations.contains(Orientation.LEFT)) {
            //System.out.println(name + " 向左走 ");
            this.x -= this.speed;
        }
    }
    public void moveRight(){
        this.orientation = Orientation.RIGHT;
        if(this.x + this.Height < TankWarGame.WIDTH &&
                !this.canNotMoveOrientations.contains(Orientation.RIGHT)) {
            //System.out.println(name + " 向右走 ");
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

    // 在可以移动的方向随机移动
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
        return (getX() > 0
                && x < TankWarGame.WIDTH
                && y > 0
                && y < TankWarGame.HEIGHT);
    }
    //
    public Vector<Orientation> getCanNotMoveOrientations(){
        return canNotMoveOrientations;
    }
    public void setCanNotMoveOrientations(Tank tank){
        //System.out.print(name + " 与 " + tank.name + "重叠, " + name);
        // 对方坦克在我方坦克的左上方
        if(tank.getX() < this.x && tank.getY() < this.y){
            //System.out.println("不能去左上方");
            this.canNotMoveOrientations.add(orientation.LEFT);
            this.canNotMoveOrientations.add(orientation.UP);
        }
        // 对方坦克在我方坦克上方
        else if(tank.getX() >= this.x && tank.getY() < this.y){
            //System.out.println("不能去右上方");
            this.canNotMoveOrientations.add(orientation.UP);
            this.canNotMoveOrientations.add(orientation.RIGHT);
        }
        // 对方坦克在我方坦克的左边
        else if(tank.getX() < this.x && tank.getY() >= this.y){
            //System.out.println("不能去左下方");
            this.canNotMoveOrientations.add(orientation.LEFT);
            this.canNotMoveOrientations.add(orientation.DOWN);
        }
        // 对方坦克在我方坦克的右下方
        else if(tank.getX() >= this.x && tank.getY() >= this.y)
        {
            //System.out.println("不能去右下方");
            this.canNotMoveOrientations.add(orientation.DOWN);
            this.canNotMoveOrientations.add(orientation.RIGHT);
        }
    }
    // 判断两个坦克是否重叠，若重叠则增加当前坦克的不能移动的方向
    public void CheckCovered(Tank tank){
        // 两个坦克方向为上或者下
        switch (orientation){
            case UP:
            case DOWN:
                switch (tank.getOrientation()){
                    case UP:
                    case DOWN:
                        //会重叠
                        if(tank.getX() >= x - Width
                                && tank.getX() <= x + Width
                                && tank.getY() >= y - Height
                                && tank.getY() <= y + Height){
                            // 根据对方坦克的位置设置我方坦克不能移动的方向
                            setCanNotMoveOrientations(tank);
                        }
                        return;
                    case LEFT:
                    case RIGHT:
                        if(tank.getX() >= x - Height
                                && tank.getX() <= x + Width
                                && tank.getY() >= y - Width
                                && tank.getY() <= y + Height){
                            setCanNotMoveOrientations(tank);
                        }
                        return;
                }
                break;
            case LEFT:
            case RIGHT:
                switch (tank.getOrientation()) {
                    case UP:
                    case DOWN:
                        if(tank.getX() >= x - Width
                                && tank.getX() <= x + Height
                                && tank.getY() >= y - Height
                                && tank.getY() <= y + Width){
                            setCanNotMoveOrientations(tank);
                        }

                        return;
                    case LEFT:
                    case RIGHT:
                        if(tank.getX() >= x - Height
                                && tank.getX() <= x + Height
                                && tank.getY() >= y - Width
                                && tank.getY() <= y + Width){
                            setCanNotMoveOrientations(tank);
                        }
                }
        }
    }

    // 判断坦克是否与其他坦克重叠 如果相邻了，则只能向其他的方向走
    public void CheckAllTankCovered(Vector<Tank> tanks){
        //System.out.println("tank " + name + " 比较是否重叠，坦克数组个数 " + tanks.size());
        for (int i = 0; i < tanks.size(); i++) {
            // 不和自己比较，检测是否与除自己以外的其他坦克重叠
            if(!tanks.get(i).equals(this))
                CheckCovered(tanks.get(i));
        }
    }
    public void setTanks(Vector<Tank> tanks){
        this.tanks = tanks;
    }
    // 敌方坦克随机移动的子线程，坦克被击中则退出线程
    @Override
    public void run() {
        while(this.isLive) {
            //测试，让坦克只往左边走，
            randomDirection();
            //this.setOrientation(Orientation.RIGHT);
            // 在一定概率下随机发射子弹，再随机移动10 - 20步,
            // 因为坦克发射子弹时根据当前的方向发射的，所以每次先发射再移动，看起来会和坦克朝向连贯
            if(Math.random() > 0.8)
                this.shot();

            // 和友方坦克依次判断是否重叠，并添加canNotMoveOrientations，根据canNotMoveOrientations进行移动，随后清除。
            // 注意：不能在依次判断时清除，否则会造成和1重叠，和3不重叠，但3的判断结果会清除1的结果。
            // 注意：这里只判断是否重叠返回布尔值，并通过转向来解决重叠问题是一种更简化的方案。
            for (int i = 0; i < Math.max(10, Math.random() * 20); i++) {
                CheckAllTankCovered(tanks);
                randomMove();
                this.canNotMoveOrientations.clear();
                try {
                    sleep(50);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        System.out.println("敌方坦克随机移动线退出：" + Thread.currentThread().getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tank tank = (Tank) o;

        if (x != tank.x) return false;
        if (y != tank.y) return false;
        if (speed != tank.speed) return false;
        if (survive != tank.survive) return false;
        if (isLive != tank.isLive) return false;
        if (orientation != tank.orientation) return false;
        if (!Objects.equals(color, tank.color)) return false;
        if (!Objects.equals(bullet, tank.bullet)) return false;
        if (!Objects.equals(bullets, tank.bullets)) return false;
        if (!Objects.equals(bomb, tank.bomb)) return false;
        if (!Objects.equals(canNotMoveOrientations, tank.canNotMoveOrientations))
            return false;
        return Objects.equals(tanks, tank.tanks);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + Width;
        result = 31 * result + Height;
        result = 31 * result + (orientation != null ? orientation.hashCode() : 0);
        result = 31 * result + speed;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + survive;
        result = 31 * result + (bullet != null ? bullet.hashCode() : 0);
        result = 31 * result + (bullets != null ? bullets.hashCode() : 0);
        result = 31 * result + (isLive ? 1 : 0);
        result = 31 * result + (bomb != null ? bomb.hashCode() : 0);
        result = 31 * result + (canNotMoveOrientations != null ? canNotMoveOrientations.hashCode() : 0);
        return result;
    }
}
