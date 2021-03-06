/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */

package ru.mvk.iluvatar.test;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mvk.iluvatar.descriptor.field.RefAble;

import java.time.LocalDate;
import java.util.Objects;

public final class Student implements RefAble<Integer> {
	private int id;
	@NotNull
	private String name;
	private short gpa;
	private short penalty;
	private boolean graduated;
	private long fileSize;
	private int lecturesTime;
	private int neighbour;
	private LocalDate enrollmentDate;

	public Student() {
		name = "";
	}

	public boolean isGraduated() {
		return graduated;
	}

	public void setGraduated(boolean graduated) {
		this.graduated = graduated;
	}

	@Override
	@NotNull
	public Integer getId() {
		return id;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public short getGpa() {
		return gpa;
	}

	public void setGpa(short gpa) {
		this.gpa = gpa;
	}

	public short getPenalty() {
		return penalty;
	}

	public void setPenalty(short penalty) {
		this.penalty = penalty;
	}

	public void setId(@NotNull Integer id) {
		this.id = id;
	}

	public void setName(@NotNull String name) {
		this.name = name;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public int getLecturesTime() {
		return lecturesTime;
	}

	public void setLecturesTime(int lecturesTime) {
		this.lecturesTime = lecturesTime;
	}

	public LocalDate getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(LocalDate enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, gpa, penalty, graduated, fileSize, lecturesTime,
				neighbour, enrollmentDate);
	}

	private boolean equals(@NotNull Student object) {
		return Objects.equals(id, object.id) && Objects.equals(name, object.name) &&
				Objects.equals(gpa, object.gpa) && Objects.equals(penalty, object.penalty) &&
				Objects.equals(graduated, object.graduated) &&
				Objects.equals(fileSize, object.fileSize) &&
				Objects.equals(lecturesTime, object.lecturesTime) &&
				Objects.equals(neighbour, object.neighbour) &&
				Objects.equals(enrollmentDate, object.enrollmentDate);
	}

	@Override
	public boolean equals(@Nullable Object object) {
		return (this == object) || ((object instanceof Student) && equals((Student) object));
	}

	@Override
	public String toString() {
		return name;
	}

	public int getNeighbour() {
		return neighbour;
	}

	public void setNeighbour(int neighbour) {
		this.neighbour = neighbour;
	}
}
