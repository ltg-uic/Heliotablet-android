package ltg.heliotablet_android.view.observation;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import com.commonsware.cwac.loaderex.acl.SQLiteCursorLoader;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import ltg.heliotablet_android.MainActivity;
import ltg.heliotablet_android.MiscUtil;
import ltg.heliotablet_android.R;
import ltg.heliotablet_android.data.Reason;
import ltg.heliotablet_android.data.ReasonDBOpenHelper;
import ltg.heliotablet_android.view.controller.ObservationReasonController;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

public class ObservationViewFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public String popOverToShowFlag = null;
    private String observationAnchor;
    private ObservationReasonController observationController;
    private ReasonDBOpenHelper db = null;
    private ObservationAnchorView observationAnchorView;
    private SQLiteCursorLoader loader = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        observationAnchorView = (ObservationAnchorView) inflater.inflate(
                R.layout.observation_anchor_view, container, false);
        observationAnchorView.setAnchor(observationAnchor);
        observationAnchorView.setOnDragListener(new TargetViewDragListener());

        db = ReasonDBOpenHelper.getInstance(this.getActivity());

        observationController = ObservationReasonController
                .getInstance(getActivity());
        observationController.add(observationAnchor, observationAnchorView);
        // setupTestListeners();
        setupLoader();

        return observationAnchorView;
    }

    private void setupLoader() {
        getLoaderManager().initLoader(
                ReasonDBOpenHelper.ALL_REASONS_OBS_LOADER_ID, null, this);
        LoaderManager.enableDebugLogging(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case ReasonDBOpenHelper.ALL_REASONS_OBS_LOADER_ID:
                return new SQLiteCursorLoader(
                        this.getActivity(),
                        db,
                        "SELECT _id, anchor, type, flag, reasonText, isReadOnly, lastTimestamp, origin FROM reason WHERE type = '"
                                + ReasonDBOpenHelper.TYPE_OBSERVATION
                                + "' AND anchor = '"
                                + observationAnchor
                                + "' ORDER BY anchor, flag, isReadOnly;", null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case ReasonDBOpenHelper.ALL_REASONS_OBS_LOADER_ID:
                updateAllViews(data);
                break;
        }

        if (popOverToShowFlag != null)
            showPopover();

        Log.d("OBSERVATION FRAGMENT", "onLoadFinished");
    }

    private void showPopover() {
        observationAnchorView.showPopover(popOverToShowFlag);
        popOverToShowFlag = null;
    }

    private void updateAllViews(Cursor data) {
        if (data != null && data.getCount() > 0) {
            List<Reason> allReasons = Lists.newArrayList();
            allReasons = Collections.synchronizedList(allReasons);
            data.moveToFirst();
            while (!data.isAfterLast()) {
                Reason reason = ReasonDBOpenHelper.cursorToReason(data);
                allReasons.add(reason);
                data.moveToNext();
            }
            quickDump(allReasons);
            ImmutableSortedSet<Reason> imReasonSet = ImmutableSortedSet.copyOf(Iterables.filter(allReasons, Reason.getAnchorPredicate(observationAnchor)));
            observationAnchorView.updateObservationCircleView(imReasonSet);
        } else {
            List<Reason> allReasons = Lists.newArrayList();
            if (observationAnchorView.getChildCount() > 0) {
                observationAnchorView.clearFlagMap();
                observationAnchorView.removeAllViews();
                observationAnchorView.invalidate();
            }
        }
    }

    private void quickDump(List<Reason> allReasons) {
        for (Reason reason : allReasons) {
            System.out.println("REASON: " + reason.toString());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs,
                          Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        TypedArray a = activity.obtainStyledAttributes(attrs,
                R.styleable.ObservationViewFragment);

        observationAnchor = StringUtils.lowerCase((String) a
                .getText(R.styleable.ObservationViewFragment_android_label));
        a.recycle();

    }

    public void dbOperation(Reason reason, String type, boolean shouldSendIntent) {
        Loader<Cursor> loader = getLoaderManager().getLoader(ReasonDBOpenHelper.ALL_REASONS_OBS_LOADER_ID);
        SQLiteCursorLoader sqliteLoader = (SQLiteCursorLoader) loader;

        if (type.equals("insert")) {
            ContentValues reasonContentValues = ReasonDBOpenHelper.getReasonContentValues(reason);
            sqliteLoader.insert(ReasonDBOpenHelper.TABLE_REASON, null, reasonContentValues);

            if (shouldSendIntent)
                sendIntent(reason, MainActivity.NEW_OBSERVATION);

            makeToast("Observation Inserted");
        } else if (type.equals("remove")) {
            String[] args = {reason.getAnchor(), reason.getFlag(), reason.getOrigin()};
            sqliteLoader.delete(ReasonDBOpenHelper.TABLE_REASON, ReasonDBOpenHelper.COLUMN_ANCHOR + "=? AND " + ReasonDBOpenHelper.COLUMN_FLAG + "=? AND " + ReasonDBOpenHelper.COLUMN_ORIGIN + "=?", args);

            if (shouldSendIntent)
                sendIntent(reason, MainActivity.REMOVE_OBSERVATION);

            makeToast("Observation Delete");
        } else if (type.equals("update")) {
            String[] args = {reason.getAnchor(), reason.getFlag(), reason.getOrigin()};
            ContentValues reasonContentValues = ReasonDBOpenHelper.getReasonContentValues(reason);
            sqliteLoader.update(ReasonDBOpenHelper.TABLE_REASON, reasonContentValues, ReasonDBOpenHelper.COLUMN_ANCHOR + "=? AND " + ReasonDBOpenHelper.COLUMN_FLAG + "=? AND " + ReasonDBOpenHelper.COLUMN_ORIGIN + "=?", args);

            if (shouldSendIntent)
                sendIntent(reason, MainActivity.UPDATE_OBSERVATION);

            makeToast("Observation Update");
        } else if (type.equals("removeAll")) {
            sqliteLoader.delete(ReasonDBOpenHelper.TABLE_REASON, null, null);

            makeToast("ALL THEORYS DELETED");
        }

    }

    public void sendIntent(Reason reason, String type) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.createReasonIntent(reason, type);
    }

    public void makeToast(String toast) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.makeToast(toast);
    }

    public void resetUI() {
        observationAnchorView.resetObsView();

    }

    class TargetViewDragListener implements OnDragListener {

        @Override
        public boolean onDrag(View targetView, DragEvent event) {

            // Dropped, reassign View to ViewGroup
            View dragged = (View) event.getLocalState();
            ViewGroup sourceView = (ViewGroup) dragged.getParent();

            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:

                    if (targetView.equals(sourceView)) {
                        Log.i("DRAG", "there equal");
                    } else {
                        // sourceView.removeView(dragged);

                        // RelativeLayout rel = (RelativeLayout) dragged;

                        if (targetView instanceof ObservationAnchorView) {
                            ObservationAnchorView tv = (ObservationAnchorView) targetView;
                            ObservationCircleView cv = (ObservationCircleView) dragged;

                            if (!tv.getAnchor().equals(cv.getFlag())) {

                                if (tv.hasReadyOnlyFalse(cv.getFlag()) == true) {
                                    MiscUtil.makeTopToast(ObservationViewFragment.this.getActivity(), "Only one allowed at time.");
                                    return false;
                                }


                                String origin = ObservationViewFragment.this.observationController.getUserName();
                                Reason reason = new Reason(tv.getAnchor(),
                                        cv.getFlag(), Reason.TYPE_OBSERVATION,
                                        origin, false);


                                ObservationViewFragment.this.dbOperation(reason, "insert", true);
                                ObservationViewFragment.this.popOverToShowFlag = cv.getFlag();
                            } else {
                                MiscUtil.makeTopToast((Context) getActivity(), "Can't drop the same colors.");
                            }
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                case DragEvent.ACTION_DRAG_LOCATION:
                    View v = (View) event.getLocalState();
                    v.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            return true;
        }
    }

}
