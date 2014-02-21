package fr.enst.markingmenus.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import fr.enst.markingmenus.R;
import fr.enst.markingmenus.interfaces.OnMenuItemMarkListener;
import fr.enst.markingmenus.objects.MarkingMenuItem;
import fr.enst.markingmenus.objects.MenuCalculations;

/**
 * {@link #MarkingMenu}
 * 
 * @author Goui
 * 
 */
public class MarkingMenu extends View {

	/**
	 * Enum for selecting the mode, novice or expert.
	 * 
	 * @author Goui
	 * 
	 */
	private enum Mode {
		NOVICE, EXPERT;
	}

	/**
	 * Current selected mode.
	 */
	private Mode mode = Mode.EXPERT;

	/**
	 * The time duration for long press event.
	 */
	private int longPressDuration;

	/**
	 * The first level item of the marking menu.
	 */
	private MarkingMenuItem firstLevel;

	/**
	 * The current level of the marking menu.
	 */
	private MarkingMenuItem currentMenu;

	/**
	 * The painter for the item background.
	 */
	private Paint paintBackground;

	/**
	 * The painter for the selected item background.
	 */
	private Paint paintSelected;

	/**
	 * The painter for the drawing in expert mode.
	 */
	private Paint paintExpert;

	/**
	 * The painter for the drawing recognition in expert mode.
	 */
	private Paint paintExpertRec;

	/**
	 * The painter for the separators between items.
	 */
	private Paint paintSeparator;

	/**
	 * The painter for the border of the menu.
	 */
	private Paint paintBorder;

	/**
	 * The painter for the text inside items.
	 */
	private Paint paintText;

	/**
	 * The bounding box for the menu.
	 */
	private RectF rect;

	/**
	 * The bounding box for the inner circle.
	 */
	private RectF rectIn;

	/**
	 * The bounding box for the outer circle.
	 */
	private RectF rectOut;

	/**
	 * The radius of the menu.
	 */
	private float radius;

	/**
	 * The size of the screen.
	 */
	private Point screenSize;

	/**
	 * The first touch point when Action.DOWN is triggered.
	 */
	private Point touchPoint;

	/**
	 * The last touch point when Action.MOVE is triggered.
	 */
	private Point currentPoint;

	/**
	 * The list of all the touch points.
	 */
	private List<Point> points;

	/**
	 * The index of the selected item.
	 */
	private int selectedItem = -1;

	/**
	 * The thickness of the menu arcs.
	 */
	private int menuThickness = 90;

	/**
	 * The canvas used to draw in the widget.
	 */
	private Canvas canvas;

	/**
	 * Default constructor.
	 * 
	 * @param context
	 */
	public MarkingMenu(Context context) {
		this(context, null);
	}

	/**
	 * The constructor initializing the menu and getting the XML attributes.
	 * 
	 * @param context
	 * @param attrs
	 */
	public MarkingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);

		initMarkingMenu();

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MarkingMenu);
		longPressDuration = a.getInteger(R.styleable.MarkingMenu_longPressDuration, 500);

		a.recycle();
	}

	/**
	 * Method used to add item to the first level menu. Make the menu redraw.
	 * 
	 * @param item
	 */
	public void addItem(MarkingMenuItem item) {
		item.setId(firstLevel.getChildren().size());
		firstLevel.addItem(item);
		invalidate();
		requestLayout();
	}

	/**
	 * Method used to initializing the menu. Instantiates objects and gets the
	 * screen size.
	 */
	private void initMarkingMenu() {
		initPaints();

		// bounding boxes.
		rect = new RectF();
		rectIn = new RectF();
		rectOut = new RectF();

		// list of all the touch points.
		points = new ArrayList<Point>();

		// creating the root containing the first level items.
		firstLevel = new MarkingMenuItem();

		// getting the screen size and setting the radius of the menu.
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		screenSize = new Point();
		display.getSize(screenSize);
		radius = screenSize.x < screenSize.y ? screenSize.x / 5 : screenSize.y / 5;
	}

	/**
	 * Method used to set up all the painters.
	 */
	private void initPaints() {
		paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintBackground.setColor(0x40404040);
		paintBackground.setStrokeWidth(menuThickness);
		paintBackground.setStyle(Paint.Style.STROKE);

		paintSeparator = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintSeparator.setColor(Color.GRAY);
		paintSeparator.setStrokeWidth(menuThickness);
		paintSeparator.setStyle(Paint.Style.STROKE);

		paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintBorder.setColor(Color.BLACK);
		paintBorder.setStrokeWidth(1);
		paintBorder.setStyle(Paint.Style.STROKE);

		paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintText.setColor(Color.BLACK);
		paintText.setTextSize(menuThickness / 2);
		paintText.setStyle(Paint.Style.STROKE);
		paintText.setTextAlign(Paint.Align.CENTER);

		paintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintSelected.setColor(Color.CYAN);
		paintSelected.setStrokeWidth(menuThickness);
		paintSelected.setStyle(Paint.Style.STROKE);

		paintExpert = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintExpert.setColor(Color.GREEN);
		paintExpert.setStrokeWidth(5);
		paintExpert.setStyle(Paint.Style.STROKE);

		paintExpertRec = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintExpertRec.setColor(Color.RED);
		paintExpertRec.setStrokeWidth(5);
		paintExpertRec.setStyle(Paint.Style.STROKE);
	}

	/**
	 * Overriding method used to draw the widget graphics. Switches on the mode
	 * and calls the right drawing method.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		this.canvas = canvas;

		if (mode == Mode.NOVICE) {
			onDrawNovice();
		}

		if (mode == Mode.EXPERT) {
			onDrawExpert();
		}
	}

	/**
	 * Method used to draw in expert mode. It will draw a curve following the
	 * finger.
	 */
	private void onDrawExpert() {
		Path path = new Path();
		boolean first = true;
		for (Point point : points) {
			if (first) {
				first = false;
				path.moveTo(point.x, point.y);
			} else {
				path.lineTo(point.x, point.y);
			}
		}
		canvas.drawPath(path, paintExpert);
	}

	/**
	 * Method used to draw in novice mode. It will set the menu location at the
	 * very first touch point, set the bounding box for the menu and draw the
	 * menu.
	 */
	private void onDrawNovice() {
		setMenuLocation(touchPoint.x, touchPoint.y);

		// setting the inner and outer bounding boxes.
		rectIn.set(touchPoint.x - radius + menuThickness / 2, touchPoint.y - radius + menuThickness / 2, touchPoint.x
				+ radius - menuThickness / 2, touchPoint.y + radius - menuThickness / 2);
		rectOut.set(touchPoint.x - radius - menuThickness / 2, touchPoint.y - radius - menuThickness / 2, touchPoint.x
				+ radius + menuThickness / 2, touchPoint.y + radius + menuThickness / 2);

		// drawing the inner and outer circles.
		canvas.drawOval(rectIn, paintBorder);
		canvas.drawOval(rectOut, paintBorder);

		// setting the bounding box for the menu and drawing the arcs
		// corresponding to the items.
		rect.set(touchPoint.x - radius, touchPoint.y - radius, touchPoint.x + radius, touchPoint.y + radius);
		for (int i = 0; i < currentMenu.getChildren().size(); i++) {
			canvas.drawArc(rect, (float) 360 / currentMenu.getChildren().size() * i - 90, (float) 360
					/ currentMenu.getChildren().size(), false, selectedItem == i ? paintSelected : paintBackground);
		}

		// drawing the separators between items.
		for (int i = 0; i < currentMenu.getChildren().size(); i++) {
			canvas.drawArc(rect, (float) (360 / currentMenu.getChildren().size() * i - 91), 1.4f, false, paintSeparator);
			canvas.drawArc(rect, (float) (360 / currentMenu.getChildren().size() * (i + 1) - 91), 1.4f, false,
					paintSeparator);
		}

		// drawing the text in the right item.
		double angle = Math.PI / currentMenu.getChildren().size() - Math.PI / 2;
		for (int i = 0; i < currentMenu.getChildren().size(); i++) {
			double x = radius * Math.cos(angle);
			double y = radius * Math.sin(angle);
			canvas.drawText(currentMenu.getChildren().get(i).getText(), touchPoint.x + (float) x, touchPoint.y
					+ (float) y, paintText);
			angle += 2 * Math.PI / currentMenu.getChildren().size();
		}
	}

	/**
	 * Method used to set the menu location. If it is supposed to be drawn out
	 * of the screen, it will set the location to the border.
	 * 
	 * @param x
	 * @param y
	 */
	private void setMenuLocation(float x, float y) {
		float space = radius + menuThickness / 2;

		x = x < space ? space : x;
		y = y < space ? space : y;

		x = x > getWidth() - space ? getWidth() - space : x;
		y = y > getHeight() - space ? getHeight() - space : y;

		touchPoint.x = (int) x;
		touchPoint.y = (int) y;
	}

	/**
	 * Overriding method used to catch touch events like Action.DOWN,
	 * Action.MOVE or Action.UP. It will determine in which mode we are and
	 * update the visual feedback.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {

		// when the screen is touched for the first time.
		case MotionEvent.ACTION_DOWN:
			initLevel();

			// if the finger doesn't move we are in the novice mode.
			handler.postDelayed(longPressed, longPressDuration);
			handler.removeCallbacks(menuSelection);

			// keeping the touch event trace.
			touchPoint = new Point((int) event.getX(), (int) event.getY());
			points.add(touchPoint);

			break;

		// when the finger moves.
		case MotionEvent.ACTION_MOVE:

			// removing call backs from long press and menu selection.
			handler.removeCallbacks(longPressed);
			handler.removeCallbacks(menuSelection);

			// keeping the touch event trace.
			currentPoint = new Point((int) event.getX(), (int) event.getY());
			points.add(currentPoint);

			// if we move we need to know where we are.
			computeMove();

			// if an item is selected a long press in it will trigger its
			// marking action.
			if (selectedItem != -1) {
				handler.postDelayed(menuSelection, longPressDuration);
			}

			// making everything redraw.
			invalidate();
			requestLayout();

			break;

		// when the touch is released.
		case MotionEvent.ACTION_UP:

			// removing call backs from long press and menu selection.
			handler.removeCallbacks(longPressed);
			handler.removeCallbacks(menuSelection);

			// if we are in the expert mode we need to analyze the scheme drawn.
			if (mode == Mode.EXPERT) {
				if (points.size() > 1) {
					analyseDrawing();
					decryptPattern();
				}
			}

			// if we are in the novice mode we need to know if the menu has been
			// released on a leaf item.
			if (mode == Mode.NOVICE) {
				releaseMenu();
			}

			// resetting all the concerned objects.
			selectedItem = -1;
			points.clear();
			mode = Mode.EXPERT;

			// make everything redraw.
			invalidate();
			requestLayout();

			break;
		}
		return true;
	}

	/**
	 * Method used to set up the indexes of the items.
	 */
	private void initLevel() {
		currentMenu = firstLevel;
		for (int i = 0; i < currentMenu.getChildren().size(); i++) {
			currentMenu.getChildren().get(i).setId(i);
		}
	}

	/**
	 * Method used to know if the menu has been released on a leaf item, if so
	 * throwing the corresponding event.
	 */
	private void releaseMenu() {
		if (selectedItem != -1) {
			if (!currentMenu.getChildren().isEmpty()) {
				OnMenuItemMarkListener listener = currentMenu.getChildren().get(selectedItem).getOnMenuMarkListener();
				if (listener != null) {
					listener.onMenuMark();
				} else {
					Toast.makeText(getContext(), "no listener here", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getContext(), "non-leaf item", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * List containing all the inflection points including the first and the
	 * last points.
	 */
	private List<Point> inflectionPoints;

	/**
	 * Method used to analyze the drawn scheme in order to select the right item
	 * reached.
	 */
	private void analyseDrawing() {
		inflectionPoints = new ArrayList<Point>();
		inflectionPoints.add(points.get(0));

		// first we calculate the first angle between the first 2 points in our
		// inflection points list.
		float refAngle = MenuCalculations.arctan(points.get(0).x, points.get(0).y, points.get(1).x, points.get(1).y);

		// then we iterate every 5 points our drawing in order to dodge smooth
		// direction changes.
		for (int i = 1; i < points.size() - 5; i += 5) {

			// calculating next angle.
			float currentAngle = MenuCalculations.arctan(points.get(i).x, points.get(i).y, points.get(i + 4).x,
					points.get(i + 4).y);

			// particular case when angles go from negative to positive.
			if (Math.abs(refAngle) > 135 && Math.abs(currentAngle) > 135) {
				if (Math.abs(((currentAngle + 360) % 360) - ((refAngle + 360) % 360)) >= 45) {
					inflectionPoints.add(points.get(i));
				}
			}

			// if the direction changes with more than 45 degrees it is an
			// inflection point.
			else {
				if (Math.abs(currentAngle - refAngle) >= 45) {
					inflectionPoints.add(points.get(i));
				}
			}
			refAngle = currentAngle;
		}

		// adding the last point.
		inflectionPoints.add(points.get(points.size() - 1));

		// drawing the simplified scheme.
		for (int i = 0; i < inflectionPoints.size() - 1; i++) {
			canvas.drawLine(inflectionPoints.get(i).x, inflectionPoints.get(i).y, inflectionPoints.get(i + 1).x,
					inflectionPoints.get(i + 1).y, paintExpertRec);
		}
	}

	/**
	 * Method used to decrypt the drawn scheme, it will browse items to know
	 * where the final selected item is.
	 */
	private void decryptPattern() {
		MarkingMenuItem currentItem = currentMenu;
		int currentSelectedItem = 0;
		for (int i = 0; i < inflectionPoints.size() - 1; i++) {
			currentSelectedItem = getSelectedItem(inflectionPoints.get(i), inflectionPoints.get(i + 1), currentItem);
			if (currentSelectedItem != -1 && !currentItem.getChildren().isEmpty()) {
				currentItem = currentItem.getChildren().get(currentSelectedItem);
			}
		}
		if (currentItem.getOnMenuMarkListener() != null) {
			currentItem.getOnMenuMarkListener().onMenuMark();
		} else if (!currentItem.getChildren().isEmpty()) {
			// TODO show novice menu because we are not on a leaf item.
			Toast.makeText(getContext(), "non-leaf item", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContext(), "no listener here", Toast.LENGTH_SHORT).show();
		}
		// TODO clear canvas.
	}

	/**
	 * Handler used to trigger a long press event or a menu selection event.
	 */
	final Handler handler = new Handler();

	/**
	 * Long press event activating the novice mode and making everything redraw.
	 */
	private Runnable longPressed = new Runnable() {
		public void run() {
			mode = Mode.NOVICE;
			invalidate();
			requestLayout();
		}
	};

	/**
	 * Menu selection event making everything redraw to the new menu if selected
	 * item contains menu.
	 */
	private Runnable menuSelection = new Runnable() {
		@Override
		public void run() {
			if (currentMenu.getChildren().get(selectedItem).containsItem()) {
				currentMenu = currentMenu.getChildren().get(selectedItem);
				setMenuLocation(currentPoint.x, currentPoint.y);
				invalidate();
				requestLayout();
			}
		}
	};

	/**
	 * Method used to know where we are when a Action.MOVE event has been
	 * triggered.
	 */
	private void computeMove() {

		// getting the radius of the inner and outer circles.
		float smallRadius = radius - menuThickness / 2;
		float bigRadius = radius + menuThickness / 2;

		// getting the distance and the angle.
		float hyp = MenuCalculations.pythagore(touchPoint.x, touchPoint.y, currentPoint.x, currentPoint.y);

		// if we are in the menu. Computing to know in which item we are.
		if (hyp > smallRadius && hyp < bigRadius) {
			selectedItem = getSelectedItem(touchPoint, currentPoint, currentMenu);
		} else {
			selectedItem = -1;
		}
	}

	/**
	 * Method used to know in which item the line made by the 2 argument points
	 * is.
	 * 
	 * @param firstPoint
	 * @param lastPoint
	 * @return the selected item's index
	 */
	private int getSelectedItem(Point firstPoint, Point lastPoint, MarkingMenuItem currentItem) {

		// first we calculate the angle between the 2 points.
		float angle = MenuCalculations.arctan(firstPoint.x, firstPoint.y, lastPoint.x, lastPoint.y);

		// then we put it positive.
		if (angle < 0) {
			angle += 360;
		}

		// finally we search for the right item.
		float startAngle = 0;
		float endAngle = 0;
		for (int i = 0; i < currentItem.getChildren().size(); i++) {

			// our first angle is always 270.
			startAngle = (270 + 360 / currentItem.getChildren().size() * i) % 360;
			endAngle = (startAngle + 360 / currentItem.getChildren().size()) % 360;
			if (startAngle > endAngle) { // particular case if we are between
											// 360 and 0.
				if (angle > startAngle || angle < endAngle) {
					return i;
				}
			}
			if (angle > startAngle && angle < endAngle) {
				return i;
			}
		}
		return -1;
	}

}
