/**
 * (c) raptor_MVK, 2015. All rights reserved.
 */

package ru.mvk.iluvatar.javafx;

import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;
import ru.mvk.iluvatar.descriptor.ListViewInfo;
import ru.mvk.iluvatar.descriptor.ListViewInfoImpl;
import ru.mvk.iluvatar.descriptor.column.*;
import ru.mvk.iluvatar.descriptor.field.RealDescriptor;
import ru.mvk.iluvatar.test.Student;
import ru.mvk.iluvatar.utils.UITests;
import ru.mvk.iluvatar.view.IdGenerator;
import ru.mvk.iluvatar.view.ListView;

import java.util.ArrayList;

public class JFXListViewNoRemoveUITests extends UITests<ListView<Student>> {
	@NotNull
	private final ListViewInfo<Student> listViewInfo = prepareListViewInfo();
	@NotNull
	private final IdGenerator idGenerator = new JFXIdGenerator(Student.class);

	@Test
	public void listViewInfoRemoveAllowedIsFalse_RemoveButtonShouldBeInvisible() {
		@NotNull String removeButtonId = idGenerator.getButtonId("Remove");
		Assert.assertFalse("Remove button should be invisible, when " +
						"listViewInfo.isRemoveAllowed is false",
				stage.getScene().lookup('#' + removeButtonId).isVisible());
	}

	@NotNull
	@Override
	protected Parent getRootNode() {
		@NotNull JFXListView<Student> listView = (JFXListView<Student>) prepareListView();
		setObjectUnderTest(listView);
		@Nullable Parent result = listView.getListView();
		return (result == null) ? new GridPane() : result;
	}

	@NotNull
	private ListView<Student> prepareListView() {
		ListView<Student> result =
				new JFXListView<>(listViewInfo, (value) -> value, idGenerator);
		result.setListSupplier(ArrayList::new);
		return result;
	}

	@NotNull
	private ListViewInfo<Student> prepareListViewInfo() {
		@NotNull ListViewInfo<Student> result = new ListViewInfoImpl<>(Student.class);
		result.addColumnInfo("id", new PlainColumnInfo("id", 10));
		result.addColumnInfo("name", new TextColumnInfo("name", 50));
		@NotNull RealDescriptor realDescriptor = new RealDescriptor(3, 2);
		result.addColumnInfo("gpa", new RationalColumnInfo("gpa", realDescriptor));
		result.addColumnInfo("penalty", new PlainColumnInfo("penalty", 5));
		result.addColumnInfo("graduated", new BooleanColumnInfo("graduated", 3));
		result.addColumnInfo("fileSize", new FileSizeColumnInfo("fileSize", 10));
		result.addColumnInfo("lecturesTime", new DurationColumnInfo("lecturesTime", 8));
		result.disableRemove();
		return result;
	}
}
