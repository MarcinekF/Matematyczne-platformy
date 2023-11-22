import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel implements Runnable
{
    public enum GameState
    {
        running,
        paused,
        menu,
        loadingSaves,
        creatingSave,
        showRewards
    }

    Image background = ImageIO.read(new File("assets/Background/Green.png"));
    int width = 800;
    int height = 600;
    List<List<Integer>> backgroundCords = getBackgroundCords(background, width, height);
    public static GameState gameState = GameState.menu;
    int FPS = 60;
    int velocity = 4;
    public int gravity = 0;
    Random random = new Random();
    String[] QandA = new String[6];
    ArrayList<Platform> platformArrayList = new ArrayList<>();
    Player player = new Player(width/2 + 15, height - 164);
    Menu menu = new Menu(width, height);
    KeyHandler keyH = new KeyHandler();
    RewardManager rewardManager = new RewardManager();
    long lastTime = System.nanoTime();
    long playerAnimationTime = System.nanoTime();
    Font customFont = new Font("Arial", Font.PLAIN, 24);
    String question;
    int answer;
    public static int timeLeft = 30;
    long timeLeftDelay = System.nanoTime();
    long currentTime = System.nanoTime();
    Thread gameThread;
    boolean correctAnswer = false;
    boolean incorrectAnswer = false;
    boolean platformMoved = false;

    public GamePanel() throws IOException
    {
        this.setPreferredSize(new Dimension(width, height)); //ScreenSize
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run()
    { //Game loop
        double drawInterval = 1000000000 / FPS; // program delay 0.0166 sec
        double delta = 0;


        while(gameThread != null)
        {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta > 1)
            {
                try {
                    update();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                repaint();
                delta --;
            }
            if(gameState == GameState.running)
            {
                if(currentTime - playerAnimationTime >=  40000000)
                {
                    playerAnimationTime = currentTime;
                    player.updateSprite();
                    rewardManager.updateSprite();
                    if(platformArrayList.size() == 2 )
                    {
                        if(platformArrayList.get(1).updateSprite())
                        {
                            platformArrayList.remove(1);
                        }
                    }
                }
                if(lastTime - timeLeftDelay > 1000000000)
                {
                    timeLeftDelay = lastTime;
                    timeLeft--;

                    if(player.hit)
                    {
                        try {
                            if(gravity == 0)
                            {
                                player.countHitTime();
                            }
                            else
                            {
                                player.hit = false;
                                player.hitDelay = 0;
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (gravity == -1)
                {
                    try {
                        player.jump();

                        if (player.rect.y <= height / 4)
                        {

                            player.fall();
                            gravity = 1;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                {
                    try {
                        checkCollisions(player, platformArrayList);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
    public void update() throws IOException
    {
        switch (gameState)
        {
            case running ->
            {
                if (correctAnswer)
                {
                    movePlatformInY();
                }
                if(platformMoved)
                {
                    try {
                        platformGoBack();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (incorrectAnswer)
                {
                    movePlatformInX();
                }
                if (gravity != 0)
                {
                    if (keyH.leftPressed) {
                        player.rect.x -= velocity;

                    } else if (keyH.rightPressed) {
                        player.rect.x += velocity;
                    }
                    player.rect.y += velocity * gravity;
                }
                if((keyH.spacePressed) && (player.jumpLimit == 1))
                {
                    gravity = -1;
                }
            }
            case paused ->
            {
                if(keyH.upPressed)
                {
                    menu.pickedAction = (menu.pickedAction - 1 + 5) % 5;
                    keyH.upPressed = false;
                }
                else if (keyH.downPressed)
                {
                    menu.pickedAction = (menu.pickedAction + 1) % 5;
                    keyH.downPressed = false;
                }
                else if(keyH.enterPressed)
                {
                    switch (menu.pickedAction)
                    {
                        case 0:
                            gameState = GameState.running;
                            keyH.enterPressed = false;
                            break;
                        case 1:
                            gameState = GameState.creatingSave;
                            keyH.saveNameFlag = true;
                            keyH.enterPressed = false;
                            break;
                        case 2:
                            gameState = GameState.loadingSaves;
                            keyH.enterPressed = false;
                            break;
                        case 3:
                            gameState = GameState.showRewards;
                            keyH.enterPressed = false;
                            break;
                        case 4:
                            platformArrayList.clear();
                            gameState = GameState.menu;
                            keyH.enterPressed = false;
                            break;
                    }
                    menu.pickedAction = 0;
                }
            }
            case showRewards ->
            {
                if (keyH.enterPressed)
                {
                    gameState = GameState.paused;
                    keyH.enterPressed = false;
                    menu.pickedAction = 0;
                }
            }
            case menu ->
            {
                if(keyH.upPressed)
                {
                    menu.pickedAction = (menu.pickedAction - 1 +3) % 3;
                    keyH.upPressed = false;
                }
                else if (keyH.downPressed)
                {
                    menu.pickedAction = (menu.pickedAction + 1) % 3;
                    keyH.downPressed = false;
                }
                else if(keyH.enterPressed)
                {
                    switch (menu.pickedAction)
                    {
                        case 0:
                            player = new Player(width/2 + 15, height - 164);
                            answer = generateQuiz();
                            question = QandA[0] + " " + QandA[2] + " " + QandA[1] + " = ";
                            platformArrayList.clear();
                            platformArrayList.add(new Platform(width/2, height - 100,"none"));
                            generatePlatforms();
                            timeLeft = 30;
                            timeLeftDelay = System.nanoTime();
                            currentTime = System.nanoTime();
                            timeLeftDelay = System.nanoTime();
                            gameState = GameState.running;
                            keyH.enterPressed = false;
                            break;
                        case 1:
                            gameState = GameState.loadingSaves;
                            keyH.enterPressed = false;
                            break;
                        case 2:
                            System.exit(0);
                            keyH.enterPressed = false;
                            break;
                    }
                    menu.pickedAction = 0;
                }
            }
            case loadingSaves ->
            {
                if(keyH.upPressed)
                {
                    menu.pickedAction = (menu.pickedAction - 1 + Menu.savesList.length + 1) % (Menu.savesList.length + 1);
                    keyH.upPressed = false;
                }
                else if (keyH.downPressed)
                {
                    menu.pickedAction = (menu.pickedAction + 1) % (Menu.savesList.length + 1);
                    keyH.downPressed = false;
                }
                if (keyH.enterPressed)
                {
                    if(menu.pickedAction == Menu.savesList.length)
                    {
                        keyH.enterPressed = false;
                        if (!platformArrayList.isEmpty())
                        {
                            gameState = GameState.paused;
                        }
                        else
                        {
                            gameState = GameState.menu;
                        }
                        break;
                    }
                    player = new Player(width/2 + 15, height - 164);
                    menu.loadGame();
                    platformArrayList.clear();
                    platformArrayList.add(new Platform(width/2, height - 100,"none"));
                    answer = generateQuiz();
                    question = QandA[0] + " " + QandA[2] + " " + QandA[1] + " = ";
                    generatePlatforms();
                    gameState = GameState.running;
                    timeLeft = 30;
                    timeLeftDelay = System.nanoTime();
                    currentTime = System.nanoTime();
                    keyH.enterPressed = false;
                }
            }
            case creatingSave ->
            {
                if(keyH.upPressed)
                {
                    menu.pickedAction = (menu.pickedAction - 1 + 2) % 2;
                    keyH.upPressed = false;
                }
                else if (keyH.downPressed)
                {
                    menu.pickedAction = (menu.pickedAction + 1) % 2;
                    keyH.downPressed = false;
                }

                if (keyH.enterPressed)
                {
                    switch (menu.pickedAction)
                    {
                        case 0:
                            String filePath = "saves/"+ KeyHandler.saveName.toString().trim() + ".txt";
                            menu.saveGame(filePath);
                            keyH.enterPressed = false;
                            keyH.saveNameFlag = false;
                            KeyHandler.saveName.setLength(0);
                            gameState = GameState.paused;
                            break;
                        case 1:
                            keyH.enterPressed = false;
                            keyH.saveNameFlag = false;
                            if(!KeyHandler.saveName.isEmpty())
                            {
                                KeyHandler.saveName.setLength(0);
                            }
                            gameState = GameState.paused;
                            break;
                    }
                    menu.pickedAction = 0;
                }
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setFont(customFont);
        g.setColor(Color.WHITE);

        switch (gameState)
        {
            case loadingSaves -> menu.loadGame(g);

            case creatingSave -> menu.createSave(g);

            case menu -> menu.mainMenu(g);

            case paused -> menu.gamePaused(g);

            case showRewards -> rewardManager.showRewards(g, width, height);

            case running ->
            {
                int i =0;
                for(List<Integer> backgroundCord : backgroundCords)
                {
                    for (int cord : backgroundCord)
                    {
                        g.drawImage(background, cord, i*background.getWidth(null), null);
                    }
                    i++;
                }
                for (Platform p : platformArrayList) {
                    g.drawImage(p.sprite, p.rect.x, p.rect.y, null);
                    if (!Objects.equals(p.answer, "none")) {
                        g.drawString(p.answer, p.rect.x + 20, p.rect.y + 40);
                    }
                }
                g.drawImage(player.sprite, player.rect.x - 15, player.rect.y, 64, 64, null);

                if(Player.streak == 5)
                {
                    Player.highestStreak = 5;
                    rewardManager.paintNewReward(g, width, height);
                }
                if(Player.streak == 10)
                {
                    Player.highestStreak = 10;
                    rewardManager.paintNewReward(g, width, height);
                }
                if(Player.streak == 15)
                {
                    Player.highestStreak = 15;
                    rewardManager.paintNewReward(g, width, height);
                }

                if (Player.lives == 0)
                {
                    platformMoved = false;
                    player.jumpLimit = 0;
                    g.drawString("Wszystkie życia utracone!", 10, 50);
                    g.drawString("Nacisnij dowolny przycisk aby wrócić do menu", width/2 - 200,height - 50);
                }

                else if ((timeLeft <1) && (gravity == 0))
                {
                    player.jumpLimit = 0;
                    g.drawString("Koniec czasu !", 10, 50);
                    g.drawString("Nacisnij dowolny przycisk aby wrócić do menu", width/2 - 200, height - 50);
                    g.setColor(Color.ORANGE);
                    g.drawString(String.valueOf(answer),width/2 + 80,100);
                    g.drawString(question, width/2, 100);
                }

                else {
                    g.drawString(question, width / 2, 100);
                    g.drawString("Pozostałe życia: " + Player.lives, 10, 50);
                    g.drawString("Pozostały czas: " + timeLeft, 10, 80);
                    g.drawString("Punkty: " + Player.points, 10, 110);
                    if (player.answer.equals("correct")) {
                        g.setColor(Color.GREEN);
                        g.drawString(String.valueOf(answer), width / 2 + 80, 100);
                        g.drawString("Odpowiedz poprawna!", width / 2 - 80, 140);
                    } else if (player.answer.equals("incorrect")) {
                        g.setColor(Color.RED);
                        g.drawString(String.valueOf(answer), width / 2 + 80, 100);
                        g.drawString("Odpowiedz niepoprawna!", width / 2 - 80, 140);
                    }
                }
                try {
                    checkPlayerOutside();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
    public void checkPlayerOutside() throws IOException {
        if (player.rect.y > height )
        {
            player.rect.x = width/2 + 15;
            player.rect.y = height - 164;
            Player.lives --;
            player.updateSheet("assets/ScarfKitten/hit.png");
            gravity = 0;
            keyH.spacePressed = false;
            player.jumpLimit = 1;
            player.hit = true;
        }
    }
    public void checkCollisions(Player player, ArrayList<Platform> platforms) throws IOException {
        for (Platform p : platforms)
        {
            if ((player.rect.intersects(p.rect)) && (!p.Animated))
            {
                gravity = 0;
                player.landed();
                if (p.answer.equals(String.valueOf(answer)))
                {
                    if(player.rect.y + 64 > p.rect.y)
                    {
                        player.rect.x -= (player.rect.x - p.rect.x)/3;
                    }
                    timeLeft = 30;
                    timeLeftDelay = System.nanoTime();
                    currentTime = System.nanoTime();
                    timeLeftDelay = System.nanoTime();
                    correctAnswer = true;
                    player.answer = "correct";
                    Player.points ++;
                    Player.streak ++;
                    platformArrayList.clear();
                    p = new Platform(p.rect.x, p.rect.y, "none");
                    platformArrayList.add(p);
                    break;
                }
                else if (!p.answer.equals("none"))
                {
                    timeLeft = 30;
                    timeLeftDelay = System.nanoTime();
                    currentTime = System.nanoTime();
                    timeLeftDelay = System.nanoTime();
                    incorrectAnswer = true;
                    player.answer = "incorrect";
                    if(player.highestStreak < Player.streak)
                    {
                        player.highestStreak = Player.streak;
                    }
                    Player.streak = 0;
                    Player.lives --;
                    for(int i =0; i<3;i++)
                    {
                        platformArrayList.remove(1);
                    }
                    p = new Platform(p.rect.x,p.rect.y, "none");
                    p.Animated = true;
                    gravity = 1;
                    platformArrayList.add(p);
                    player.updateSheet("assets/ScarfKitten/hit.png");
                    break;
                }
                else if (!correctAnswer)
                {
                    if(p.rect.x!= width/2)
                    {
                        platformMoved = true; //błąd
                    }
                    else
                    {
                        player.jumpLimit = 1;
                    }
                }
            }
        }
    }
    public int generateQuiz()   // generates values in QandA array
    // [0] - number1, [1] - number2, [2] - operator, [3] ... [5] - answers
    //returns correct answer
    {
        String[] chars = {"+", "-", "*", "/"};
        int answer = 0;
        int number1 = random.nextInt(9) + 1;
        int number2 = random.nextInt(9) + 1;
        int index = random.nextInt(chars.length);
        String operator = chars[index];

        switch(operator)
        {
            case "+":
                answer = number1 + number2;
                break;
            case "-":
                answer = number1 - number2;
                break;
            case "*":
                answer = number1 * number2;
                break;
            case "/":
                do
                {
                    number1 = random.nextInt(9) + 1;
                    number2 = random.nextInt(9) + 1;
                }while(number1 % number2 != 0);

                answer = number1 / number2;
                break;
        }
        QandA[0] = String.valueOf(number1);
        QandA[1] = String.valueOf(number2);
        QandA[2] = operator;

        int [] wrongAnswers = {answer +2, answer - 2};

        int randomOutput = random.nextInt(3) + 3;

        QandA[randomOutput] = String.valueOf(answer);

        index = 0;
        for (int i=3; i<=5;i++)
        {
            if(i == randomOutput)
            {
                continue;
            }
            QandA[i] = String.valueOf(wrongAnswers[index]);
            index++;
        }

        return answer;
    }

    public void generatePlatforms() throws IOException
    {
        int counter = 1;
        for (int i = 3; i <= 5; i++)
        {
            Platform platform = new Platform(200*counter, height / 2, QandA[i]);
            counter ++;
            platformArrayList.add(platform);
        }

    }

    private void movePlatformInY()
    {
        for (Platform p : platformArrayList)
        {
            p.rect.y += velocity;
            player.rect.y+= velocity;
            if(p.rect.y >= height - 100)
            {
                p.rect.y = height - 100;
                player.rect.y = height - 100 - 64;
                correctAnswer = false;
                platformMoved = true;
                break;
            }
        }
    }
    private void platformGoBack() throws IOException {
        for (Platform p : platformArrayList)
        {
            if (p.rect.x < width/2)
            {
                p.rect.x += velocity;
                player.rect.x+= velocity;
                if(p.rect.x >= width / 2)
                {
                    p.rect.x = width/2;
                    platformMoved = false;
                    player.answer = "none";
                    player.jumpLimit = 1;
                    answer = generateQuiz();
                    question = QandA[0] + " " + QandA[2] + " " + QandA[1] + " = ";
                    generatePlatforms();
                    break;
                }
            }
            else if(p.rect.x > width/2)
            {
                p.rect.x -= velocity;
                player.rect.x-= velocity;
                if(p.rect.x <= width / 2)
                {
                    p.rect.x = width/2;
                    platformMoved = false;
                    player.answer = "none";
                    player.jumpLimit = 1;
                    answer = generateQuiz();
                    question = QandA[0] + " " + QandA[2] + " " + QandA[1] + " = ";
                    generatePlatforms();
                    break;
                }
            }
            else if (Player.lives != 0)
            {
                platformMoved = false;
                player.answer = "none";
                player.jumpLimit = 1;
                answer = generateQuiz();
                question = QandA[0] + " " + QandA[2] + " " + QandA[1] + " = ";
                generatePlatforms();
                break;
            }

        }
    }

    private void movePlatformInX()
    {
        Platform p = platformArrayList.get(0);
        if (player.rect.x < width/2)
        {
            p.rect.x -= velocity;
            if((p.rect.x < player.rect.x - 15) || (gravity == 0))
            {
                incorrectAnswer = false;
            }
        }
        else if(player.rect.x > width/2)
        {
            p.rect.x += velocity;
            if((p.rect.x >= player.rect.x - 15) || (gravity == 0))
            {
                incorrectAnswer = false;
            }
        }
        else if(player.jumpLimit == 1)
        {
            incorrectAnswer = false;
        }
    }

    private List<List<Integer>> getBackgroundCords(Image background, int screenWidth, int screenHeight)
    {
        List<List<Integer>> backgroundCords = new ArrayList<>();

        double imageWidth = background.getWidth(null);
        double imageHeight = background.getHeight(null);
        for (int i = 0; i <= (double)screenWidth / imageWidth ; i++)
        {
            backgroundCords.add(new ArrayList<>());
            for(int j=0; j<= (double)screenHeight / imageHeight + 3; j++)
            {
                backgroundCords.get(i).add(j*background.getHeight(null));
            }
        }
        return backgroundCords;
    }
}