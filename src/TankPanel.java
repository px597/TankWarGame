import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.Vector;

/**
 * Created on 2022/2/2 20:35.
 * 用了git控制版本
 * idea中记成了git修改.
 * 在dev分支中进行开发，开发完成后合并到master。
 * 2022.3.30 实现敌方坦克消失的功能。
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

    private Tank hero = null;
    private Vector<Tank> allTanks = new Vector<>();     //所有坦克数组
    private Vector<Tank> enemyTanks = new Vector<>();   //敌方坦克数组
    private int enemySize = 4;
    private Vector<Bomb> bombs = new Vector<>();        // 炸弹


    // 面板构造函数，可以在里面放置一些初始化的内容；
    public TankPanel() throws IOException {
        // 读取配置数据
        Recorder.restore();

        // 生成我方坦克
        hero = new Tank(100,400, Orientation.UP,Color.yellow,1);
        hero.setTanks(allTanks);
        allTanks.add(hero);
        // 生成敌方坦克
        for(int i = 0; i < enemySize; i++){
            // 建立敌方坦克并加入敌方坦克集合
            Tank enemyTank = new Tank(i * 100,100, Orientation.DOWN,Color.CYAN,1);
            enemyTank.setName(Integer.toString(i));

            enemyTanks.add(enemyTank);
            allTanks.add(enemyTank);

            enemyTank.setTanks(allTanks);

            new Thread(enemyTank).start();
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

            // 判断敌方坦克是否被击中
            if(hero.getBullet() != null && hero.getBullet().isLive){
                for(int i = 0; i < enemyTanks.size(); i++){
                    hero.getBullet().hitTank(enemyTanks.get(i));
                    // 敌方坦克被击中则生成炮弹，并加入数组中
                    if(!enemyTanks.get(i).isLive){
                        //System.out.println("敌方坦克被击中");
                        Recorder.addShotEnemyTankNumber();
                        enemyTanks.get(i).setBomb();
                        bombs.add(enemyTanks.get(i).getBomb());

                        allTanks.remove(enemyTanks.get(i));
                        enemyTanks.remove(enemyTanks.get(i));
                    }
                }
            }

            // 判断我方坦克是否被击中
            if(hero.isLive) {
                for (int i = 0; i < enemyTanks.size(); i++) {
                    Tank enemyTank = enemyTanks.get(i);
                    for (int j = 0; j < enemyTank.getBullets().size(); j++) {
                        enemyTank.getBullets().get(j).hitTank(hero);
                        if (!hero.isLive) {
                            System.out.println("我方坦克被击中");
                            hero.setBomb();
                            allTanks.remove(hero);
                        }
                    }
                }
            }

            this.repaint();
        }
    }
    // 显示计分板
    public void showInfo(Graphics g){
        // 绘制Score
        g.setColor(Color.BLACK);
        Font infoFont = new Font("Time New Romans", Font.BOLD, 25);
        g.setFont(infoFont);
        // 绘制坦克图标
        g.drawString("Score:", TankWarGame.WIDTH + 25, 25);
        drawTank(new Tank(TankWarGame.WIDTH + 25,35, Orientation.UP,Color.CYAN,1), g);
        // 绘制分数
        g.setColor(Color.BLACK);
        g.drawString(Integer.toString(Recorder.getShotEnemyTankNumber()), TankWarGame.WIDTH + 25 + 60, 65);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0,0, TankWarGame.WIDTH, TankWarGame.HEIGHT);
        showInfo(g);
        //画出我方坦克 以及炸弹 如果有的话
        if(hero.isLive)
            drawTank(hero, g);
        if(hero.getBomb() != null && hero.getBomb().isLive()) {
            System.out.println("我方坦克爆炸");
            drawBomb(hero.getBomb(), g);
        }

        // 画出子弹，因为英雄坦克可能也有很多的子弹，所以这里是用这个
        if(hero.isLive) {
            for (int i = 0; i < hero.getBullets().size(); i++) {
                Bullet heroBullet = hero.getBullets().get(i);
                // 如果英雄的子弹击中敌方坦克或者超出范围，就不画出来
                if (heroBullet.isLive && heroBullet.inRange())
                    drawBullet(heroBullet, g);
                else hero.getBullets().remove(heroBullet);
            }
        }

        // 画出敌方坦克 和 敌方坦克的子弹
        for (int i = 0; i < enemyTanks.size(); i++) {
            // 生成并画出敌方存活的坦克
            Tank enemyTank = enemyTanks.get(i);
            if(enemyTank.isLive)
                drawTank(enemyTank, g);

            // 画出坦克的所有子弹
            for(int j = 0; j < enemyTank.getBullets().size(); j++) {
                Bullet enemyTankBullet = enemyTank.getBullets().get(j);
                if (enemyTankBullet.isLive && enemyTankBullet.inRange())   // 子弹在范围内就绘制，不在范围内就要移除出去。
                    drawBullet(enemyTankBullet, g);
                else enemyTank.getBullets().remove(enemyTankBullet);
            }
        }

        // 画出爆炸图像 爆炸图像是单独管理的，所以和敌方坦克无关。
        for(int j = 0; j < bombs.size(); j++)
        {
            Bomb bomb = bombs.get(j);
            if(bomb.isLive())
                drawBomb(bomb, g);
        }
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
    public void drawBullet(Bullet bullet, Graphics g){
        if(bullet == null) return;
        g.setColor(bullet.getColor());
        g.fillOval(bullet.getX(), bullet.getY(), bullet.getRadian(), bullet.getRadian());
    }
    public void drawBomb(Bomb bomb, Graphics g){
        if(bomb.getLife() > 6){
            g.drawImage(bomb.getImage01(), bomb.getX(), bomb.getY(), 60, 60, this);
        }
        else if(bomb.getLife() > 3){
            g.drawImage(bomb.getImage02(), bomb.getX(), bomb.getY(), 60, 60, this);
        }
        else if(bomb.getLife() > 0){
            g.drawImage(bomb.getImage03(), bomb.getX(), bomb.getY(), 60, 60, this);
        }
        else if(bomb.getLife() == 0)    // 炸弹生命值 = 0 则从数组中删除
        {
            bomb.setLive(false);
            bombs.remove(bomb);
        }
        bomb.lifeDown();    // 让炸弹的生命值减少
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
            // 移动时首先判断是否重叠，不好确定清除的位置，最好还是重写类
            case KeyEvent.VK_W:
                hero.CheckAllTankCovered(allTanks);
                hero.moveUp();
                hero.getCanNotMoveOrientations().clear();
                break;
            case KeyEvent.VK_S:
                hero.CheckAllTankCovered(allTanks);
                hero.moveDown();
                hero.getCanNotMoveOrientations().clear();
                break;
            case KeyEvent.VK_A:
                hero.CheckAllTankCovered(allTanks);
                hero.moveLeft();
                hero.getCanNotMoveOrientations().clear();
                break;
            case KeyEvent.VK_D:
                hero.CheckAllTankCovered(allTanks);
                hero.moveRight();
                hero.getCanNotMoveOrientations().clear();
                break;
            case KeyEvent.VK_J:
                // 我方英雄发射子弹，用子线程处理。
                // 需要MyPanel不停重绘子弹
                // 当子弹移动到面板边界时，销毁该线程。
                // shot()方法中启动了子弹线程
                hero.shot();
                // System.out.println("英雄坦克子弹个数：" + hero.getBullets().size());
                /*for (int i = 0; i < hero.getBullets().size(); i++) {
                    new Thread(hero.getBullets().get(i)).start();
                }*/
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

    public Tank getHero() {
        return hero;
    }

    public Vector<Tank> getEnemyTanks() {
        return enemyTanks;
    }
}
