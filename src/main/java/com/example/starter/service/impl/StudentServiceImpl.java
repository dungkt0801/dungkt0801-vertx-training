package com.example.starter.service.impl;

import com.example.starter.dto.ClassDto;
import com.example.starter.dto.StudentDto;
import com.example.starter.model.Class;
import com.example.starter.model.Student;
import com.example.starter.repository.ClassRepository;
import com.example.starter.repository.StudentRepository;
import com.example.starter.service.StudentService;
import com.example.starter.util.Util;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;

  private final ClassRepository classRepository;

  @Override
  public Future<List<StudentDto>> findAll(JsonObject query) {
    return studentRepository.findAll(query)
      .compose(students -> {
        List<Future> futures = students.stream()
          .map(student -> classRepository.findById(student.getClassId())
            .map(clazz -> buildStudentResponseDto(student, clazz)))
          .collect(Collectors.toList());

        return CompositeFuture.all(futures).map(CompositeFuture::list);
      });
  }

  @Override
  public Future<StudentDto> findById(String id) {
    return studentRepository.findById(id)
      .compose(student -> classRepository.findById(student.getClassId())
        .map(clazz -> buildStudentResponseDto(student, clazz))
      );
  }

  @Override
  public Future<StudentDto> insertOne(Student student) {
    if(!Util.isValidObjectId(student.getClassId())) {
      return Future.failedFuture(new IllegalArgumentException("Invalid class id"));
    }

    return classRepository.findById(student.getClassId())
      .compose(clazz -> checkClassAvailableAndInsertStudent(clazz, student)
        .map(insertedStudent -> buildStudentResponseDto(student, clazz)))
      .recover(this::handleInsertStudentError);
  }

  private Future<Student> checkClassAvailableAndInsertStudent(Class clazz, Student student) {
    Long enrolledStudents = clazz.getEnrolledStudent();
    Long totalStudents = clazz.getTotalStudents();
    if(enrolledStudents < totalStudents) {
      return insertStudentAndUpdateClass(clazz, student);
    } else {
      return Future.failedFuture(new IllegalArgumentException("The class is at maximum enrollment capacity"));
    }
  }

  private Future<Student> insertStudentAndUpdateClass(Class clazz, Student student){
    return studentRepository.insert(student)
      .compose(insertedStudent -> {
        clazz.setEnrolledStudent(clazz.getEnrolledStudent() + 1);
        return classRepository.updateOne(clazz.getId(), clazz)
          .map(insertedStudent);
      });
  }

  private Future<StudentDto> handleInsertStudentError(Throwable err) {
    if(err instanceof IllegalArgumentException
      && err.getMessage().equals("The class is at maximum enrollment capacity")) {
      return Future.failedFuture(err);
    } else{
      return Future.failedFuture(new IllegalArgumentException(err.getMessage()));
    }
  }

  @Override
  public Future<String> updateOne(String id, Student student) {
    if(!Util.isValidObjectId(id)) {
      return Future.failedFuture(new IllegalArgumentException("Invalid class id"));
    }

    if(!Util.isValidObjectId(student.getClassId())) {
      return Future.failedFuture(new IllegalArgumentException("Invalid class id"));
    }

    student.setId(id);
    return studentRepository.findById(student.getId())
      .compose(oldStudent -> {
        String oldClassId = oldStudent.getClassId();
        return classRepository.findById(oldClassId)
          .compose(oldClass -> {
            oldClass.setEnrolledStudent(oldClass.getEnrolledStudent() - 1);
            return classRepository.updateOne(oldClassId, oldClass);
          });
      })
      .compose(newClass -> classRepository.findById(student.getClassId()))
      .compose(newClass -> checkClassAvailableAndUpdateStudent(newClass, student))
      .recover(this::handleUpdateStudentError);

  }

  private Future<String> checkClassAvailableAndUpdateStudent(Class clazz, Student student) {
    Long enrolledStudents = clazz.getEnrolledStudent();
    Long totalStudents = clazz.getTotalStudents();
    if(enrolledStudents < totalStudents) {
      return updateStudentAndUpdateClass(clazz, student);
    } else {
      return Future.failedFuture(new IllegalArgumentException("The class is at maximum enrollment capacity"));
    }
  }

  private Future<String> updateStudentAndUpdateClass(Class clazz, Student student) {
    return studentRepository.update(student.getId(), student)
      .compose(insertedStudent -> {
        clazz.setEnrolledStudent(clazz.getEnrolledStudent() + 1);
        return classRepository.updateOne(clazz.getId(), clazz)
          .map(insertedStudent);
      });
  }

  private Future<String> handleUpdateStudentError(Throwable err) {
    if(err instanceof IllegalArgumentException
      && err.getMessage().equals("The class is at maximum enrollment capacity")) {
      return Future.failedFuture(err);
    } else {
      return Future.failedFuture(new IllegalArgumentException(err.getMessage()));
    }
  }

  @Override
  public Future<String> deleteOne(String id) {

    if(!Util.isValidObjectId(id)) {
      return Future.failedFuture(new IllegalArgumentException("Invalid student id"));
    }

    return studentRepository.findById(id)
      .compose(student -> {
        String classId = student.getClassId();
        return classRepository.findById(classId)
          .compose(clazz -> {
            clazz.setEnrolledStudent(clazz.getEnrolledStudent() - 1);
            return classRepository.updateOne(classId, clazz);
          })
          .compose(s -> studentRepository.delete(id));
      })
      .recover(err -> Future.failedFuture(new IllegalArgumentException(err.getMessage())));
  }

  private StudentDto buildStudentResponseDto(Student student, Class clazz) {
    return StudentDto.builder()
      .id(student.getId())
      .name(student.getName())
      .birthDay(student.getBirthDay())
      .classInfo(
        ClassDto.builder()
          .id(clazz.getId())
          .className(clazz.getClassName())
          .totalStudents(clazz.getTotalStudents())
          .enrolledStudents(clazz.getEnrolledStudent())
          .build()
      )
      .build();
  }

}
