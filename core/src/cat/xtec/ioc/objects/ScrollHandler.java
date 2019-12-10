package cat.xtec.ioc.objects;

import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;


public class ScrollHandler extends Group {

    // Fons de pantalla
    Background bg, bg_back;

    // Asteroides
    int numAsteroids;
    //monedas
    int numMonedas;
    int numMonedasAzul;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Dinero> dineros;
    private ArrayList<DineroAzul> dinerosAzul;

    // Objecte Random
    Random r;
    Random r1;
    Random r2;
//TODo: ejercicio 3: medidas aleatorias para las monedas amarilla y azul:
    // Definim una mida aleatòria entre el mínim i el màxim
    float newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;
    float newSizeDinero = Methods.randomFloat(Settings.MIN_MONEDA,Settings.MAX_MONEDA) *34;
    float newSizeDineroAzul = Methods.randomFloat(Settings.MIN_MONEDA,Settings.MAX_MONEDA) *34;
    int velocidad;


    public ScrollHandler() {
       velocidad= Settings.BG_SPEED;

        // Creem els dos fons
        bg = new Background(0, 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, velocidad);
        bg_back = new Background(bg.getTailX(), 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, velocidad);

        // Afegim els fons al grup
        addActor(bg);
        addActor(bg_back);

        // Creem l'objecte random
        r = new Random();
        r1= new Random();
        r2= new Random();

        // Comencem amb 3 asteroids
       numAsteroids = 3;
       numMonedas=3;
       numMonedasAzul=3;

        // Creem l'ArrayList
        asteroids = new ArrayList<Asteroid>();
        dineros=new ArrayList<Dinero>();
        dinerosAzul=new ArrayList<DineroAzul>();


        // Afegim el primer Asteroid a l'Array i al grup
        Asteroid asteroid = new Asteroid(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED);
        asteroids.add(asteroid);
        addActor(asteroid);

        Dinero dinero=new Dinero(Settings.GAME_WIDTH,r1.nextInt(Settings.GAME_HEIGHT - (int) newSizeDinero), newSizeDinero, newSizeDinero, Settings.SCORE_SPEED);
        dineros.add(dinero);
        addActor(dinero);

        DineroAzul dineroAzul=new DineroAzul(Settings.GAME_WIDTH,r2.nextInt(Settings.GAME_HEIGHT - (int) newSizeDineroAzul), newSizeDineroAzul, newSizeDineroAzul, Settings.SCORE_BLUE);
        dinerosAzul.add(dineroAzul);
        addActor(dineroAzul);

        // Des del segon fins l'últim asteroide
        for (int i = 1; i < numAsteroids; i++) {
            // Creem la mida al·leatòria
            newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 20;
            // Afegim l'asteroid.
            asteroid = new Asteroid(asteroids.get(asteroids.size() - 1).getTailX() + Settings.ASTEROID_GAP, r1.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED);
            // Afegim l'asteroide a l'ArrayList
            asteroids.add(asteroid);
            // Afegim l'asteroide al grup d'actors
            addActor(asteroid);
        }

        // Des del segon fins l'últim moneda
        for (int i = 1; i < numMonedas; i++) {
            // Creem la mida al·leatòria
            newSizeDinero = Methods.randomFloat(Settings.MIN_MONEDA,Settings.MAX_MONEDA) ;
            // Afegim moneda.
            dinero = new Dinero(dineros.get(dineros.size() - 1).getTailX() + Settings.MONEDA_GAP, r1.nextInt(Settings.GAME_HEIGHT - (int) newSizeDinero), newSizeDinero, newSizeDinero, Settings.SCORE_SPEED);
            // Afegim moneda a l'ArrayList
            dineros.add(dinero);
            // Afegim moneda al grup d'actors
            addActor(dinero);
        }
        for (int i = 1; i < numMonedasAzul; i++) {
            newSizeDineroAzul = Methods.randomFloat(Settings.MIN_MONEDA,Settings.MAX_MONEDA) ;
            dineroAzul = new DineroAzul(dineros.get(dinerosAzul.size() - 1).getTailX() + Settings.MONEDA_GAP_BLUE, r1.nextInt(Settings.GAME_HEIGHT - (int) newSizeDineroAzul), newSizeDineroAzul, newSizeDineroAzul, Settings.SCORE_BLUE);
            dinerosAzul.add(dineroAzul);
            addActor(dineroAzul);
        }

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Si algun element està fora de la pantalla, fem un reset de l'element.
        if (bg.isLeftOfScreen()) {
            bg.reset(bg_back.getTailX());

        } else if (bg_back.isLeftOfScreen()) {
            bg_back.reset(bg.getTailX());

        }

        for (int i = 0; i < asteroids.size(); i++) {

            Asteroid asteroid = asteroids.get(i);
            if (asteroid.isLeftOfScreen()) {
                if (i == 0) {
                    asteroid.reset(asteroids.get(asteroids.size() - 1).getTailX() + Settings.ASTEROID_GAP);
                } else {
                    asteroid.reset(asteroids.get(i - 1).getTailX() + Settings.ASTEROID_GAP);
                }
            }
        }
        for (int i = 0; i < dineros.size(); i++) {

            Dinero dinero = dineros.get(i);
            if (dinero.isLeftOfScreen()) {
                if (i == 0) {
                    dinero.reset(dineros.get(dineros.size() - 1).getTailX() + Settings.MONEDA_GAP);
                } else {
                    dinero.reset(dineros.get(i - 1).getTailX() + Settings.MONEDA_GAP);
                }
            }
        }
        for (int i = 0; i < dinerosAzul.size(); i++) {

            DineroAzul dineroAzul = dinerosAzul.get(i);
            if (dineroAzul.isLeftOfScreen()) {
                if (i == 0) {
                    dineroAzul.reset(dinerosAzul.get(dinerosAzul.size() - 1).getTailX() + Settings.MONEDA_GAP);
                } else {
                    dineroAzul.reset(dinerosAzul.get(i - 1).getTailX() + Settings.MONEDA_GAP_BLUE);
                }
            }
        }
    }


    public boolean collides(Spacecraft nau) {

        // Comprovem les col·lisions entre cada asteroid i la nau
        for (Asteroid asteroid : asteroids) {
            if (asteroid.collides(nau)) {

                return true;
            }
        }
        return false;
    }
//Todo : ejercicio 3 velocidad
    public boolean collidesMonedas(Spacecraft nau) {


        for (Dinero dinero : dineros) {
            if (dinero.collidesMonedas(nau)) {
                Settings.setBgSpeed(-175);
                return true;
            }

        }
        return false;
    }
    public boolean collidesMonedasAzul(Spacecraft nau) {

        for (DineroAzul dineroAzul : dinerosAzul) {
            if (dineroAzul.collidesMonedasAzul(nau)) {
                Settings.setBgSpeed(-250);
                return true;
            }
        }
        return false;
    }


//Todo ejercicio3: posiciones de monedas amarillas y azules
    public void reset() {

        // Posem el primer asteroid fora de la pantalla per la dreta
        asteroids.get(0).reset(Settings.GAME_WIDTH);
        // Calculem les noves posicions de la resta d'asteroids.
        for (int i = 1; i < asteroids.size(); i++) {
            asteroids.get(i).reset(asteroids.get(i - 1).getTailX() + Settings.ASTEROID_GAP);
        }
        // Posem el primer asteroid fora de la pantalla per la dreta
        dineros.get(0).reset(Settings.GAME_WIDTH);
        // Calculem les noves posicions de la resta d'asteroids.
        for (int i = 1; i < dineros.size(); i++) {
            dineros.get(i).reset(dineros.get(i - 1).getTailX() + Settings.MONEDA_GAP);
        }


    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }
    //Todo : ejercicio 3
    public ArrayList<Dinero> getDineros() {
        return dineros;
    }

}