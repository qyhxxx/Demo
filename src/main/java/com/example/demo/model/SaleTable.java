package com.example.demo.model;

import java.util.List;

public class SaleTable {
    public List<CompleteInfo> list;
    public int totalCount;
    public int totalAmount;

    public SaleTable(List<CompleteInfo> list, int totalCount, int totalAmount) {
        this.list = list;
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
    }
}
