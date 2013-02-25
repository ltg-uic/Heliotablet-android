package ltg.heliotablet_android.view.controller;

import ltg.heliotablet_android.data.Reason;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class OrderingViewData {

	public static Ordering<Reason> isReadOnlyOrdering = new Ordering<Reason>() {
		@Override
		public int compare(Reason r1, Reason r2) {
			// TODO Auto-generated method stub
			return ComparisonChain.start()
			        .compareTrueFirst(r1.isReadonly(), r2.isReadonly())
			        .compare(r1.getId(), r2.getId())
			        .result();
		}
	   };
}
