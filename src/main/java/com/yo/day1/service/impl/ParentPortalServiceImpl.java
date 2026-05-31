package com.yo.day1.service.impl;

import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.domain.entity.Parent;
import com.yo.day1.domain.entity.User;
import com.yo.day1.domain.enums.NotificationRecipientType;
import com.yo.day1.domain.enums.UserRole;
import com.yo.day1.dto.parent.*;
import com.yo.day1.repository.NotificationRepository;
import com.yo.day1.repository.ParentRepository;
import com.yo.day1.repository.TuitionInvoiceRepository;
import com.yo.day1.service.AuthService;
import com.yo.day1.service.ParentPortalService;
import com.yo.day1.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentPortalServiceImpl implements ParentPortalService {

    private final ParentRepository parentRepository;
    private final AuthService authService;
    private final StudentService studentService;
    private final TuitionInvoiceRepository tuitionInvoiceRepository;
    private final NotificationRepository notificationRepository;

    private final ModelMapper mapper;

    @Override
    public List<ParentResponse> getAll() {

        return parentRepository.findAll().stream().map(parent -> mapper.map(parent, ParentResponse.class)).toList();
    }

    @Override
    public ParentResponse getById(Long id) {

        Parent parent = parentRepository.findById(id).orElseThrow(() -> new NotFoundException("Parent not found"));

        return mapper.map(parent, ParentResponse.class);
    }

    @Override
    public ParentResponse create(ParentUpsertRequest request) {

        //Check phone trùng
        if (parentRepository.existsByPhone(request.getPhone())) {
            try {
                throw new BadRequestException("Phone already exists");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        //Check email trùng
        if (
                request.getEmail() != null && parentRepository.existsByEmail(request.getEmail())
        ) {
            try {
                throw new BadRequestException("Email already exists");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }

        //Map DTO -> Entity
        Parent parent = mapper.map(request, Parent.class);

        //Save DB
        parentRepository.save(parent);

        //Trả response
        return mapper.map(parent, ParentResponse.class);
    }

    @Override
    public ParentResponse update(Long id, ParentUpsertRequest request) {

        Parent parent = parentRepository.findById(id).orElseThrow(() -> new NotFoundException("Parent not found"));

        //Update field
        parent.setFullName(request.getFullName());

        parent.setPhone(request.getPhone());

        parent.setEmail(request.getEmail());

        parent.setAddress(request.getAddress());

        parent.setRelationship(request.getRelationship());

        parent.setGender(request.getGender());

        //Save DB
        parentRepository.save(parent);

        //Return response
        return mapper.map(parent, ParentResponse.class);
    }

    @Override
    public void delete(Long id) {

        Parent parent = parentRepository.findById(id).orElseThrow(() -> new NotFoundException("Parent not found"));

        parentRepository.delete(parent);
    }
    @Transactional(readOnly = true)
    public ParentDashboardResponse getDashboard(String username) throws BadRequestException, NotFoundException {
        User user = authService.findActiveUserByUsername(username);
        if (user.getRole() != UserRole.PARENT || user.getParent() == null) {
            throw new BadRequestException("Current user is not a parent account");
        }

        Long parentId = user.getParent().getId();
        List<StudentCard> students = studentService.findByParentId(parentId).stream()
                .map(s -> new StudentCard(
                        s.getId(), s.getStudentCode(), s.getFullName(), s.getStatus().toString(), s.getLatestScore()))
                .toList();

        List<InvoiceCard> invoices = tuitionInvoiceRepository.findByStudentParentId(parentId).stream()
                .map(i -> new InvoiceCard(
                        i.getId(),
                        i.getInvoiceCode(),
                        i.getStudent().getFullName(),
                        i.getCourseClass().getName(),
                        i.getBillingMonth(),
                        i.getFinalAmount(),
                        i.getAmountPaid(),
                        i.getBalanceAmount(),
                        i.getStatus().name(),
                        i.getDueDate()
                ))
                .toList();

        List<NotificationCard> notifications = notificationRepository
                .findByRecipientTypeAndRecipientRefIdOrderByCreatedAtDesc(NotificationRecipientType.PARENT, parentId)
                .stream()
                .map(n -> new NotificationCard(
                        n.getId(),
                        n.getType().name(),
                        n.getTitle(),
                        n.getContent(),
                        n.getIsRead(),
                        n.getCreatedAt()
                ))
                .toList();

        return new ParentDashboardResponse(
                parentId,
                user.getParent().getFullName(),
                user.getUsername(),
                students,
                invoices,
                notifications
        );
    }
    }