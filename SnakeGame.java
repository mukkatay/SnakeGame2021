import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Random;


public class SnakeGame {
    public static void main(String[] args) {

        Frame frame = new Frame();
    }
}


class Panel extends JPanel implements ActionListener {

    static final int Screen_w = 800;
    static final int Screen_h = 600;
    static final int Unit_size = 25;
    static final int Game_Units = (Screen_w*Screen_h)/(Unit_size*Unit_size);
    static final int Delay = 130;

    final int x[] = new int[Game_Units];

    final int y[] = new int[Game_Units];


    final int x2[] = new int[Game_Units];

    final int y2[] = new int[Game_Units];

    int bodyParts = 4;
    int bodyParts2 = 4;

    int applesEaten;
    int applesEaten2;

    int appleX;
    int appleY;

    char direction = 'R';
    char direction2 = 'R';
    boolean running = false;

    Timer timer;
    Random random;

    Panel(){
        random = new Random();
        this.setPreferredSize(new Dimension(Screen_w,Screen_h));
        this.setBackground(new Color(100,55,150));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(Delay,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if(running) {

            g.setColor(new Color(73,156,84));
            g.fillOval(appleX, appleY, Unit_size, Unit_size);

            for(int i = 0; i< bodyParts;i++) {
                    g.setColor(new Color(100, 255, 100));
                    g.fillRect(x[i], y[i], Unit_size, Unit_size);

            }
            for(int j = 0; j< bodyParts2;j++) {
                g.setColor(new Color(255,255,0));
                g.fillRect(x2[j], y2[j], Unit_size, Unit_size);
            }


            g.setColor(Color.white);
            g.setFont( new Font("DialogInput",Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("First player: "+applesEaten,100 , Screen_h-10);
            g.drawString("Second player: "+applesEaten2, 500, Screen_h-10);
        }
        else {
            gameOver(g);
        }

    }

    public void newApple(){
        appleX = random.nextInt((int)(Screen_w/Unit_size))*Unit_size;
        appleY = random.nextInt((int)(Screen_h/Unit_size))*Unit_size;
    }

    public void move(){
        for(int i = bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        for(int i = bodyParts2;i>0;i--) {

            x2[i] = x2[i-1];
            y2[i] = y2[i-1];
        }
        switch(direction) {
            case 'U':
                y[0] = y[0] - Unit_size;
                break;
            case 'D':
                y[0] = y[0] + Unit_size;
                break;
            case 'L':
                x[0] = x[0] - Unit_size;
                break;
            case 'R':
                x[0] = x[0] + Unit_size;
                break;
        }
        switch(direction2) {
            case 'U':
                y2[0] = y2[0] - Unit_size;
                break;
            case 'D':
                y2[0] = y2[0] + Unit_size;
                break;
            case 'L':
                x2[0] = x2[0] - Unit_size;
                break;
            case 'R':
                x2[0] = x2[0] + Unit_size;
                break;
        }
    }

    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
        if((x2[0] == appleX) && (y2[0] == appleY)) {
            bodyParts2++;
            applesEaten2++;
            newApple();
        }
    }

    public void checkCollisions(){

        for(int i = bodyParts;i>0;i--) {
            if((x[0] == x[i])&& (y[0] == y[i])) {
                running = false;
            }
            if((x2[0] == x2[i])&& (y2[0] == y2[i])) {
                running = false;
            }
        }

        if(x[0] < 0 || x2[0] < 0) {
            running = false;
        }

        if(x[0] > Screen_w || x2[0] > Screen_w) {
            running = false;
        }

        if(y[0] < 0 || y2[0] < 0) {
            running = false;
        }

        if(y[0] > Screen_h || y2[0] > Screen_h) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //Текст

        g.setColor(new Color(255,255,0));
        g.setFont( new Font("DialogInput",Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (Screen_w - metrics2.stringWidth("Game Over"))/2, Screen_h/2);

    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_A:
                    if(direction2 != 'R') {
                        direction2 = 'L';
                    }
                    break;
                case KeyEvent.VK_D:
                    if(direction2 != 'L') {
                        direction2 = 'R';
                    }
                    break;
                case KeyEvent.VK_W:
                    if(direction2 != 'D') {
                        direction2 = 'U';
                    }
                    break;
                case KeyEvent.VK_S:
                    if(direction2 != 'U') {
                        direction2 = 'D';
                    }
                    break;
            }
        }
    }
}
