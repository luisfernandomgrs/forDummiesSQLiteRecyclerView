package com.codinginflow.sqliterecyclerview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
	private SQLiteDatabase mDatabase;
	private GroceryAdapter mAdapter;
	private EditText mEditTextName;
	private TextView mTextViewAmount;
	private int mAmount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		GroceryDBHelper dbHelper = new GroceryDBHelper(this);
		mDatabase = dbHelper.getWritableDatabase();

		RecyclerView recyclerView = findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		mAdapter = new GroceryAdapter(this, getAllItems());
		recyclerView.setAdapter(mAdapter);

		new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
				ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				removeItem((long) viewHolder.itemView.getTag());
			}
		}).attachToRecyclerView(recyclerView);

		mEditTextName = findViewById(R.id.edit_text_name);
		mTextViewAmount = findViewById(R.id.textview_amount);

		Button buttonIncrease = findViewById(R.id.button_increase);
		Button buttonDecrease = findViewById(R.id.button_decrease);
		Button buttonAdd = findViewById(R.id.button_add);

		buttonIncrease.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				closeKeyboard();
				increase();
			}
		});

		buttonDecrease.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				closeKeyboard();
				decrease();
			}
		});

		buttonAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				closeKeyboard();
				addItem();
			}
		});
	}

	private void increase() {
		mAmount++;
		mTextViewAmount.setText(String.valueOf(mAmount));
	}

	private void decrease() {
		if (mAmount > 0) {
			mAmount--;
			mTextViewAmount.setText(String.valueOf(mAmount));
		}
	}

	private void addItem() {
		if (mEditTextName.getText().toString().trim().length() == 0 || mAmount == 0) {
			return;
		}

		String name = mEditTextName.getText().toString().trim();
		ContentValues cv = new ContentValues();

		cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);
		cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, mAmount);

		mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
		mAdapter.swapCursor(getAllItems());

		mEditTextName.getText().clear();
		mAmount = 0;
		mTextViewAmount.setText(String.valueOf(mAmount));
	}

	private Cursor getAllItems() {
		return mDatabase.query(
				GroceryContract.GroceryEntry.TABLE_NAME,
				null,
				null,
				null,
				null,
				null,
				GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
		);
	}

	private void removeItem(long id) {
		mDatabase.delete(GroceryContract.GroceryEntry.TABLE_NAME,
				GroceryContract.GroceryEntry._ID + "=" + id,
				null);
		mAdapter.swapCursor(getAllItems());
	}

	private void closeKeyboard() {
		View view = mEditTextName.getRootView();	//Force to get focus of Edit Text...
		// this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow( view.getWindowToken(), 0);
		}
	}
}