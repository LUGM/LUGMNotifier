package chipset.lugmnotifier.resources;

public interface RPResultListener {
  void onPermissionGranted();

  void onPermissionDenied();
}
