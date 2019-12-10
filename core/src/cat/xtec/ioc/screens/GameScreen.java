package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Button;
import java.awt.Graphics;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.helpers.InputHandler;
import cat.xtec.ioc.objects.Asteroid;
import cat.xtec.ioc.objects.Background;
import cat.xtec.ioc.objects.Dinero;
import cat.xtec.ioc.objects.ScrollHandler;
import cat.xtec.ioc.objects.Spacecraft;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;



public class GameScreen implements Screen {

    String marcado="0";
    String nivel="";
    int total;//puntuación de la partida
    int score;//mejor puntuacion
    // Els estats del joc

    public enum GameState {

        READY, RUNNING, GAMEOVER,PAUSE

    }

    private GameState currentState;

    // Objectes necessaris
    private Stage stage;
    private Spacecraft spacecraft;
    private Spacecraft pausa;
    private ScrollHandler scrollHandler;

    // Encarregats de dibuixar elements per pantalla

    private ShapeRenderer shapeRenderer;
//    private ShapeRenderer shapeRenderer2;
    private Batch batch;

    // Per controlar l'animació de l'explosió
    private float explosionTime = 0;

    // Preparem el textLayout per escriure text
    private GlyphLayout textLayout;




    public GameScreen(Batch prevBatch, Viewport prevViewport) {

        // Iniciem la música
        AssetManager.music.play();

        // Creem el ShapeRenderer
        shapeRenderer = new ShapeRenderer();

        // Creem l'stage i assginem el viewport
        stage = new Stage(prevViewport, prevBatch);

        batch = stage.getBatch();

        // Creem la nau i la resta d'objectes
        spacecraft = new Spacecraft(Settings.SPACECRAFT_STARTX, Settings.SPACECRAFT_STARTY, Settings.SPACECRAFT_WIDTH, Settings.SPACECRAFT_HEIGHT);
        scrollHandler = new ScrollHandler();

        // Afegim els actors a l'stage
        stage.addActor(scrollHandler);
        stage.addActor(spacecraft);
        // Donem nom a l'Actor
        spacecraft.setName("spacecraft");

        // Iniciem el GlyphLayout
        textLayout = new GlyphLayout();
        textLayout.setText(AssetManager.font, "Are you\nready?");

        currentState = GameState.READY;

        //TODO : Ejercicio 4: inicializacion del boton

        Image pausat=new Image(AssetManager.pause);//inicializamos la imagen
        pausat.setName("Pausa");//añadimos un nombre al botón
        pausat.setPosition(190,10);//le asiganamos una posición
        pausat.setSize(30,18);//le asignamos una medida al botón
        stage.addActor(pausat);//y accionamos el stage



        // Assignem com a gestor d'entrada la classe InputHandler
        Gdx.input.setInputProcessor(new InputHandler(this));

    }

    private void drawElements() {

        // Recollim les propietats del Batch de l'Stage
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Pintem el fons de negre per evitar el "flickering"
        //Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Inicialitzem el shaperenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Definim el color (verd)
        shapeRenderer.setColor(new Color(0, 1, 0, 1));

        // Pintem la nau
        shapeRenderer.rect(spacecraft.getX(), spacecraft.getY(), spacecraft.getWidth(), spacecraft.getHeight());

        // Recollim tots els Asteroid
        ArrayList<Asteroid> asteroids = scrollHandler.getAsteroids();
        Asteroid asteroid;
        // Recollim totes monedas
        ArrayList<Dinero> money = scrollHandler.getDineros();




        for (int i = 0; i < asteroids.size(); i++) {

            asteroid = asteroids.get(i);
            switch (i) {
                case 0:
                    shapeRenderer.setColor(1, 0, 0, 1);
                    break;
                case 1:
                    shapeRenderer.setColor(0, 0, 1, 1);
                    break;
                case 2:
                    shapeRenderer.setColor(1, 1, 0, 1);
                    break;
                default:
                    shapeRenderer.setColor(1, 1, 1, 1);
                    break;
            }
            shapeRenderer.circle(asteroid.getX() + asteroid.getWidth() / 2, asteroid.getY() + asteroid.getWidth() / 2, asteroid.getWidth() / 2);
        }

        shapeRenderer.end();

    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        // Dibuixem tots els actors de l'stage
        stage.draw();


        // Depenent de l'estat del joc farem unes accions o unes altres
        switch (currentState) {

            case GAMEOVER:
                updateGameOver(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            case READY:
                updateReady();
                break;
            case PAUSE://TODO: ejercicio 4 : condición de PAUSA para llamar al método de updatePausa()
                updatePausa();
                break;
        }

        //drawElements();

    }

    private void updateReady() {

        // Dibuixem el text al centre de la pantalla
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH / 2) - textLayout.width / 2, (Settings.GAME_HEIGHT / 2) - textLayout.height / 2);
        //stage.addActor(textLbl);
        batch.end();

    }
//TODO : ejercicio 4 metodo de pausa para volver a ready:

    private void updatePausa() {
        AssetManager.music.setVolume(0.1f);//bajamos el volumen de la música
        textLayout.setText(AssetManager.font,"Pause\n\n Points:"+marcado);//añadimos el mensaje de PAUSE en el centro de pantalla
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH / 2) - textLayout.width / 2, (Settings.GAME_HEIGHT / 2) - textLayout.height / 2);
        batch.end();

    }

    private void updateRunning(float delta) {

        AssetManager.music.setVolume(0.6f);//volumen estabilizado

        stage.act(delta);

        // Dibuixem el text al centre de la pantalla
        batch.begin();
        //TODO : ejercicio 2 : posición de marcador en la parte izquierda arriba:
       AssetManager.font.draw(batch, textLayout, 10 ,  10);
        //stage.addActor(textLbl);
        batch.end();
        textLayout = new GlyphLayout();


        textLayout.setText(AssetManager.font,marcado);
        if (scrollHandler.collides(spacecraft)) {// COLISIÓ
            // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
            AssetManager.explosionSound.play();
            stage.getRoot().findActor("spacecraft").remove();
            currentState = GameState.GAMEOVER;



            String mensaje="";
            Integer resultat=Integer.valueOf(marcado);//convertimos a integer y aplicamos condiciones:
            if (resultat<100){
                mensaje="You’re a n00b!";
                nivel="n00b";
            }else if(resultat>=100&&resultat<150){
                mensaje="Well done!";
                nivel="Person";
            }else if(resultat>=150){
                nivel="Pro";
               mensaje= "Oh yeah!! You’re a pro!";
            }
            String texto=mensaje+"\nPoints:"+marcado+"\nGame Over :'(";// Resultado de puntuación
            textLayout.setText(AssetManager.font,texto);
            if(resultat>score){
                score=resultat;
            }

            marcado="0";// recolocamos a cero el marcador para un nuevo juego


        }

        //TODO : EJERCICIO 2: MONEDAS AMARILLAS Y AZULES

        if (scrollHandler.collidesMonedas(spacecraft)) {//MONEDA GROGA
            Integer Diez=1;
            AssetManager.bonus.play();//TODO: ejercicio 4.2 musica añadida al bonus
            Integer sumaDiez =  Integer.valueOf(marcado);
            total=sumaDiez +Diez;
            marcado= String.valueOf(total);
            textLayout.setText(AssetManager.font,marcado);
            Settings.setBgSpeed(-175);


        }
        if (scrollHandler.collidesMonedasAzul(spacecraft)) {//MONEDA BLAVA
            Integer Diez=2;
            AssetManager.bonusExtra.play();//todo ejercicio 4.2 música añadida al bonusExtra
            Integer sumaDiez =  Integer.valueOf(marcado);
            total=sumaDiez +Diez;
            marcado= String.valueOf(total);
            textLayout.setText(AssetManager.font,marcado);
            Settings.setBgSpeed(-250);

        }



    }

    private void updateGameOver(float delta) {
        stage.act(delta);
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH - textLayout.width) / 2, (Settings.GAME_HEIGHT - textLayout.height) / 2);
        // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
        batch.draw((TextureRegion) AssetManager.explosionAnim.getKeyFrame(explosionTime, false), (spacecraft.getX() + spacecraft.getWidth() / 2) - 32, spacecraft.getY() + spacecraft.getHeight() / 2 - 32, 64, 64);
        batch.end();
        explosionTime += delta;

    }

    public void reset() {
        //TODO: ejercicio 5.2 :mostramos los valores de HighScore para mostrarlos en la pantalla de ready
        // Posem el text d'inici
        String win=String.valueOf(score);//convertimos el valor score a texto
        textLayout.setText(AssetManager.font, "level:............"+nivel+"\nHighScore:..."+win+"\n\nAre you\nready?");
        // Cridem als restart dels elements.
        spacecraft.reset();
        scrollHandler.reset();

        // Posem l'estat a 'Ready'
        currentState = GameState.READY;

        // Afegim la nau a l'stage
        stage.addActor(spacecraft);

        // Posem a 0 les variables per controlar el temps jugat i l'animació de l'explosió
        explosionTime = 0.0f;

    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public Spacecraft getSpacecraft() {
        return spacecraft;
    }

    public Stage getStage() {
        return stage;
    }

    public ScrollHandler getScrollHandler() {
        return scrollHandler;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }
}
