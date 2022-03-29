import javax.swing.*;

/**
 * Created on 2022/2/2 20:02.
 *
 * @author Peng Xin
 * @version 1.0
 */

enum Orientation {        // 坦克朝向
    LEFT,RIGHT,UP,DOWN
}

public class TankWarGame extends JFrame {
    private TankPanel tankPanel;
    public static final int WIDTH = 1000, HEIGHT = 750;

    public static void main(String[] args) {
        // 启动坦克大战游戏
        new TankWarGame().TankWarGameStart();
    }
    public void TankWarGameStart(){
        // 初始化一个面板 添加到框架中，并设置尺寸和可见性
        tankPanel = new TankPanel();
        // 创建新的线程启动
        Thread repaintThread = new Thread(tankPanel);
        repaintThread.start();

        this.add(tankPanel);
        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);

        // 此面板监听键盘事件，即只有选中了本面板之后的按键响应才有效。
        this.addKeyListener(tankPanel);

        // 设置点击窗口关闭按钮时程序退出。
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
