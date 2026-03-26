package github.com_1009project.logicEngine.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import github.com_1009project.abstractEngine.DynamicEntity;
import github.com_1009project.abstractEngine.Entity;
import github.com_1009project.abstractEngine.ICollidable;
import github.com_1009project.abstractEngine.IRenderable;
import github.com_1009project.logicEngine.Ingredient;

/**
 * Player entity that can move, collide, and hold/interact with ingredients and stations.
 * Holds at most one item at a time (an Ingredient or a Plate).
 */
public class Player extends DynamicEntity implements ICollidable, IRenderable {
    public boolean hasCollided = false;
    /** The ingredient (or Plate) currently held by the player. Null = empty hands. */
    private Ingredient heldItem;

    /** How far from the player centre interactions can reach (in world units). */
    private static final float INTERACT_RANGE = 80f;

    /** Offset for rendering the held item above the player. */
    private static final float HELD_OFFSET_Y = 50f;

    private boolean isChopping = false;
    private float chopTimer = 0f;

    public Player(float x, float y, float w, float h, Texture idleSheet, Texture runSheet, Texture chopSheet1, Texture chopSheet2, Texture deathSheet) {
        super();
        this.setPosition(x, y);
        this.setSize(w, h);
        this.createCollisionComponent(60, 10, 20, 10);
        this.setOnGround(true);
        this.setCanMove(true);
        this.setInputEnabled(true);
        this.setMovementComponent(1000f, 0.85f);
        this.setPersistent(true);
        this.createAnimationComponent("IDLE");
        this.getAnimationComponent().addAnimation("IDLE", idleSheet, 4, 1, 0.3f);
        this.getAnimationComponent().addAnimation("RUN", runSheet, 8, 1, 0.05f);
        this.getAnimationComponent().addAnimation("CHOP2", chopSheet2, 4, 1, 0.1f);
        this.getAnimationComponent().addAnimation("DEATH", deathSheet, 8, 1, 0.1f);
        this.getAnimationComponent().setState("IDLE");
    }

    @Override
    public void updateMovement(float deltaTime) {
        Vector2 vel = this.getMovementComponent().getVelocity();
        float playerMaxSpd = this.getMovementComponent().getMaxSpeed();

        if (isChopping) {
            vel.setZero();
            this.chopTimer += deltaTime;
            this.getAnimationComponent().setState("CHOP2");
        } else {
            if (vel.len() > playerMaxSpd) {
                vel.setLength(playerMaxSpd);
            }

            this.getPosition().add(vel.x * deltaTime, vel.y * deltaTime);

            if (vel.x != 0 || vel.y != 0) {
                this.getAnimationComponent().setState("RUN");
            } else {
                this.getAnimationComponent().setState("IDLE");
            }
        }

        if (heldItem != null) {
            float itemX = this.getPosition().x + this.getSize().x / 2f - heldItem.getSize().x / 2f;
            float itemY = this.getPosition().y + HELD_OFFSET_Y;
            heldItem.setPosition(itemX, itemY);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = this.getAnimationComponent().getCurrentFrame(Gdx.graphics.getDeltaTime(), true);
        batch.draw(currentFrame, this.getPosition().x, this.getPosition().y, this.getSize().x, this.getSize().y);

        // Render held item above the player
        if (heldItem != null && heldItem.isActive()) {
            heldItem.render(batch);
        }
    }

    @Override
    public void onCollision(Entity other) {
        Rectangle playerRect = this.getCollisionComponent().getBounds();
        Rectangle otherRect = other.getCollisionComponent().getBounds();

        if (playerRect.overlaps(otherRect)) {
            this.getPosition().set(this.getPreviousPosition());
            this.hasCollided = true;
        } else {
            this.hasCollided = false;
        }
    }

    public void setChopping(boolean chopping) {
        this.isChopping = chopping;
        if (!chopping) {
            this.chopTimer = 0f; // reset chop timer when stopping
        }
    }

    public boolean isChopping() {
        return this.isChopping;
    }

    // ── Held-item API ────────────────────────────────────────────────────────

    /** @return the ingredient currently held, or null if hands are empty. */
    public Ingredient getHeldItem() { return heldItem; }

    /** @return true if the player is holding something. */
    public boolean isHolding() { return heldItem != null; }

    /**
     * Makes the player hold the given ingredient. The ingredient is deactivated
     * from the world (no collision, positioned on the player).
     */
    public void pickUp(Ingredient item) {
        this.heldItem = item;
        item.setCollisionActive(false);
        item.setActive(true);
    }

    /**
     * Removes and returns the held item, clearing the player's hands.
     */
    public Ingredient dropItem() {
        Ingredient item = this.heldItem;
        this.heldItem = null;
        return item;
    }

    // ── Interaction range ────────────────────────────────────────────────────

    /** Returns the centre of the player for proximity checks. */
    public Vector2 getCentre() {
        return new Vector2(
            this.getPosition().x + this.getSize().x / 2f,
            this.getPosition().y + this.getSize().y / 2f
        );
    }

    /** Returns the interaction range radius. */
    public float getInteractRange() { return INTERACT_RANGE; }

    // ── Legacy stubs (kept for compatibility) ────────────────────────────────
    public void pickup(int itemID) {}
    public void drop(int itemID) {}
}
