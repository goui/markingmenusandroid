package fr.enst.markingmenus.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
import fr.enst.markingmenus.R;
import fr.enst.markingmenus.interfaces.OnMenuItemMarkListener;
import fr.enst.markingmenus.objects.MarkingMenuItem;
import fr.enst.markingmenus.views.MarkingMenu;

/**
 * Activity used to show how to use MarkingMenu widget.
 * 
 * @author Goui
 * 
 */
public class MainActivity extends Activity {

	/**
	 * The marking menu view.
	 */
	private MarkingMenu mm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// getting the marking menu view.
		mm = (MarkingMenu) findViewById(R.id.marking_menu);

		// first item with 3 sub items.
		MarkingMenuItem item_1 = new MarkingMenuItem("1");
		MarkingMenuItem item_1_1 = new MarkingMenuItem("A");
		item_1_1.setOnMenuMarkListener(new OnMenuItemMarkListener() {
			@Override
			public void onMenuMark() {
				Toast.makeText(MainActivity.this, "Item 1-A marked", Toast.LENGTH_SHORT).show();
			}
		});
		MarkingMenuItem item_1_2 = new MarkingMenuItem("B");
		item_1_2.setOnMenuMarkListener(new OnMenuItemMarkListener() {
			@Override
			public void onMenuMark() {
				Toast.makeText(MainActivity.this, "Item 1-B marked", Toast.LENGTH_SHORT).show();
			}
		});
		MarkingMenuItem item_1_3 = new MarkingMenuItem("C");
		item_1_3.setOnMenuMarkListener(new OnMenuItemMarkListener() {
			@Override
			public void onMenuMark() {
				Toast.makeText(MainActivity.this, "Item 1-C marked", Toast.LENGTH_SHORT).show();
			}
		});
		item_1.addItem(item_1_1);
		item_1.addItem(item_1_2);
		item_1.addItem(item_1_3);

		// second item with 2 sub items.
		MarkingMenuItem item_2 = new MarkingMenuItem("2");
		MarkingMenuItem item_2_1 = new MarkingMenuItem("A");
		item_2_1.setOnMenuMarkListener(new OnMenuItemMarkListener() {
			@Override
			public void onMenuMark() {
				Toast.makeText(MainActivity.this, "Item 2-A marked", Toast.LENGTH_SHORT).show();
			}
		});
		MarkingMenuItem item_2_2 = new MarkingMenuItem("B");
		item_2_2.setOnMenuMarkListener(new OnMenuItemMarkListener() {
			@Override
			public void onMenuMark() {
				Toast.makeText(MainActivity.this, "Item 2-B marked", Toast.LENGTH_SHORT).show();
			}
		});
		item_2.addItem(item_2_1);
		item_2.addItem(item_2_2);

		// third item with no sub item and a listener.
		MarkingMenuItem item_3 = new MarkingMenuItem("3");
		item_3.setOnMenuMarkListener(new OnMenuItemMarkListener() {
			@Override
			public void onMenuMark() {
				Toast.makeText(MainActivity.this, "Item 3 marked", Toast.LENGTH_SHORT).show();
			}
		});

		// fourth item with no sub item and no listener.
		MarkingMenuItem item_4 = new MarkingMenuItem("4");

		// adding first level items to the marking menu.
		mm.addItem(item_1);
		mm.addItem(item_2);
		mm.addItem(item_3);
		mm.addItem(item_4);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
