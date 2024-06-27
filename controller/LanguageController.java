package com.devdoc.backend.controller;

import com.devdoc.backend.dto.LanguageDTO;
import com.devdoc.backend.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
public class LanguageController {


    @Autowired
    private LanguageService languageService;


    // language 데이터 저장 또는 수정
    @PostMapping("/{resumeId}/languages")
    public ResponseEntity<LanguageDTO> saveOrUpdateLanguage(@PathVariable int resumeId, @RequestBody LanguageDTO languageDTO) {
        try {
            LanguageDTO updatedLanguage = languageService.saveOrUpdateLanguage(resumeId, languageDTO);
            return ResponseEntity.ok(updatedLanguage); // 업데이트된 language 데이터 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 서버 에러 발생 시 500 반환
        }
    }

    // language 데이터 삭제
    @DeleteMapping("/{resumeId}/languages/{languageId}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable int resumeId, @PathVariable int languageId) {
        try {
            languageService.deleteLanguage(resumeId, languageId);
            return ResponseEntity.noContent().build(); // language 데이터 삭제 후 no content 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // language 데이터 수정
    @PutMapping("/{resumeId}/languages")
    public ResponseEntity<LanguageDTO> updateLanguage(@PathVariable int resumeId, @RequestBody LanguageDTO languageDTO) {
        try {
            LanguageDTO updatedLanguage = languageService.saveOrUpdateLanguage(resumeId, languageDTO);
            return ResponseEntity.ok(updatedLanguage); // 수정된 language 데이터 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
