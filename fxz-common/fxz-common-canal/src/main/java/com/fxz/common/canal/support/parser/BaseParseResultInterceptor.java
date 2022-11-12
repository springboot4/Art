/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.fxz.common.canal.support.parser;

import com.fxz.common.canal.model.ModelTable;
import com.fxz.common.canal.support.BaseParameterizedTypeReferenceSupport;

/**
 * @author fxz
 */
public abstract class BaseParseResultInterceptor<T> extends BaseParameterizedTypeReferenceSupport<T> {

	public BaseParseResultInterceptor() {
		super();
	}

	public void onParse(ModelTable modelTable) {

	}

	public void onBeforeInsertProcess(ModelTable modelTable, T beforeData, T afterData) {

	}

	public void onAfterInsertProcess(ModelTable modelTable, T beforeData, T afterData) {

	}

	public void onBeforeUpdateProcess(ModelTable modelTable, T beforeData, T afterData) {

	}

	public void onAfterUpdateProcess(ModelTable modelTable, T beforeData, T afterData) {

	}

	public void onBeforeDeleteProcess(ModelTable modelTable, T beforeData, T afterData) {

	}

	public void onAfterDeleteProcess(ModelTable modelTable, T beforeData, T afterData) {

	}

	public void onBeforeDDLProcess(ModelTable modelTable, T beforeData, T afterData, String sql) {

	}

	public void onAfterDDLProcess(ModelTable modelTable, T beforeData, T afterData, String sql) {

	}

	public void onParseFinish(ModelTable modelTable) {

	}

	public void onParseCompletion(ModelTable modelTable) {

	}

}
