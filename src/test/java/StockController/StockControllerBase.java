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
        CMDBController.executeSQL("delete from hlhtxc5_dmOfx_dev.ClosedStockFIFO where JoomlaId = " + USER_ID);
        
        //clear closedStockTrans for user id
        CMDBController.executeSQL("delete from hlhtxc5_dmOfx_dev.ClosedStockTrans where JoomlaId = " + USER_ID);

        //clear openStockFIFO for user id
        CMDBController.executeSQL("delete from hlhtxc5_dmOfx_dev.OpenStockFIFO where JoomlaId = " + USER_ID);

        //clear OpeningStock
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx_dev.OpeningStock1 where JoomlaId = " + USER_ID);

        //clear ClosingStock
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx_dev.ClosingStock1 where JoomlaId = " + USER_ID);

        //clear OpeningTransactions
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx_dev.OpeningTransactions where JoomlaId = " + USER_ID);
        
        //clear ClosingTransactions
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx_dev.ClosingTransactions where JoomlaId = " + USER_ID);

        //clear fifoOpenTransactions
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx_dev.FIFOOpenTransactions where JoomlaId = " + USER_ID);

        //clear fifoClosedTransactions
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx_dev.FIFOClosedTransactions where JoomlaId = " + USER_ID);

        stockController.getAccountList().clear();
        stockController.getEquityIdList().clear();
        stockController.getStockOpeningList().clear();
        stockController.getStockClosingList().clear();
        stockController.getStockClosedList().clear();
        stockController.getStockClosedTransList().clear();
        stockController.getStockOpenList().clear();
    }
}
