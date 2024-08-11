package OptionController;

import com.hpi.TPCCMcontrollers.*;
import com.hpi.TPCCMsql.*;
import com.hpi.entities.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import lombok.*;
import org.junit.*;

@AllArgsConstructor
public class OptionTest2
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
     * sell short, buy to close
     */
//    @Ignore
    @Test
    public void OptionTest2A()
    {
        String sql;
        ClosedOptionFIFOModel cofm;
        ResultSet rs;
        
        cofm = null;

        //opening
        optionController.getOptionOpeningBuyList().add(oom1);

        //closing
        optionController.getOptionClosingSellList().add(com1);

        //process
        optionController.processFIFOOptionLotsBuyToOpen();

        //read back from database
        sql = "select * from hlhtxc5_dmOfx.ClosedOptionFIFO where Ticker = 'pcg' and JoomlaId = '5'";

        try (Connection con = CMDBController.getConnection();
            PreparedStatement pStmt = con.prepareStatement(sql))
        {
            rs = pStmt.executeQuery();

            while (rs.next())
            {
                cofm = ClosedOptionFIFOModel.builder()
                    .equityId(rs.getString("EquityId"))
                    .transactionName(rs.getString("TransactionName"))
                    .units(rs.getDouble("Units"))
                    .priceOpen(rs.getDouble("PriceOpen"))
                    .priceClose(rs.getDouble("PriceClose"))
                    .totalOpen(rs.getDouble("TotalOpen"))
                    .totalClose(rs.getDouble("TotalClose"))
                    .build();
            }

            rs.close();
        } catch (SQLException ex)
        {
            throw new CMDAOException(CMLanguageController.
                getDBErrorProp("Title"),
                Thread.currentThread().getStackTrace()[1].getClassName(),
                Thread.currentThread().getStackTrace()[1].getMethodName(),
                ex.getMessage(), JOptionPane.INFORMATION_MESSAGE);
        }

        String[][] stringTests =
        {
            {
                "a", cofm.getEquityId(), "PCG   200918C00013000"
            },
            {
                "b", cofm.getTransactionName(), "PCG 18Sep20 13.0 Call"
            }
        };

        Integer[][] integerTests =
        {
            {
//                1, optionController.getOptionOpenList().size(), 1
            }
        };

        Double[][] doubleTests =
        {
            {
                1.0, cofm.getUnits(), 10.0
            },
            {
                2.0, cofm.getPriceOpen(), 2.73
            },
            {
                3.0, cofm.getTotalOpen(), 2728.7500
            },
            {
                4.0, cofm.getTotalClose(), -2631.16
            }
        };

        for (String[] test : stringTests)
        {
            (new TestString(test[0], test[1], test[2])).doTest();
        }

//        for (Integer[] test : integerTests)
//        {
//            (new TestInteger(test[0], test[1], test[2])).doTest();
//        }

        for (Double[] test : doubleTests)
        {
            (new TestDouble(test[0], test[1], test[2])).doTest();
        }
    }

    private static final ArrayList<OpeningStockModel> transactionList = new ArrayList<>();

    //do not change the oom* elements as it will break tests
    //  always use what is available for a start point; then
    //  always add more for further testing
    private final OpeningOptionModel oom1 = OpeningOptionModel.builder()
        .dmAcctId(2)
        .joomlaId(USER_ID)
        .fiTId("200413_1161_0")
        .ticker("PCG")
        //        .transactionName("")  //no transaction names in openingOptions
        .equityId("PCG   200918C00013000")
        .dateOpen(java.sql.Date.valueOf("2020-04-13"))
        //        .dateClose(null)
        .dateExpire(java.sql.Date.valueOf("2020-09-18"))
        .shPerCtrct(100)
        .units(-10.0)
        .priceOpen(2.73)
        //        .priceClose(null)
        //        .markUpDn(null)
        //        .commission(null)
        //        .taxes(null)
        .fees(1.25)
        //        .transLoad(null)
        .totalOpen(2728.75)
        //        .totalClose(null)
        //        .curSym(null)
        .subAcctSec("CASH")
        .subAcctFund("CASH")
        //        .reversalFiTId(null)
        //        .comment("")
        .openingOpen(11.82)
        .openingHigh(11.91)
        .openingLow(10.925)
        .openingClose(11.6)
        .equityType("OPTION")
        .optionType("CALL")
        .transactionType("SELLTOOPEN")
        .strikePrice(13.0)
        .build();

    private final ClosingOptionModel com1 = ClosingOptionModel.builder()
        .dmAcctId(2)
        .joomlaId(USER_ID)
        .fiTId("200415_1193_0")
        .ticker("PCG")
        //        .transactionName("")  //no transaction names in openingOptions
        .equityId("PCG   200918C00013000")
        //        .dateOpen(null)
        .dateClose(java.sql.Date.valueOf("2020-04-15"))
        .dateExpire(java.sql.Date.valueOf("2020-09-18"))
        .shPerCtrct(100)
        .units(10.0)
        //        .priceOpen(null)
        .priceClose(2.63)
        //        .markUpDn(null)
        //        .commission(null)
        //        .taxes(null)
        .fees(1.16)
        //        .transLoad(null)
        //        .totalOpen(null)
        .totalClose(-2631.16)
        //        .curSym(null)
        .subAcctSec("CASH")
        .subAcctFund("CASH")
        //        .reversalFiTId(null)
        //        .comment("")
        //        .closingOpen(11.82)
        //        .closingHigh(11.91)
        //        .closingLow(10.925)
        //        .closingClose(11.6)
        .equityType("OPTION")
        .optionType("CALL")
        .transactionType("BUYTOCLOSE")
        .strikePrice(13.0)
        .build();
}
