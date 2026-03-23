package github.com_1009project.logicEngine;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import github.com_1009project.abstractEngine.Entity;
import github.com_1009project.abstractEngine.EntityManager;
import github.com_1009project.abstractEngine.Event;
import github.com_1009project.abstractEngine.EventObserver;
import github.com_1009project.logicEngine.entities.*;

/**
 * Handles all player-station interactions driven by the PlayerInteract and PlayerChop events.
 * <p>
 * Workflow:
 * <ol>
 *   <li>PlayerInteract while empty-handed near an IngredientBox/PlateBox → pick up new ingredient/plate</li>
 *   <li>PlayerInteract while empty-handed near a station with an ingredient → pick up from station</li>
 *   <li>PlayerInteract while holding an ingredient near a compatible station → place on station</li>
 *   <li>PlayerInteract while holding a Plate near a Counter with stacked items → load items onto plate</li>
 *   <li>PlayerInteract while holding a Plate near CounterSubmission → submit order</li>
 *   <li>PlayerInteract while holding anything near RubbishBin → discard</li>
 *   <li>PlayerChop while near a ChoppingStation with a raw choppable ingredient → apply one chop action</li>
 * </ol>
 */
public class InteractionManager implements EventObserver {

    private final EntityManager entityManager;
    private final AssetManager assetManager;
    private final FoodQueue foodQueue;

    public InteractionManager(EntityManager entityManager, AssetManager assetManager, FoodQueue foodQueue) {
        this.entityManager = entityManager;
        this.assetManager = assetManager;
        this.foodQueue = foodQueue;
    }

    @Override
    public void onNotify(Event event, Boolean up) {
        // Only act on key-press (not release)
        if (up != null && up) return;

        Player player = findPlayer();
        if (player == null) return;

        if (event == Event.PlayerInteract) {
            handleInteract(player);
        } else if (event == Event.PlayerChop) {
            handleChop(player);
        }
    }

    @Override
    public void onNotify(Event event, Boolean up, int screenX, int screenY) {
        // mouse events not used
    }

    // ── INTERACT ────────────────────────────────────────────────────────────

    private void handleInteract(Player player) {
        Entity nearest = findNearestStation(player);
        if (nearest == null) return;

        if (!player.isHolding()) {
            handleInteractEmptyHanded(player, nearest);
        } else {
            handleInteractHolding(player, nearest);
        }
    }

    /**
     * Player has empty hands → pick up from boxes or stations.
     */
    private void handleInteractEmptyHanded(Player player, Entity nearest) {
        // 1. Pick up from IngredientBox (infinite supply)
        if (nearest instanceof IngredientBox) {
            IngredientBox box = (IngredientBox) nearest;
            Ingredient spawned = spawnIngredient(box.getIngredientType(), player);
            if (spawned != null) {
                player.pickUp(spawned);
                Gdx.app.log("Interact", "Picked up " + spawned.getName() + " from " + box.getIngredientType() + " box");
            }
            return;
        }

        // 2. Pick up plate from PlateBox
        if (nearest instanceof PlateBox) {
            Plate plate = new Plate(0, 0, 40, 40, assetManager.get("food/dish.png", Texture.class), assetManager);
            player.pickUp(plate);
            Gdx.app.log("Interact", "Picked up empty plate");
            return;
        }

        // 3. Pick up from ChoppingStation
        if (nearest instanceof ChoppingStation) {
            ChoppingStation cs = (ChoppingStation) nearest;
            if (cs.hasIngredient() && cs.isChopComplete()) {
                Ingredient item = cs.removeIngredient();
                player.pickUp(item);
                Gdx.app.log("Interact", "Picked up chopped " + item.getName());
            } else if (cs.hasIngredient()) {
                // ingredient not done chopping, pick it up raw
                Ingredient item = cs.removeIngredient();
                player.pickUp(item);
                Gdx.app.log("Interact", "Picked up unchopped " + item.getName());
            }
            return;
        }

        // 4. Pick up from Stove
        if (nearest instanceof Stove) {
            Stove stove = (Stove) nearest;
            if (stove.hasIngredient()) {
                Ingredient item = stove.removeIngredient();
                player.pickUp(item);
                Gdx.app.log("Interact", "Picked up " + item.getState() + " " + item.getName() + " from stove");
            }
            return;
        }

        // 5. Pick up from Counter (top of stack, or the whole plate if top is a Plate)
        if (nearest instanceof Counter) {
            Counter counter = (Counter) nearest;
            if (counter.hasIngredients()) {
                Ingredient top = counter.removeTopIngredient();
                player.pickUp(top);
                Gdx.app.log("Interact", "Picked up " + top.getName() + " from counter");
            }
            return;
        }
    }

    /**
     * Player is holding something → place on station or interact.
     */
    private void handleInteractHolding(Player player, Entity nearest) {
        Ingredient held = player.getHeldItem();

        // 1. Rubbish bin → discard
        if (nearest instanceof RubbishBin) {
            Ingredient discarded = player.dropItem();
            discarded.setActive(false);
            Gdx.app.log("Interact", "Discarded " + discarded.getName());
            return;
        }

        // 2. ChoppingStation → place choppable ingredient
        if (nearest instanceof ChoppingStation) {
            ChoppingStation cs = (ChoppingStation) nearest;
            if (!(held instanceof Plate) && cs.canAccept(held)) {
                player.dropItem();
                Texture choppedTex = getChoppedTexture(held.getName());
                cs.placeIngredient(held, choppedTex);
                Gdx.app.log("Interact", "Placed " + held.getName() + " on chopping station");
            }
            return;
        }

        // 3. Stove → place cookable ingredient
        if (nearest instanceof Stove) {
            Stove stove = (Stove) nearest;
            if (!(held instanceof Plate) && stove.canAccept(held)) {
                player.dropItem();
                stove.setCookedTexture(assetManager.get("food/patty_cooked.png", Texture.class));
                stove.setStoveCookingTexture(assetManager.get("foodstations/stove_cooking.png", Texture.class));
                stove.placeIngredient(held);
                Gdx.app.log("Interact", "Placed " + held.getName() + " on stove");
            }
            return;
        }

        // 4. CounterSubmission → submit order if holding a Plate with items
        if (nearest instanceof CounterSubmission) {
            if (held instanceof Plate) {
                Plate plate = (Plate) held;
                if (!plate.isEmpty()) {
                    boolean success = foodQueue.submitOrder(plate.getAssembledItems());
                    player.dropItem();
                    held.setActive(false);
                    Gdx.app.log("Interact", "Submitted plate: " + (success ? "ORDER MATCHED!" : "No matching order"));
                }
            }
            return;
        }

        // 5. Counter → stacking / plate loading logic
        if (nearest instanceof Counter) {
            Counter counter = (Counter) nearest;

            // 5a. If player holds a Plate and counter has ingredients → load them onto the plate
            if (held instanceof Plate) {
                Plate plate = (Plate) held;
                if (counter.hasIngredients() && !plate.isComplete()) {
                    List<Ingredient> items = counter.removeAllIngredients();
                    for (Ingredient item : items) {
                        plate.addIngredient(item);
                    }
                    Gdx.app.log("Interact", "Loaded " + items.size() + " items onto plate. Total: " + plate.getItemCount());
                } else {
                    // Empty counter or plate is complete → place plate down
                    player.dropItem();
                    counter.placeIngredient(held);
                    Gdx.app.log("Interact", "Placed plate on counter");
                }
                return;
            }

            // 5b. If counter has a Plate on top → add held ingredient directly to the plate
            if (counter.hasIngredients()) {
                List<Ingredient> stack = counter.getIngredientStack();
                Ingredient topItem = stack.get(stack.size() - 1);
                if (topItem instanceof Plate) {
                    Plate plate = (Plate) topItem;
                    if (!plate.isComplete() && isFinishedIngredient(held)) {
                        player.dropItem();
                        plate.addIngredient(held);
                        Gdx.app.log("Interact", "Added " + held.getName() + " to plate on counter. Total: " + plate.getItemCount());
                        return;
                    }
                }
            }

            // 5c. Normal placement: place finished ingredient or bun on counter
            if (counter.canAccept(held)) {
                player.dropItem();
                counter.placeIngredient(held);
                Gdx.app.log("Interact", "Placed " + held.getName() + " on counter");
            }
            return;
        }

        // 6. IngredientBox or PlateBox while holding → do nothing (can't place items on supply boxes)
    }

    // ── CHOP ────────────────────────────────────────────────────────────────

    private void handleChop(Player player) {
        Entity nearest = findNearestStation(player);
        if (nearest == null) return;

        if (nearest instanceof ChoppingStation) {
            ChoppingStation cs = (ChoppingStation) nearest;
            if (cs.hasIngredient() && !cs.isChopComplete()) {
                boolean finished = cs.chop();
                if (finished) {
                    Gdx.app.log("Interact", "Chopping complete! " + cs.getPlacedIngredient().getName() + " is now chopped.");
                } else {
                    Gdx.app.log("Interact", "Chop! Progress: " + (int)(cs.getChopProgress() * 100) + "%");
                }
            }
        }
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────

    /**
     * Find the Player entity in the entity list.
     */
    private Player findPlayer() {
        for (Entity e : entityManager.getEntities()) {
            if (e instanceof Player && e.isActive()) {
                return (Player) e;
            }
        }
        return null;
    }

    /**
     * Find the nearest Station within the player's interaction range.
     * Compares distance from the player's centre to each station's centre.
     */
    private Entity findNearestStation(Player player) {
        Vector2 playerCentre = player.getCentre();
        float range = player.getInteractRange();
        float bestDist = Float.MAX_VALUE;
        Entity bestEntity = null;

        for (Entity e : entityManager.getEntities()) {
            if (e == player || !e.isActive()) continue;
            if (!(e instanceof Station)) continue;

            float cx = e.getPosition().x + e.getSize().x / 2f;
            float cy = e.getPosition().y + e.getSize().y / 2f;
            float dist = Vector2.dst(playerCentre.x, playerCentre.y, cx, cy);

            if (dist <= range && dist < bestDist) {
                bestDist = dist;
                bestEntity = e;
            }
        }
        return bestEntity;
    }

    /**
     * Spawns a new raw ingredient of the given type at the player's position.
     */
    private Ingredient spawnIngredient(String type, Player player) {
        float x = player.getPosition().x;
        float y = player.getPosition().y;
        float w = 40f;
        float h = 40f;

        switch (type.toLowerCase()) {
            case "bun":
                return new Bun(x, y, w, h, assetManager.get("food/bun.png", Texture.class));
            case "lettuce":
                return new Lettuce(x, y, w, h, assetManager.get("food/lettuce.png", Texture.class));
            case "tomato":
                return new Tomato(x, y, w, h, assetManager.get("food/tomato.png", Texture.class));
            case "cheese":
                return new Cheese(x, y, w, h, assetManager.get("food/cheese.png", Texture.class));
            case "patty":
                return new Patty(x, y, w, h, assetManager.get("food/patty.png", Texture.class));
            default:
                Gdx.app.log("Interact", "Unknown ingredient type: " + type);
                return null;
        }
    }

    /**
     * Returns the chopped texture for a given ingredient name.
     */
    private Texture getChoppedTexture(String name) {
        switch (name.toLowerCase()) {
            case "lettuce": return assetManager.get("food/lettuce_chopped.png", Texture.class);
            case "tomato":  return assetManager.get("food/tomato_chopped.png", Texture.class);
            case "cheese":  return assetManager.get("food/cheese_chopped.png", Texture.class);
            default: return null;
        }
    }

    /**
     * Returns true if the ingredient is considered "finished" for burger assembly.
     * Buns are always ready. Chopped/Cooked ingredients are ready. Raw patties are not.
     */
    private boolean isFinishedIngredient(Ingredient ingredient) {
        if (ingredient instanceof Plate) return false;
        String name = ingredient.getName().toLowerCase();
        // Bun is always ready (no processing needed)
        if (name.equals("bread") || name.equals("bun")) return true;
        // Other ingredients need to be Cooked (post-processing)
        return ingredient.getState() == FoodState.Cooked;
    }
}
