package com.carely.backend.controller;

import com.carely.backend.code.SuccessCode;
import com.carely.backend.controller.docs.MapAPI;
import com.carely.backend.domain.enums.UserType;
import com.carely.backend.dto.response.ResponseDTO;
import com.carely.backend.dto.user.MapUserDTO;
import com.carely.backend.jwt.JWTUtil;
import com.carely.backend.repository.RefreshRepository;
import com.carely.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController implements MapAPI {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRedisRepository;
    private final UserService userService;

//    @GetMapping("/user-list")
//    public ResponseEntity<ResponseDTO> findUsersByCityAndOptionalUserTypes(
//            @RequestParam("city") String city,
//            @RequestParam(value = "userType", required = false) List<UserType> userTypes) {
//        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        List<MapUserDTO> res;
//
//        res = userService.findUsersByCityAndOptionalUserTypes(city, userTypes, kakaoId);
//
//        return ResponseEntity
//                .status(SuccessCode.SUCCESS_RETRIEVE_USER.getStatus().value())
//                .body(new ResponseDTO<>(SuccessCode.SUCCESS_RETRIEVE_USER, res));
//    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO> findUsersByCityAndOptionalUserTypes(
            @RequestParam(value = "userType", required = false) List<UserType> userTypes) {
        String kakaoId = SecurityContextHolder.getContext().getAuthentication().getName();


        List<MapUserDTO> res;

        if (userTypes == null || userTypes.isEmpty() ||userTypes.contains(UserType.ALL)) {
            // userType이 없을 경우
            res = userService.findAllUsers(kakaoId);
        } else {
            // userType이 있을 경우
            res = userService.findAllUsersByCityAndUserTypes(userTypes, kakaoId);
        }

        return ResponseEntity
                .status(SuccessCode.SUCCESS_RETRIEVE_USER.getStatus().value())
                .body(new ResponseDTO<>(SuccessCode.SUCCESS_RETRIEVE_USER, res));
    }

}