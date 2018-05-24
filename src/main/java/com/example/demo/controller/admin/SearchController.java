package com.example.demo.controller.admin;

import com.example.demo.helper.Function;
import com.example.demo.mapper.GoodsMapper;
import com.example.demo.mapper.InventoryMapper;
import com.example.demo.model.CompleteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/search")
public class SearchController {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @GetMapping("/{typeid}")
    public String searchPage(@PathVariable(value = "typeid") int typeid, Model model) {
        model.addAttribute("typeid", typeid);
        model.addAttribute("method", "get");
        return "/admin/search/search";
    }

    @PostMapping("/{typeid}")
    public String search(@PathVariable(value = "typeid") int typeid, Model model,
                       @RequestParam(value = "gname", required = false) String gname,
                       @RequestParam(value = "brand", required = false) String brand,
                       @RequestParam(value = "color", required = false) String color,
                       @RequestParam(value = "size", required = false) String size,
                       @RequestParam(value = "crowd", required = false) String crowd,
                       @RequestParam(value = "deadline", required = false) String deadline,
                       @RequestParam(value = "provenance", required = false) String provenance,
                       @RequestParam(value = "starttime", required = false) String starttime,
                       @RequestParam(value = "endtime", required = false) String endtime) {
        gname = this.concat(gname);
        brand = this.concat(brand);
        color = this.concat(color);
        size = this.concat(size);
        crowd = this.concat(crowd);
        deadline = this.concat(deadline);
        provenance = this.concat(provenance);
        Date startDate = Function.getSqlDate(starttime);
        Date endDate = Function.getSqlDate(endtime);
        List<CompleteInfo> list;
        if (typeid == 1) {
            list = goodsMapper.searchClothes(gname, brand, color, size, crowd);
        } else {
            list = goodsMapper.searchFoods(gname, brand, deadline, provenance);
        }
        for (CompleteInfo goods : list) {
            CompleteInfo saleInfo = inventoryMapper.saleInfo(goods.getGid(), startDate, endDate);
            if (saleInfo != null) {
                goods.setSaleCount(saleInfo.getSaleCount());
                goods.setSaleAmount(saleInfo.getSaleAmount());
            }
        }
        model.addAttribute("typeidid", typeid);
        model.addAttribute("method", "post");
        model.addAttribute("list", list);
        return "/admin/search/search";
    }

    public String concat(String value) {
        return '%' + value + '%';
    }
}
