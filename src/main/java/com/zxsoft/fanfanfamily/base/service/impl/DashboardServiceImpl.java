package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.domain.vo.*;
import com.zxsoft.fanfanfamily.base.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Override
    public Dashboard fetchDashboardData() {
        //演示数据
        Dashboard newItem = new Dashboard();

        List<MortAmount> mortAmounts = new ArrayList<>();
        BestEmployeeMonth bestEmployeeMonth = new BestEmployeeMonth();
        List<HeadNumber> headNumbers = new ArrayList<>();
        List<CompleteRateByDept> completeRateByDepts = new ArrayList<>();
        List<CompleteRateByBank> completeRateByBanks = new ArrayList<>();

        //region add mortAmount
        MortAmount itemMort1 = new MortAmount();
        itemMort1.setMonth(1);
        itemMort1.setSaveAmount(264);
        itemMort1.setKeepAmount(241);

        MortAmount itemMort2 = new MortAmount();
        itemMort2.setMonth(2);
        itemMort2.setSaveAmount(237);
        itemMort2.setKeepAmount(253);

        MortAmount itemMort3 = new MortAmount();
        itemMort3.setMonth(3);
        itemMort3.setSaveAmount(207);
        itemMort3.setKeepAmount(369);

        MortAmount itemMort4 = new MortAmount();
        itemMort4.setMonth(4);
        itemMort4.setSaveAmount(359);
        itemMort4.setKeepAmount(289);

        MortAmount itemMort5 = new MortAmount();
        itemMort5.setMonth(5);
        itemMort5.setSaveAmount(425);
        itemMort5.setKeepAmount(198);

        MortAmount itemMort6 = new MortAmount();
        itemMort6.setMonth(6);
        itemMort6.setSaveAmount(374);
        itemMort6.setKeepAmount(260);

        MortAmount itemMort7 = new MortAmount();
        itemMort7.setMonth(7);
        itemMort7.setSaveAmount(496);
        itemMort7.setKeepAmount(383);

        mortAmounts.add(itemMort1);
        mortAmounts.add(itemMort2);
        mortAmounts.add(itemMort3);
        mortAmounts.add(itemMort4);
        mortAmounts.add(itemMort5);
        mortAmounts.add(itemMort6);
        mortAmounts.add(itemMort7);
        //endregion

        //region add headNumber
        HeadNumber itemHead1 = new HeadNumber();
        itemHead1.setColor(Color.green);
        itemHead1.setTitle("存量");
        itemHead1.setSubTitle1("成交笔数");
        itemHead1.setSubTitle2("金额 (万元)");
        itemHead1.setNumbers(323);
        itemHead1.setAmount(28512.2927);
        itemHead1.setIcon("pay-circle-o");

        HeadNumber itemHead2 = new HeadNumber();
        itemHead2.setTitle("在保");
        itemHead2.setColor(Color.blue);
        itemHead2.setSubTitle1("成交笔数");
        itemHead2.setSubTitle2("金额 (万元)");
        itemHead2.setNumbers(370);
        itemHead2.setAmount(34168.7);
        itemHead2.setIcon("pay-circle-o");

        HeadNumber itemHead3 = new HeadNumber();
        itemHead3.setTitle("在线用户");
        itemHead3.setColor(Color.purple);
        itemHead3.setSubTitle1("人数");
        itemHead3.setNumbers(86);
        itemHead3.setIcon("team");

        headNumbers.add(itemHead1);
        headNumbers.add(itemHead2);
        headNumbers.add(itemHead3);
        //endregion

        //region add Dept
        CompleteRateByDept itemDept1 = new CompleteRateByDept();
        itemDept1.setName("虎门分公司");
        itemDept1.setDealAmount(2200.56);
        itemDept1.setDealNumber(200);
        itemDept1.setRate(0.89);
        itemDept1.setStatus(3);
        CompleteRateByDept itemDept2 = new CompleteRateByDept();
        itemDept2.setName("常平分公司");
        itemDept2.setDealAmount(3200.56);
        itemDept2.setDealNumber(256);
        itemDept2.setRate(0.68);
        itemDept2.setStatus(2);
        CompleteRateByDept itemDept3 = new CompleteRateByDept();
        itemDept3.setName("松山湖分公司");
        itemDept3.setDealAmount(2200.56);
        itemDept3.setDealNumber(200);
        itemDept3.setRate(0.46);
        itemDept3.setStatus(1);
        CompleteRateByDept itemDept4 = new CompleteRateByDept();
        itemDept4.setName("城区分公司");
        itemDept4.setDealAmount(2200.56);
        itemDept4.setDealNumber(200);
        itemDept4.setRate(1.08);
        itemDept4.setStatus(4);
        completeRateByDepts.add(itemDept1);
        completeRateByDepts.add(itemDept2);
        completeRateByDepts.add(itemDept3);
        completeRateByDepts.add(itemDept4);
        //endregion

        //region add Dept
        CompleteRateByBank itemBank1 = new CompleteRateByBank();
        itemBank1.setName("建设银行");
        itemBank1.setDealAmount(2200.56);
        itemBank1.setDealNumber(200);
        CompleteRateByBank itemBank2 = new CompleteRateByBank();
        itemBank2.setName("华夏银行");
        itemBank2.setDealAmount(3200.56);
        itemBank2.setDealNumber(256);
        CompleteRateByBank itemBank3 = new CompleteRateByBank();
        itemBank3.setName("招商银行");
        itemBank3.setDealAmount(2200.56);
        itemBank3.setDealNumber(200);

        completeRateByBanks.add(itemBank1);
        completeRateByBanks.add(itemBank2);
        completeRateByBanks.add(itemBank3);
        //endregion

        //region add Employee
        bestEmployeeMonth.setMonth(8);
        bestEmployeeMonth.setMsg("如果机会只有一次，那就全力翻滚");
        bestEmployeeMonth.setDealAmount(652.8);
        bestEmployeeMonth.setNumbers(10);
        bestEmployeeMonth.setName("东东");
        bestEmployeeMonth.setAvatarPath("http://tva4.sinaimg.cn/crop.0.0.996.996.180/6ee6a3a3jw8f0ks5pk7btj20ro0rodi0.jpg");
        //endregion

        newItem.setBestEmployeeMonth(bestEmployeeMonth);
        newItem.setCompleteRateByBanks(completeRateByBanks);
        newItem.setCompleteRateByDepts(completeRateByDepts);
        newItem.setHeadNumbers(headNumbers);
        newItem.setMortAmounts(mortAmounts);

        return newItem;
    }
}
