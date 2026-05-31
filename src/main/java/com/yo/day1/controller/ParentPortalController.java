package com.yo.day1.controller;

import com.yo.day1.common.ApiResponse;
import com.yo.day1.common.exception.BadRequestException;
import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.dto.parent.ParentDashboardResponse;
import com.yo.day1.dto.parent.ParentResponse;
import com.yo.day1.dto.parent.ParentUpsertRequest;
import com.yo.day1.service.ParentPortalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/parents")
@RequiredArgsConstructor
public class ParentPortalController {

    private final ParentPortalService parentPortalService;

    //Lấy toàn bộ parent
    @GetMapping
    public List<ParentResponse> getAll() {

        return parentPortalService.getAll();
    }

    //Lấy parent theo id
    @GetMapping("/{id}")
    public ParentResponse getById(@PathVariable Long id) {

        return parentPortalService.getById(id);
    }

    //Tạo parent mới
    @PostMapping
    public ParentResponse create(@Valid @RequestBody ParentUpsertRequest request) {

        return parentPortalService.create(request);
    }

    //Update parent
    @PutMapping("/{id}")
    public ParentResponse update(@PathVariable Long id, @Valid @RequestBody ParentUpsertRequest request) {

        return parentPortalService.update(id, request);
    }

    //Xóa parent
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        parentPortalService.delete(id);
    }
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('PARENT')")
    public ApiResponse<ParentDashboardResponse> dashboard(Principal principal) throws BadRequestException, NotFoundException, org.apache.coyote.BadRequestException {
        return ApiResponse.success(parentPortalService.getDashboard(principal.getName()));
    }
}