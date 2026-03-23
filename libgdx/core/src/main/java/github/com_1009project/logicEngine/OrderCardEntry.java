package github.com_1009project.logicEngine;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

class OrderCardEntry {

    private static final float URGENCY_HIGH   = 0.6f;
    private static final float URGENCY_MEDIUM = 0.3f;

    final int         orderId;
    final Table       card;
    final ProgressBar timerBar;

    // Pre-built drawables for each urgency level — swapped wholesale each frame
    private final NinePatchDrawable fillGreen;
    private final NinePatchDrawable fillYellow;
    private final NinePatchDrawable fillRed;

    OrderCardEntry(int orderId, Table card, ProgressBar timerBar,
                   NinePatchDrawable fillGreen,
                   NinePatchDrawable fillYellow,
                   NinePatchDrawable fillRed) {
        this.orderId    = orderId;
        this.card       = card;
        this.timerBar   = timerBar;
        this.fillGreen  = fillGreen;
        this.fillYellow = fillYellow;
        this.fillRed    = fillRed;
    }

    void updateTimer(float timerProgress, float urgency) {
        timerBar.setValue(timerProgress * 100f);

        NinePatchDrawable fill;
        if      (urgency > URGENCY_HIGH)   fill = fillRed;
        else if (urgency > URGENCY_MEDIUM) fill = fillYellow;
        else                               fill = fillGreen;

        // Swap the drawable reference — no color mutation, no shared state
        timerBar.getStyle().knobBefore = fill;
    }

    void removeFromStage() { 
    	card.remove(); 
    }

    void setPosition(float x, float y) { 
    	card.setPosition(x, y); 
    }
}