package com.example.demo.mapper;

import com.example.demo.controller.admin.SaleController;
import com.example.demo.model.CompleteInfo;
import com.example.demo.model.Inventory;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface InventoryMapper {
    @Insert("insert into inventory (gid, uid, invtime, issale, count, unitprice, totalprice) values " +
            "(#{gid}, #{uid}, #{invtime}, #{issale}, #{count}, #{unitprice}, #{totalprice})")
    @Options(useGeneratedKeys = true, keyColumn = "invid", keyProperty = "invid")
    void insert(Inventory inventory);

    @Select("select sum(count) as salecount, sum(totalprice) as saleAmount from Inventory " +
            "where gid = #{gid} and issale = 1 and " +
            "(#{startDate} is null or invtime >= #{startDate}) and " +
            "(#{endDate} is null or invtime <= #{endDate}) " +
            "group by gid")
    CompleteInfo saleInfo(@Param(value = "gid") int gid, @Param("startDate") Date startDate,
                        @Param(value = "endDate") Date endDate);

    @SelectProvider(type = SaleController.class, method = "saleSql")
    List<CompleteInfo> saleList(Map<String, String> map);

    @Select("select GT.type, G.gname, G.brand, I.invtime, I.count, I.unitprice, I.totalprice " +
            "from inventory I " +
            "left join goods G on G.gid = I.gid " +
            "left join goods_type GT on GT.typeid = G.typeid " +
            "where I.issale = 0 " +
            "order by I.invtime desc")
    List<CompleteInfo> stockList();

    @Select("select GT.type, G.gname, G.brand, I.invtime, I.count, I.unitprice, I.totalprice " +
            "from inventory I " +
            "left join goods G on G.gid = I.gid " +
            "left join goods_type GT on GT.typeid = G.typeid " +
            "where I.issale = 1 " +
            "order by I.invtime desc")
    List<CompleteInfo> saleRecord();
}
