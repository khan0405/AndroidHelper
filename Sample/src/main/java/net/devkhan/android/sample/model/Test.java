package net.devkhan.android.sample.model;

import android.os.Parcel;
import net.devkhan.android.library.db.BaseModel;
import net.devkhan.android.library.db.DbType;
import net.devkhan.android.library.db.annotation.Column;
import net.devkhan.android.library.db.annotation.Entity;

/**
 * Created by KHAN on 2015-07-10.
 */
@Entity("test_table")
public class Test extends BaseModel<Integer> {

    @Column(dbType = DbType.TEXT)
    private String name;
    @Column
    private String column1;

    private String nonColumn;

    public Test() {
    }

    public Test(Integer id) {
        super(id);
    }

    public Test(String name, String column1, String nonColumn) {
        this.name = name;
        this.column1 = column1;
        this.nonColumn = nonColumn;
    }

    protected Test(Parcel in) {
        super(in);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getNonColumn() {
        return nonColumn;
    }

    public void setNonColumn(String nonColumn) {
        this.nonColumn = nonColumn;
    }

    @Override
    public void readFromParcel(Parcel in) {
        setId(in.readInt());
        setName(in.readString());
        setColumn1(in.readString());
        setNonColumn(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
        dest.writeString(getColumn1());
        dest.writeString(getNonColumn());
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel source) {
            return new Test(source);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };

    @Override
    public String toString() {
        return toMap().toString();
    }
}
