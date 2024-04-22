package com.mygdx.infinitegolf.scene;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mygdx.infinitegolf.gameobject.model.PlayerModel;
import com.mygdx.infinitegolf.utils.Assets;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

public class EndScreen extends Scene {

    private Game game;

    private HashMap<Integer, Integer> perHoleScore;

    public EndScreen(Game game, HashMap<Integer, Integer> perHoleScore) {
        this.game = game;
        this.perHoleScore = perHoleScore;
    }

    private Stage stage;
    private Music backgroundMusic;

    public void initScene() {
        Assets.init();
        this.loadAssets();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(30, 30 * (h / w));
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        gameObjectViews = new TreeMap<>();


        stage = new Stage(new StretchViewport(w, h));
        Gdx.input.setInputProcessor(stage);

        Texture backgroundTexture = new Texture(Gdx.files.internal("Images/Menu.jpg"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(w, h);

        Texture quitButtonTexture = new Texture(Gdx.files.internal("Images/QuitButton.png"));
        TextureRegion quitButtonRegion = new TextureRegion(quitButtonTexture);
        ImageButton.ImageButtonStyle quitButtonStyle = new ImageButton.ImageButtonStyle();
        quitButtonStyle.imageUp = new TextureRegionDrawable(quitButtonRegion);
        quitButtonStyle.imageDown = new TextureRegionDrawable(quitButtonRegion);
        final ImageButton quitButton = new ImageButton(quitButtonStyle);

        Texture restartButtonTexture = new Texture(Gdx.files.internal("Images/RestartButton.png"));
        TextureRegion restartButtonRegion = new TextureRegion(restartButtonTexture);
        ImageButton.ImageButtonStyle restartButtonStyle = new ImageButton.ImageButtonStyle();
        restartButtonStyle.imageUp = new TextureRegionDrawable(restartButtonRegion);
        restartButtonStyle.imageDown = new TextureRegionDrawable(restartButtonRegion);
        final ImageButton restartButton = new ImageButton(restartButtonStyle);

        float buttonWidth = 250;
        float buttonHeight = 200;

        quitButton.setWidth(buttonWidth);
        quitButton.setHeight(buttonHeight);
        restartButton.setWidth(buttonWidth);
        restartButton.setHeight(buttonHeight);


        HashMap<Integer, Integer> mockScore = new HashMap<>();
        int total = 0;
        for (int i = 0; i < perHoleScore.size(); i++) {
            mockScore.put(i, perHoleScore.get(i));
            total += perHoleScore.get(i);
        }

        Table scorecardTable = new Table();
        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.WHITE);
        bgPixmap.fill();
        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
        scorecardTable.setBackground(textureRegionDrawableBg);

        BitmapFont font = new BitmapFont();
        Label holeHeader = new Label("Hole", new Label.LabelStyle(font, Color.BLACK));
        holeHeader.setFontScale(2f);
        scorecardTable.add(holeHeader).pad(5);

        for (int holeNumber : mockScore.keySet()) {
            Label holeLabel = new Label(String.valueOf(holeNumber + 1), new Label.LabelStyle(font, Color.BLACK));
            holeLabel.setColor(Color.BLACK); // Set label text color
            holeLabel.setFontScale(2f);
            scorecardTable.add(holeLabel).pad(5);
        }
        Label holeLabel = new Label("TOTAL", new Label.LabelStyle(font, Color.RED));
        holeLabel.setColor(Color.RED); // Set label text color
        holeLabel.setFontScale(2f);
        scorecardTable.add(holeLabel).pad(5);
        scorecardTable.row();
        Label scoreHeader = new Label("Score", new Label.LabelStyle(font, Color.BLACK));
        scoreHeader.setFontScale(2f);
        scorecardTable.add(scoreHeader).pad(5);
        for (int score : mockScore.values()) {
            Label scoreLabel = new Label(String.valueOf(score), new Label.LabelStyle(font, Color.BLACK));
            scoreLabel.setColor(Color.BLACK); // Set label text color
            scoreLabel.setFontScale(2f);
            scorecardTable.add(scoreLabel).pad(5);
        }
        Label scoreLabel = new Label(String.valueOf(total), new Label.LabelStyle(font, Color.RED));
        scoreLabel.setColor(Color.RED); // Set label text color
        scoreLabel.setFontScale(2f);
        scorecardTable.add(scoreLabel).pad(5);

        float centerX = w / 2f;
        float tableWidth = 450;
        float tableHeight = 150;
        scorecardTable.setWidth(tableWidth);
        scorecardTable.setHeight(tableHeight);
        scorecardTable.setBounds(centerX - scorecardTable.getWidth() / 2f, 300, tableWidth, tableHeight); // Set position and dimensions
        quitButton.setPosition(centerX - quitButton.getWidth() / 2f, 0);
        restartButton.setPosition(centerX - restartButton.getWidth() / 2f, 100);

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle exit button click
                Gdx.app.exit();
                endGame();
            }
        });

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle exit button click
                Hole hole = new Hole(game, "Maps/Hole1.tmx");
                hole.initScene();
                game.setScreen(hole);
            }
        });

        stage.addActor(backgroundImage);
        stage.addActor(quitButton);
        stage.addActor(restartButton);
        stage.addActor(scorecardTable);

    }

    @Override
    public void updateScene(float dt) {

    }

    @Override
    public void render(float dt) {
        super.render(dt);
        // Draw the stage
        stage.draw();
    }

    @Override
    public void dispose() {

    }

    private void loadAssets() {
        Assets.loadAll();
    }

    private void endGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name to be added to the leaderboard: ");
        String name = scanner.nextLine();
        int totalScore = 0;
        for (int value : perHoleScore.values()) {
            totalScore += value;
        }
        addScoreToDB(totalScore, name);
        printLeaderboard();
        scanner.close();
        System.exit(0); //fixme end game screen
    }

    private void printLeaderboard() {
        List<PlayerModel> leaderboard = getScoresFromDB();
        System.out.println("LEADERBOARD:");
        int counter = 1;
        for (PlayerModel player : leaderboard) {
            if (counter > 10) {
                break;
            }
            System.out.println(counter + ": " + player.getName() + " - " + player.getScore());
            counter++;
        }
    }

    private void addScoreToDB(int score, String name) {
        @SuppressWarnings("AuthLeak") String uri = "mongodb+srv://InfiniteGolfDev:InfiniteGolfDev@cluster0.ilveng7.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("golfScores");
            MongoCollection<Document> collection = database.getCollection("golfScores");
            // Create a document with the number field
            Document doc = new Document("name", name).append("score", score);
            // Insert the document into the collection
            collection.insertOne(doc);
            System.out.println("Number inserted successfully.");
        }
    }

    @SuppressWarnings("NewApi")
    private List<PlayerModel> getScoresFromDB() {
        @SuppressWarnings("AuthLeak") String uri = "mongodb+srv://InfiniteGolfDev:InfiniteGolfDev@cluster0.ilveng7.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        List<PlayerModel> leaderboard = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("golfScores");
            MongoCollection<Document> collection = database.getCollection("golfScores");
            try (MongoCursor<Document> cursor = collection.find().iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    // Extract name and score from each document
                    String name = doc.getString("name");
                    int score = doc.getInteger("score");
                    // Create a Player object and add it to the list
                    leaderboard.add(new PlayerModel(name, score));
                }
            }
        }
        leaderboard.sort(Comparator.comparingInt(PlayerModel::getScore));
        return leaderboard;
    }

}
