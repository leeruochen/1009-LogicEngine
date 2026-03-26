package github.com_1009project.logicEngine;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

class OrderCardEntry {

    private static final float URGENCY_HIGH   = 0.6f;
    private static final float URGENCY_MEDIUM = 0.3f;

    private final int         orderID;
    private final Table       card;
    private final ProgressBar timerBar;

    // Pre-built drawables for each urgency level — swapped wholesale each frame
    private final NinePatchDrawable fillGreen;
    private final NinePatchDrawable fillYellow;
    private final NinePatchDrawable fillRed;

    public OrderCardEntry(int orderID, Table card, ProgressBar timerBar,
                   NinePatchDrawable fillGreen,
                   NinePatchDrawable fillYellow,
                   NinePatchDrawable fillRed) {
        this.orderID    = orderID;
        this.card       = card;
        this.timerBar   = timerBar;
        this.fillGreen  = fillGreen;
        this.fillYellow = fillYellow;
        this.fillRed    = fillRed;
    }

    public void updateTimer(float timerProgress, float urgency) {
        timerBar.setValue(timerProgress * 100f);

        NinePatchDrawable fill;
        if      (urgency > URGENCY_HIGH)   fill = fillRed;
        else if (urgency > URGENCY_MEDIUM) fill = fillYellow;
        else                               fill = fillGreen;

        // Swap the drawable reference — no color mutation, no shared state
        timerBar.getStyle().knobBefore = fill;
    }

    public void removeFromStage() { 
    	card.remove(); 
    }

    public void setPosition(float x, float y) { 
    	card.setPosition(x, y); 
    }
    
    public int getOrderID() {
    	return orderID;
    }
}