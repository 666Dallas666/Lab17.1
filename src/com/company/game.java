package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

public class game {
    public static void main(String[] args) {
        String rez = JOptionPane.showInputDialog(null, "Введите сложность игры от 1 до 7:", "Сложность игры", 1);
        int slogn = rez.charAt(0) - '0';
        if ((slogn >= 1) && (slogn <= 7)) {
            okno window = new okno(slogn);
            window.setLocationRelativeTo(null);
        }
    }
}
class okno extends JFrame{
    private pole gameP;
    private int slogn;
    private class myKey implements KeyListener{
        public void keyPressed(KeyEvent e){
            int key_ = e.getKeyCode();
            if(key_ == 27)
                System.exit(0);
            else if(key_ == 37){
                if(gameP.x-30>-48)
                    gameP.x-=30;
                else gameP.x = 752;
            }
            else if(key_ == 39){
                if(gameP.x+30<752)
                    gameP.x+=30;
                else gameP.x=-48;
            }
        }
        public void keyReleased(KeyEvent e){}
        public void keyTyped(KeyEvent e){}
    }
public okno(int slogn){
        this.slogn = slogn;
        addKeyListener(new myKey());
        setFocusable(true);
        setBounds(0,0,800,600);
        setTitle("Сбор подарков");
        gameP = new pole(slogn);
        Container con = getContentPane();
        con.add(gameP);
        setVisible(true);
}
}
class pole extends JPanel {
    private Image shapka;
    private Image fon;
    public int x = 400;
    public int points = 0;
    private int slogn;
    private podar[] gamePodar;
    private Image end_game;
    public Timer timerUpdate, timerDraw;
    public Points point;
    public pole(int slogn) {
        this.slogn = slogn;
        try {
            shapka = ImageIO.read(new File("c:\\shapka.png"));
        } catch (IOException ex) {
        }
        try {
            fon = ImageIO.read(new File("c:\\fon.png"));
        } catch (IOException ex) {
        }
        try {
            end_game = ImageIO.read(new File("c:\\end_game.png"));
        } catch (IOException ex) {
        }
        gamePodar = new podar[7];
        for (int i = 0; i < 7; i++) {
            try {
                gamePodar[i] = new podar(ImageIO.read(new File("c:\\p" + i + ".png")));
            } catch (IOException ex) {
            }
        }
        timerUpdate = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateStart();
            }
        });
        timerUpdate.start();
        timerDraw = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        timerDraw.start();
    }

    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        gr.drawImage(fon, 0, 0, null);
        gr.drawImage(shapka, x, 465, null);
        for (int i = 0; i < 7; i++) {
            gamePodar[i].draw(gr);
            if (gamePodar[i].act == true) {
                if ((gamePodar[i].y + gamePodar[i].img.getHeight(null)) >= 480)
                    if ((gamePodar[i].x - x) > 145 || (gamePodar[i].x - x) < 0) {
                        gr.drawImage(end_game, 100, 100, null);
                        timerDraw.stop();
                        timerUpdate.stop();
                        point = new Points(points);
                        break;
                    } else {
                        gamePodar[i].act = false;
                        points++;
                    }
            }
        }
    }
    private void updateStart() {
        int kol =0;
        for(int i = 0; i < 7; i++){
            if(gamePodar[i].act == false){
                if(kol<slogn){
                    gamePodar[i].start();
                    break;
                }
            }
            else kol++;
        }
    }
}
class Points{
    public JLabel res;
    /*private class Key1 implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}
        public void keyPressed(KeyEvent e) {
            int key_ = e.getKeyCode();
            if (key_ == 27) {
                System.exit(0);
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {}
    }*/
    Points(int points){
        res = new JLabel();
        JFrame frame = new JFrame("Результат");
        //addKeyListener(new Key1());
        res.setText("Вы набрали "+points+ " очков");
        frame.setMinimumSize(new Dimension(100,100));
        frame.add(res);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
class podar{
    public Image img;
    public int x,y;
    public Boolean act;
    Timer timerUpdate;
    public podar(Image img){
       timerUpdate = new Timer(60, new ActionListener(){
           public void actionPerformed(ActionEvent e){
               vniz();
           }
       });
       this.img = img;
       act = false;
    }
    public void start(){
        timerUpdate.start();
        y = 0;
        x = (int)(Math.random()*700);
        act = true;
    }
    public void vniz(){
        if (act == true){
            y+=4;
        }
        if((y+img.getHeight(null))>=480){
            timerUpdate.stop();
        }
    }
    public void draw(Graphics gr){
        if(act == true){
            gr.drawImage(img,x,y,null);
        }
    }
}