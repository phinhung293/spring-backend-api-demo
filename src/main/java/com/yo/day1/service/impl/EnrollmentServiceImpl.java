package com.yo.day1.service.impl;

import com.yo.day1.common.exception.BadRequestException;
import com.yo.day1.common.exception.ConflictException;
import com.yo.day1.common.exception.NotFoundException;
import com.yo.day1.domain.entity.CourseClass;
import com.yo.day1.domain.entity.Enrollment;
import com.yo.day1.domain.entity.Student;
import com.yo.day1.domain.entity.User;
import com.yo.day1.domain.enums.ClassStatus;
import com.yo.day1.domain.enums.EnrollmentStatus;
import com.yo.day1.dto.enrollment.EnrollmentCreateRequest;
import com.yo.day1.dto.enrollment.EnrollmentResponse;
import com.yo.day1.repository.EnrollmentRepository;
import com.yo.day1.service.AuthService;
import com.yo.day1.service.CourseClassService;
import com.yo.day1.service.EnrollmentService;
import com.yo.day1.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final CourseClassService courseClassService;
    private final AuthService authService;
    private final ModelMapper mapper;
    @Transactional
    public EnrollmentResponse create(EnrollmentCreateRequest request) {
        if (enrollmentRepository.existsByStudentIdAndCourseClassId(request.studentId(), request.courseClassId())) {
            throw new ConflictException("Student is already enrolled in this class");
        }
        CourseClass courseClass = courseClassService.getCourseClass(request.courseClassId());
        long activeCount = enrollmentRepository.countByCourseClassIdAndStatus(request.courseClassId(), EnrollmentStatus.ACTIVE);
        if (activeCount >= courseClass.getMaxStudents()) {
            throw new BadRequestException("Class is full");
        }
        //Chặn đăng ký nếu lớp đã đóng (CLOSED)
        if (ClassStatus.CLOSED.equals(courseClass.getStatus())) {
            throw new BadRequestException("Lớp học đã đóng ghi danh");
        }
        Student student = studentService.getStudent(request.studentId());
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourseClass(courseClass);
        enrollment.setEnrolledAt(request.enrolledAt());
        enrollment.setStatus(request.status());
        enrollment.setNote(request.note());
        try {
            return toResponse(enrollmentRepository.saveAndFlush(enrollment));
        } catch (DataIntegrityViolationException ex) {
            if (isDuplicateEnrollment(ex)) {
                throw new ConflictException("Student is already enrolled in this class");
            }
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByClassId(Long classId) {
        return enrollmentRepository.findByCourseClassId(classId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> findByStudentId(Long studentId, String username) throws BadRequestException, NotFoundException {
        User user = authService.findActiveUserByUsername(username);
        if (user.getRole().name().equals("PARENT")) {
            studentService.getStudentForParent(studentId, user.getParent().getId());
        }
        return enrollmentRepository.findByStudentId(studentId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true) // Thêm readOnly và chuyển sang NotFoundException cho đúng ngữ nghĩa
    public Enrollment getEnrollment(Long studentId, Long classId) {
        return enrollmentRepository.findByStudentIdAndCourseClassId(studentId, classId)
                .orElseThrow(() -> new NotFoundException("Enrollment not found for student and class"));
    }
    private EnrollmentResponse toResponse(Enrollment enrollment) {
        CourseClass cc = enrollment.getCourseClass();
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getStudent().getFullName(),
                cc.getId(),
                cc.getCodeCode(), // Lưu ý: Đảm bảo cc.getClassCode() tồn tại, nếu đỏ hãy check mục lưu ý phía dưới
                cc.getName(),
                cc.getCourse() != null ? cc.getCourse().getName() : null,
                cc.getMainTeacher() != null ? cc.getMainTeacher().getFullName() : null,
                cc.getTuitionFee(),
                enrollment.getEnrolledAt(),
                enrollment.getStatus().name(),
                enrollment.getNote(),
                enrollment.getCreatedAt(),
                enrollment.getUpdatedAt()
        );
    }
    private boolean isDuplicateEnrollment(DataIntegrityViolationException ex) {
        Throwable cause = ex.getMostSpecificCause();
        String message = cause != null ? cause.getMessage() : ex.getMessage();
        return message != null && (message.toLowerCase(Locale.ROOT).contains("uq_enrollment")
                || message.toLowerCase(Locale.ROOT).contains("duplicate entry"));
    }

}
