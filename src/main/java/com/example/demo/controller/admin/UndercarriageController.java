package com.example.demo.controller.admin;

import com.example.demo.helper.Function;
import com.example.demo.mapper.GoodsMapper;
import com.example.demo.mapper.UndercarriageMapper;
import com.example.demo.model.CompleteInfo;
import com.example.demo.model.Goods;
import com.example.demo.model.Undercarriage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/undercarriage")
public class UndercarriageController {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private UndercarriageMapper undercarriageMapper;

    @GetMapping("/goods/{gid}")
    public String undercarriagePage(@PathVariable(value = "gid") int gid, Model model) {
        Goods goods = goodsMapper.findByGid(gid);
        model.addAttribute("goods", goods);
        return "/admin/undercarriage/goods";
    }

    @PostMapping("/goods/{gid}")
    @Transactional
    public String undercarriage(@PathVariable(value = "gid") int gid, @RequestParam(value = "reason") String reason) {
        Goods goods = goodsMapper.findByGid(gid);
        goods.setStatus(0);
        goodsMapper.updateStatus(goods);
        Undercarriage undercarriage = new Undercarriage();
        undercarriage.setGid(gid);
        undercarriage.setUndtime(Function.getSqlDate());
        undercarriage.setReason(reason);
        undercarriageMapper.insert(undercarriage);
        return "redirect:/admin/undercarriage/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<CompleteInfo> list = undercarriageMapper.list();
        model.addAttribute("list", list);
        return "/admin/undercarriage/list";
    }

    @GetMapping("/recovery/goods/{gid}")
    @Transactional
    public String recovery(@PathVariable(value = "gid") int gid) {
        Goods goods = goodsMapper.findByGid(gid);
        goods.setStatus(1);
        goodsMapper.updateStatus(goods);
        undercarriageMapper.delete(gid);
        return "redirect:/admin/goods/list/" + goods.getTypeid();
    }
}
