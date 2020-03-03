package com.alejandra.neya.refri;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemCallback {

	private RecyclerView rvItems;
	private ItemsAdapter adapter;
	private List<Item> itemList;
	private final int RESULT_ADD = 50;
	private final int RESULT_REMOVE = 60;
	private DataHelper dbHelper;

	// Permite ver archivos svg
	static {
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				//Agrega nuevo objeto
				addNewItem();
			}
		});

		dbHelper = new DataHelper(this);
		itemList = dbHelper.getListItems();
		setValueRecyclerView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data != null && data.getExtras() != null) {
				Item item = data.getExtras().getParcelable(Item.ID);
				switch (requestCode) {
					case RESULT_ADD:
						adapter.addItem(item);
						dbHelper.insert(item);
						break;
					case RESULT_REMOVE:
						adapter.removeItem(item);
						dbHelper.remove(item);
						break;
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		dbHelper.close();
		super.onDestroy();
	}

	private void addNewItem() {
		Intent intent = new Intent(this, DetailProductActivity.class);
		startActivityForResult(intent, RESULT_ADD);
	}

	private void showItem(Item item) {
		Intent intent = new Intent(this, DetailProductActivity.class);
		intent.putExtra(DetailProductActivity.BUNDLE_ONLY_VIEW, true);
		intent.putExtra(Item.ID, item);
		startActivityForResult(intent, RESULT_REMOVE);
	}

	private void setValueRecyclerView() {
		rvItems = findViewById(R.id.recyclerView);
		adapter = new ItemsAdapter(this, itemList, this);
		rvItems.setAdapter(adapter);
		rvItems.setLayoutManager(new LinearLayoutManager(this));
	}

	@Override
	public void onItemSelected(Item item) {
		showItem(item);
	}
}
