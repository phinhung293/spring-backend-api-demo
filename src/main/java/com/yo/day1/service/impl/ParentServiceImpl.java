package com.yo.day1.service.impl;

import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.domain.entity.Parent;
import com.yo.day1.dto.parent.ParentResponse;
import com.yo.day1.dto.parent.ParentUpsertRequest;
import com.yo.day1.repository.ParentRepository;
import com.yo.day1.service.ParentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;

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
}
