package org.khan.android.library.db;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import org.khan.android.library.db.annotation.Id;
import org.khan.android.library.util.ObjectConverter;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by KHAN on 2015-07-08.
 */
public abstract class BaseModel<ID extends Serializable> implements Parcelable {
    @Id(value = "id", autoIncrement = true, dbType = DbType.INTEGER)
    private ID id;

    public BaseModel() { }

    public BaseModel(ID id) {
        this.id = id;
    }

    protected BaseModel(Parcel in) {
        readFromParcel(in);
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public abstract void readFromParcel(Parcel in);

    public ContentValues toContentValues() {
        return ObjectConverter.toContentValues(this);
    }

    public Map<String, Object> toMap() {
        return ObjectConverter.toMap(this);
    }
}
