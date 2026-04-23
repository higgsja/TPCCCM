package OptionController;

import com.hpi.TPCCMcontrollers.*;
import com.hpi.TPCCMprefs.*;
import lombok.*;
import org.junit.*;

@Getter @Setter
public class OptionControllerBase
{

    public static OptionController optionController;

    public static final Integer USER_ID = 5;

    @BeforeClass
    public static void beforeClass()
    {
        CMDBModel.getInstance();
        CMDBModel.setUserId(USER_ID);
        CMDBController.initDBConnection();

        CMLanguageController.getInstance();

        optionController = OptionController.getInstance();
    }

    @AfterClass
    public static void afterClass()
    {
    }

    @Before
    public void before()
    {
        //clear closedOptionFIFO for user id
        CMDBController.executeSQL("delete from hlhtxc5_dmOfx.ClosedOptionFIFO where JoomlaId = " + USER_ID);

        //clear closedOptionTrans for user id
        CMDBController.executeSQL("delete from hlhtxc5_dmOfx.ClosedOptionTrans where JoomlaId = " + USER_ID);

        //clear openOptionFIFO for user id
        CMDBController.executeSQL("delete from hlhtxc5_dmOfx.OpenOptionFIFO where JoomlaId = " + USER_ID);

        //clear OpeningOptions
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx.OpeningOptions where JoomlaId = " + USER_ID);

        //clear ClosingOptions
        CMDBController
            .executeSQL("delete from hlhtxc5_dmOfx.ClosingOptions where JoomlaId = " + USER_ID);

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

        optionController.getAccountList().clear();
        optionController.getEquityIdList().clear();
        optionController.getOptionOpeningBuyList().clear();
        optionController.getOptionClosingSellList().clear();
        optionController.getOptionOpeningSellList().clear();
        optionController.getOptionClosingBuyList().clear();
        optionController.getOptionClosedList().clear();
        optionController.getOptionClosedTransList().clear();
        optionController.getOptionOpenList().clear();
    }
}
