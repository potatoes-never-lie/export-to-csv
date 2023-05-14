package com.example.exportcsv.service;

import com.example.exportcsv.mapper.MemberMapper;
import com.example.exportcsv.model.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberMapper memberMapper;
    private static final String[] HEADERS = { "ID", "NAME", "AGE"};

    public List<MemberDTO> findMembers(){
        return memberMapper.findItemsById(1);
    }

    public ByteArrayInputStream memberToCSV(){  //id 읽어서 1 csv file
        List<MemberDTO> memberList = memberMapper.findItemsById(1);
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(HEADERS).build();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), csvFormat);
            for (MemberDTO member : memberList){
                List<String> data = Arrays.asList(String.valueOf(member.getId()),
                        member.getName(),
                        String.valueOf(member.getAge()));
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
