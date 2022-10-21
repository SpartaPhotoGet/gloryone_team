package com.example.loginlivesession2.controller;

import com.example.loginlivesession2.global.ResponseDto;
import com.example.loginlivesession2.security.user.UserDetailsImpl;
import com.example.loginlivesession2.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/folder/{folderId}")
    public ResponseDto<?> createFolder(@RequestPart(required = false, value = "file") MultipartFile multipartFile,
                                      @PathVariable Long folderId,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return ResponseDto.success(folderService.createFolder(multipartFile, folderId, userDetails.getAccount()));
    }
}
