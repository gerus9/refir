package com.alejandra.neya.refri;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

public class DetailProductActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

	public static final String BUNDLE_ONLY_VIEW = "VISIBLE";
	private boolean isOnlyVisibleView;

	private AppCompatTextView txtCalendar;
	private AppCompatEditText edtWeigth;
	private AppCompatEditText edtName;
	private Calendar calendar;
	private Item itemValue;

	// Permite ver archivos svg
	static {
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_product);

		if (getIntent().getExtras() != null) {
			isOnlyVisibleView = getIntent().getExtras().getBoolean(BUNDLE_ONLY_VIEW, false);
			itemValue = getIntent().getExtras().getParcelable(Item.ID);
		}

		txtCalendar = findViewById(R.id.calendar);
		txtCalendar.setOnClickListener(this);
		edtWeigth = findViewById(R.id.weight);
		edtName = findViewById(R.id.name);

		calendar = Calendar.getInstance();

		// Se desactivan los controles.
		if (isOnlyVisibleView) {
			txtCalendar.setEnabled(false);
			edtWeigth.setEnabled(false);
			edtName.setEnabled(false);

			edtName.setText(itemValue.getName());
			edtWeigth.setText(itemValue.getWeigth());
			calendar.setTimeInMillis(itemValue.getDate());
		}

		updateLabel();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu_detail, menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_detail, menu);

		MenuItem menu_deleted = menu.findItem(R.id.menu_delete);
		MenuItem menu_save = menu.findItem(R.id.menu_save);

		if (isOnlyVisibleView) {
			menu_save.setVisible(false);
			menu_deleted.setVisible(true);
		} else {
			menu_save.setVisible(true);
			menu_deleted.setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
			case R.id.menu_delete:
				setResult(this.itemValue);
				break;
			case R.id.menu_save:
				validateData();
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void validateData() {
		if (isValidInputs()) {
			setResult(getItem());
		}
	}

	private void setResult(Item item) {
		Intent intent = new Intent();
		intent.putExtra(Item.ID, item);
		setResult(RESULT_OK, intent);
		finish();
	}

	private Item getItem() {
		Item item = new Item();
		item.setId(getID());
		item.setDate(calendar.getTimeInMillis());
		item.setName(edtName.getText().toString());
		item.setWeigth(edtWeigth.getText().toString());
		return item;
	}

	public static String getID() {
		final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int count = 15;
		StringBuilder builder = new StringBuilder();

		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}

		return builder.toString();
	}

	private boolean isValidInputs() {
		if (TextUtils.isEmpty(edtName.getText())) {
			edtName.setError(getApplicationContext().getString(R.string.empty_field));
			return false;
		}

		if (TextUtils.isEmpty(edtWeigth.getText())) {
			edtWeigth.setError(getApplicationContext().getString(R.string.empty_field));
			return false;
		}

		return true;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.calendar) {
			openCalendar();
		}
	}

	private void openCalendar() {
		new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, monthOfYear);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		updateLabel();
	}

	private void updateLabel() {
		String myFormat = "E, dd MMM yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
		txtCalendar.setText(sdf.format(calendar.getTime()));
	}
}
