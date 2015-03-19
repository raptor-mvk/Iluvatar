/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.test;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "object")
public final class TestObject {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rowid", nullable = false, unique = true)
  private long id;

  @NotNull
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  public TestObject() {
    name = "";
  }

  public TestObject(@NotNull String name) {
    this.name = name;
  }

  public TestObject(long id, @NotNull String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  @NotNull
  public String getName() {
    return name;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setName(@NotNull String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public boolean equals(Object object) {
    boolean result;
    if (this == object) {
      result = true;
    } else if (object == null || getClass() != object.getClass()) {
      result = false;
    } else {
      @NotNull final TestObject testObject = (TestObject) object;
      result = id == testObject.id && Objects.equals(name, testObject.name);
    }
    return result;
  }
}
