package com.diyetisyen.dietlist.service;

import com.diyetisyen.dietlist.model.request.DietListCreateRequest;
import com.diyetisyen.dietlist.model.response.DietListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DietListService {

    public DietListResponse saveDietList(DietListCreateRequest request) {
        // Burada henüz DB'ye kaydetmiyoruz, sadece örnek dönüş yapıyoruz
        DietListResponse response = new DietListResponse();
        response.setId(1L); // Dummy id
        response.setCustomerId(request.getCustomerId());
        response.setStartDate(request.getStartDate());
        response.setEndDate(request.getEndDate());
        return response;
    }
}
