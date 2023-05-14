package com.example.exportcsv.controller;

import com.example.exportcsv.model.MemberDTO;
import com.example.exportcsv.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public void findMembers(){
        System.out.println("=======");
        List<MemberDTO> members = memberService.findMembers();
        System.out.println(members.get(1));  //MemberDTO{id=1, name=betty, age=15}
        System.out.println("=======");
    }

    @GetMapping("/download/single")
    public ResponseEntity<InputStreamResource>
    exportCSV(HttpServletResponse response)
            throws Exception {

        String filename = "members.csv";
        InputStreamResource file = new InputStreamResource
                (memberService.memberToCSV());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + filename)
                .contentType(MediaType
                        .parseMediaType("application/csv")).
                        body(file);

    }


    @GetMapping("/download/multiple")
    public ResponseEntity<StreamingResponseBody> downloadZip(HttpServletResponse response){
        int BUFFER_SIZE = 1024;
        StreamingResponseBody streamingResponseBody = out -> {
            final ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
            ZipEntry zipEntry = null;
            ByteArrayInputStream byteArrayInputStream = null;

            for (int i=0; i<3; i++){
                byteArrayInputStream = memberService.memberToCSV();
                zipEntry = new ZipEntry("memberfile"+i+".csv");
                zipOutputStream.putNextEntry(zipEntry);
                byte[] bytes = new byte[BUFFER_SIZE];
                int length;
                while ((length = byteArrayInputStream.read(bytes))>=0){
                    zipOutputStream.write(bytes, 0, length);
                }
            }
            response.setContentLength((int) (zipEntry !=null ? zipEntry.getSize() : 0));
            if (byteArrayInputStream !=null) byteArrayInputStream.close();
            if (zipOutputStream !=null) zipOutputStream.close();

        };
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=example.zip");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");

        return ResponseEntity.ok(streamingResponseBody);
    }
}
