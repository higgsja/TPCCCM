package StockController;

import com.hpi.TPCCMcontrollers.*;
import com.hpi.TPCCMprefs.*;
import com.hpi.entities.*;
import java.sql.Date;
import lombok.*;
import org.junit.*;

@AllArgsConstructor
public class StockTest1
{

    private static StockController stockController;
    private static final Integer USER_ID = 5;

    @BeforeClass
    public static void setUpClass()
    {
        CMDBModel.getInstance();
        //CMDBModel.setUserId(816);
        CMDBModel.setUserId(StockTest1.USER_ID);
        stockController = StockController.getInstance();
        CMDBController.initDBConnection();

        stockController.getAccountList().clear();
        stockController.getEquityIdList().clear();
        stockController.getStockOpeningList().clear();
        stockController.getStockClosingList().clear();
        stockController.getStockClosedList().clear();
        stockController.getStockClosedTransList().clear();
        stockController.getStockOpenList().clear();

        //clear closedStockFIFO for user id
        CMDBController.executeSQL("delete from hlhtxc5_dmOfx.ClosedStockFIFO where JoomlaId = " + CMDBModel.getUserId());

        //clear openStockFIFO for user id
        CMDBController.executeSQL("delete from hlhtxc5_dmOfx.OpenStockFIFO where JoomlaId = " + CMDBModel.getUserId());

    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Ignore
    @Test
    public void StockTest1()
    {
        FIFOOpenTransactionModel ftm;
        OpeningStockModel openingStockModel;

        //get opening list
        //we will instead specify some particular opening transactions
        openingStockModel = OpeningStockModel.builder()
            .dmAcctId(1)
            .joomlaId(CMDBModel.getUserId())
            .fiTId("210412_5961_0")
            .ticker("GM")
            .equityId("GM")
            .dateOpen(Date.valueOf("2021-04-12"))
            //            .dateClose(null)
            .shPerCtrct(1)
            .units(250.0)
            .priceOpen(60.18)
            //            .priceClose(null)
            //            .markUpDn(null)
            //            .commission(null)
            //            .taxes(null)
            //            .fees(null)
            //            .transLoad(null)
            .totalOpen(-15045.0000)
            //            .totalClose(null)
            //            .curSym(null)
            .subAcctSec("MARGIN")
            .subAcctFund("MARGIN")
            .equityType("STOCK")
            //            .optionType(null)
            .transactionType("BUY")
            //            .reversalFiTId(null)
            .comment("")
            .build();

        stockController.getStockOpeningList().add(openingStockModel);

        /**
         * get closing list
         * there are none here as these are tests of open positions
         */
        // process lots
        stockController.processFIFOStockLots();

        String[][] stringTests =
        {
            {
                "a", stockController.getStockOpenList().get(0).getEquityId(), "GM"
            },
            {
                "b", stockController.getStockOpenList().get(0).getTicker(), "gm"
            }
        };

        Integer[][] integerTests =
        {
            {
                1, stockController.getStockOpenList().size(), 1
            }
        };

        Double[][] doubleTests =
        {
            {
                1.0, stockController.getStockOpenList().get(0).getUnits(), 250.0
            },
            {
                2.0, stockController.getStockOpenList().get(0).getPriceOpen(), 60.18
            },
            {
                4.0, stockController.getStockOpenList().get(0).getTotalOpen(), -15045.0000
            }
        };

        for (String[] test : stringTests)
        {
            (new TestString(test[0], test[1], test[2])).doTest();
        }

        for (Integer[] test : integerTests)
        {
            (new TestInteger(test[0], test[1], test[2])).doTest();
        }

        for (Double[] test : doubleTests)
        {
            (new TestDouble(test[0], test[1], test[2])).doTest();
        }

//        assertTrue("Position GMTDtTradeOpen '" + opsc.getPositionModels().get(0).getDateOpen().toString()
//                       + "' not the expected value of '2021-03-05T00:00'",
//            opsc.getPositionModels().get(0).getDateOpen()
//                .equals(CMHPIUtils.convertStringToLocalDateTime("2021-03-05 00:00:00")));
        //do not leave uncommented
//        stockController..doSQL();
    }
}
