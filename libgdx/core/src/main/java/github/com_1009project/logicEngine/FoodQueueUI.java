package github.com_1009project.logicEngine;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import github.com_1009project.abstractEngine.UIFactory;
import github.com_1009project.abstractEngine.UILayer;

public class FoodQueueUI {

    private static final float card_x = 20f;
    private static final float card_y = 800f;
    private static final float card_width = 160f;
    private static final float card_height = 130f;
    private static final float card_gap = 12f;
    private static final float BURGER_IMG_WIDTH = 80f;
    private static final float BURGER_IMG_HEIGHT = 60f;
    private static final float ICON_SIZE = 22f;

    private final UIFactory       uiFactory;
    private final UILayer         uiLayer;
    private final FoodQueue       foodQueue;
    private final FoodAssetLoader assetLoader;
    
    private final NinePatchDrawable fillGreen;
    private final NinePatchDrawable fillYellow;
    private final NinePatchDrawable fillRed;
    private final Texture           fillGreenTex;
    private final Texture           fillYellowTex;
    private final Texture           fillRedTex;

    private final List<OrderCardEntry> cardEntries = new ArrayList<>();

    public FoodQueueUI(UIFactory uiFactory, UILayer uiLayer,
            FoodQueue foodQueue, FoodAssetLoader assetLoader) {
    	this.uiFactory   = uiFactory;
    	this.uiLayer     = uiLayer;
    	this.foodQueue   = foodQueue;
    	this.assetLoader = assetLoader;

    	fillGreenTex  = solidTexture(200, 12, Color.GREEN);
    	fillYellowTex = solidTexture(200, 12, Color.YELLOW);
    	fillRedTex    = solidTexture(200, 12, Color.RED);

    	fillGreen  = new NinePatchDrawable(new NinePatch(fillGreenTex,  1, 1, 1, 1));
    	fillYellow = new NinePatchDrawable(new NinePatch(fillYellowTex, 1, 1, 1, 1));
    	fillRed    = new NinePatchDrawable(new NinePatch(fillRedTex,    1, 1, 1, 1));
    }

    public void update() {
        List<FoodOrder> liveOrders = foodQueue.getOrders();
        removeStaleCards(liveOrders);
        addNewCards(liveOrders);
        repositionCards(liveOrders);
        refreshTimerBars(liveOrders);
    }

    private void removeStaleCards(List<FoodOrder> liveOrders) {
        for (int i = cardEntries.size() - 1; i >= 0; i--) {
            if (!isOrderPresent(cardEntries.get(i).getOrderID(), liveOrders)) {
                cardEntries.get(i).removeFromStage();
                cardEntries.remove(i);
            }
        }
    }

    private void addNewCards(List<FoodOrder> liveOrders) {
        for (FoodOrder order : liveOrders) {
            if (findEntry(order.getId()) == null) {
                cardEntries.add(buildCard(order));
            }
        }
    }

    private void repositionCards(List<FoodOrder> liveOrders) {
        for (int pos = 0; pos < liveOrders.size(); pos++) {
            OrderCardEntry entry = findEntry(liveOrders.get(pos).getId());
            if (entry != null) {
                entry.setPosition(card_x + pos * (card_width + card_gap), card_y);
            }
        }
    }

    private void refreshTimerBars(List<FoodOrder> liveOrders) {
        for (FoodOrder order : liveOrders) {
            OrderCardEntry entry = findEntry(order.getId());
            if (entry != null) {
                entry.updateTimer(order.getTimerProgress(), order.getUrgency());
            }
        }
    }

    private OrderCardEntry buildCard(FoodOrder order) {
        BurgerRecipe recipe = order.getRecipe();
 
        Table card = uiFactory.createCardTable(
                0, 0, card_width, card_height,
                new Color(0.15f, 0.15f, 0.15f, 0.9f));
 
        // Burger preview
        Image burgerImage = uiFactory.wrapTexture(
                assetLoader.getTexture(recipe.getBurgerImagePath()));
        card.add(burgerImage)
                .size(BURGER_IMG_WIDTH, BURGER_IMG_HEIGHT)
                .center().padBottom(4f).row();
 
        // Ingredient icons
        Table iconsRow = new Table();
        float iconStep = Math.min(ICON_SIZE, (card_width - 12f) / recipe.getIngredients().size());
        for (FoodItem item : recipe.getIngredients()) {
            iconsRow.add(uiFactory.wrapTexture(assetLoader.getTexture(item.getAssetPath())))
                    .size(iconStep, ICON_SIZE).padRight(2f);
        }
        card.add(iconsRow).center().padBottom(6f).row();
 
        // Timer bar — each card gets its own style so knobBefore swaps are isolated.
        // The fill drawables (fillGreen/Yellow/Red) are shared but never mutated.
        ProgressBar timerBar = uiFactory.createResourceBar(0, 0, false);
        timerBar.setValue(100f);
        timerBar.remove();
 
        ProgressBar.ProgressBarStyle sharedStyle = timerBar.getStyle();
        ProgressBar.ProgressBarStyle ownStyle    = new ProgressBar.ProgressBarStyle();
        ownStyle.background = sharedStyle.background;
        ownStyle.knobBefore = fillGreen;   // start green; updateTimer swaps this each frame
        ownStyle.knob       = sharedStyle.knob;
        timerBar.setStyle(ownStyle);
 
        card.add(timerBar).expandX().fillX().padLeft(6f).padRight(6f);
 
        return new OrderCardEntry(order.getId(), card, timerBar,
                fillGreen, fillYellow, fillRed);
    }

    private OrderCardEntry findEntry(int orderId) {
        for (OrderCardEntry e : cardEntries) {
            if (e.getOrderID() == orderId) return e;
        }
        return null;
    }
 
    private boolean isOrderPresent(int orderId, List<FoodOrder> orders) {
        for (FoodOrder o : orders) {
            if (o.getId() == orderId) return true;
        }
        return false;
    }
 
    private static Texture solidTexture(int w, int h, Color color) {
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(color);
        pm.fill();
        Texture tex = new Texture(pm);
        pm.dispose();
        return tex;
    }

    public void dispose() {
        for (OrderCardEntry e : cardEntries) e.removeFromStage();
        cardEntries.clear();
    }
}