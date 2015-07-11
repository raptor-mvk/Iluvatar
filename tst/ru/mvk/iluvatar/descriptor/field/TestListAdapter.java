/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.descriptor.field;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.test.Student;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class TestListAdapter implements ListAdapter<Integer, Student> {
  @NotNull
  private final List<Student> students;

  public TestListAdapter(@NotNull List<Student> students) {
    this.students = students;
  }

  @NotNull
  @Override
  public Class<Integer> getType() {
    return Integer.class;
  }

  @NotNull
  @Override
  public Class<Student> getRefType() {
    return Student.class;
  }

  @NotNull
  @Override
  public Supplier<List<Student>> getListSupplier() {
    return () -> students;
  }

  @NotNull
  @Override
  public Function<Serializable, Student> getFinder() {
    return (id) -> {
      @Nullable Student result = null;
      for (Student student : students) {
        if (student.getId() == id) {
          result = student;
          break;
        }
      }
      return result;
    };
  }
}
