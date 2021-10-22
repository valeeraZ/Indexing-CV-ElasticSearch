package com.daar.indexcv.controller;

import com.daar.indexcv.entity.CV;
import com.daar.indexcv.service.CVService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * Created by Wenzhuo Zhao on 20/10/2021.
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api")
@Slf4j
@Validated
public class CVController {
    private final CVService cvService;

    @PostMapping("/cv")
    public ResponseEntity<Void> addCV(@NotNull(message = "The file cannot be null") MultipartFile file) throws IOException {
        cvService.save(file);
        return ResponseEntity.ok().build();
    }


    //Zhaojie LU
    @ResponseBody
    @GetMapping("/AllCV")
    public List<CV> getAllCV(){
        return cvService.query();
    }


    //Zhaojie LU
    @ResponseBody
    @GetMapping("/search")
    public List<CV> queryInContent(@RequestParam("keyword") String keyword){
        return cvService.queryInContent(keyword);
    }
}
