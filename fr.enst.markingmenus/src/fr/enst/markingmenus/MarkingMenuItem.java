package fr.enst.markingmenus;

import java.util.ArrayList;
import java.util.List;

public class MarkingMenuItem {

	private int id;
	
	private String text;

	private boolean containsItem;

	private List<MarkingMenuItem> children;

	private OnMenuItemClickListener clickListener;

	public MarkingMenuItem() {
		this("");
	}
	
	public MarkingMenuItem(String text) {
		this.text = text;
		children = new ArrayList<MarkingMenuItem>();
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void addItem(MarkingMenuItem item) {
		containsItem = true;
		children.add(item);
	}

	public boolean containsItem() {
		return containsItem;
	}

	public List<MarkingMenuItem> getChildren() {
		return children;
	}

	public void setOnMenuClickListener(OnMenuItemClickListener listener) {
		clickListener = listener;
	}

	public OnMenuItemClickListener getOnMenuClickListener() {
		if (containsItem) {
			return null;
		}
		return clickListener;
	}

}
