import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created on 2022/2/2 17:19.
 * 练习java绘图基础
 * @author Peng Xin
 * @version 1.0
 */

public class DrawCircleTest extends JFrame {
    // 定义一个面板
    private MyPanel myPanel = new MyPanel();
    public static void main(String[] args) {
        new DrawCircleTest().DrawCircle();
    }
    public void DrawCircle(){
        // 把画板放入画框
        this.add(myPanel);
        // 设置画框的尺寸 并可以显示。
        this.setSize(400,300);
        this.setVisible(true);
        // 设置点击窗口关闭按钮时程序退出。
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

//1、先定义一个MyPanel
class MyPanel extends JPanel{

    // Graphics类描述的是一个画笔
    // JPanel类描述的是一个画板
    // JFrame类描述的是一个画框

    // paint方法
    // 1、当组件第一次在屏幕显示的时候，程序会自动的调用paint()方法来绘制组件；
    // 2、窗口尺寸被更改时会实时调用；
    // 3、窗口最大化再最小化时会调用，反之亦然；
    // 4、repaint()函数被调用时。
    @Override
    public void paint(Graphics g) { //绘图方法
        System.out.println("paint方法被调用");
        super.paint(g); // 调用父类的方法完成初始化
        g.setColor(Color.BLUE);
        //绘制出坦克
        //两条履带

//        g.fillRect(0, 0, 10,50);
//        g.fillRect(30, 0, 10,50);
//        g.fillRect(10,10,20,30);    //坦克中间的方块
//        g.fillOval(10,15,20,20);    //坦克中间的圆
//        g.fillRect(18,0,4,25);      //坦克炮筒

//        g.drawOval(0, 0, 100, 100);

/*
        // 画图片
        // 首先获取图片文件
        Image image = Toolkit.getDefaultToolkit().getImage("D:/files/01_Books/08_Java/韩顺平分享资料/分享资料/" +
                "bg.png");
        g.drawImage(image,100,100,Color.BLUE,this);

        //画字符串
        g.drawString("Hello", 200,100);
        //设置画笔的颜色和字体
        g.setColor(Color.RED);
        g.setFont(new Font("Consolas",Font.PLAIN,50));
        g.drawString("Hello", 200,200);
*/
    }
}