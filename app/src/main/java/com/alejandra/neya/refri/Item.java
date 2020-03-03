package com.alejandra.neya.refri;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

	public static final String ID = Item.class.getSimpleName();

	private String id;
	private String name;
	private String weigth;
	private long date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWeigth() {
		return weigth;
	}

	public void setWeigth(String weigth) {
		this.weigth = weigth;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.weigth);
		dest.writeLong(this.date);
	}

	public Item() {

	}

	private Item(Parcel in) {
		this.id = in.readString();
		this.name = in.readString();
		this.weigth = in.readString();
		this.date = in.readLong();
	}

	public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {

		@Override
		public Item createFromParcel(Parcel source) {
			return new Item(source);
		}

		@Override
		public Item[] newArray(int size) {
			return new Item[size];
		}
	};
}
