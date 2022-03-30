import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

/**
 * Created on 2022/2/2 20:35.
 * 用了git控制版本
 * idea中记成了git修改。
 * @author Peng Xin
 * @version 1.0
 */

// KeyListener 为监听器，可以监听键盘事件
// 该接口包括三个函数，
//    public void keyTyped(KeyEvent e);
//    public void keyPressed(KeyEvent e);
//    public void keyReleased(KeyEvent e);
// 根据按下去的不同的按键，处理对应的移动和射击关系。
public class TankPanel extends JPanel implements KeyListener, Runnable{
    // 先创建一个球，实际上这里感觉应该是一个工厂来创建的
    Ball ball = null;
    Tank hero = null;

    Vector<Tank> enemyTanks = new Vector<>();
    int enemySize = 5;

    // 面板构造函数，可以在里面放置一些初始化的内容；
    public TankPanel(){
        // 生成我方坦克
        hero = new Tank(100,400, Orientation.UP,10,Color.yellow,1);
        // 生成敌方坦克，敌方坦克出生时要生成子弹
        for(int i = 0; i < enemySize; i++){
            // 建立敌方坦克并加入敌方坦克集合
            Tank enemyTank = new Tank(i * 100,100, Orientation.DOWN,10,Color.CYAN,1);
            enemyTanks.add(enemyTank);
            // 建立敌方坦克的子弹同时建立子进程计算每颗子弹的位置，并加入敌方坦克子弹集合，以便于后面通过子进程遍历重绘
            Bullet enemyTankBullet = new Bullet(enemyTank);
            enemyTank.getBullets().add(enemyTankBullet);
            new Thread(enemyTankBullet).start();
        }
    }

    // 每隔一定时间重绘，就是不停地执行重绘的动作。
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //画出我方坦克
        drawTank(hero, g);

        // 画出子弹 因为要不停地重绘，这里要用子线程实现。
        for(int i = 0; i < hero.getBullets().size(); i++){
            Bullet heroBullet = hero.getBullets().get(i);
            if(heroBullet.inRange())
                drawBullet(heroBullet, g);
            else hero.getBullets().remove(heroBullet);
        }
        /*if(hero.getBullet() != null && hero.getBullet().inRange())
            drawBullet(hero.getBullet(), g);    // 这里刚开始没有子弹的。*/

        // 画出敌方坦克 和 敌方坦克的子弹
        for (int i = 0; i < enemyTanks.size(); i++) {
            // 画出敌方坦克
            Tank enemyTank = enemyTanks.get(i);
            drawTank(enemyTank, g);
            // 画出坦克的所有子弹
            for(int j = 0; j < enemyTank.getBullets().size(); j++) {
                Bullet enemyTankBullet = enemyTank.getBullets().get(j);
                if (enemyTankBullet.inRange())   // 子弹在范围内就绘制，不在范围内就要移除出去。
                    drawBullet(enemyTankBullet, g);
                else enemyTank.getBullets().remove(enemyTankBullet);
            }
        }
        // drawBall(ball, g);
    }
    public void drawTank(Tank tank, Graphics g){
        // 设置画笔颜色
        g.setColor(tank.getColor());
        // 根据朝向绘制坦克
        int x = tank.getX(), y = tank.getY();
        switch (tank.getOrientation()){
            case UP:
                g.fill3DRect(x, y, 10,50,true);
                g.fill3DRect(x + 30, y, 10,50,true);
                g.fill3DRect(x + 10,y + 10,20,30,false);    //坦克中间的方块
                g.fillOval( x + 10,y + 15,20,20);                 //坦克中间的圆
                g.fill3DRect(x + 18,y,4,25,true);      //坦克炮筒
                break;
            case DOWN:
                g.fill3DRect(x, y, 10,50,true);
                g.fill3DRect(x + 30, y, 10,50,true);
                g.fill3DRect(x + 10,y + 10,20,30,false);    //坦克中间的方块
                g.fillOval( x + 10,y + 15,20,20);                 //坦克中间的圆
                g.fill3DRect(x + 18,y + 25,4,25,true);      //坦克炮筒
                break;
            case LEFT:
                g.fill3DRect(x, y, 50,10,true);
                g.fill3DRect(x , y + 30, 50,10,true);
                g.fill3DRect(x + 10,y + 10,30,20,false);    //坦克中间的方块
                g.fillOval( x + 15,y + 10,20,20);                 //坦克中间的圆
                g.fill3DRect(x,y + 18,25,4,true);      //坦克炮筒
                break;
            case RIGHT:
                g.fill3DRect(x, y, 50,10,true);
                g.fill3DRect(x , y + 30, 50,10,true);
                g.fill3DRect(x + 10,y + 10,30,20,false);    //坦克中间的方块
                g.fillOval( x + 15,y + 10,20,20);                 //坦克中间的圆
                g.fill3DRect(x + 25,y + 18,25,4,true);      //坦克炮筒
            default:
                break;
        }
    }
    public void  drawBall(Ball ball, Graphics g){
        // 设置画笔颜色
        g.setColor(ball.getColor());
        // 绘制球
        g.fillOval(ball.getX(), ball.getY(),ball.WIDTH, ball.HEIGHT);
    }
    public void drawBullet(Bullet bullet, Graphics g){
        if(bullet == null) return;
        g.setColor(bullet.getColor());
        g.fillOval(bullet.getX(), bullet.getY(), bullet.getRadian(), bullet.getRadian());
    }

    // 重写监听器的函数
    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("typed: " +e);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("pressed: " + e);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                hero.moveUp();
                break;
            case KeyEvent.VK_S:
                hero.moveDown();
                break;
            case KeyEvent.VK_A:
                hero.moveLeft();
                break;
            case KeyEvent.VK_D:
                hero.moveRight();
                break;
            case KeyEvent.VK_J:
                // 我方英雄发射子弹，用子线程处理。
                // 需要MyPanel不停重绘子弹
                // 当子弹移动到面板边界时，销毁该线程。
                hero.shot();
                System.out.println("英雄坦克子弹个数：" + hero.getBullets().size());
                for (int i = 0; i < hero.getBullets().size(); i++) {
                    new Thread(hero.getBullets().get(i)).start();
                }
                this.repaint();
                break;
            default:
                break;
        }
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("released: " +e);
    }
}
