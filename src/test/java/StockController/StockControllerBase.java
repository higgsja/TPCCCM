package StockController;

import com.hpi.TPCCMcontrollers.*;
import com.hpi.TPCCMprefs.*;
import lombok.*;
import org.junit.*;

@Getter @Setter
public class StockControllerBase
{
    public static StockController stockController;
    
    public static final Integer USER_ID = 5;
    
    @BeforeClass
    public static void setupClass()
    {
        CMDBModel.getInstance();
        CMDBModel.setUserId(USER_ID);
        CMDBController.initDBConnection();

        CMLanguageController.getInstance();

        stockController = StockController.getInstance();
    }

    @AfterClass
    public static void tearDownClass()
    {
    }
    
@Before
    public void before()
    {
        //clear closedStockFIFO for user id
        CMDBController.executeSQL("delete from hlhtxc5_dmOfx.ClosedStockFIFO where JoomlaId = " + USER_ID);
        
        //clear closedStockTrans for user id
        CMDBController.executeSQL("delete from hlhtxc5_dmOfx.ClosedStockTrans where JoomlaId = " + USER_ID);

        //clear openStockFIFO for user id
        CMDBController.executeSQL("delete from hlhtxc5_dmOfx.OpenStockFIFO where JoomlaId = " + USER_ID);

        //clear OpeningStock
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx.OpeningStock where JoomlaId = " + USER_ID);

        //clear ClosingStock
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx.ClosingStock where JoomlaId = " + USER_ID);

        //clear OpeningTransactions
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx.OpeningTransactions where JoomlaId = " + USER_ID);
        
        //clear ClosingTransactions
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx.ClosingTransactions where JoomlaId = " + USER_ID);

        //clear fifoOpenTransactions
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx.FIFOOpenTransactions where JoomlaId = " + USER_ID);

        //clear fifoClosedTransactions
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx.FIFOClosedTransactions where JoomlaId = " + USER_ID);

        stockController.getAccountList().clear();
        stockController.getEquityIdList().clear();
        stockController.getStockOpeningList().clear();
        stockController.getStockClosingList().clear();
        stockController.getStockClosedList().clear();
        stockController.getStockClosedTransList().clear();
        stockController.getStockOpenList().clear();
    }
}
