package com.example.demo.controller.admin;

import com.example.demo.mapper.InventoryMapper;
import com.example.demo.model.CompleteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/stock")
public class StockController {
    @Autowired
    private InventoryMapper inventoryMapper;

    @GetMapping("/list")
    public String list(Model model) {
        List<CompleteInfo> list = inventoryMapper.stockList();
        int totalAmount = 0;
        for (CompleteInfo goods : list) {
            totalAmount += goods.getTotalprice();
        }
        model.addAttribute("list", list);
        model.addAttribute("totalAmount", totalAmount);
        return "/admin/stock/list";
    }
}
