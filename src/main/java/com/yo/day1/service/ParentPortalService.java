package com.yo.day1.service;

import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.dto.parent.ParentDashboardResponse;
import com.yo.day1.dto.parent.ParentResponse;
import com.yo.day1.dto.parent.ParentUpsertRequest;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ParentPortalService {

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
    ParentDashboardResponse getDashboard(String username) throws BadRequestException, NotFoundException;
}