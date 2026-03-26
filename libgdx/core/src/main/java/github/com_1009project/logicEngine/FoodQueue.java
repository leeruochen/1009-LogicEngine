package github.com_1009project.logicEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FoodQueue {

    public static final int   MAX_QUEUE_SIZE   = 3;
    public static final float SPAWN_INTERVAL   = 20f;
    public static final float ORDER_TIME_LIMIT = 60f;

    private final List<FoodOrder> orders;
    private final Random          random;

    private FoodQueueListener listener;
    private float             spawnTimer;
    private int               clearedCount;

    public FoodQueue() {
        this.orders       = new ArrayList<>();
        this.random       = new Random();
        this.spawnTimer   = SPAWN_INTERVAL;
        this.clearedCount = 0;
    }

    /** Inject the listener that receives queue outcome callbacks. */
    public void setListener(FoodQueueListener listener) {
        this.listener = listener;
    }

    public void update(float deltaTime) {
        updateSpawner(deltaTime);
        updateOrderTimers(deltaTime);
    }

    private void updateSpawner(float deltaTime) {
        spawnTimer += deltaTime;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTimer = 0f;
            if (orders.size() < MAX_QUEUE_SIZE) {
                spawnRandomOrder();
            }
        }
    }

    private void updateOrderTimers(float deltaTime) {
        Iterator<FoodOrder> it = orders.iterator();
        while (it.hasNext()) {
            FoodOrder order = it.next();
            if (order.update(deltaTime)) {
                it.remove();
                if (listener != null) listener.onOrderExpired(order);
            }
        }
    }

    private void spawnRandomOrder() {
        BurgerRecipe[] recipes = BurgerRecipe.values();
        BurgerRecipe   recipe  = recipes[random.nextInt(recipes.length)];
        orders.add(new FoodOrder(recipe, ORDER_TIME_LIMIT));
    }

    /**
     * Resolves {@code assembled} to a recipe (order-independent), then removes
     * the earliest matching order from the queue.
     */
    public boolean submitOrder(List<FoodItem> assembled) {
        BurgerRecipe resolved = BurgerRecipe.fromIngredients(assembled);

        if (resolved != null) {
            Iterator<FoodOrder> it = orders.iterator();
            while (it.hasNext()) {
                FoodOrder order = it.next();
                if (order.getRecipe() == resolved) {
                    it.remove();
                    clearedCount++;
                    if (listener != null) listener.onOrderCleared(order);
                    return true;
                }
            }
        }

        if (listener != null) {
        	listener.onOrderWrong();
        }
        return false;
    }

    public List<FoodOrder> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public int getClearedCount() { 
    	return clearedCount; 
    }

    public void reset() {
        orders.clear();
        clearedCount = 0;
        spawnTimer   = SPAWN_INTERVAL;
    }
}