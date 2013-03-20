package ltg.heliotablet_android;

import ltg.heliotablet_android.view.theory.CircleView;
import android.view.View;

public interface FragmentCommunicator {
	
	public void addUsedPlanetColors(CircleView someView);
	public void showPlanetColor(String color);
}
