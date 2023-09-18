package com.university.universityproject.web;

import com.university.universityproject.model.GradeEntity;
import com.university.universityproject.model.StudentEntity;
import com.university.universityproject.model.SubjectEntity;
import com.university.universityproject.model.UserEntity;
import com.university.universityproject.model.dto.AddStudentDTO;
import com.university.universityproject.repository.GradeRepository;
import com.university.universityproject.repository.StudentRepository;
import com.university.universityproject.repository.SubjectRepository;
import com.university.universityproject.repository.UserRepository;
import com.university.universityproject.service.StudentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/students")
@PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
public class StudentController {
    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public StudentController(StudentService studentService, StudentRepository studentRepository, GradeRepository gradeRepository, UserRepository userRepository,
                             SubjectRepository subjectRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping("/add")
    public String getAddStudent() {
        return "add-student";
    }

    @PostMapping("/add")
    public String addStudent(AddStudentDTO studentDTO) {
        studentService.createStudent(studentDTO);
        return "redirect:/";
    }

    @GetMapping("/grades")
    public String getGrades(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found " + email));
        model.addAttribute("student", user);
        return "student-grades";
    }

    @PostMapping("/grades")
    public String viewGrades(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found " + email));

        StudentEntity student = studentRepository.findByUser(user);
        List<SubjectEntity> subjectList = subjectRepository.findAllByStudentId(student.getId());
        model.addAttribute("subjects",subjectList);

        return "student-grades";

    }

    @GetMapping("/subjects")
    public String getSubjects(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found " + email));
        model.addAttribute("student", user);
        return "subjects";
    }

    @PostMapping("/subjects")
    public String viewSubjects(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Get the username
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found " + email));
        StudentEntity student = studentRepository.findByUser(user);
        model.addAttribute("student", student);

        List<SubjectEntity> subjectSet = subjectRepository.findAllByStudentId(student.getId());
        model.addAttribute("subjects", subjectSet);
        return "subjects";
    }
//    @GetMapping
//    public String getStudent(@ModelAttribute("searchForm") SearchForm searchForm) {
//        return "students";
//    }
//
//    @PostMapping("/view")
//    public String viewStudent(Model model, @ModelAttribute("searchString") String searchString) {
//        List<StudentEntity> students = studentService.viewAllStudents(searchString);
//        model.addAttribute("students", students);
//        return "students";
//    }


}
