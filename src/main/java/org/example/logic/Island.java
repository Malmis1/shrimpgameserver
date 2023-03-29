package org.example.logic;

/**
 * The Island class represents an island where players can catch shrimp.
 * <p>
 * It contains information about the name of the island and the current amount of shrimp pounds
 * available on the island.
 */
public class Island
{
    private String islandName;
    private int shrimpStock;
    private final int MAX_SHRIMP_STOCK = 1000; // predefined maximum shrimp stock

    /**
     * Constructs an Island object with the given name and initial shrimp stock.
     *
     * @param islandName  the name of the island
     * @param shrimpStock the initial amount of shrimp pounds available on the island
     */
    public Island(String islandName, int shrimpStock)
    {
        this.islandName = islandName;
        this.shrimpStock = shrimpStock;
    }

    /**
     * Returns the name of the island.
     *
     * @return the name of the island
     */
    public String getIslandName()
    {
        return this.islandName;
    }

    /**
     * Returns the current amount of shrimp pounds available on the island.
     *
     * @return the current amount of shrimp pounds available on the island
     */
    public int getShrimpStock()
    {
        return this.shrimpStock;
    }

    /**
     * Sets the current amount of shrimp pounds available on the island to the given value.
     *
     * @param shrimpStock the new amount of shrimp pounds available on the island
     */
    public void setShrimpStock(int shrimpStock)
    {
        this.shrimpStock = shrimpStock;
    }

    /**
     * Adds the given amount of shrimp pounds to the current amount of shrimp pounds available on
     * the island.
     * If the new amount of shrimp pounds exceeds the maximum shrimp stock, the shrimp stock is
     * set to the maximum value.
     *
     * @param shrimpToAdd the amount of shrimp pounds to add to the current stock
     */
    public void addShrimpStock(int shrimpToAdd)
    {
        this.shrimpStock += shrimpToAdd;
        if (this.shrimpStock > MAX_SHRIMP_STOCK)
        {
            this.shrimpStock = MAX_SHRIMP_STOCK;
        }
    }

    /**
     * Removes the given amount of shrimp pounds from the current amount of shrimp pounds
     * available on the island.
     * If the new amount of shrimp pounds is negative, the shrimp stock is set to 0.
     *
     * @param shrimpToRemove the amount of shrimp pounds to remove from the current stock
     */
    public void removeShrimpStock(int shrimpToRemove)
    {
        this.shrimpStock -= shrimpToRemove;
        if (this.shrimpStock < 0)
        {
            this.shrimpStock = 0;
        }
    }

    /**
     * Attempts to catch the given amount of shrimp pounds from the current amount of shrimp
     * pounds available on the island.
     * If the catch is successful (there is enough shrimp on the island), the shrimp stock is
     * updated and the method returns true.
     * If the catch is unsuccessful (there is not enough shrimp on the island), the shrimp stock
     * is not updated and the method returns false.
     *
     * @param shrimpCaught the amount of shrimp pounds to catch from the island
     * @return true if the catch is successful, false otherwise
     */
    public boolean catchShrimp(int shrimpCaught)
    {
        if (this.shrimpStock >= shrimpCaught)
        {
            this.shrimpStock -= shrimpCaught;
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Replenishes the shrimp stock of the island to the predefined maximum value.
     */
    public void replenishShrimpStock()
    {
        this.shrimpStock = MAX_SHRIMP_STOCK;
    }
}
