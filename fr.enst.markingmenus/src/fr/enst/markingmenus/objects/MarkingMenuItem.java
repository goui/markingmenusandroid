package fr.enst.markingmenus.objects;

import java.util.ArrayList;
import java.util.List;

import fr.enst.markingmenus.interfaces.OnMenuItemMarkListener;

/**
 * Class representing a menu item. It can contain items and has id and text.
 * 
 * @author Goui
 * 
 */
public class MarkingMenuItem {

	/**
	 * The menu index.
	 */
	private int id;

	/**
	 * The text displayed in the item.
	 */
	private String text;

	/**
	 * If the item contains items.
	 */
	private boolean containsItem;

	/**
	 * The list of items the item contains.
	 */
	private List<MarkingMenuItem> children;

	/**
	 * The click event listener.
	 */
	private OnMenuItemMarkListener markListener;

	/**
	 * Default constructor.
	 */
	public MarkingMenuItem() {
		this("");
	}

	/**
	 * Constructor setting the text in the item.
	 * 
	 * @param text
	 */
	public MarkingMenuItem(String text) {
		this.text = text;
		children = new ArrayList<MarkingMenuItem>();
	}

	/**
	 * Getting the item index.
	 * 
	 * @return the index
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setting the menu index.
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getting the text displayed in the item.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setting the text displayed in the item.
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Adding an item in the current item.
	 * 
	 * @param item
	 */
	public void addItem(MarkingMenuItem item) {
		containsItem = true;
		children.add(item);
	}

	/**
	 * If the item contains items.
	 * 
	 * @return boolean
	 */
	public boolean containsItem() {
		return containsItem;
	}

	/**
	 * Getting all the items contained in the item.
	 * 
	 * @return the list of children
	 */
	public List<MarkingMenuItem> getChildren() {
		return children;
	}

	/**
	 * Setting the menu click listener.
	 * 
	 * @param listener
	 */
	public void setOnMenuMarkListener(OnMenuItemMarkListener listener) {
		markListener = listener;
	}

	/**
	 * Getting the menu click listener, if item has items returns null.
	 * 
	 * @return the click listener
	 */
	public OnMenuItemMarkListener getOnMenuMarkListener() {
		if (containsItem) {
			return null;
		}
		return markListener;
	}

}
