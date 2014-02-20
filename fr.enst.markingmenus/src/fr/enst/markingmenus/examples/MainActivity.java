package fr.enst.markingmenus.examples;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
import fr.enst.markingmenus.R;
import fr.enst.markingmenus.interfaces.OnMenuItemMarkListener;
import fr.enst.markingmenus.objects.MarkingMenuItem;
import fr.enst.markingmenus.views.MarkingMenu;

public class MainActivity extends Activity {

	private MarkingMenu mm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mm = (MarkingMenu) findViewById(R.id.marking_menu);

		MarkingMenuItem item_1 = new MarkingMenuItem("1");
		
		MarkingMenuItem item_1_1 = new MarkingMenuItem("1_1");
		MarkingMenuItem item_1_1_1 = new MarkingMenuItem("1_1_1");
		MarkingMenuItem item_1_1_2 = new MarkingMenuItem("1_1_2");
		item_1_1.addItem(item_1_1_1);
		item_1_1.addItem(item_1_1_2);
		
		MarkingMenuItem item_1_2 = new MarkingMenuItem("1_2");
		
		item_1.addItem(item_1_1);
		item_1.addItem(item_1_2);

		MarkingMenuItem item_2 = new MarkingMenuItem("2");
		item_2.setOnMenuMarkListener(new OnMenuItemMarkListener() {
			@Override
			public void onMenuMark() {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "Sfwdgf", Toast.LENGTH_SHORT).show();
			}
		});
		
		MarkingMenuItem item_2_1 = new MarkingMenuItem("2_1");
		item_2_1.setOnMenuMarkListener(new OnMenuItemMarkListener() {
			@Override
			public void onMenuMark() {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "item_2_1", Toast.LENGTH_SHORT).show();
			}
		});
		
		MarkingMenuItem item_2_2 = new MarkingMenuItem("2_2");
		item_2_2.setOnMenuMarkListener(new OnMenuItemMarkListener() {
			@Override
			public void onMenuMark() {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "item_2_2", Toast.LENGTH_SHORT).show();
			}
		});
		
		MarkingMenuItem item_2_3 = new MarkingMenuItem("2_3");
		item_2_3.setOnMenuMarkListener(new OnMenuItemMarkListener() {
			@Override
			public void onMenuMark() {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "item_2_3", Toast.LENGTH_SHORT).show();
			}
		});
		
		item_2.addItem(item_2_1);
		item_2.addItem(item_2_2);
		item_2.addItem(item_2_3);

		MarkingMenuItem item_3 = new MarkingMenuItem("3");
		item_3.setOnMenuMarkListener(new OnMenuItemMarkListener() {
			@Override
			public void onMenuMark() {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "poyfoqiufhis", Toast.LENGTH_SHORT).show();
			}
		});
		
		MarkingMenuItem item_4 = new MarkingMenuItem("4");

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
