package com.devdoc.backend.controller;

import com.devdoc.backend.dto.AwardDTO;
import com.devdoc.backend.service.AwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
public class AwardController {

    @Autowired
    private AwardService awardService;


    // Award 데이터 저장 또는 수정
    @PostMapping("/{resumeId}/awards")
    public ResponseEntity<AwardDTO> saveOrUpdateAward(@PathVariable int resumeId, @RequestBody AwardDTO awardDTO) {
        try {
            AwardDTO updatedAward = awardService.saveOrUpdateAward(resumeId, awardDTO);
            return ResponseEntity.ok(updatedAward); // 수정된 Award 데이터 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // Award 데이터 삭제
    @DeleteMapping("/{resumeId}/awards/{awardId}")
    public ResponseEntity<Void> deleteAward(@PathVariable int resumeId, @PathVariable int awardId) {
        try {
            awardService.deleteAward(resumeId, awardId);
            return ResponseEntity.noContent().build(); //Award 데이터 삭제 후 no content 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Award 데이터 수정
    @PutMapping("/{resumeId}/awards")
    public ResponseEntity<AwardDTO> updateAward(@PathVariable int resumeId, @RequestBody AwardDTO awardDTO) {
        try {
            AwardDTO updatedAward = awardService.saveOrUpdateAward(resumeId, awardDTO);
            return ResponseEntity.ok(updatedAward); // 업데이트된 Award 데이터 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
