package com.dmens.pokeno.UtilsTest;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dmens.pokeno.database.AbilitiesDatabase;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.deck.Deck;
import com.dmens.pokeno.utils.FileUtils;
import com.dmens.pokeno.utils.DeckCreator;


@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUtils.class)
@PowerMockIgnore("javax.management.*")
public class DeckCreatorTest {
	private final String validDeckString = "33\n26\n26\n33\n26\n26\n33\n26\n26\n33\n" +
										   "26\n26\n33\n26\n26\n33\n26\n26\n33\n26\n" +
										   "26\n33\n26\n26\n33\n26\n26\n33\n26\n26\n" +
										   "33\n26\n26\n33\n26\n26\n33\n26\n26\n33\n" +
										   "26\n26\n33\n26\n26\n33\n26\n26\n33\n26\n" +
										   "26\n33\n26\n26\n33\n26\n26\n33\n26\n57";
	
	private final String invalidDeckString = "26\n26\n26";

    private static final Logger LOG = LogManager.getLogger(DeckCreatorTest.class);
    
    @Rule
	public TemporaryFolder folder= new TemporaryFolder();
    
    @Before
	public void setUp() throws IOException {
    	AbilitiesDatabase.getInstance().initialize("abilities.txt");
		CardsDatabase.getInstance().initialize("cards.txt");
    	
		PowerMockito.spy(FileUtils.class);
		Mockito.when(FileUtils.getResourcesLocation()).thenReturn(folder.getRoot().toPath().toUri().toString());
		
		folder.newFolder("data");
		
		// Create valid deck
		FileUtils.createFileAndWriteContents("validDeck.txt", validDeckString);
		// create invalid deck
		FileUtils.createFileAndWriteContents("invalidDeck.txt", invalidDeckString);
	}
	
	@Test
	public void testCreateDeck() {
		// Assert valid deck creation
		Deck deck = DeckCreator.Instance().DeckCreation("validDeck.txt");
        assertNotNull(deck);
        assertEquals(deck.size(), 60);
        assertTrue(deck.checkValidity());
        
        // Assert invalid deck creation
        deck = DeckCreator.Instance().DeckCreation("invalidDeck.txt");
        assertNotNull(deck);
        assertEquals(deck.size(), 3);
        assertFalse(deck.checkValidity());
	}
}
