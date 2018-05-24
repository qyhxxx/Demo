package com.example.demo.mapper;

import com.example.demo.model.CompleteInfo;
import com.example.demo.model.Undercarriage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UndercarriageMapper {
    @Insert("insert into undercarriage (gid, undtime, reason) values (#{gid}, #{undtime}, #{reason})")
    @Options(useGeneratedKeys = true, keyColumn = "uid", keyProperty = "uid")
    void insert(Undercarriage undercarriage);

    @Select("select G.gname, G.brand, GT.type, U.undtime, U.reason " +
            "from undercarriage U " +
            "left join goods G on U.gid = G.gid " +
            "left join goods_type GT on GT.typeid = G.typeid " +
            "order by U.undtime desc")
    List<CompleteInfo> list();

    @Delete("delete from undercarriage where gid = #{gid}")
    void delete(int gid);
}
