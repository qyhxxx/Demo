package com.example.demo.controller.admin;

import com.example.demo.helper.Function;
import com.example.demo.mapper.GoodsMapper;
import com.example.demo.mapper.InventoryMapper;
import com.example.demo.model.Goods;
import com.example.demo.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/goods")
public class GoodsController {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @GetMapping("/list/{typeid}")
    public String list(@PathVariable(value = "typeid") int typeid, Model model) {
        List<Goods> list = goodsMapper.list(typeid);
        model.addAttribute("list", list);
        model.addAttribute("typeid", typeid);
        return "/admin/goods/list";
    }

    @GetMapping("/add/{typeid}")
    public String addPage(@PathVariable(value = "typeid") int typeid, Model model) {
        model.addAttribute("goods", new Goods());
        model.addAttribute("typeid", typeid);
        return "/admin/goods/add";
    }

    @PostMapping("/add/{typeid}")
    @Transactional
    public String add(@PathVariable(value = "typeid") int typeid, @ModelAttribute Goods goods) {
        goods.setStatus(1);
        Date starttime = Function.getSqlDate();
        goods.setStarttime(starttime);
        goodsMapper.insert(goods);
        Inventory inventory = new Inventory();
        inventory.setGid(goods.getGid());
        inventory.setUid(1);
        inventory.setInvtime(starttime);
        inventory.setIssale(0);
        int count = goods.getCount();
        int price = goods.getPrice();
        inventory.setCount(count);
        inventory.setUnitprice(price);
        inventory.setTotalprice(count * price);
        inventoryMapper.insert(inventory);
        return "redirect:/admin/goods/list/" + typeid;
    }

    @GetMapping("/stock/{gid}")
    public String stockPage(@PathVariable(value = "gid") int gid, Model model) {
        Goods goods = goodsMapper.findByGid(gid);
        model.addAttribute("goods", goods);
        return "/admin/goods/stock";
    }

    @PostMapping("/stock/{gid}")
    @Transactional
    public String stock(@PathVariable(value = "gid") int gid, @RequestParam(value = "count") int count) {
        Goods goods = goodsMapper.findByGid(gid);
        goods.setCount(goods.getCount() + count);
        goodsMapper.updateCount(goods);
        Inventory inventory = new Inventory();
        inventory.setGid(goods.getGid());
        inventory.setUid(1);
        inventory.setInvtime(Function.getSqlDate());
        inventory.setIssale(0);
        inventory.setCount(count);
        int price = goods.getPrice();
        inventory.setUnitprice(price);
        inventory.setTotalprice(count * price);
        inventoryMapper.insert(inventory);
        return "redirect:/admin/goods/list/" + goods.getTypeid();
    }
}
