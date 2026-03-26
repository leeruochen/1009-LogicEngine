package github.com_1009project.logicEngine;

public interface FoodQueueListener {

    /** An order's individual timer hit zero and it was removed from the queue. */
    public void onOrderExpired(FoodOrder order);

    /** A submitted ingredient list matched an order and cleared it. */
    public void onOrderCleared(FoodOrder order);

    /** A submitted ingredient list did not match any order in the queue. */
    public void onOrderWrong();
}
