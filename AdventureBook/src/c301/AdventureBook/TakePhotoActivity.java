/* Copyright (C) <2013>  <Zhao Zhang>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package c301.AdventureBook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adventurebook.R;

/**
 * Take Photo Activity allow user to take a photo from camera, or select a photo from phone's gallery.
 * It will resize proccess and resize picture to be suitable for the app and then return its byte representation on return.
 * 
 * @author Zhao Zhang
 */
public class TakePhotoActivity extends Activity implements OnSeekBarChangeListener{
	//int REQUEST_CODE = 0;
	private static final int SELECT_PHOTO = 100;
	private static final int TAKE_PHOTO = 101;
	static Uri capturedImageUri=null;
	private String show_path;
	private String imageByte;
	int select_result = 0;
	SeekBar sb;
	TextView resize;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_media);
		Button uploadFromPhone = (Button)findViewById(R.id.fromPhoneButton);
		Button uploadFromWebCam = (Button)findViewById(R.id.fromWebCamButton);
		Button uploadConfirm = (Button)findViewById(R.id.confirmButton);
		//set seekbar and textview status		
		tv = (TextView)findViewById(R.id.percent);
		tv.setText("DISABLED");

		resize = (TextView)findViewById(R.id.resize);
		resize.setEnabled(false);

		sb = (SeekBar)findViewById(R.id.slider);
		sb.setMax(5);
		sb.setProgress(3);
		sb.setOnSeekBarChangeListener(this);
		sb.setEnabled(false);

		//button for click select images from local
		uploadFromPhone.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);				
				//photoPickerIntent.setType("image/*");
				//photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(photoPickerIntent, SELECT_PHOTO);
				//startActivityForResult(Intent.createChooser(photoPickerIntent,"Select Picture"), SELECT_PHOTO);				
			}
		});
		
		//button for click camera
		uploadFromWebCam.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
				String curentDateandTime = sdf.format(new Date());
				String newFilePath = Environment.getExternalStorageDirectory() + "/"+curentDateandTime+".jpg"; 
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(Environment.getExternalStorageDirectory(),  curentDateandTime+ ".jpg");
				show_path = newFilePath;
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				capturedImageUri = Uri.fromFile(file);
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
				startActivityForResult(cameraIntent, TAKE_PHOTO);
			}
		});
		//button for confirm
		uploadConfirm.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(select_result == 1){
					saveAndFinish();
				}

			}

		});
	}
	
	/**
	 * Handles the progress of the sliding bar when resizing an image.
	 */
	@Override
	public void onProgressChanged(SeekBar v, int scalePercent, boolean isUser) {
		tv.setText(String.valueOf(scalePercent));
		imageByte = processImage(show_path, (1 - scalePercent*0.5) + 2.0);

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	/**
	 * Saves the image byte and stores the data in the intent to pass to
	 * another activity.
	 */
	private void saveAndFinish() {
		Intent intent = new Intent();		
		intent.putExtra("imagebyte", imageByte);
		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * This function processes an image into bytes by compressing and calling a scaling function upon it
	 * 
	 * @param the image's path and desired scaling size
	 * @return String a image's byte array
	 */
	public String processImage(String path, double size_scale){
		Bitmap bitmapOrg = resizeBitmap(path, size_scale);
		ByteArrayOutputStream imageByte = new ByteArrayOutputStream();

		bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 95, imageByte);
		ImageView test = (ImageView) findViewById(R.id.upload_photo_view);
		test.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		test.setImageBitmap(bitmapOrg);
		byte[] bytefile = imageByte.toByteArray();
		String bytefile64 = Base64.encodeToString(bytefile, Base64.DEFAULT);
		return bytefile64;
	}

	/**
	 * This function resizes Bitmaps based on the size_scale passed
	 * 
	 * @param the image's path and desired scaling size
	 * @return a resized Bitmap
	 */
	private Bitmap resizeBitmap(String path, double size_scale) {
		Bitmap bitmapOrg = BitmapFactory.decodeFile(path);
		double width = bitmapOrg.getWidth();
		double height = bitmapOrg.getHeight();
		double ratio = 400.00 / width;
		int newheight = (int) (ratio * height);
		int newwidth = (int) (400.00 / size_scale);
		int new_height = (int) (newheight / size_scale);
		bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, newwidth, new_height,
				true);
		return bitmapOrg;
	}

	/**
	 * This displays the uploaded picture upon the activity screen on return from the Android webcam or mediaStore.
	 * 
	 * @param the requestCode, returnCode, and intent of the returning function
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnIntent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, imageReturnIntent);
		switch(requestCode){			
		case SELECT_PHOTO:
			if(resultCode == RESULT_OK){
				if(imageReturnIntent != null){
					if(imageReturnIntent.getData() != null){
						try {
							Uri selectedImage = imageReturnIntent.getData();
							String[] filePathColumn = {MediaStore.Images.Media.DATA};
							Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
							cursor.moveToFirst();
							int columnIndex =  cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
							String filePath = cursor.getString(columnIndex);
							cursor.close();
							ImageView test = (ImageView) findViewById(R.id.upload_photo_view);
							test.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
							test.setImageBitmap(BitmapFactory.decodeFile(filePath));
							show_path = filePath;
							imageByte = processImage(show_path,1.5);

							sb.setEnabled(true);
							resize.setText("Re-size");
							tv.setText("3");
							sb.setProgress(3);

							select_result = 1;
							break;
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				break;

			}
		case TAKE_PHOTO:
			if(resultCode == RESULT_OK){
				try {
					ImageView test = (ImageView) findViewById(R.id.upload_photo_view);
					Bitmap bitmap = MediaStore.Images.Media.getBitmap( getApplicationContext().getContentResolver(),  capturedImageUri);
					test.setImageBitmap(bitmap);
					imageByte = processImage(show_path,1.5);

					sb.setEnabled(true);
					resize.setText("Re-size");
					tv.setText("3");
					sb.setProgress(3);

					select_result = 1;
					break;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Picture Was Not Taken", Toast.LENGTH_SHORT).show();
            }
			
		}
	}

	/**
	 * When the back button is pressed, finishes the current activity.
	 */
	public void onBackPressed() {
		finish();
	}
}




