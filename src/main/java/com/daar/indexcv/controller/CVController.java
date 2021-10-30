package com.daar.indexcv.controller;

import com.daar.indexcv.entity.CV;
import com.daar.indexcv.entity.CVShort;
import com.daar.indexcv.representation.CVShortRepresentation;
import com.daar.indexcv.service.CVService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    /**
     * @author Wenzhuo ZHAO
     * add a cv with the name of published
     * @param username the name of publisher of CV
     * @param file the CV file in pdf or docx
     * @return ResponseEntity<String> containing the CV's id in ElasticSearch
     */
    @PostMapping("/cvs")
    public ResponseEntity<String> addCV(@NotNull(message = "The username cannot be null") @NotEmpty(message = "The username cannot be empty") String username ,
                                        @NotNull(message = "The file cannot be null") MultipartFile file) throws IOException {
        String id = cvService.save(username, file);
        return ResponseEntity.ok().body(id);
    }


    /**
     * @author Zhaojie LU
     * get all cv only without data(base64)
     */
    @GetMapping("/cvs")
    public ResponseEntity<List<CVShort>> getAllCV(){
        return ResponseEntity.ok().body(cvService.query());
    }

    /**
     * @author Zhaojie LU
     * search CVs with a keyword
     * @param keyword the keyword to search with
     * @return ResponseEntity<List<CVShort>> a list of CVs without the data(base64)
     */
    @GetMapping("/cvs/search")
    public ResponseEntity<List<CVShort>> queryInContent(@RequestParam("keyword") @NotNull(message = "The keyword cannot be null") String keyword)throws IOException {
        return ResponseEntity.ok().body(cvService.queryInContent(keyword));
    }

    /**
     * @author Zhen HOU
     * get a CV by its id
     * TODO: use PathVariable to make the API RestFul
     * @param id the id of CV in ElasticSearch
     * @return ResponseEntity<CV> the CV
     */
    @GetMapping("/cvs/get")
    public ResponseEntity<CV> getCVbyId(@RequestParam("id") @NotNull(message = "The id cannot be null") @NotEmpty(message = "The id cannot be empty") String id)throws IOException {
        return ResponseEntity.ok().body(cvService.queryGetById(id));
    }

    /**
     * @author Zhen HOU
     * update a cv by its id
     * @param username the username to modify
     * @param file the file to modify
     * @param id the CV's id
     * @return ResponseEntity<String> containing the CV's id in ElasticSearch
     */
    @PutMapping("/cvs/update")
    public ResponseEntity<String> updateCV(@NotNull(message = "The username cannot be null") @NotEmpty(message = "The username cannot be empty") String username ,
                                        @NotNull(message = "The file cannot be null") MultipartFile file,
                                           @RequestParam("id") @NotNull(message = "The keyword cannot be null") String id  ) throws IOException {
         cvService.updateCV(id, file,username);
        return ResponseEntity.ok().body(id);
    }

    /**
     * @author Chengyu YANG
     * delete a CV by its id
     * @param id the id of the CV to be deleted
     */
    @DeleteMapping("/cvs/{id}, /cvs")
    public ResponseEntity<Void> deleteCV(@PathVariable(value = "id") String id){
        cvService.deleteCV(id);
        return ResponseEntity.ok().build();
    }

}
