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
import fr.enst.markingmenus.MarkingMenuItem;
import fr.enst.markingmenus.OnMenuItemClickListener;
import fr.enst.markingmenus.R;

/**
 * {@link #MarkingMenu}
 * 
 * @author Goui
 * 
 */
public class MarkingMenu extends View {

	private enum Mode {
		BEGINNER, EXPERT;
	}

	private Mode mode = Mode.EXPERT;

	public static int LONG_PRESS_DURATION = 500;

	private MarkingMenuItem firstLevel;
	private MarkingMenuItem currentMenu;

	private Paint paintBackground;
	private Paint paintSelected;
	private Paint paintExpert;
	private Paint paintSeparator;
	private Paint paintBorder;
	private Paint paintText;

	private RectF rect;
	private RectF rectIn;
	private RectF rectOut;
	private float radius;
	private Point screenSize;

	private Point touchPoint;
	private Point currentPoint;
	private List<Point> points;

	private int itemSelected = -1;
	private int menuThickness = 90;

	public MarkingMenu(Context context) {
		this(context, null);
	}

	public MarkingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);

		initMarkingMenu();

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MarkingMenu);

		// int numItems = a.getInteger(R.styleable.MarkingMenu_numItems, 2);

		// int color = a.getColor(R.styleable.MarkingMenu_strokeColor,
		// 0x70404040);

		a.recycle();
	}

	public void addItem(MarkingMenuItem item) {
		item.setId(firstLevel.getChildren().size());
		firstLevel.addItem(item);
		invalidate();
		requestLayout();
	}

	private void initMarkingMenu() {
		initPaints();

		rect = new RectF();
		rectIn = new RectF();
		rectOut = new RectF();
		points = new ArrayList<Point>();
		firstLevel = new MarkingMenuItem();

		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		screenSize = new Point();
		display.getSize(screenSize);
		radius = screenSize.x < screenSize.y ? screenSize.x / 5 : screenSize.y / 5;
	}

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
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mode == Mode.BEGINNER) {
			onDrawBeginner(canvas);
		}

		if (mode == Mode.EXPERT) {
			onDrawExpert(canvas);
		}
	}

	private void onDrawExpert(Canvas canvas) {
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

	private void onDrawBeginner(Canvas canvas) {
		setMenuLocation(touchPoint.x, touchPoint.y);

		rectIn.set(touchPoint.x - radius + menuThickness / 2, touchPoint.y - radius + menuThickness / 2, touchPoint.x
				+ radius - menuThickness / 2, touchPoint.y + radius - menuThickness / 2);
		rectOut.set(touchPoint.x - radius - menuThickness / 2, touchPoint.y - radius - menuThickness / 2, touchPoint.x
				+ radius + menuThickness / 2, touchPoint.y + radius + menuThickness / 2);
		canvas.drawOval(rectIn, paintBorder);
		canvas.drawOval(rectOut, paintBorder);

		rect.set(touchPoint.x - radius, touchPoint.y - radius, touchPoint.x + radius, touchPoint.y + radius);
		for (int i = 0; i < currentMenu.getChildren().size(); i++) {
			canvas.drawArc(rect, (float) 360 / currentMenu.getChildren().size() * i - 90, (float) 360
					/ currentMenu.getChildren().size(), false, itemSelected == i ? paintSelected : paintBackground);
		}
		for (int i = 0; i < currentMenu.getChildren().size(); i++) {
			canvas.drawArc(rect, (float) (360 / currentMenu.getChildren().size() * i - 91), 1.4f, false, paintSeparator);
			canvas.drawArc(rect, (float) (360 / currentMenu.getChildren().size() * (i + 1) - 91), 1.4f, false,
					paintSeparator);
		}
		double angle = Math.PI / currentMenu.getChildren().size() - Math.PI / 2;
		for (int i = 0; i < currentMenu.getChildren().size(); i++) {
			double x = radius * Math.cos(angle);
			double y = radius * Math.sin(angle);
			canvas.drawText(currentMenu.getChildren().get(i).getText(), touchPoint.x + (float) x, touchPoint.y
					+ (float) y, paintText);
			angle += 2 * Math.PI / currentMenu.getChildren().size();
		}
	}

	private void setMenuLocation(float x, float y) {
		float space = radius + menuThickness / 2;

		x = x < space ? space : x;
		y = y < space ? space : y;

		x = x > getWidth() - space ? getWidth() - space : x;
		y = y > getHeight() - space ? getHeight() - space : y;

		touchPoint.x = (int) x;
		touchPoint.y = (int) y;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			initLevel();

			handler.postDelayed(longPressed, LONG_PRESS_DURATION);
			handler.removeCallbacks(menuSelection);

			touchPoint = new Point((int) event.getX(), (int) event.getY());
			points.add(touchPoint);

			break;
		case MotionEvent.ACTION_MOVE:
			handler.removeCallbacks(longPressed);
			handler.removeCallbacks(menuSelection);

			currentPoint = new Point((int) event.getX(), (int) event.getY());
			points.add(currentPoint);
			computeMove();
			if (itemSelected != -1) {
				handler.postDelayed(menuSelection, LONG_PRESS_DURATION);
			}

			invalidate();
			requestLayout();

			break;
		case MotionEvent.ACTION_UP:
			handler.removeCallbacks(longPressed);
			handler.removeCallbacks(menuSelection);

			if (mode == Mode.EXPERT) {
				analyseDrawing();
			}

			if (mode == Mode.BEGINNER) {
				releaseMenu();
			}

			itemSelected = -1;
			points.clear();
			mode = Mode.EXPERT;
			invalidate();
			requestLayout();

			break;
		}
		return true;
	}

	private void initLevel() {
		currentMenu = firstLevel;
		for (int i = 0; i < currentMenu.getChildren().size(); i++) {
			currentMenu.getChildren().get(i).setId(i);
		}
	}

	private void releaseMenu() {
		if (itemSelected != -1) {
			OnMenuItemClickListener listener = currentMenu.getChildren().get(itemSelected).getOnMenuClickListener();
			if (listener != null) {
				listener.onMenuClick();
			}
		}
	}

	private void analyseDrawing() {
		// TODO Auto-generated method stub

	}

	final Handler handler = new Handler();
	Runnable longPressed = new Runnable() {
		public void run() {
			mode = Mode.BEGINNER;
			invalidate();
			requestLayout();
		}
	};
	Runnable menuSelection = new Runnable() {
		@Override
		public void run() {
			for (MarkingMenuItem item : currentMenu.getChildren()) {
				if (item.getId() == itemSelected) {
					if (item.containsItem()) {
						currentMenu = currentMenu.getChildren().get(itemSelected);
						setMenuLocation(currentPoint.x, currentPoint.y);
						invalidate();
						requestLayout();
					}
				}
			}
		}
	};

	private void computeMove() {
		float smallRadius = radius - menuThickness / 2;
		float bigRadius = radius + menuThickness / 2;
		float hyp = MenuCalculations.pythagore(touchPoint.x, touchPoint.y, currentPoint.x, currentPoint.y);
		float angle = MenuCalculations.arctan(touchPoint.x, touchPoint.y, currentPoint.x, currentPoint.y);
		if (hyp > smallRadius && hyp < bigRadius) {
			if (angle < 0) {
				angle += 360;
			}
			float startAngle = 0;
			float endAngle = 0;
			for (int i = 0; i < currentMenu.getChildren().size(); i++) {
				startAngle = (270 + 360 / currentMenu.getChildren().size() * i) % 360;
				endAngle = (startAngle + 360 / currentMenu.getChildren().size()) % 360;
				if (startAngle > endAngle) {
					if (angle > startAngle || angle < endAngle) {
						itemSelected = i;
						return;
					}
				}
				if (angle > startAngle && angle < endAngle) {
					itemSelected = i;
					return;
				}
			}
		}
		itemSelected = -1;
	}

}
