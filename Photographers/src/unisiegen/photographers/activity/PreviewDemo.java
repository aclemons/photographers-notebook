package unisiegen.photographers.activity;

/**
 * Für die Nutzung der Camera in der "Vorschau"
 */

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PreviewDemo extends Activity {
	private SurfaceView preview = null;
	private SurfaceHolder previewHolder = null;
	private Camera camera = null;
	private boolean inPreview = false;
	private byte[] pic;
	private Button save;
	private int Snappi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camview);
		Snappi = 0;
		preview = (SurfaceView) findViewById(R.id.preview);
		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		final Button cancel = (Button) findViewById(R.id.cancel);
		final Button snap = (Button) findViewById(R.id.snap);
		save = (Button) findViewById(R.id.snaps);

		save.setEnabled(false);

		final Intent intent = new Intent(PreviewDemo.this,
				NewFilmActivity.class);
		final Activity test = this;

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				test.finish();
			}
		});

		snap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Snappi == 0) {
					camera.takePicture(shutterCallback, rawCallback,
							jpegCallback);
					Snappi = 1;
					snap.setText("Neues Bild");
				} else {
					camera.startPreview();
					Snappi = 0;
					save.setEnabled(false);
					snap.setText("Klick");
				}
			}
		});

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Snappi == 1) {
					intent.putExtra("image", pic);
					test.setResult(1277, intent);
					test.finish();
				}
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();

		camera = Camera.open();
		camera.setDisplayOrientation(90);
	}

	@Override
	public void onPause() {
		if (inPreview) {
			camera.stopPreview();
		}

		camera.release();
		camera = null;
		inPreview = false;

		super.onPause();
	}

	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				camera.setPreviewDisplay(holder);
			} catch (Exception e) {
				camera.release();
				camera = null;
				Toast.makeText(
						getApplicationContext(),
						"Fehler bei der Camera, bitte versuchen Sie es nochmal",
						Toast.LENGTH_SHORT).show();
			}
		}

		/*
		 * Geringste Qualitäts Einstellungen, soll nur der Vorschau dienen
		 */
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Camera.Parameters parameters = camera.getParameters();
			parameters.setJpegQuality(10);
			parameters.setJpegThumbnailQuality(1);
			// parameters.setJpegThumbnailSize(70, 50);

			// parameters.setPictureSize(70, 50);
			int minsearch = 0;
			int minwert = 9999;
			for (int i = 0; i < parameters.getSupportedPictureSizes().size(); i++) {
				if (minwert > parameters.getSupportedPictureSizes().get(i).height) {
					minwert = parameters.getSupportedPictureSizes().get(i).height;
					minsearch = i;
				}
			}
			parameters.set("orientation", "portrait");
			parameters.setRotation(90);
			parameters.setPreviewSize(parameters.getPreviewSize().width,
					parameters.getPreviewSize().height);
			parameters.setPictureSize(parameters.getSupportedPictureSizes()
					.get(minsearch).width, parameters
					.getSupportedPictureSizes().get(minsearch).height);

			camera.setParameters(parameters);
			camera.startPreview();
			inPreview = true;
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			if (camera != null) {
				camera.stopPreview();
				camera.setPreviewCallback(null);
				camera.release();
				camera = null;
			}
		}
	};

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {

		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			pic = data;
			Log.v("Check", "Bild Check : " + data);
			camera.stopPreview();
			save.setEnabled(true);
		}
	};

}