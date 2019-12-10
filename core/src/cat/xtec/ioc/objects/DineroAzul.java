package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;

import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class DineroAzul extends  Scrollable{
    private Circle collisionCircle;

    Random r;

    int assetDinero;
    public DineroAzul(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);

        // Creem el cercle
        collisionCircle = new Circle();

        /* Accions */
        r = new Random();
        assetDinero = r.nextInt(15);

        setOrigin();

        // Rotacio
        RotateByAction rotateAction = new RotateByAction();
        rotateAction.setAmount(-190f);
        rotateAction.setDuration(0.4f);
        // Accio de repetició
        RepeatAction repeat = new RepeatAction();
        repeat.setAction(rotateAction);
        repeat.setCount(RepeatAction.FOREVER);
        this.addAction(repeat);

    }

    private void setOrigin() {
        this.setOrigin(width/2 + 1, height/2);
    }
    @Override
    public void act(float delta) {
        super.act(delta);

        collisionCircle.set(position.x + width / 2.0f, position.y + width / 2.0f, width / 2.0f);
    }
    @Override
    public void reset(float newX) {
        super.reset(newX);
        float newSizeAzul = Methods.randomFloat(Settings.MIN_MONEDA, Settings.MAX_MONEDA);
        width = height = 34 * newSizeAzul;
        // La posició serà un valor aleatòri entre 0 i l'alçada de l'aplicació menys l'alçada
        position.y =  new Random().nextInt(Settings.GAME_HEIGHT - (int) height);
        assetDinero = r.nextInt(15);
        setOrigin();
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetManager.dineroAzul[assetDinero], position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
    }
    // Agafa moneda blau

    public boolean collidesMonedasAzul(Spacecraft nau) {
        if (position.x <= nau.getX() + nau.getWidth()) {
            return (Intersector.overlaps(collisionCircle, nau.getCollisionRect()));
        }
        return false;
    }


}
