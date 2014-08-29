package ltg.heliotablet_android.view.controller;

import android.content.Context;
import ltg.heliotablet_android.view.theory.TheoryPlanetView;

import java.util.HashMap;

public class TheoryReasonController extends ReasonController {

    private static TheoryReasonController tInstance;

    private HashMap<String, TheoryPlanetView> theoryViewsToAnchors = new HashMap<String, TheoryPlanetView>();

    private TheoryReasonController(Context context) {
        super(context);
    }

    public static TheoryReasonController getInstance(Context context) {
        if (tInstance == null) {
            tInstance = new TheoryReasonController(context);
        }
        return tInstance;
    }


    public void add(String anchor, TheoryPlanetView theoryview) {
        this.theoryViewsToAnchors.put(anchor, theoryview);
    }

    public HashMap<String, TheoryPlanetView> getTheoryViewsToAnchors() {
        return theoryViewsToAnchors;
    }

    public void setTheoryViewsToAnchors(
            HashMap<String, TheoryPlanetView> theoryViewsToAnchors) {
        this.theoryViewsToAnchors = theoryViewsToAnchors;

    }


}
