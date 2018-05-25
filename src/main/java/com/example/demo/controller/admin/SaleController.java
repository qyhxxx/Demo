package com.example.demo.controller.admin;

import com.example.demo.mapper.InventoryMapper;
import com.example.demo.model.CompleteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/sale")
public class SaleController {
    @Autowired
    private InventoryMapper inventoryMapper;

    @GetMapping("/{period}/list/{para}")
    public String  listPage(@PathVariable(value = "period") String period, @PathVariable(value = "para") String para, Model model) {
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
        model.addAttribute("period", period);
        model.addAttribute("para", para);
        model.addAttribute("list", list);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalAmount", totalAmount);
        return "/admin/sale/list";
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

    public void export() {

    }
}
