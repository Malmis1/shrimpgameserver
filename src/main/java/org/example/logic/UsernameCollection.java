package org.example.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UsernameCollection
{
    private final List<String> usernames;
    private final Random randomizer;

    public UsernameCollection()
    {
        this.usernames = new ArrayList<String>();
        this.fillCollectionWithUsernames();
        this.randomizer = new Random();
    }

    public void addUsername(String username)
    {
        if (username == null)
        {
            throw new IllegalArgumentException("Username cannot be null");
        }
        else if (username.isBlank())
        {
            throw new IllegalArgumentException("Username cannot be blank!");
        }
        this.usernames.add(username);
    }

    public List<String> getUsernames()
    {
        return this.usernames;
    }

    public void fillCollectionWithUsernames()
    {
        this.addUsername("Commodore");
        this.addUsername("Aqua");
        this.addUsername("Bay");
        this.addUsername("Coral");
        this.addUsername("Cove");
        this.addUsername("Delta");
        this.addUsername("Drake");
        this.addUsername("Harbor");
        this.addUsername("Jetty");
        this.addUsername("Ocean");
        this.addUsername("Reef");
        this.addUsername("Tide");
        this.addUsername("River");
        this.addUsername("Wave");
        this.addUsername("Wharf");
        this.addUsername("Anchor");
        this.addUsername("Breeze");
        this.addUsername("Captain");
        this.addUsername("Cast");
        this.addUsername("Catcher");
        this.addUsername("Current");
        this.addUsername("Deep");
        this.addUsername("Fisher");
        this.addUsername("Flounder");
        this.addUsername("Gale");
        this.addUsername("Hauler");
        this.addUsername("Hook");
        this.addUsername("Leviathan");
        this.addUsername("Lobster");
        this.addUsername("Navigator");
        this.addUsername("Netty");
        this.addUsername("Octave");
        this.addUsername("Paddle");
        this.addUsername("Pilot");
        this.addUsername("Poseidon");
        this.addUsername("Quay");
        this.addUsername("Ray");
        this.addUsername("Sailor");
        this.addUsername("Scallop");
        this.addUsername("Skipper");
        this.addUsername("Snapper");
        this.addUsername("Spray");
        this.addUsername("Trawler");
        this.addUsername("Voyager");
        this.addUsername("Abyss");
        this.addUsername("Breaker");
        this.addUsername("Crabber");
        this.addUsername("Drift");
        this.addUsername("Horizon");
        this.addUsername("Mackerel");
        this.addUsername("Narwhal");
        this.addUsername("Seaworthy");
        this.addUsername("Storm");
        this.addUsername("Billow");
        this.addUsername("Buoy");
        this.addUsername("Dolphin");
        this.addUsername("Gull");
        this.addUsername("Kelp");
        this.addUsername("Kraken");
        this.addUsername("Narrows");
        this.addUsername("Oyster");
        this.addUsername("Pelican");
        this.addUsername("Seaside");
    }

    public String getRandomUsername()
    {
        int randomIndex = randomizer.nextInt(this.usernames.size());
        String randomUserName = this.usernames.get(randomIndex);
        this.usernames.remove(randomIndex);
        return randomUserName;
    }
}
