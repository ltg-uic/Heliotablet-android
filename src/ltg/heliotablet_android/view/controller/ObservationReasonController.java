package ltg.heliotablet_android.view.controller;

import android.content.Context;
import ltg.heliotablet_android.view.observation.ObservationAnchorView;

import java.util.HashMap;

public class ObservationReasonController extends ReasonController {

    private static ObservationReasonController tInstance;
    private HashMap<String, ObservationAnchorView> observationViewsToAnchors = new HashMap<String, ObservationAnchorView>();

    private ObservationReasonController(Context context) {
        super(context);
    }

    public static ObservationReasonController getInstance(Context context) {
        if (tInstance == null) {
            tInstance = new ObservationReasonController(context);
        }
        return tInstance;
    }

    public void add(String anchor, ObservationAnchorView theoryview) {
        this.observationViewsToAnchors.put(anchor, theoryview);
    }


}
