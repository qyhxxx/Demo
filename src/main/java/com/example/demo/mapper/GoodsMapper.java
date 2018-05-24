package com.example.demo.mapper;

import com.example.demo.model.CompleteInfo;
import com.example.demo.model.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsMapper {
    @Select("select * from goods where typeid = #{typeid} and status = 1 order by gid desc")
    List<Goods> list(int typeid);

    @Insert("insert into goods (gname, typeid, brand, color, size, crowd, starttime, deadline, provenance, price, count, status) values " +
            "(#{gname}, #{typeid}, #{brand}, #{color}, #{size}, #{crowd}, #{starttime}, #{deadline}, #{provenance}, #{price}, #{count}, #{status})")
    @Options(useGeneratedKeys = true, keyColumn = "gid", keyProperty = "gid")
    void insert(Goods goods);

    @Select("select * from goods where gid = #{gid}")
    Goods findByGid(int gid);

    @Update("update goods set count = #{count} where gid = #{gid}")
    void updateCount(Goods goods);

    @Update("update goods set status = #{status} where gid = #{gid}")
    void updateStatus(Goods goods);

    @Select("select * from goods where typeid = 1 and (#{gname} is null or gname like #{gname}) and " +
            "(#{brand} is null or brand like #{brand}) and (#{color} is null or color like #{color}) and " +
            "(#{size} is null or size like #{size}) and (#{crowd} is null or crowd like #{crowd}) " +
            "order by gid desc")
    List<CompleteInfo> searchClothes(@Param("gname") String gname, @Param("brand") String brand,
                                     @Param("color") String color, @Param("size") String size,
                                     @Param("crowd") String crowd);

    @Select("select * from goods where typeid = 2 and (#{gname} is null or gname like #{gname}) and " +
            "(#{brand} is null or brand like #{brand}) and (#{deadline} is null or deadline like #{deadline}) and " +
            "(#{provenance} is null or provenance like #{provenance}) " +
            "order by gid desc")
    List<CompleteInfo> searchFoods(@Param("gname") String gname, @Param("brand") String brand,
                            @Param("deadline") String deadline, @Param("provenance") String provenance);
}
