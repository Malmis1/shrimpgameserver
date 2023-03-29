package org.example.logic;

/**
 * The Player class represents a player in the game. It contains information about the player
 * such as name,
 * <p>
 * money, expenses, island and shrimp pounds caught.
 */
public class Player
{
    private String name;
    private int money;
    private int expenses;
    private Island island;
    private int shrimpPoundsCaught;

    /**
     * Constructor for creating a Player object with the given name, money, expenses, island and
     * shrimp pounds caught.
     *
     * @param name               The name of the player.
     * @param money              The amount of money the player has.
     * @param expenses           The total expenses of the player.
     */
    public Player(String name, int money, int expenses)
    {
        this.name = name;
        this.money = money;
        this.expenses = expenses;
    }

    /**
     * Returns the name of the player.
     *
     * @return The name of the player.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the name of the player.
     *
     * @param name The name of the player.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the amount of money the player has.
     *
     * @return The amount of money the player has.
     */
    public int getMoney()
    {
        return this.money;
    }

    /**
     * Sets the amount of money the player has.
     *
     * @param money The amount of money the player has.
     */
    public void setMoney(int money)
    {
        this.money = money;
    }

    /**
     * Returns the total expenses of the player.
     *
     * @return The total expenses of the player.
     */
    public int getExpenses()
    {
        return this.expenses;
    }

    /**
     * Sets the total expenses of the player.
     *
     * @param expenses The total expenses of the player.
     */
    public void setExpenses(int expenses)
    {
        this.expenses = expenses;
    }

    /**
     * Returns the island on which the player is currently located.
     *
     * @return The island on which the player is currently located.
     */
    public Island getIsland()
    {
        return this.island;
    }

    /**
     * Sets the island on which the player is currently located.
     *
     * @param island The island on which the player is currently located.
     */
    public void setIsland(Island island)
    {
        this.island = island;
    }

    /**
     * Returns the total amount of shrimp pounds caught by the player.
     *
     * @return The total amount of shrimp pounds caught by the player.
     */
    public int getShrimpPoundsCaught()
    {
        return this.shrimpPoundsCaught;
    }

    /**
     * Sets the total amount of shrimp pounds caught by the player.
     *
     * @param shrimpPoundsCaught The total amount of shrimp pounds caught by the player.
     */
    public void setShrimpPoundsCaught(int shrimpPoundsCaught)
    {
        this.shrimpPoundsCaught = shrimpPoundsCaught;
    }

    /**
     * Adds the given amount of shrimp pounds to the total amount of shrimp pounds caught by the
     * player.
     *
     * @param shrimpPounds The amount of shrimp pounds to be added.
     */
    public void addShrimpPoundsCaught(int shrimpPounds)
    {
        this.shrimpPoundsCaught += shrimpPounds;
    }

    /**
     * Calculates the profit of the player based on the price of shrimp and the expenses incurred.
     *
     * @param shrimpPrice the price of shrimp per pound
     * @return the calculated profit of the player
     */
    public int calculateProfit(int shrimpPrice)
    {
        return (this.shrimpPoundsCaught * shrimpPrice) - this.expenses;
    }

    /**
     * Resets the shrimp pounds caught and expenses of the player to 0.
     */
    public void resetStats()
    {
        this.shrimpPoundsCaught = 0;
        this.expenses = 0;
    }
}
