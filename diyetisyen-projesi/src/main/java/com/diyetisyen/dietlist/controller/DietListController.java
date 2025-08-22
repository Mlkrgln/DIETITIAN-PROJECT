package com.diyetisyen.dietlist.controller;

import com.diyetisyen.dietlist.model.request.DietListCreateRequest;
import com.diyetisyen.dietlist.model.response.DietListResponse;
import com.diyetisyen.dietlist.service.DietListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dietlist")
@RequiredArgsConstructor
public class DietListController {

    private final DietListService dietListService;

    @PostMapping("/save")
    public DietListResponse saveDietList(@RequestBody DietListCreateRequest request) {
        return dietListService.saveDietList(request);
    }
}
