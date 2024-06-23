
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class ChromeDinosaur extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 750;
    int boardHeight = 250;

    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;

    class Block {
        int x, y, width, height;
        Image img;

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    int dinosaurWidth = 88;
    int dinosaurHeight = 94;
    int dinosaurX = 50;
    int dinosaurY = boardHeight - dinosaurHeight;

    Block dinosaur;

    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;
    int cactusHeight = 70;
    int cactusX = 700;
    int cactusY = boardHeight - cactusHeight;
    ArrayList<Block> cactusArray;

    int velocityX = -12;
    int velocityY = 0;
    int gravity = 1;

    boolean gameOver = false;
    boolean isPaused = false;
    int score = 0;
    int level = 1;

    Timer gameLoop;
    Timer placeCactusTimer;

    public ChromeDinosaur() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);

        //Image urls
        dinosaurImg = new ImageIcon(getClass().getResource("./img/dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinosaurJumpImg = new ImageIcon(getClass().getResource("./img/dino-jump.png")).getImage();
        cactus1Img = new ImageIcon(getClass().getResource("./img/cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./img/cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./img/cactus3.png")).getImage();

        dinosaur = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurImg);
        cactusArray = new ArrayList<>();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        placeCactusTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeCactus();
            }
        });
        placeCactusTimer.start();
    }

    void placeCactus() {
        if (gameOver || isPaused) {
            return;
        }

        double placeCactusChance = Math.random();
        if (placeCactusChance > .90) {
            Block cactus = new Block(cactusX, cactusY, cactus3Width, cactusHeight, cactus3Img);
            cactusArray.add(cactus);
        } else if (placeCactusChance > .70) {
            Block cactus = new Block(cactusX, cactusY, cactus2Width, cactusHeight, cactus2Img);
            cactusArray.add(cactus);
        } else if (placeCactusChance > .50) {
            Block cactus = new Block(cactusX, cactusY, cactus1Width, cactusHeight, cactus1Img);
            cactusArray.add(cactus);
        }

        if (cactusArray.size() > 10) {
            cactusArray.remove(0);
        }
    }


    // Graphics were taken from ImKennyYip/ chrome-dinosaur-java
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    void draw(Graphics g) {
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null);

        for (Block cactus : cactusArray) {
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }

        g.setColor(Color.black);
        g.setFont(new Font("Courier", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + score, 10, 35);
            g.drawString("Press R to Restart", 10, 70);
        } else {
            g.drawString("Score: " + score, 10, 35);
            g.drawString("Level: " + level, 10, 70);
            if (isPaused) {
                g.drawString("Paused", boardWidth / 2 - 50, boardHeight / 2);
            }
        }
    }

    void move() {
        if (isPaused) return;

        velocityY += gravity;
        dinosaur.y += velocityY;

        if (dinosaur.y > dinosaurY) {
            dinosaur.y = dinosaurY;
            velocityY = 0;
            dinosaur.img = dinosaurImg;
        }

        for (Block cactus : cactusArray) {
            cactus.x += velocityX;

            if (collision(dinosaur, cactus)) {
                gameOver = true;
                dinosaur.img = dinosaurDeadImg;
            }
        }

        if (score % 100 == 0 && score != 0) {
            level++;
            velocityX -= 2;
            placeCactusTimer.setDelay(placeCactusTimer.getDelay() - 100);
        }

        score++;
    }

    // Collision detectmemt and key event handlers and listeners were also from source code on github
    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placeCactusTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                if (dinosaur.y == dinosaurY && !isPaused) {
                    velocityY = -17;
                    dinosaur.img = dinosaurJumpImg;
                }
                if (gameOver) {
                    restartGame();
                }
                break;
            case KeyEvent.VK_P:
                if (!gameOver) {
                    isPaused = !isPaused;
                }
                break;
            case KeyEvent.VK_R:
                if (gameOver) {
                    restartGame();
                }
                break;
        }
    }

    void restartGame() {
        dinosaur.y = dinosaurY;
        dinosaur.img = dinosaurImg;
        velocityY = 0;
        cactusArray.clear();
        score = 0;
        level = 1;
        velocityX = -12;
        placeCactusTimer.setDelay(1500);
        gameOver = false;
        isPaused = false;
        gameLoop.start();
        placeCactusTimer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chrome Dinosaur Game");
        ChromeDinosaur game = new ChromeDinosaur();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}


