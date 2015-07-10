package org.khan.android.sample;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import org.khan.android.library.app.BaseActivity;
import org.khan.android.library.logging.L;
import org.khan.android.sample.model.Test;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    TextView txtMain;
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setWidget() {
        txtMain = setView(txtMain, R.id.txtMain);
    }

    @Override
    protected void setData(Intent intent) {
        TestDao dao = new TestDao(getApplicationContext());
        List<Test> list = new ArrayList<Test>();
        list.add(new Test("name1", "col1-1", "col2-1"));
        list.add(new Test("name2", "col1-2", "col2-2"));
        list.add(new Test("name3", "col1-3", "col2-3"));
        list.add(new Test("name4", "col1-4", "col2-4"));
        list.add(new Test("name5", "col1-5", "col2-5"));
        list.add(new Test("name6", "col1-6", "col2-6"));
        L.d(MainActivity.class.getSimpleName(), list.toString());
        dao.saveAll(list);


        dao.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TestDao dao = new TestDao(getApplicationContext());
        List<Test> list = dao.findAll();
        txtMain.setText(list.toString());
        dao.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
