package com.example.starter.service;

import com.example.starter.model.Class;
import com.example.starter.model.Student;
import com.example.starter.repository.ClassRepository;
import com.example.starter.repository.StudentRepository;
import com.example.starter.util.Util;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentService {

  private final StudentRepository studentRepository;

  private final ClassRepository classRepository;

  public Future<List<Student>> findAll(JsonObject query) {
    return studentRepository.findAll(query);
  }
  public Future<Student> findById(String id) {
    return studentRepository.findById(id);
  }

  public Future<Student> insertOne(Student student) {
    if(!Util.isValidObjectId(student.getClassId())) {
      return Future.failedFuture(new IllegalArgumentException("Invalid class id"));
    }

    return classRepository.findById(student.getClassId())
      .compose(clazz -> checkClassAvailableAndInsertStudent(clazz, student))
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

  private Future<Student> handleInsertStudentError(Throwable err) {
    if(err instanceof IllegalArgumentException
      && err.getMessage().equals("The class is at maximum enrollment capacity")) {
      return Future.failedFuture(err);
    } else{
      return Future.failedFuture(new IllegalArgumentException(err.getMessage()));
    }
  }

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

  public Future<String> deleteOne(String id) {
    return studentRepository.delete(id);
  }

}
