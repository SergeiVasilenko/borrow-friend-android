package vision.genesis.manager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import vision.genesis.util.log.LogManager;


public class PermissionManager {

	private static final LogManager log = new LogManager("PermissionManager");

	private static final int PERMISSIONS_REQUEST_CODE = 1;

	private PublishSubject<String> mPermissionSubject;

	PermissionManager() {
		mPermissionSubject = PublishSubject.create();
	}

	public Observable<String> observePermission() {
		return mPermissionSubject;
	}

	public void requestPermissions(@NonNull Activity activity, @NonNull String[] permissions) {
		List<String> notGrantedPermissions = new ArrayList<>(permissions.length);
		for (String permission : permissions) {
			if (!isPermissionsGranted(activity, permission)) {
				notGrantedPermissions.add(permission);
			}
		}

		if (!notGrantedPermissions.isEmpty()) {
			ActivityCompat.requestPermissions(activity,
											  notGrantedPermissions.toArray(new String[notGrantedPermissions.size()]),
											  PERMISSIONS_REQUEST_CODE);
		}
	}

	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		if (requestCode != PERMISSIONS_REQUEST_CODE) {
			return;
		}

		StringBuilder permissionsBuilder = new StringBuilder();
		for (int i = 0; i < permissions.length; i++) {
			boolean isGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
			permissionsBuilder.append("\n")
							  .append(" -- ")
							  .append(permissions[i])
							  .append(" is ")
							  .append(isGranted ? "GRANTED" : "DENIED");
			if (isGranted) {
				mPermissionSubject.onNext(permissions[i]);
			}
		}
		log.d("Received result of permissions request: %s", permissionsBuilder.toString());
	}

	public static boolean isPermissionsGranted(@NonNull Context context, @NonNull String permission) {
		// noinspection SimplifiableIfStatement
		if (Build.VERSION.SDK_INT < 23) {
			return true;
		}
		return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
	}

	public static boolean isPermissionsGranted(@NonNull Context context, @NonNull String[] permissions) {
		for (String permission : permissions) {
			if (!isPermissionsGranted(context, permission)) {
				return false;
			}
		}
		return true;
	}

}