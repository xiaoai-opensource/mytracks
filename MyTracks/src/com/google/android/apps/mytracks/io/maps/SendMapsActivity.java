/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.android.apps.mytracks.io.maps;

import com.google.android.apps.mytracks.io.docs.SendDocsActivity;
import com.google.android.apps.mytracks.io.fusiontables.SendFusionTablesActivity;
import com.google.android.apps.mytracks.io.gdata.maps.MapsConstants;
import com.google.android.apps.mytracks.io.sendtogoogle.AbstractSendActivity;
import com.google.android.apps.mytracks.io.sendtogoogle.AbstractSendAsyncTask;
import com.google.android.apps.mytracks.io.sendtogoogle.SendRequest;
import com.google.android.apps.mytracks.io.sendtogoogle.UploadResultActivity;
import com.google.android.maps.mytracks.R;

import android.content.Intent;

/**
 * An activity to send a track to Google Maps.
 *
 * @author Jimmy Shih
 */
public class SendMapsActivity extends AbstractSendActivity {

  private static final String TAG = SendMapsActivity.class.getSimpleName();
  
  protected String getTag() {
    return TAG;
  }

  @Override
  protected String getAuthTokenType() {
    return MapsConstants.SERVICE_NAME;
  }

  @Override
  protected PermissionCallback getPermissionCallback() {
    return new PermissionCallback() {
      @Override
      public void onSuccess() {
        executeAsyncTask();
      }
      @Override
      public void onFailure() {
        startNextActivity(false, false);
      }
    };
  }
  
  @Override
  protected AbstractSendAsyncTask createAsyncTask() {
    return new SendMapsAsyncTask(
        this, sendRequest.getTrackId(), sendRequest.getAccount(), sendRequest.getMapId());
  }

  @Override
  protected String getServiceName() {
    return getString(R.string.send_google_maps);
  }

  @Override
  protected void startNextActivity(boolean success, boolean isCancel) {
    sendRequest.setMapsSuccess(success);

    Class<?> next;
    if (isCancel) {
      next = UploadResultActivity.class;
    } else {
      if (sendRequest.isSendFusionTables()) {
        next = SendFusionTablesActivity.class;
      } else if (sendRequest.isSendDocs()) {
        next = SendDocsActivity.class;
      } else {
        next = UploadResultActivity.class;
      }
    }
    Intent intent = new Intent(this, next).putExtra(SendRequest.SEND_REQUEST_KEY, sendRequest);
    startActivity(intent);
    finish();
  }
}
