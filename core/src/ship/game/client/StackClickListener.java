package ship.game.client;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class StackClickListener extends InputListener {

    private StackGroup stackGroup;

    public StackClickListener(StackGroup stackGroup) {
        this.stackGroup = stackGroup;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Click");
    //  stackGroup.getTopCard().flipCard();
        return true;
    }


}

