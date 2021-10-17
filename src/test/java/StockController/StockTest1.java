package StockController;

import com.hpi.entities.*;
import java.util.*;
import lombok.*;
import org.junit.*;

@AllArgsConstructor
public class StockTest1
    extends StockControllerBase
{

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
        super.before();
    }

    @After
    public void tearDown()
    {
    }

    /**
     * single buy, 2 sell lots
     */
//    @Ignore
    @Test
    public void StockTest1()
    {
//        String sql;
//
//        sql = "insert ignore into hlhtxc5_dmOfx.OpeningStock () values(JoomlaId, FiTId, Ticker, EquityId, DateOpen, ShPerCtrct, Units, PriceOpen, TotalOpen, SubAcctSec, SubAcctFund, EquityType, TransactionType";
//        sql += osm1.getJoomlaId();
//        sql += osm1.getFiTId();
//        sql += osm1.getTicker();
//        sql += osm1.getEquityId();
//        sql += osm1.getDateOpen();
//        sql += osm1.getShPerCtrct();
//        sql += osm1.getUnits();
//        sql += osm1.getPriceOpen();
//        sql += osm1.getTotalOpen();
//        sql += osm1.getSubAcctSec();
//        sql += osm1.getSubAcctFund();
//        sql += osm1.getEquityType();
//        sql += osm1.getTransactionType();
//        sql += ")";
//
//        //and push to the database
//        CMDBController.executeSQL(sql);
//
//        sql = "insert ignore into hlhtxc5_dmOfx.ClosingStock () values(JoomlaId, FiTId, Ticker, EquityId, DateClose, ShPerCtrct, Units, PriceClose, TotalClose, SubAcctSec, SubAcctFund, EquityType, TransactionType";
//        sql += csm1.getJoomlaId();
//        sql += csm1.getFiTId();
//        sql += csm1.getTicker();
//        sql += csm1.getEquityId();
//        sql += csm1.getDateClose();
//        sql += csm1.getShPerCtrct();
//        sql += csm1.getUnits();
//        sql += csm1.getPriceClose();
//        sql += csm1.getTotalClose();
//        sql += csm1.getSubAcctSec();
//        sql += csm1.getSubAcctFund();
//        sql += csm1.getEquityType();
//        sql += csm1.getTransactionType();
//        sql += ")";
//
//        //and push to the database
//        CMDBController.executeSQL(sql);
//        
//        sql = "insert ignore into hlhtxc5_dmOfx.ClosingStock () values(JoomlaId, FiTId, Ticker, EquityId, DateClose, ShPerCtrct, Units, PriceClose, TotalClose, SubAcctSec, SubAcctFund, EquityType, TransactionType";
//        sql += csm2.getJoomlaId();
//        sql += csm2.getFiTId();
//        sql += csm2.getTicker();
//        sql += csm2.getEquityId();
//        sql += csm2.getDateClose();
//        sql += csm2.getShPerCtrct();
//        sql += csm2.getUnits();
//        sql += csm2.getPriceClose();
//        sql += csm2.getTotalClose();
//        sql += csm2.getSubAcctSec();
//        sql += csm2.getSubAcctFund();
//        sql += csm2.getEquityType();
//        sql += csm2.getTransactionType();
//        sql += ")";
//
//        //and push to the database
//        CMDBController.executeSQL(sql);
//
//        // process lots
//        stockController.getDistinctEquityId(USER_ID);
        
        //opening
        stockController.getStockOpeningList().add(osm1);
        
        //closing
        stockController.getStockClosingList().add(csm1);
        stockController.getStockClosingList().add(csm2);
        
        //process
        stockController.processFIFOStockLots();
        
//        String[][] stringTests =
//        {
//            {
//                "a", stockController.getStockOpenList().get(0).getEquityId(), "GM"
//            },
//            {
//                "b", stockController.getStockOpenList().get(0).getTicker(), "gm"
//            }
//        };
//
//        Integer[][] integerTests =
//        {
//            {
//                1, stockController.getStockOpenList().size(), 1
//            }
//        };
//
//        Double[][] doubleTests =
//        {
//            {
//                1.0, stockController.getStockOpenList().get(0).getUnits(), 250.0
//            },
//            {
//                2.0, stockController.getStockOpenList().get(0).getPriceOpen(), 60.18
//            },
//            {
//                4.0, stockController.getStockOpenList().get(0).getTotalOpen(), -15045.0000
//            }
//        };

//        for (String[] test : stringTests)
//        {
//            (new TestString(test[0], test[1], test[2])).doTest();
//        }
//
//        for (Integer[] test : integerTests)
//        {
//            (new TestInteger(test[0], test[1], test[2])).doTest();
//        }
//
//        for (Double[] test : doubleTests)
//        {
//            (new TestDouble(test[0], test[1], test[2])).doTest();
//        }
    }

    private static final ArrayList<OpeningStockModel> transactionList = new ArrayList<>();

    //do not change the osm* elements as it will break tests
    //  always use what is available for a start point; then
    //  always add more for further testing
    private final OpeningStockModel osm1 = OpeningStockModel.builder()
        .dmAcctId(2)
        .joomlaId(USER_ID)
        .fiTId("210212_3249_0")
        .ticker("STZ")
        .equityId("STZ")
        .dateOpen(java.sql.Date.valueOf("2021-02-12"))
        .dateClose(null)
        .shPerCtrct(1)
        .units(100.0)
        .priceOpen(236.51)
        .priceClose(null)
        .markUpDn(null)
        .commission(null)
        .taxes(null)
        .fees(null)
        .transLoad(null)
        .totalOpen(-23651.0)
        .totalClose(null)
        .curSym(null)
        .subAcctSec("CASH")
        .subAcctFund("CASH")
        .equityType("STOCK")
        .optionType(null)
        .transactionType("BUY")
        .comment("")
        .build();

    private final ClosingStockModel csm1 = ClosingStockModel.builder()
        .dmAcctId(2)
        .joomlaId(USER_ID)
        .fiTId("210611_3934_0")
        .ticker("STZ")
        .equityId("STZ")
        .dateOpen(null)
        .dateClose(java.sql.Date.valueOf("2021-06-11"))
        .shPerCtrct(1)
        .units(-88.0)
        .priceOpen(null)
        .markUpDn(null)
        .commission(null)
        .taxes(null)
        .fees(.12)
        .transLoad(null)
        .totalOpen(null)
        .priceClose(238.0)
        .totalClose(20943.88)
        .curSym(null)
        .optionType(null)
        .reversalFiTId(null)
        .comment("")
        .subAcctSec("CASH")
        .subAcctFund("CASH")
        .equityType("STOCK")
        .transactionType("SELL")
        .build();

    private final ClosingStockModel csm2 = ClosingStockModel.builder()
        .dmAcctId(2)
        .joomlaId(USER_ID)
        .fiTId("210611_3940_0")
        .ticker("STZ")
        .equityId("STZ")
        .dateOpen(null)
        .dateClose(java.sql.Date.valueOf("2021-06-11"))
        .shPerCtrct(1)
        .units(-12.0)
        .priceClose(237.29)
        .priceOpen(null)
        .markUpDn(null)
        .commission(null)
        .taxes(null)
        .fees(.02)
        .transLoad(null)
        .totalOpen(null)
        .totalClose(2847.46)
        .curSym(null)
        .optionType(null)
        .reversalFiTId(null)
        .comment("")
        .subAcctSec("CASH")
        .subAcctFund("CASH")
        .equityType("STOCK")
        .transactionType("SELL")
        .build();
}
