package com.yo.day1.service.impl;

import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.domain.entity.Student;
import com.yo.day1.dto.parent.ParentResponse;
import com.yo.day1.dto.student.StudentResponse;
import com.yo.day1.dto.student.StudentUpsertRequest;
import com.yo.day1.repository.ParentRepository;
import com.yo.day1.repository.StudentRepository;
import com.yo.day1.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final ModelMapper mapper;

    private StudentResponse map(Student student){
        return mapper.map(student, StudentResponse.class);
    }

    @Override
    public List<StudentResponse> findByAll() {
        return studentRepository.findAll().stream()
                .map(s-> map(s))
                .toList();
    }
    public Optional<StudentResponse> findById(long id) {
        return studentRepository.findById(id)
                .map(this :: map);
    }

    public StudentResponse create(StudentUpsertRequest req) {
        Student stu = mapper.map(req, Student.class); //trong đối tượng studentupserRe sẽ k có Partent, k có print, k có createAt và UpdateAt.
        // Có parentID nma k map về đây đc

        //Những gì còn thiếu phải làm = tay
        parentRepository.findById(req.getParentId())
                .ifPresent(p->stu.setParent(p));
        stu.setCreatedAt(LocalDateTime.now());
        stu.setUpdatedAt(LocalDateTime.now());
        Student result = studentRepository.save(stu);
        return map(result);
    }

    public StudentResponse update(Long id, StudentUpsertRequest req) {
        Student stu = mapper.map(req, Student.class);
        stu.setId(id);
        parentRepository.findById(req.getParentId())
                .ifPresent(p->stu.setParent(p));
        stu.setUpdatedAt(LocalDateTime.now());
        Student result = studentRepository.save(stu);
        return map(result);
    }
    //create và update tạo v thôi. Bth vẫn viết chung được. K vde

//    public void delete(Long id) {
//        studentRepository.deleteById(id);
//    }
    //Do k có trả về nên là k có ID -> Trả về vẫn là đúng (200)

    public void delete(Long id) throws NotFoundException {
        if(studentRepository.existsById(id)){
            studentRepository.deleteById(id);
        }else{
            throw new NotFoundException("Delete error");
        }

    }

    private StudentResponse map2(Student student) {
        StudentResponse result = new StudentResponse();

        result.setId(student.getId());
        result.setStudentCode(student.getStudentCode());
        result.setFullName(student.getFullName());
        result.setDateOfBirth(student.getDateOfBirth());
        result.setGender(student.getGender());
        result.setGradeLevel(student.getGradeLevel());
        result.setSchoolName(student.getSchoolName());
        result.setPhone(student.getPhone());
        result.setDescription(student.getDescription());
        result.setStatus(student.getStatus());
        result.setLatestScore(student.getLatestScore());
        result.setNote(student.getNote());
        result.setCreatedAt(student.getCreatedAt());
        result.setUpdateAt(student.getUpdatedAt());

        // map parent
        if (student.getParent() != null) {
            ParentResponse p = new ParentResponse();
            p.setId(student.getParent().getId());
            p.setFullName(student.getParent().getFullName());
            p.setGender(student.getParent().getGender());
            p.setAddress(student.getParent().getAddress());
            p.setEmail(student.getParent().getEmail());
            p.setPhone(student.getParent().getPhone());

            result.setParentResponse(p);
        }

        return result;
    }
}