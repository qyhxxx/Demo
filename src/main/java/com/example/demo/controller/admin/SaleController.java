package com.example.demo.controller.admin;

import com.example.demo.helper.Function;
import com.example.demo.mapper.GoodsMapper;
import com.example.demo.mapper.InventoryMapper;
import com.example.demo.model.CompleteInfo;
import com.example.demo.model.Goods;
import com.example.demo.model.Inventory;
import com.example.demo.model.SaleTable;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/sale")
public class SaleController {
    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @GetMapping("/{period}/list/{para}")
    public String listPage(@PathVariable(value = "period") String period, @PathVariable(value = "para") String para, Model model) {
        SaleTable saleTable = this.getSaleTable(period, para);
        model.addAttribute("period", period);
        model.addAttribute("para", para);
        model.addAttribute("list", saleTable.list);
        model.addAttribute("totalCount", saleTable.totalCount);
        model.addAttribute("totalAmount", saleTable.totalAmount);
        return "/admin/sale/list";
    }

    private SaleTable getSaleTable(String period, String para) {
        Map<String, String> map = new HashMap<>();
        map.put("period", period);
        map.put("para", para);
        List<CompleteInfo> list = inventoryMapper.saleList(map);
        int totalCount = 0;
        int totalAmount = 0;
        for (CompleteInfo saleInfo : list) {
            totalCount += saleInfo.getSaleCount();
            totalAmount += saleInfo.getSaleAmount();
        }
        return new SaleTable(list, totalCount, totalAmount);
    }

    public String saleSql(Map<String, String> map) {
        String period = map.get("period");
        String para = map.get("para");
        if (para.equals("1")) {
            return "select GT.type, G.gid, G.gname, G.brand, G.status, sum(I.count) as saleCount, sum(I.totalprice) as saleAmount, " + period + "(curdate()) as time " +
                    "from inventory I " +
                    "left join goods G on G.gid = I.gid " +
                    "left join goods_type GT on GT.typeid = G.typeid " +
                    "where I.issale = 1 and " + period + "(invtime) = " + period + "(curdate()) " +
                    "group by I.gid " +
                    "order by I.gid desc";
        } else {
            return "select G.brand, G.status, sum(I.count) as saleCount, sum(I.totalprice) as saleAmount, " + period + "(curdate()) as time " +
                    "from inventory I " +
                    "left join goods G on G.gid = I.gid " +
                    "where I.issale = 1 and " + period + "(invtime) = " + period + "(curdate()) " +
                    "group by G.brand " +
                    "order by I.gid desc";
        }
    }

    @GetMapping("/{period}/export/{para}")
    public void export(@PathVariable(value = "period") String period, @PathVariable(value = "para") String para,
                       HttpServletResponse httpServletResponse) {
        SaleTable saleTable = this.getSaleTable(period, para);
        List<CompleteInfo> list = saleTable.list;
        int totalCount = saleTable.totalCount;
        int totalAmount = saleTable.totalAmount;
        Object[] outfit, tail;
        Object[][] cellData;
        if (para.equals("1")) {
            outfit = new Object[]{"类别", "名称", "品牌", "销售数量", "销售金额"};
            tail = new Object[]{"合计", "", "", totalCount, totalAmount};
            cellData = new Object[list.size()][5];
            for (int i = 0; i < list.size(); i++) {
                CompleteInfo completeInfo = list.get(i);
                cellData[i][0] = completeInfo.getType();
                cellData[i][1] = completeInfo.getGname();
                cellData[i][2] = completeInfo.getBrand();
                cellData[i][3] = completeInfo.getSaleCount();
                cellData[i][4] = completeInfo.getSaleAmount();
            }
        } else {
            outfit = new Object[]{"品牌", "销售数量", "销售金额"};
            tail = new Object[]{"合计", totalCount, totalAmount};
            cellData = new Object[list.size()][3];
            for (int i = 0; i < list.size(); i++) {
                CompleteInfo completeInfo = list.get(i);
                cellData[i][0] = completeInfo.getBrand();
                cellData[i][1] = completeInfo.getSaleCount();
                cellData[i][2] = completeInfo.getSaleAmount();
            }
        }
        String filename = "销售情况";
        Function.exportExcel(outfit, cellData, tail, filename, httpServletResponse);
    }

    @GetMapping("/simulateBuy/{gid}")
    public String buyPage(@PathVariable(value = "gid") int gid, Model model) {
        Goods goods = goodsMapper.findByGid(gid);
        model.addAttribute("goods", goods);
        model.addAttribute("message", "");
        return "/admin/sale/buy";
    }

    @PostMapping("/simulateBuy/{gid}")
    @Transactional
    public String buy(@PathVariable(value = "gid") int gid, @RequestParam(value = "count") int count, Model model) {
        Goods goods = goodsMapper.findByGid(gid);
        if (goods.getCount() - count < 0) {
            model.addAttribute("goods", goods);
            model.addAttribute("message", "库存不足");
            return "/admin/sale/buy";
        } else {
            goods.setCount(goods.getCount() - count);
            goodsMapper.updateCount(goods);
            Inventory inventory = new Inventory();
            inventory.setGid(goods.getGid());
            inventory.setUid(1);
            inventory.setInvtime(Function.getSqlDate());
            inventory.setIssale(1);
            inventory.setCount(count);
            int price = goods.getPrice();
            inventory.setUnitprice(price);
            inventory.setTotalprice(count * price);
            inventoryMapper.insert(inventory);
            return "redirect:/admin/sale/record";
        }
    }

    @GetMapping("/record")
    public String record(Model model) {
        List<CompleteInfo> record = inventoryMapper.saleRecord();
        int totalAmount = 0;
        for (CompleteInfo goods : record) {
            totalAmount += goods.getTotalprice();
        }
        model.addAttribute("list", record);
        model.addAttribute("totalAmount", totalAmount);
        return "/admin/sale/record";
    }
}
