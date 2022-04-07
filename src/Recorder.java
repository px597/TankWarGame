import java.io.*;
import java.util.Vector;

/**
 * Created on 2022/4/6 19:29.
 *
 * @author Peng Xin
 * @version 1.0
 */

/*
 * 该类记录游戏数据, 和文件交互
 */
public class Recorder {
    // static 在于不用生成类对象即可调用
    private static int shotEnemyTankNumber = 0;
    // 定义IO对象
    private static final String  path = "D:\\files\\JavaProject\\TankWarGame\\src\\game.txt";    // 记录玩家总成绩
    private static ObjectInputStream ois = null;
    private static ObjectOutputStream oos =null;
    private static Tank hero = null;
    private static Vector<Tank> enemyTanks = new Vector<>();
    private static boolean isRestored;

    public static int getShotEnemyTankNumber() {
        return shotEnemyTankNumber;
    }

    public static void addShotEnemyTankNumber() {
        Recorder.shotEnemyTankNumber += 1;
    }

    public static void setIsRestored(boolean b){
        isRestored = b;
    }
    public static boolean getIsRestored(){
        return isRestored;
    }
    public static Tank getHero() {
        return hero;
    }

    public static void setHero(Tank hero) {
        Recorder.hero = hero;
    }

    public static Vector<Tank> getEnemyTanks() {
        return enemyTanks;
    }

    public static void setEnemyTanks(Vector<Tank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    // 游戏开始时将数据加载到游戏中,加载的数据保存到类中的变量中，可通过赋值添加到游戏中
    // 要解决首次restore()数据中不齐全的问题；
    public static void restore(){
        try {
            ois = new ObjectInputStream(new FileInputStream(path));

            shotEnemyTankNumber = ois.readInt();
            int enemyTanksSize = ois.readInt();

            System.out.println(shotEnemyTankNumber);
            System.out.println(enemyTanksSize);

            enemyTanks = (Vector<Tank>) ois.readObject();
            System.out.println(enemyTanks);
            hero = (Tank)ois.readObject();
            System.out.println(hero);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 游戏退出时将数据保存到文件中
    public static void store(){
        try {
            // 分别向文件中写入击中坦克数量，现存坦克总数，以及各个敌方坦克对象
            oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeInt(shotEnemyTankNumber);
            oos.writeInt(enemyTanks.size());
            oos.writeObject(enemyTanks);
            oos.writeObject(hero);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
