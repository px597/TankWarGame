
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Scanner;

/**
 * Created on 2022/2/2 20:02.
 * 2022.4.6 记录玩家的总成绩
 * @author Peng Xin
 * @version 1.0
 */

enum Orientation {        // 坦克朝向
    LEFT,RIGHT,UP,DOWN
}

public class TankWarGame extends JFrame {
    private TankPanel tankPanel;
    public static final int WIDTH = 1000, HEIGHT = 750;


    public static void main(String[] args) throws IOException {
        // 启动坦克大战游戏
        System.out.println("请选择游戏模式(输入其他退出)：");
        System.out.println("1、新游戏 \t2、读取存档 \t ");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.next();

        // 根据输入选择开始新游戏开始读取存档，并设置isRestore布尔值
        if(str.equals("1")){
            Recorder.setIsRestored(false);
        }
        else if(str.equals("2")){
            // 读取配置数据
            Recorder.restore();
            Recorder.setIsRestored(true);
        }
        else
            System.exit(0);

        new TankWarGame().TankWarGameStart();
    }
    public void TankWarGameStart() throws IOException {


        // 初始化一个面板 添加到框架中，并设置尺寸和可见性
        tankPanel = new TankPanel();
        // 创建新的线程启动
        Thread repaintThread = new Thread(tankPanel);
        repaintThread.start();

        this.add(tankPanel);
        this.setSize(WIDTH + 300 , HEIGHT);
        this.setVisible(true);

        // 此面板监听键盘事件，即只有选中了本面板之后的按键响应才有效。
        this.addKeyListener(tankPanel);
        // 设置点击窗口关闭按钮时程序退出。
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 在JFrame中增加相应的关闭窗口的相应
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                // 保存相关数据
                Recorder.setHero(tankPanel.getHero());
                Recorder.setEnemyTanks(tankPanel.getEnemyTanks());
                Recorder.store();

                System.exit(0);
            }
        });
    }
}
