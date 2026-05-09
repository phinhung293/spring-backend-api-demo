package com.yo.day1.service;

import com.yo.day1.dto.parent.ParentResponse;
import com.yo.day1.dto.parent.ParentUpsertRequest;

import java.util.List;

public interface ParentService {

    //Lấy toàn bộ parent
    List<ParentResponse> getAll();

    //Lấy parent theo id
    ParentResponse getById(Long id);

    //Tạo parent mới
    ParentResponse create(
            ParentUpsertRequest request
    );

    //Update parent
    ParentResponse update(
            Long id,
            ParentUpsertRequest request
    );

    //Xóa parent
    void delete(Long id);
}
