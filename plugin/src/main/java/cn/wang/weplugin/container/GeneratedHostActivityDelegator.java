package cn.wang.weplugin.container;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.app.DirectAction;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.SharedElementCallback;
import android.app.TaskStackBuilder;
import android.app.VoiceInteractor;
import android.app.assist.AssistContent;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Display;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toolbar;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * 由
 * {@link com.tencent.shadow.coding.code_generator.ActivityCodeGenerator}
 * 自动生成
 * HostActivityDelegator作为委托者的接口。主要提供它的委托方法的super方法，
 * 以便Delegate可以通过这个接口调用到Activity的super方法。
 */
public interface GeneratedHostActivityDelegator {
  Activity getParent();

  boolean isDestroyed();

  void setResult(int arg0);

  void setResult(int arg0, Intent arg1);

  void dump(String arg0, FileDescriptor arg1, PrintWriter arg2, String[] arg3);

  CharSequence getTitle();

  void setTitle(int arg0);

  void setTitle(CharSequence arg0);

  ComponentName getComponentName();

  int getTaskId();

  void setContentView(View arg0);

  void setContentView(View arg0, ViewGroup.LayoutParams arg1);

  void setContentView(int arg0);

  boolean isChild();

  View getCurrentFocus();

  Application getApplication();

  WindowManager getWindowManager();

  Intent getIntent();

  Window getWindow();

  void setIntent(Intent arg0);

  LoaderManager getLoaderManager();

  void openContextMenu(View arg0);

  void showDialog(int arg0);

  boolean showDialog(int arg0, Bundle arg1);

  void setTheme(int arg0);

  boolean hasWindowFocus();

  SearchEvent getSearchEvent();

  Cursor managedQuery(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4);

  void addContentView(View arg0, ViewGroup.LayoutParams arg1);

  <T extends View> T requireViewById(int arg0);

  void dismissDialog(int arg0);

  void reportFullyDrawn();

  Scene getContentScene();

  <T extends View> T findViewById(int arg0);

  void setActionBar(Toolbar arg0);

  void openOptionsMenu();

  void triggerSearch(String arg0, Bundle arg1);

  ActionBar getActionBar();

  void takeKeyEvents(boolean arg0);

  MenuInflater getMenuInflater();

  void removeDialog(int arg0);

  void startSearch(String arg0, boolean arg1, Bundle arg2, boolean arg3);

  void closeOptionsMenu();

  void closeContextMenu();

  boolean showAssist(Bundle arg0);

  void setTurnScreenOn(boolean arg0);

  Uri getReferrer();

  void startActivities(Intent[] arg0);

  void startActivities(Intent[] arg0, Bundle arg1);

  Object getSystemService(String arg0);

  void finishFromChild(Activity arg0);

  void setImmersive(boolean arg0);

  boolean isImmersive();

  void stopLockTask();

  SharedPreferences getPreferences(int arg0);

  void startActivity(Intent arg0, Bundle arg1);

  void startActivity(Intent arg0);

  void setProgress(int arg0);

  boolean isTaskRoot();

  void setTitleColor(int arg0);

  boolean releaseInstance();

  boolean isFinishing();

  void finishActivity(int arg0);

  void runOnUiThread(Runnable arg0);

  void setVrModeEnabled(boolean arg0, ComponentName arg1) throws
      PackageManager.NameNotFoundException;

  ActionMode startActionMode(ActionMode.Callback arg0, int arg1);

  ActionMode startActionMode(ActionMode.Callback arg0);

  int getTitleColor();

  boolean navigateUpTo(Intent arg0);

  void startLockTask();

  void finishAffinity();

  boolean moveTaskToBack(boolean arg0);

  void setVisible(boolean arg0);

  boolean shouldShowRequestPermissionRationale(String arg0);

  void setProgressBarIndeterminateVisibility(boolean arg0);

  void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks arg0);

  void unregisterActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks arg0);

  VoiceInteractor getVoiceInteractor();

  void dismissKeyboardShortcutsHelper();

  boolean enterPictureInPictureMode(PictureInPictureParams arg0);

  void enterPictureInPictureMode();

  void requestShowKeyboardShortcuts();

  FragmentManager getFragmentManager();

  void stopManagingCursor(Cursor arg0);

  boolean isLocalVoiceInteractionSupported();

  boolean isVoiceInteraction();

  boolean isInPictureInPictureMode();

  int getChangingConfigurations();

  TransitionManager getContentTransitionManager();

  void setContentTransitionManager(TransitionManager arg0);

  void startLocalVoiceInteraction(Bundle arg0);

  void stopLocalVoiceInteraction();

  boolean isInMultiWindowMode();

  boolean isVoiceInteractionRoot();

  void setPictureInPictureParams(PictureInPictureParams arg0);

  int getMaxNumPictureInPictureActions();

  Object getLastNonConfigurationInstance();

  void startManagingCursor(Cursor arg0);

  void setFeatureDrawableResource(int arg0, int arg1);

  void setFeatureDrawableUri(int arg0, Uri arg1);

  boolean requestWindowFeature(int arg0);

  void registerForContextMenu(View arg0);

  void setFeatureDrawable(int arg0, Drawable arg1);

  void setFeatureDrawableAlpha(int arg0, int arg1);

  void requestPermissions(String[] arg0, int arg1);

  void invalidateOptionsMenu();

  void unregisterForContextMenu(View arg0);

  void setFinishOnTouchOutside(boolean arg0);

  void setDefaultKeyMode(int arg0);

  boolean navigateUpToFromChild(Activity arg0, Intent arg1);

  Intent getParentActivityIntent();

  void setEnterSharedElementCallback(SharedElementCallback arg0);

  void setMediaController(MediaController arg0);

  boolean shouldUpRecreateTask(Intent arg0);

  void startIntentSenderFromChild(Activity arg0, IntentSender arg1, int arg2, Intent arg3, int arg4,
                                  int arg5, int arg6) throws IntentSender.SendIntentException;

  void startIntentSenderFromChild(Activity arg0, IntentSender arg1, int arg2, Intent arg3, int arg4,
                                  int arg5, int arg6, Bundle arg7) throws IntentSender.SendIntentException;

  String getCallingPackage();

  void setExitSharedElementCallback(SharedElementCallback arg0);

  void postponeEnterTransition();

  void startPostponedEnterTransition();

  boolean startNextMatchingActivity(Intent arg0);

  boolean startNextMatchingActivity(Intent arg0, Bundle arg1);

  void startActivityFromFragment(Fragment arg0, Intent arg1, int arg2, Bundle arg3);

  void startActivityFromFragment(Fragment arg0, Intent arg1, int arg2);

  void overridePendingTransition(int arg0, int arg1);

  void setTaskDescription(ActivityManager.TaskDescription arg0);

  void startActivityForResult(Intent arg0, int arg1, Bundle arg2);

  void startActivityForResult(Intent arg0, int arg1);

  void startActivityFromChild(Activity arg0, Intent arg1, int arg2, Bundle arg3);

  void startActivityFromChild(Activity arg0, Intent arg1, int arg2);

  void setProgressBarIndeterminate(boolean arg0);

  int getVolumeControlStream();

  boolean startActivityIfNeeded(Intent arg0, int arg1);

  boolean startActivityIfNeeded(Intent arg0, int arg1, Bundle arg2);

  int getRequestedOrientation();

  void finishActivityFromChild(Activity arg0, int arg1);

  void finishAndRemoveTask();

  boolean isActivityTransitionRunning();

  void setRequestedOrientation(int arg0);

  void finishAfterTransition();

  String getLocalClassName();

  void startIntentSenderForResult(IntentSender arg0, int arg1, Intent arg2, int arg3, int arg4,
                                  int arg5) throws IntentSender.SendIntentException;

  void startIntentSenderForResult(IntentSender arg0, int arg1, Intent arg2, int arg3, int arg4,
                                  int arg5, Bundle arg6) throws IntentSender.SendIntentException;

  void setVolumeControlStream(int arg0);

  MediaController getMediaController();

  PendingIntent createPendingResult(int arg0, Intent arg1, int arg2);

  boolean requestVisibleBehind(boolean arg0);

  void startIntentSender(IntentSender arg0, Intent arg1, int arg2, int arg3, int arg4, Bundle arg5)
      throws IntentSender.SendIntentException;

  void startIntentSender(IntentSender arg0, Intent arg1, int arg2, int arg3, int arg4) throws
      IntentSender.SendIntentException;

  void setProgressBarVisibility(boolean arg0);

  void setSecondaryProgress(int arg0);

  DragAndDropPermissions requestDragAndDropPermissions(DragEvent arg0);

  void showLockTaskEscapeMessage();

  void setShowWhenLocked(boolean arg0);

  void setInheritShowWhenLocked(boolean arg0);

  void setTheme(Resources.Theme arg0);

  Resources.Theme getTheme();

  void applyOverrideConfiguration(Configuration arg0);

  AssetManager getAssets();

  int checkPermission(String arg0, int arg1, int arg2);

  String getPackageName();

  PackageManager getPackageManager();

  File getCacheDir();

  boolean deleteFile(String arg0);

  File getDir(String arg0, int arg1);

  Context getBaseContext();

  Looper getMainLooper();

  Executor getMainExecutor();

  FileInputStream openFileInput(String arg0) throws FileNotFoundException;

  FileOutputStream openFileOutput(String arg0, int arg1) throws FileNotFoundException;

  String getOpPackageName();

  String[] fileList();

  File getDataDir();

  File getFilesDir();

  boolean stopService(Intent arg0);

  File getObbDir();

  boolean deleteDatabase(String arg0);

  File[] getObbDirs();

  File getDatabasePath(String arg0);

  Drawable peekWallpaper();

  File getCodeCacheDir();

  void unbindService(ServiceConnection arg0);

  Drawable getWallpaper();

  void sendBroadcast(Intent arg0);

  void sendBroadcast(Intent arg0, String arg1);

  boolean bindService(Intent arg0, ServiceConnection arg1, int arg2);

  boolean bindService(Intent arg0, int arg1, Executor arg2, ServiceConnection arg3);

  void setWallpaper(InputStream arg0) throws IOException;

  void setWallpaper(Bitmap arg0) throws IOException;

  ComponentName startService(Intent arg0);

  Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1, int arg2);

  Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1, String arg2, Handler arg3,
                          int arg4);

  Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1);

  Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1, String arg2, Handler arg3);

  String[] databaseList();

  void clearWallpaper() throws IOException;

  boolean isRestricted();

  boolean moveDatabaseFrom(Context arg0, String arg1);

  Context createDeviceProtectedStorageContext();

  void enforceCallingOrSelfUriPermission(Uri arg0, int arg1, String arg2);

  boolean moveSharedPreferencesFrom(Context arg0, String arg1);

  File getNoBackupFilesDir();

  File[] getExternalFilesDirs(String arg0);

  File getExternalCacheDir();

  File[] getExternalMediaDirs();

  SQLiteDatabase openOrCreateDatabase(String arg0, int arg1, SQLiteDatabase.CursorFactory arg2);

  SQLiteDatabase openOrCreateDatabase(String arg0, int arg1, SQLiteDatabase.CursorFactory arg2,
                                      DatabaseErrorHandler arg3);

  int getWallpaperDesiredMinimumWidth();

  int getWallpaperDesiredMinimumHeight();

  ApplicationInfo getApplicationInfo();

  File getFileStreamPath(String arg0);

  boolean deleteSharedPreferences(String arg0);

  File[] getExternalCacheDirs();

  File getExternalFilesDir(String arg0);

  ContentResolver getContentResolver();

  Context getApplicationContext();

  String getPackageResourcePath();

  String getPackageCodePath();

  SharedPreferences getSharedPreferences(String arg0, int arg1);

  int checkCallingUriPermission(Uri arg0, int arg1);

  boolean bindIsolatedService(Intent arg0, int arg1, String arg2, Executor arg3,
                              ServiceConnection arg4);

  String getSystemServiceName(Class<?> arg0);

  int checkCallingOrSelfUriPermission(Uri arg0, int arg1);

  Context createDisplayContext(Display arg0);

  boolean isDeviceProtectedStorage();

  boolean startInstrumentation(ComponentName arg0, String arg1, Bundle arg2);

  void updateServiceGroup(ServiceConnection arg0, int arg1, int arg2);

  void unregisterReceiver(BroadcastReceiver arg0);

  int checkSelfPermission(String arg0);

  void enforcePermission(String arg0, int arg1, int arg2, String arg3);

  void sendStickyBroadcastAsUser(Intent arg0, UserHandle arg1);

  void enforceUriPermission(Uri arg0, int arg1, int arg2, int arg3, String arg4);

  void enforceUriPermission(Uri arg0, String arg1, String arg2, int arg3, int arg4, int arg5,
                            String arg6);

  Context createConfigurationContext(Configuration arg0);

  void sendStickyBroadcast(Intent arg0);

  int checkUriPermission(Uri arg0, String arg1, String arg2, int arg3, int arg4, int arg5);

  int checkUriPermission(Uri arg0, int arg1, int arg2, int arg3);

  int checkCallingOrSelfPermission(String arg0);

  void removeStickyBroadcastAsUser(Intent arg0, UserHandle arg1);

  ComponentName startForegroundService(Intent arg0);

  void sendOrderedBroadcast(Intent arg0, String arg1);

  void sendOrderedBroadcast(Intent arg0, String arg1, BroadcastReceiver arg2, Handler arg3,
                            int arg4, String arg5, Bundle arg6);

  void sendOrderedBroadcastAsUser(Intent arg0, UserHandle arg1, String arg2, BroadcastReceiver arg3,
                                  Handler arg4, int arg5, String arg6, Bundle arg7);

  void sendStickyOrderedBroadcast(Intent arg0, BroadcastReceiver arg1, Handler arg2, int arg3,
                                  String arg4, Bundle arg5);

  int checkCallingPermission(String arg0);

  void grantUriPermission(String arg0, Uri arg1, int arg2);

  Context createPackageContext(String arg0, int arg1) throws PackageManager.NameNotFoundException;

  void enforceCallingPermission(String arg0, String arg1);

  void sendBroadcastAsUser(Intent arg0, UserHandle arg1, String arg2);

  void sendBroadcastAsUser(Intent arg0, UserHandle arg1);

  void enforceCallingOrSelfPermission(String arg0, String arg1);

  void removeStickyBroadcast(Intent arg0);

  void sendStickyOrderedBroadcastAsUser(Intent arg0, UserHandle arg1, BroadcastReceiver arg2,
                                        Handler arg3, int arg4, String arg5, Bundle arg6);

  void revokeUriPermission(Uri arg0, int arg1);

  void revokeUriPermission(String arg0, Uri arg1, int arg2);

  void enforceCallingUriPermission(Uri arg0, int arg1, String arg2);

  Context createContextForSplit(String arg0) throws PackageManager.NameNotFoundException;

  void registerComponentCallbacks(ComponentCallbacks arg0);

  void unregisterComponentCallbacks(ComponentCallbacks arg0);

  void attachBaseContext(Context arg0);

  boolean isChangingConfigurations();

  void finish();

  ClassLoader getClassLoader();

  LayoutInflater getLayoutInflater();

  Resources getResources();

  void recreate();

  ComponentName getCallingActivity();

  boolean onPreparePanel(int arg0, View arg1, Menu arg2);

  boolean onMenuOpened(int arg0, Menu arg1);

  void onPanelClosed(int arg0, Menu arg1);

  void onContentChanged();

  void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> arg0, Menu arg1, int arg2);

  void onWindowAttributesChanged(WindowManager.LayoutParams arg0);

  void onWindowFocusChanged(boolean arg0);

  boolean onMenuItemSelected(int arg0, MenuItem arg1);

  void onDetachedFromWindow();

  boolean onSearchRequested(SearchEvent arg0);

  boolean onSearchRequested();

  View onCreatePanelView(int arg0);

  void onAttachedToWindow();

  boolean onCreatePanelMenu(int arg0, Menu arg1);

  void onActionModeStarted(ActionMode arg0);

  ActionMode onWindowStartingActionMode(ActionMode.Callback arg0);

  ActionMode onWindowStartingActionMode(ActionMode.Callback arg0, int arg1);

  void onActionModeFinished(ActionMode arg0);

  void onPointerCaptureChanged(boolean arg0);

  boolean dispatchKeyEvent(KeyEvent arg0);

  boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent arg0);

  boolean dispatchKeyShortcutEvent(KeyEvent arg0);

  boolean dispatchTouchEvent(MotionEvent arg0);

  boolean dispatchGenericMotionEvent(MotionEvent arg0);

  boolean dispatchTrackballEvent(MotionEvent arg0);

  boolean superIsChangingConfigurations();

  void superFinish();

  ClassLoader superGetClassLoader();

  LayoutInflater superGetLayoutInflater();

  Resources superGetResources();

  void superRecreate();

  ComponentName superGetCallingActivity();

  void superOnCreate(Bundle arg0, PersistableBundle arg1);

  void superOnPostCreate(Bundle arg0, PersistableBundle arg1);

  void superOnStateNotSaved();

  void superOnLowMemory();

  boolean superOnKeyDown(int arg0, KeyEvent arg1);

  void superOnBackPressed();

  boolean superOnPreparePanel(int arg0, View arg1, Menu arg2);

  boolean superOnMenuOpened(int arg0, Menu arg1);

  void superOnPanelClosed(int arg0, Menu arg1);

  boolean superOnNavigateUp();

  boolean superOnKeyMultiple(int arg0, int arg1, KeyEvent arg2);

  void superOnContentChanged();

  boolean superOnKeyShortcut(int arg0, KeyEvent arg1);

  void superOnAttachFragment(Fragment arg0);

  boolean superOnKeyUp(int arg0, KeyEvent arg1);

  boolean superOnTrackballEvent(MotionEvent arg0);

  boolean superOnKeyLongPress(int arg0, KeyEvent arg1);

  void superOnTrimMemory(int arg0);

  boolean superOnTouchEvent(MotionEvent arg0);

  View superOnCreateView(View arg0, String arg1, Context arg2, AttributeSet arg3);

  View superOnCreateView(String arg0, Context arg1, AttributeSet arg2);

  CharSequence superOnCreateDescription();

  void superOnPerformDirectAction(String arg0, Bundle arg1, CancellationSignal arg2,
                                  Consumer<Bundle> arg3);

  void superOnPictureInPictureModeChanged(boolean arg0);

  void superOnPictureInPictureModeChanged(boolean arg0, Configuration arg1);

  void superOnSaveInstanceState(Bundle arg0, PersistableBundle arg1);

  void superOnMultiWindowModeChanged(boolean arg0, Configuration arg1);

  void superOnMultiWindowModeChanged(boolean arg0);

  boolean superOnCreateThumbnail(Bitmap arg0, Canvas arg1);

  void superOnLocalVoiceInteractionStopped();

  void superOnLocalVoiceInteractionStarted();

  void superOnGetDirectActions(CancellationSignal arg0, Consumer<List<DirectAction>> arg1);

  void superOnProvideAssistData(Bundle arg0);

  void superOnRestoreInstanceState(Bundle arg0, PersistableBundle arg1);

  void superOnTopResumedActivityChanged(boolean arg0);

  void superOnProvideKeyboardShortcuts(List<KeyboardShortcutGroup> arg0, Menu arg1, int arg2);

  void superOnProvideAssistContent(AssistContent arg0);

  Object superOnRetainNonConfigurationInstance();

  void superOnConfigurationChanged(Configuration arg0);

  void superOnContextMenuClosed(Menu arg0);

  void superOnWindowAttributesChanged(WindowManager.LayoutParams arg0);

  boolean superOnCreateOptionsMenu(Menu arg0);

  void superOnWindowFocusChanged(boolean arg0);

  boolean superOnContextItemSelected(MenuItem arg0);

  boolean superOnMenuItemSelected(int arg0, MenuItem arg1);

  void superOnPrepareNavigateUpTaskStack(TaskStackBuilder arg0);

  void superOnDetachedFromWindow();

  boolean superOnSearchRequested(SearchEvent arg0);

  boolean superOnSearchRequested();

  boolean superOnNavigateUpFromChild(Activity arg0);

  View superOnCreatePanelView(int arg0);

  void superOnAttachedToWindow();

  void superOnOptionsMenuClosed(Menu arg0);

  void superOnCreateContextMenu(ContextMenu arg0, View arg1, ContextMenu.ContextMenuInfo arg2);

  boolean superOnGenericMotionEvent(MotionEvent arg0);

  boolean superOnCreatePanelMenu(int arg0, Menu arg1);

  boolean superOnPrepareOptionsMenu(Menu arg0);

  boolean superOnOptionsItemSelected(MenuItem arg0);

  void superOnCreateNavigateUpTaskStack(TaskStackBuilder arg0);

  void superOnUserInteraction();

  void superOnActionModeStarted(ActionMode arg0);

  void superOnRequestPermissionsResult(int arg0, String[] arg1, int[] arg2);

  void superOnActivityReenter(int arg0, Intent arg1);

  Uri superOnProvideReferrer();

  void superOnEnterAnimationComplete();

  void superOnVisibleBehindCanceled();

  ActionMode superOnWindowStartingActionMode(ActionMode.Callback arg0);

  ActionMode superOnWindowStartingActionMode(ActionMode.Callback arg0, int arg1);

  void superOnActionModeFinished(ActionMode arg0);

  void superOnPointerCaptureChanged(boolean arg0);

  void superOnStart();

  void superOnCreate(Bundle arg0);

  void superOnStop();

  void superOnNewIntent(Intent arg0);

  void superOnPostCreate(Bundle arg0);

  void superOnRestart();

  void superOnResume();

  void superOnPostResume();

  void superOnUserLeaveHint();

  Dialog superOnCreateDialog(int arg0);

  Dialog superOnCreateDialog(int arg0, Bundle arg1);

  void superOnPrepareDialog(int arg0, Dialog arg1);

  void superOnPrepareDialog(int arg0, Dialog arg1, Bundle arg2);

  void superOnPause();

  void superOnDestroy();

  void superOnTitleChanged(CharSequence arg0, int arg1);

  void superOnActivityResult(int arg0, int arg1, Intent arg2);

  void superOnSaveInstanceState(Bundle arg0);

  void superOnRestoreInstanceState(Bundle arg0);

  void superOnApplyThemeResource(Resources.Theme arg0, int arg1, boolean arg2);

  void superOnChildTitleChanged(Activity arg0, CharSequence arg1);

  boolean superDispatchKeyEvent(KeyEvent arg0);

  boolean superDispatchPopulateAccessibilityEvent(AccessibilityEvent arg0);

  boolean superDispatchKeyShortcutEvent(KeyEvent arg0);

  boolean superDispatchTouchEvent(MotionEvent arg0);

  boolean superDispatchGenericMotionEvent(MotionEvent arg0);

  boolean superDispatchTrackballEvent(MotionEvent arg0);

  Activity superGetParent();

  boolean superIsDestroyed();

  void superSetResult(int arg0);

  void superSetResult(int arg0, Intent arg1);

  void superDump(String arg0, FileDescriptor arg1, PrintWriter arg2, String[] arg3);

  CharSequence superGetTitle();

  void superSetTitle(int arg0);

  void superSetTitle(CharSequence arg0);

  ComponentName superGetComponentName();

  int superGetTaskId();

  void superSetContentView(View arg0);

  void superSetContentView(View arg0, ViewGroup.LayoutParams arg1);

  void superSetContentView(int arg0);

  boolean superIsChild();

  View superGetCurrentFocus();

  Application superGetApplication();

  WindowManager superGetWindowManager();

  Intent superGetIntent();

  Window superGetWindow();

  void superSetIntent(Intent arg0);

  LoaderManager superGetLoaderManager();

  void superOpenContextMenu(View arg0);

  void superShowDialog(int arg0);

  boolean superShowDialog(int arg0, Bundle arg1);

  void superSetTheme(int arg0);

  boolean superHasWindowFocus();

  SearchEvent superGetSearchEvent();

  Cursor superManagedQuery(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4);

  void superAddContentView(View arg0, ViewGroup.LayoutParams arg1);

  <T extends View> T superRequireViewById(int arg0);

  void superDismissDialog(int arg0);

  void superReportFullyDrawn();

  Scene superGetContentScene();

  <T extends View> T superFindViewById(int arg0);

  void superSetActionBar(Toolbar arg0);

  void superOpenOptionsMenu();

  void superTriggerSearch(String arg0, Bundle arg1);

  ActionBar superGetActionBar();

  void superTakeKeyEvents(boolean arg0);

  MenuInflater superGetMenuInflater();

  void superRemoveDialog(int arg0);

  void superStartSearch(String arg0, boolean arg1, Bundle arg2, boolean arg3);

  void superCloseOptionsMenu();

  void superCloseContextMenu();

  boolean superShowAssist(Bundle arg0);

  void superSetTurnScreenOn(boolean arg0);

  Uri superGetReferrer();

  void superStartActivities(Intent[] arg0);

  void superStartActivities(Intent[] arg0, Bundle arg1);

  Object superGetSystemService(String arg0);

  void superFinishFromChild(Activity arg0);

  void superSetImmersive(boolean arg0);

  boolean superIsImmersive();

  void superStopLockTask();

  SharedPreferences superGetPreferences(int arg0);

  void superStartActivity(Intent arg0, Bundle arg1);

  void superStartActivity(Intent arg0);

  void superSetProgress(int arg0);

  boolean superIsTaskRoot();

  void superSetTitleColor(int arg0);

  boolean superReleaseInstance();

  boolean superIsFinishing();

  void superFinishActivity(int arg0);

  void superRunOnUiThread(Runnable arg0);

  void superSetVrModeEnabled(boolean arg0, ComponentName arg1) throws
      PackageManager.NameNotFoundException;

  ActionMode superStartActionMode(ActionMode.Callback arg0, int arg1);

  ActionMode superStartActionMode(ActionMode.Callback arg0);

  int superGetTitleColor();

  boolean superNavigateUpTo(Intent arg0);

  void superStartLockTask();

  void superFinishAffinity();

  boolean superMoveTaskToBack(boolean arg0);

  void superSetVisible(boolean arg0);

  boolean superShouldShowRequestPermissionRationale(String arg0);

  void superSetProgressBarIndeterminateVisibility(boolean arg0);

  void superRegisterActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks arg0);

  void superUnregisterActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks arg0);

  VoiceInteractor superGetVoiceInteractor();

  void superDismissKeyboardShortcutsHelper();

  boolean superEnterPictureInPictureMode(PictureInPictureParams arg0);

  void superEnterPictureInPictureMode();

  void superRequestShowKeyboardShortcuts();

  FragmentManager superGetFragmentManager();

  void superStopManagingCursor(Cursor arg0);

  boolean superIsLocalVoiceInteractionSupported();

  boolean superIsVoiceInteraction();

  boolean superIsInPictureInPictureMode();

  int superGetChangingConfigurations();

  TransitionManager superGetContentTransitionManager();

  void superSetContentTransitionManager(TransitionManager arg0);

  void superStartLocalVoiceInteraction(Bundle arg0);

  void superStopLocalVoiceInteraction();

  boolean superIsInMultiWindowMode();

  boolean superIsVoiceInteractionRoot();

  void superSetPictureInPictureParams(PictureInPictureParams arg0);

  int superGetMaxNumPictureInPictureActions();

  Object superGetLastNonConfigurationInstance();

  void superStartManagingCursor(Cursor arg0);

  void superSetFeatureDrawableResource(int arg0, int arg1);

  void superSetFeatureDrawableUri(int arg0, Uri arg1);

  boolean superRequestWindowFeature(int arg0);

  void superRegisterForContextMenu(View arg0);

  void superSetFeatureDrawable(int arg0, Drawable arg1);

  void superSetFeatureDrawableAlpha(int arg0, int arg1);

  void superRequestPermissions(String[] arg0, int arg1);

  void superInvalidateOptionsMenu();

  void superUnregisterForContextMenu(View arg0);

  void superSetFinishOnTouchOutside(boolean arg0);

  void superSetDefaultKeyMode(int arg0);

  boolean superNavigateUpToFromChild(Activity arg0, Intent arg1);

  Intent superGetParentActivityIntent();

  void superSetEnterSharedElementCallback(SharedElementCallback arg0);

  void superSetMediaController(MediaController arg0);

  boolean superShouldUpRecreateTask(Intent arg0);

  void superStartIntentSenderFromChild(Activity arg0, IntentSender arg1, int arg2, Intent arg3,
                                       int arg4, int arg5, int arg6) throws IntentSender.SendIntentException;

  void superStartIntentSenderFromChild(Activity arg0, IntentSender arg1, int arg2, Intent arg3,
                                       int arg4, int arg5, int arg6, Bundle arg7) throws IntentSender.SendIntentException;

  String superGetCallingPackage();

  void superSetExitSharedElementCallback(SharedElementCallback arg0);

  void superPostponeEnterTransition();

  void superStartPostponedEnterTransition();

  boolean superStartNextMatchingActivity(Intent arg0);

  boolean superStartNextMatchingActivity(Intent arg0, Bundle arg1);

  void superStartActivityFromFragment(Fragment arg0, Intent arg1, int arg2, Bundle arg3);

  void superStartActivityFromFragment(Fragment arg0, Intent arg1, int arg2);

  void superOverridePendingTransition(int arg0, int arg1);

  void superSetTaskDescription(ActivityManager.TaskDescription arg0);

  void superStartActivityForResult(Intent arg0, int arg1, Bundle arg2);

  void superStartActivityForResult(Intent arg0, int arg1);

  void superStartActivityFromChild(Activity arg0, Intent arg1, int arg2, Bundle arg3);

  void superStartActivityFromChild(Activity arg0, Intent arg1, int arg2);

  void superSetProgressBarIndeterminate(boolean arg0);

  int superGetVolumeControlStream();

  boolean superStartActivityIfNeeded(Intent arg0, int arg1);

  boolean superStartActivityIfNeeded(Intent arg0, int arg1, Bundle arg2);

  int superGetRequestedOrientation();

  void superFinishActivityFromChild(Activity arg0, int arg1);

  void superFinishAndRemoveTask();

  boolean superIsActivityTransitionRunning();

  void superSetRequestedOrientation(int arg0);

  void superFinishAfterTransition();

  String superGetLocalClassName();

  void superStartIntentSenderForResult(IntentSender arg0, int arg1, Intent arg2, int arg3, int arg4,
                                       int arg5) throws IntentSender.SendIntentException;

  void superStartIntentSenderForResult(IntentSender arg0, int arg1, Intent arg2, int arg3, int arg4,
                                       int arg5, Bundle arg6) throws IntentSender.SendIntentException;

  void superSetVolumeControlStream(int arg0);

  MediaController superGetMediaController();

  PendingIntent superCreatePendingResult(int arg0, Intent arg1, int arg2);

  boolean superRequestVisibleBehind(boolean arg0);

  void superStartIntentSender(IntentSender arg0, Intent arg1, int arg2, int arg3, int arg4,
                              Bundle arg5) throws IntentSender.SendIntentException;

  void superStartIntentSender(IntentSender arg0, Intent arg1, int arg2, int arg3, int arg4) throws
      IntentSender.SendIntentException;

  void superSetProgressBarVisibility(boolean arg0);

  void superSetSecondaryProgress(int arg0);

  DragAndDropPermissions superRequestDragAndDropPermissions(DragEvent arg0);

  void superShowLockTaskEscapeMessage();

  void superSetShowWhenLocked(boolean arg0);

  void superSetInheritShowWhenLocked(boolean arg0);

  void superSetTheme(Resources.Theme arg0);

  Resources.Theme superGetTheme();

  void superApplyOverrideConfiguration(Configuration arg0);

  AssetManager superGetAssets();

  int superCheckPermission(String arg0, int arg1, int arg2);

  String superGetPackageName();

  PackageManager superGetPackageManager();

  File superGetCacheDir();

  boolean superDeleteFile(String arg0);

  File superGetDir(String arg0, int arg1);

  Context superGetBaseContext();

  Looper superGetMainLooper();

  Executor superGetMainExecutor();

  FileInputStream superOpenFileInput(String arg0) throws FileNotFoundException;

  FileOutputStream superOpenFileOutput(String arg0, int arg1) throws FileNotFoundException;

  String superGetOpPackageName();

  String[] superFileList();

  File superGetDataDir();

  File superGetFilesDir();

  boolean superStopService(Intent arg0);

  File superGetObbDir();

  boolean superDeleteDatabase(String arg0);

  File[] superGetObbDirs();

  File superGetDatabasePath(String arg0);

  Drawable superPeekWallpaper();

  File superGetCodeCacheDir();

  void superUnbindService(ServiceConnection arg0);

  Drawable superGetWallpaper();

  void superSendBroadcast(Intent arg0);

  void superSendBroadcast(Intent arg0, String arg1);

  boolean superBindService(Intent arg0, ServiceConnection arg1, int arg2);

  boolean superBindService(Intent arg0, int arg1, Executor arg2, ServiceConnection arg3);

  void superSetWallpaper(InputStream arg0) throws IOException;

  void superSetWallpaper(Bitmap arg0) throws IOException;

  ComponentName superStartService(Intent arg0);

  Intent superRegisterReceiver(BroadcastReceiver arg0, IntentFilter arg1, int arg2);

  Intent superRegisterReceiver(BroadcastReceiver arg0, IntentFilter arg1, String arg2, Handler arg3,
                               int arg4);

  Intent superRegisterReceiver(BroadcastReceiver arg0, IntentFilter arg1);

  Intent superRegisterReceiver(BroadcastReceiver arg0, IntentFilter arg1, String arg2,
                               Handler arg3);

  String[] superDatabaseList();

  void superClearWallpaper() throws IOException;

  boolean superIsRestricted();

  boolean superMoveDatabaseFrom(Context arg0, String arg1);

  Context superCreateDeviceProtectedStorageContext();

  void superEnforceCallingOrSelfUriPermission(Uri arg0, int arg1, String arg2);

  boolean superMoveSharedPreferencesFrom(Context arg0, String arg1);

  File superGetNoBackupFilesDir();

  File[] superGetExternalFilesDirs(String arg0);

  File superGetExternalCacheDir();

  File[] superGetExternalMediaDirs();

  SQLiteDatabase superOpenOrCreateDatabase(String arg0, int arg1,
                                           SQLiteDatabase.CursorFactory arg2);

  SQLiteDatabase superOpenOrCreateDatabase(String arg0, int arg1, SQLiteDatabase.CursorFactory arg2,
                                           DatabaseErrorHandler arg3);

  int superGetWallpaperDesiredMinimumWidth();

  int superGetWallpaperDesiredMinimumHeight();

  ApplicationInfo superGetApplicationInfo();

  File superGetFileStreamPath(String arg0);

  boolean superDeleteSharedPreferences(String arg0);

  File[] superGetExternalCacheDirs();

  File superGetExternalFilesDir(String arg0);

  ContentResolver superGetContentResolver();

  Context superGetApplicationContext();

  String superGetPackageResourcePath();

  String superGetPackageCodePath();

  SharedPreferences superGetSharedPreferences(String arg0, int arg1);

  int superCheckCallingUriPermission(Uri arg0, int arg1);

  boolean superBindIsolatedService(Intent arg0, int arg1, String arg2, Executor arg3,
                                   ServiceConnection arg4);

  String superGetSystemServiceName(Class<?> arg0);

  int superCheckCallingOrSelfUriPermission(Uri arg0, int arg1);

  Context superCreateDisplayContext(Display arg0);

  boolean superIsDeviceProtectedStorage();

  boolean superStartInstrumentation(ComponentName arg0, String arg1, Bundle arg2);

  void superUpdateServiceGroup(ServiceConnection arg0, int arg1, int arg2);

  void superUnregisterReceiver(BroadcastReceiver arg0);

  int superCheckSelfPermission(String arg0);

  void superEnforcePermission(String arg0, int arg1, int arg2, String arg3);

  void superSendStickyBroadcastAsUser(Intent arg0, UserHandle arg1);

  void superEnforceUriPermission(Uri arg0, int arg1, int arg2, int arg3, String arg4);

  void superEnforceUriPermission(Uri arg0, String arg1, String arg2, int arg3, int arg4, int arg5,
                                 String arg6);

  Context superCreateConfigurationContext(Configuration arg0);

  void superSendStickyBroadcast(Intent arg0);

  int superCheckUriPermission(Uri arg0, String arg1, String arg2, int arg3, int arg4, int arg5);

  int superCheckUriPermission(Uri arg0, int arg1, int arg2, int arg3);

  int superCheckCallingOrSelfPermission(String arg0);

  void superRemoveStickyBroadcastAsUser(Intent arg0, UserHandle arg1);

  ComponentName superStartForegroundService(Intent arg0);

  void superSendOrderedBroadcast(Intent arg0, String arg1);

  void superSendOrderedBroadcast(Intent arg0, String arg1, BroadcastReceiver arg2, Handler arg3,
                                 int arg4, String arg5, Bundle arg6);

  void superSendOrderedBroadcastAsUser(Intent arg0, UserHandle arg1, String arg2,
                                       BroadcastReceiver arg3, Handler arg4, int arg5, String arg6, Bundle arg7);

  void superSendStickyOrderedBroadcast(Intent arg0, BroadcastReceiver arg1, Handler arg2, int arg3,
                                       String arg4, Bundle arg5);

  int superCheckCallingPermission(String arg0);

  void superGrantUriPermission(String arg0, Uri arg1, int arg2);

  Context superCreatePackageContext(String arg0, int arg1) throws
      PackageManager.NameNotFoundException;

  void superEnforceCallingPermission(String arg0, String arg1);

  void superSendBroadcastAsUser(Intent arg0, UserHandle arg1, String arg2);

  void superSendBroadcastAsUser(Intent arg0, UserHandle arg1);

  void superEnforceCallingOrSelfPermission(String arg0, String arg1);

  void superRemoveStickyBroadcast(Intent arg0);

  void superSendStickyOrderedBroadcastAsUser(Intent arg0, UserHandle arg1, BroadcastReceiver arg2,
                                             Handler arg3, int arg4, String arg5, Bundle arg6);

  void superRevokeUriPermission(Uri arg0, int arg1);

  void superRevokeUriPermission(String arg0, Uri arg1, int arg2);

  void superEnforceCallingUriPermission(Uri arg0, int arg1, String arg2);

  Context superCreateContextForSplit(String arg0) throws PackageManager.NameNotFoundException;

  void superRegisterComponentCallbacks(ComponentCallbacks arg0);

  void superUnregisterComponentCallbacks(ComponentCallbacks arg0);

  void superAttachBaseContext(Context arg0);
}
