/* Copyright (C) 2012 Nico Castelli, Christopher Mayworm 
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

import android.widget.TextView;

class PicturesViewHolder {
	private TextView textViewTime;
	private TextView textViewName;
	private TextView textViewObjektiv;

	public PicturesViewHolder(TextView textViewname, TextView textViewtime,
			TextView textViewobjektiv) {
		this.textViewTime = textViewtime;
		this.textViewObjektiv = textViewobjektiv;
		this.textViewName = textViewname;
	}

	public TextView getTextViewName() {
		return textViewName;
	}

	public TextView getTextViewTime() {
		return textViewTime;
	}

	public TextView getTextViewObjektiv() {
		return this.textViewObjektiv;
	}
}