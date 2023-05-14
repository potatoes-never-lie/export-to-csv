package com.example.exportcsv.mapper;

import com.example.exportcsv.model.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemberMapper {


    public List<MemberDTO> findItemsById(int id);
}
