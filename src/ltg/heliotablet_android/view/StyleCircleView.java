package ltg.heliotablet_android.view;

import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.Reason;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class StyleCircleView {

	public static void styleView(ICircleView view, String color, Resources resources) {
		Drawable drawable = null;

		int textColor = 0;
		int textColorWhite = resources.getColor(R.color.White);
		int textColorBlack = resources.getColor(R.color.Black);

		if (color.equals(Reason.CONST_RED)) {
			drawable = resources.getDrawable(R.drawable.earth_shape);
			textColor = textColorWhite;
		} else if (color.equals(Reason.CONST_BLUE)) {
			drawable = resources.getDrawable(R.drawable.neptune_shape);
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_BROWN)) {
			drawable = resources.getDrawable(R.drawable.mercury_shape);
			textColor = textColorWhite;
		} else if (color.equals(Reason.CONST_YELLOW)) {
			drawable = resources.getDrawable(R.drawable.saturn_shape);
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_PINK)) {
			drawable = resources.getDrawable(R.drawable.venus_shape);
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_GREEN)) {
			drawable = resources.getDrawable(R.drawable.jupiter_shape);
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_GREY)) {
			drawable = resources.getDrawable(R.drawable.mars_shape);
			textColor = textColorBlack;
		} else if (color.equals(Reason.CONST_ORANGE)) {
			drawable = resources.getDrawable(R.drawable.uranus_shape);
			textColor = textColorBlack;

		}
		view.setTextColor(textColor);
		view.setBackground(drawable);

}

}
