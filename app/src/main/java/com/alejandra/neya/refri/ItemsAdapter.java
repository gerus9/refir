package com.alejandra.neya.refri;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private Context context;
	private List<Item> itemList;
	private ItemCallback callback;

	private final int VIEW_TYPE_EMPTY = 1;
	private final int VIEW_ITEMS = 2;

	public ItemsAdapter(Context context, List<Item> itemList, @NonNull ItemCallback callback) {
		this.context = context;
		this.itemList = itemList;
		this.callback = callback;
	}

	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup poParent, int viewType) {
		LayoutInflater voInflater = LayoutInflater.from(context);
		View voView;
		RecyclerView.ViewHolder voViewHolder;
		switch (viewType) {
			case VIEW_TYPE_EMPTY:
				voView = voInflater.inflate(R.layout.empty_layout, poParent, false);
				voViewHolder = new EmptyViewHolder(voView);
				break;
			case VIEW_ITEMS:
			default:
				voView = voInflater.inflate(R.layout.item_row, poParent, false);
				voViewHolder = new ItemViewHolder(voView);
				break;
		}
		return voViewHolder;
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof ItemViewHolder) {
			ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
			final Item item = itemList.get(position);

			itemViewHolder.txtTitle.setText(item.getName());
			itemViewHolder.txtDate.setText(getData(item.getDate()));
			itemViewHolder.txtWeigth.setText(item.getWeigth());
			itemViewHolder.txtTimeOut.setVisibility(isTimeOut(item.getDate()) ? View.VISIBLE : View.INVISIBLE);

			itemViewHolder.root.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					callback.onItemSelected(item);
				}
			});
		}
	}

	private String getData(long date) {
		SimpleDateFormat format1 = new SimpleDateFormat("dd MMMM yyyy");
		Calendar calendarItem = Calendar.getInstance();
		calendarItem.setTimeInMillis(date);
		return format1.format(calendarItem.getTime());
	}

	private boolean isTimeOut(long date) {
		if (DateUtils.isToday(date)) {
			return false;
		} else {
			Calendar itemCalendar = Calendar.getInstance();
			itemCalendar.setTimeInMillis(date);
			return Calendar.getInstance().after(itemCalendar);
		}
	}

	@Override
	public int getItemCount() {
		return isEmpty() ? 1 : itemList.size();
	}

	private boolean isEmpty() {
		return itemList == null || itemList.size() == 0;
	}

	@Override
	public int getItemViewType(int position) {
		return (isEmpty()) ? VIEW_TYPE_EMPTY : VIEW_ITEMS;
	}

	public void addItem(Item item) {
		itemList.add(item);
		notifyDataSetChanged();
	}

	public void removeItem(Item item) {
		itemList.remove(getPosition(item));
		notifyDataSetChanged();
	}

	private int getPosition(Item item) {
		for (int i = 0; i < itemList.size(); i++) {
			if (itemList.get(i).getId().equals(item.getId())) {
				return i;
			}
		}
		return -1;
	}

	public class ItemViewHolder extends RecyclerView.ViewHolder {

		private LinearLayout root;
		private TextView txtTitle;
		private TextView txtDate;
		private TextView txtWeigth;
		private TextView txtTimeOut;

		public ItemViewHolder(View itemView) {
			super(itemView);
			root = itemView.findViewById(R.id.root);
			txtTitle = itemView.findViewById(R.id.title);
			txtDate = itemView.findViewById(R.id.date);
			txtWeigth = itemView.findViewById(R.id.weight);
			txtTimeOut = itemView.findViewById(R.id.timeout);
		}
	}


	public class EmptyViewHolder extends RecyclerView.ViewHolder {

		public EmptyViewHolder(View itemView) {
			super(itemView);
		}
	}
}
