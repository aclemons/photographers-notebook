/* Copyright (C) 2012 Nico Castelli, Christopher Maiworm 
 * Copyright (C) 2012 Sebastian Draxler, Alexander Boden, Christian Woehrl (Committers)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 *        
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package unisiegen.photographers.helper;

import android.widget.ImageView;
import android.widget.TextView;

public class FilmsViewHolder {
	private TextView textViewTime;
	private TextView textViewName;
	private TextView textViewCam;
	private TextView textViewPics;
	private ImageView imageViewBild;

	public FilmsViewHolder(TextView textViewname, TextView textViewtime,
			TextView textViewcam, TextView textViewpics, ImageView Bilds) {

		this.textViewTime = textViewtime;
		this.textViewName = textViewname;
		this.textViewCam = textViewcam;
		this.textViewPics = textViewpics;
		this.imageViewBild = Bilds;
	}

	public TextView getTextViewName() {
		return textViewName;
	}

	public TextView getTextViewTime() {
		return textViewTime;
	}

	public TextView getTextViewCam() {
		return textViewCam;
	}

	public TextView getTextViewPics() {
		return textViewPics;
	}

	public ImageView getBildView() {
		return imageViewBild;
	}
}