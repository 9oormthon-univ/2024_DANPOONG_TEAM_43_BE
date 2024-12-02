package com.carely.backend.controller;

import com.carely.backend.code.SuccessCode;
import com.carely.backend.dto.certificate.CertificateDTO;
import com.carely.backend.dto.response.ResponseDTO;
import com.carely.backend.dto.user.CustomUserDetails;
import com.carely.backend.service.ocr.OCRService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/certificates")
public class OCRController  {

    private final OCRService ocrService;

    public OCRController(OCRService ocrService) {
        this.ocrService = ocrService;
    }
    @PostMapping(value = "/extract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> extractText(@RequestPart("file") MultipartFile file) throws Exception {
//        if (user.getUsername() == null) {
//            // ���� �� �� ����... 1�� �����ְų� �ؾ� ��....
//            // �������, �̸� �־�� ��... �׷��� �ڰ��� ���� ������ ��?
//        }
//        else {
//            // ������ �� �������........... ������ �ϰ� �� ������ isCertificated �� 1�� �ٲ������
//            CertificateDTO certificateDTO = ocrService.extractTextAlreadyUser(file, user.getUsername());
//        }


        return ResponseEntity
                .status(SuccessCode.SUCCESS_OCR.getStatus().value())
                .body(new ResponseDTO<>(SuccessCode.SUCCESS_OCR, null));
    }
}
