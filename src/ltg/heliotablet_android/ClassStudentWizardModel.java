/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ltg.heliotablet_android;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import ltg.heliotablet_android.wizard.model.AbstractWizardModel;
import ltg.heliotablet_android.wizard.model.BranchPage;
import ltg.heliotablet_android.wizard.model.CustomerInfoPage;
import ltg.heliotablet_android.wizard.model.MultipleFixedChoicePage;
import ltg.heliotablet_android.wizard.model.PageList;
import ltg.heliotablet_android.wizard.model.SingleFixedChoicePage;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

public class ClassStudentWizardModel extends AbstractWizardModel {
	
    public ClassStudentWizardModel(Context context) {
        super(context);
       
    }

    @Override
    protected PageList onNewRootPageList() {
    	
    	Resources act = mContext.getApplicationContext().getResources();
    	String bensClassString = act.getString(R.string.ben_class_title);
    	String juliaClassString = act.getString(R.string.julia_class_title);
    	
    	String fifthGrade = act.getString(R.string.class_title_5th);
    	String sixthGrade = act.getString(R.string.class_title_6th);
    	
    	//Ben 5th
    	Iterable<String> ben5th = Splitter.on(',')
        .trimResults()
        .omitEmptyStrings()
        .split(act.getString(R.string.ben_class_5th));
    	String[] ben5thArray = Iterables.toArray(ben5th, String.class);
    	
    	//Ben 6th
    	Iterable<String> ben6th = Splitter.on(',')
        .trimResults()
        .omitEmptyStrings()
        .split(act.getString(R.string.ben_class_6th));
    	String[] ben6thArray = Iterables.toArray(ben6th, String.class);
    	
    	//Julia 5th
    	Iterable<String> julia5th = Splitter.on(',')
        .trimResults()
        .omitEmptyStrings()
        .split(act.getString(R.string.julia_class_5th));
    	String[] julia5thArray = Iterables.toArray(julia5th, String.class);
    	
    	//Julia 6th
    	Iterable<String> julia6th = Splitter.on(',')
        .trimResults()
        .omitEmptyStrings()
        .split(act.getString(R.string.julia_class_6th));
    	String[] julia6thArray = Iterables.toArray(julia6th, String.class);
    	
    	//ben branch
    	BranchPage benBranch = new BranchPage(this, act.getString(R.string.which_grade_));
    	benBranch .addBranch(fifthGrade,
						new SingleFixedChoicePage(this, act.getString(R.string.choose_your_name_))
                			.setChoices(ben5thArray)
                			.setRequired(true));
                			
        benBranch.addBranch(sixthGrade,
        				new SingleFixedChoicePage(this, act.getString(R.string.choose_your_name_))
						.setChoices(ben6thArray)
						.setRequired(true)).setRequired(true);
    	benBranch.setRequired(true);
    	
    	BranchPage juliaBranch = new BranchPage(this, act.getString(R.string.which_grade_));
		juliaBranch.addBranch(fifthGrade,
						new SingleFixedChoicePage(this, act.getString(R.string.choose_your_name_) + " ")
                			.setChoices(julia5thArray)
                			.setRequired(true));
		juliaBranch.setRequired(true);
        juliaBranch.addBranch(sixthGrade,
        				new SingleFixedChoicePage(this, act.getString(R.string.choose_your_name_) +" ")
						.setChoices(julia6thArray)
						.setRequired(true));
    	juliaBranch.setRequired(true);
        return new PageList(
                new BranchPage(this, act.getString(R.string.pick_your_teacher_))
                        .addBranch(bensClassString, benBranch)
                        	

                        .addBranch(juliaClassString,juliaBranch)
                        .setRequired(true)
                      		
                        
        );
    }
}
