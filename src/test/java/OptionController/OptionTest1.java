package OptionController;

import com.hpi.entities.*;
import java.util.*;
import lombok.*;
import org.junit.*;

@AllArgsConstructor
public class OptionTest1
    extends OptionControllerBase
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
    public void OptionTest1()
    {
        //opening
        optionController.getOptionOpeningBuyList().add(oom1);

        //closing
        optionController.getOptionClosingSellList().add(com1);
        optionController.getOptionClosingSellList().add(com2);

        //process
        optionController.processFIFOOptionLotsBuyToOpen();

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

    //do not change the oom* elements as it will break tests
    //  always use what is available for a start point; then
    //  always add more for further testing
    private final OpeningOptionModel oom1 = OpeningOptionModel.builder()
        .dmAcctId(2)
        .joomlaId(USER_ID)
        .fiTId("210212_3249_0")
        .ticker("STZ")
        .equityId("STZ   200117C00140000")
        .strikePrice(100.00)
        .dateOpen(java.sql.Date.valueOf("2021-02-12"))
        .dateClose(null)
        .dateExpire(java.sql.Date.valueOf("2021-09-18"))
        .shPerCtrct(100)
        .units(2.0)
        .priceOpen(10.00)
        .priceClose(null)
        .markUpDn(null)
        .commission(null)
        .taxes(null)
        .fees(null)
        .transLoad(null)
        .totalOpen(-2000.0)
        .totalClose(null)
        .curSym(null)
        .subAcctSec("CASH")
        .subAcctFund("CASH")
        .equityType("OPTION")
        .optionType("CALL")
        .transactionType("BUYTOOPEN")
        .comment("")
        .build();

    private final ClosingOptionModel com1 = ClosingOptionModel.builder()
        .dmAcctId(2)
        .joomlaId(USER_ID)
        .fiTId("210611_3934_0")
        .ticker("STZ")
        .equityId("STZ   200117C00140000")
        .strikePrice(100.00)
        .dateOpen(null)
        .dateClose(java.sql.Date.valueOf("2021-06-11"))
        .dateExpire(java.sql.Date.valueOf("2021-09-18"))
        .shPerCtrct(100)
        .units(-1.0)
        .priceOpen(null)
        .markUpDn(null)
        .commission(null)
        .taxes(null)
        .fees(.12)
        .transLoad(null)
        .totalOpen(null)
        .priceClose(11.0)
        .totalClose(1099.88)
        .curSym(null)
        .optionType("CALL")
        .reversalFiTId(null)
        .comment("")
        .subAcctSec("CASH")
        .subAcctFund("CASH")
        .equityType("OPTION")
        .transactionType("SELLTOCLOSE")
        .build();

    private final ClosingOptionModel com2 = ClosingOptionModel.builder()
        .dmAcctId(2)
        .joomlaId(USER_ID)
        .fiTId("210611_3940_0")
        .ticker("STZ")
        .equityId("STZ   200117C00140000")
        .strikePrice(100.00)
        .dateOpen(null)
        .dateClose(java.sql.Date.valueOf("2021-06-11"))
        .dateExpire(java.sql.Date.valueOf("2021-09-18"))
        .shPerCtrct(100)
        .units(-1.0)
        .priceClose(12.0)
        .priceOpen(null)
        .markUpDn(null)
        .commission(null)
        .taxes(null)
        .fees(.12)
        .transLoad(null)
        .totalOpen(null)
        .totalClose(1199.88)
        .curSym(null)
        .optionType(null)
        .reversalFiTId(null)
        .comment("")
        .subAcctSec("CASH")
        .subAcctFund("CASH")
        .equityType("OPTION")
        .transactionType("SELLTOCLOSE")
        .build();
}
