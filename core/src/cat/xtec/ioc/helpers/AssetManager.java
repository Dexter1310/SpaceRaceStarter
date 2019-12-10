package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.swing.text.html.ImageView;

public class AssetManager {

    // Sprite Sheet
    public static Texture sheet;
    public static Texture verde;

    // Nau i fons
    public static TextureRegion spacecraft, spacecraftDown, spacecraftUp, background,pause;
    public static TextureRegion[] asteroid;
    public static Animation asteroidAnim;

    // Asteroid
    // Dinero del bonus
    public static TextureRegion[] dinero;
    public static Animation dineroAnim;
    public static TextureRegion[] dineroAzul;
    public static Animation dineroAzulAnim;

    // Explosió
    public static TextureRegion[] explosion;
    public static Animation explosionAnim;

    //Todo: ejercicio 3.2// Sons//
    public static Sound explosionSound;
    public static Sound bonus;
    public static Music bonusExtra;
    public static Music music;


    // Font
    public static BitmapFont font;





    public static void load() {

        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        sheet = new Texture(Gdx.files.internal("sheet.png"));
        sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
//TODO: ejercicio 1 Asignamos unas nuevas texturas del archvo verdes.jps ara crear nuevo personaje y nuevas formas del bonus y asteoides ("estrellas")
        verde = new Texture(Gdx.files.internal("verdes.png"));//nuevo Sprite añadido
        verde.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
//TODO: ejercicio 2.2 Valores al nuevo personaje para darle la posición según indique los movimientos:
        // Sprites nuevo muñeco verde
        spacecraft = new TextureRegion(verde, 12, 9, 53, 64);
        spacecraft.flip(false, true);

        spacecraftUp = new TextureRegion(verde, 72, 9, 53, 64);
        spacecraftUp.flip(false, true);

        spacecraftDown = new TextureRegion(verde, 130, 9, 53, 64);
        spacecraftDown.flip(false, true);

        pause= new TextureRegion(verde,566,5,65,72);
        pause.flip(false,true);




        asteroid = new TextureRegion[16];
        for (int i = 0; i < asteroid.length; i++) {
            asteroid[i] = new TextureRegion(verde, i * 50, 79, 50, 50);
            //  asteroid[i].flip(false, true);
        }
        // Carreguem  les monedes:
        dinero=new TextureRegion[16];
        for (int i = 0; i < dinero.length; i++) {
            dinero[i] = new TextureRegion(verde, i * 50, 132, 50, 50);
            //  asteroid[i].flip(false, true);
        }
        dineroAzul=new TextureRegion[16];
        for (int i = 0; i < dineroAzul.length; i++) {
            dineroAzul[i] = new TextureRegion(verde, i * 50, 188, 50, 55);
            //  asteroid[i].flip(false, true);
        }

        dineroAnim = new Animation(0.05f, dinero);
        dineroAnim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
        dineroAzulAnim = new Animation(0.05f, dinero);
        dineroAzulAnim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        // Creem l'animació de l'asteroid i fem que s'executi contínuament en sentit anti-horari
        asteroidAnim = new Animation(0.05f, asteroid);
        asteroidAnim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        // Creem els 16 estats de l'explosió
       explosion = new TextureRegion[16];

        // Carreguem els 16 estats de l'explosió
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                explosion[index++] = new TextureRegion(sheet, j * 64,  i * 64 + 49, 64, 64);
                //  explosion[index-1].flip(false, true);
            }
        }

        // Finalment creem l'animació
        explosionAnim = new Animation(0.04f, explosion);

        // Fons de pantalla
        background = new TextureRegion(sheet, 0, 177, 480, 135);

        /******************************* Sounds *************************************/
        // Explosió
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        //TODO: ejercico 4.2 sonido  del  bonus de las monedas:

        //bonus
        bonus = Gdx.audio.newSound(Gdx.files.internal("sounds/mario-coin.mp3"));
        //bonusExtra
        bonusExtra = Gdx.audio.newMusic(Gdx.files.internal("sounds/live.mp3"));
        bonusExtra.setVolume(0.3f);


        // Música del joc
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/Afterburner.ogg"));
        music.setVolume(0.6f);
        music.setLooping(true);




        /******************************* Text *************************************/
        // Font space
        FileHandle fontFile = Gdx.files.internal("fonts/space.fnt");
        font = new BitmapFont(fontFile, true);
        font.getData().setScale(0.4f);


    }



    public static void dispose() {

        // Alliberem els recursos gràfics i de audio
        sheet.dispose();
        verde.dispose();
        explosionSound.dispose();
        music.dispose();


    }
}
