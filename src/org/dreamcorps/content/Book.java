package org.dreamcorps.content;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable{
	private String imageURL;
	private String name;
	private String description;
	
	public Book(String name, String description, String imageURL) {
		this.name = name;
		this.description = description;
		this.imageURL = imageURL;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Book(Parcel in) {
		String[] data = new String[3];
		
		in.readStringArray(data);
		this.imageURL = data[0];
		this.name = data[1];
		this.description = data[2];
	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(new String[] {
				this.imageURL,
				this.name,
				this.description
		});
		
	}
	
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Book createFromParcel(Parcel in) {
            return new Book(in); 
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
