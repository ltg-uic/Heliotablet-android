package ltg.heliotablet_android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;



public class ScratchPadFragment extends Fragment {

	int dx;
	int dy;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View view = inflater
				.inflate(R.layout.sketch_activity, container, false);
		
		FrameLayout sketchView = (FrameLayout) view
				.findViewById(R.id.planetSketchView);

		int childCount = sketchView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View sketchItem = sketchView.getChildAt(i);
			sketchItem.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					drag(event, v);
					return true;
				}
			});
		}
//		
//		View cameraButton = sketchView.findViewById(R.id.cameraButton);
//		cameraButton.setOnClickListener(new OnClickListener() {
//            
//            @Override
//            public void onClick(View v) {
//            	SharedPreferences settings = getActivity().getSharedPreferences(getString(R.string.xmpp_prefs),getActivity().MODE_PRIVATE);
//
//				String userName = settings.getString(getString(R.string.user_name), "");
//				
//				String fileName = userName + Math.random() + ".jpg";
//				
//                View v1 = v.getRootView();
//                Bitmap bitmap;
//                v1.setDrawingCacheEnabled(true); 
//                bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//                v1.setDrawingCacheEnabled(true);
//                
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
//                File f = new File(Environment.getExternalStorageDirectory() + File.separator + fileName);
//                try {
//					f.createNewFile();
//					 FileOutputStream fo = new FileOutputStream(f);
//		                fo.write(bytes.toByteArray()); 
//		                fo.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//               
//            }
//        });
//		
		return view;
	}


	public void drag(MotionEvent event, View v) {

		FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) v
				.getLayoutParams();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			dx = (int) event.getX();
			dy = (int) event.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			int x = (int) event.getX();
			int y = (int) event.getY();
			FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v
					.getLayoutParams();
			int left = lp.leftMargin + (x - dx);
			int top = lp.topMargin + (y - dy);
			lp.leftMargin = left;
			lp.topMargin = top;
			v.setLayoutParams(lp);
			break;
		}
	}

}
