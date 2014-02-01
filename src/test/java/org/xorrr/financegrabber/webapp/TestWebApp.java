package org.xorrr.financegrabber.webapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.logging.Level;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.xorrr.financegrabber.db.DbProperties;
import org.xorrr.financegrabber.db.EmbeddedMongo;
import org.xorrr.financegrabber.db.MongoFundsDatastore;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Ignore
public class TestWebApp {

    private MongoClient client;
    private DBCollection col;
    private MongoFundsDatastore ds;

    private WebClient webClient;
    private HtmlPage page;

    @BeforeClass
    public static void startEmbeddedMongo() throws Exception {
        EmbeddedMongo.startEmbeddedMongo(12345);
    }

    @Before
    public void setUp() throws Exception {
        MongoClientURI uri = new MongoClientURI(System.getenv("MONGODB_URI"));
        this.client = new MongoClient(uri);
        this.col = this.client.getDB(DbProperties.DB).getCollection(
                DbProperties.COL);
        this.ds = new MongoFundsDatastore(this.client);

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit")
                .setLevel(Level.OFF);
        this.webClient = new WebClient(BrowserVersion.FIREFOX_17);
        this.page = webClient
                .getPage("http://localhost:8080/financegrabber");
        webClient.waitForBackgroundJavaScript(5000);
    }

    @Test
    public void testInitialPageStructure() throws Exception {
        HtmlTextInput fundIdDiv = page.getHtmlElementById("add_fund_id_field");
        HtmlDivision addFundButtonDiv = page
                .getHtmlElementById("add_fund_button");

        assertEquals("financegrabber", page.getTitleText());
        assertNotNull(fundIdDiv);
        assertNotNull(addFundButtonDiv);
        assertEquals("ADD_FUND", addFundButtonDiv.getFirstChild()
                .getFirstChild().asText());
    }

    @Test
    public void testAddingAFund() throws Exception {
        assertEquals(0, ds.getAllProducts().size());

        HtmlTextInput fundIdDiv = page.getHtmlElementById("add_fund_id_field");
        fundIdDiv.setValueAttribute("iddqd");
        assertEquals("iddqd", fundIdDiv.asText());
        HtmlDivision addFundButtonDiv = page
                .getHtmlElementById("add_fund_button");
        addFundButtonDiv.click();
        webClient.waitForBackgroundJavaScript(2000);

        assertEquals(1, ds.getAllProducts().size());
        assertEquals("iddqd", ds.getAllProducts().get(0).getIsin());
    }

    @After
    public void tearDown() throws Exception {
        col.drop();
        webClient.closeAllWindows();
    }

    @AfterClass
    public static void stopEmbeddedMongo() throws Exception {
        EmbeddedMongo.stopEmbeddedMongo();
    }
}