package tests.logic.player;

import dnd.logic.Tick;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MageTest {
    MageMock mage;

    @Before
    public void setUp() {
        mage = new MageMock("Test Mage", 100, 10, 5, 20, 50,30, 3, 4);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void onTick_manaRegenerated() {
        mage.setCurrentMana(40);
        mage.onTick(new Tick(0));
        Assert.assertEquals("mana should have regenerated by 1 from 40 to 41", mage.getCurrentMana(), 41);
    }

    @Test
    public void onTick_manaNotSurpassingManaPool() {
        mage.setCurrentMana(50);
        mage.onTick(new Tick(0));
        Assert.assertEquals("current mana should have not surpassed 50", mage.getCurrentMana(), 50);
    }
}