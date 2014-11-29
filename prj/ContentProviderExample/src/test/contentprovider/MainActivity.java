package test.contentprovider;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				contactProviderTest();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void contactProviderTest() {
		ContentResolver cr = getContentResolver();
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		int cnt = 0;

		try{
		// insert
		contactProviderTest_insert(cr);
		Toast.makeText(getApplicationContext(), cnt++, Toast.LENGTH_SHORT)
				.show();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
		}
		// delete
		try{
		contactProviderTest_delete(cr);
		Toast.makeText(getApplicationContext(), cnt++, Toast.LENGTH_SHORT)
				.show();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
		}
		// update
		try{
		contactProviderTest_update(cr);
		Toast.makeText(getApplicationContext(), cnt++, Toast.LENGTH_SHORT)
				.show();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
		}
		// query
		try {
		Cursor query = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				ContactsContract.Contacts._ID + "=?", new String[] { "2" },
				null);
		query.close();
		Toast.makeText(getApplicationContext(), cnt++, Toast.LENGTH_SHORT)
				.show();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
		}
		// bulk insert
		try{
		contactProviderTest_bulkInsert(cr);
		Toast.makeText(getApplicationContext(), cnt++, Toast.LENGTH_SHORT)
				.show();
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
		}

	}

	private void contactProviderTest_insert(ContentResolver resolver) {
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ContentValues values = new ContentValues();
		long contact_id = ContentUris.parseId(resolver.insert(uri, values));
		// 插入data表
		uri = Uri.parse("content://com.android.contacts/data");
		// add Name
		values.put("raw_contact_id", contact_id);
		values.put(ContactsContract.Data.MIMETYPE,
				"vnd.android.cursor.item/name");
		values.put("data2", "zdong");
		values.put("data1", "xzdong");
		resolver.insert(uri, values);
		values.clear();
		// add Phone
		values.put("raw_contact_id", contact_id);
		values.put(ContactsContract.Data.MIMETYPE,
				"vnd.android.cursor.item/phone_v2");
		values.put("data2", "2"); // 手机
		values.put("data1", "87654321");
		resolver.insert(uri, values);
		values.clear();
		// add email
		values.put("raw_contact_id", contact_id);
		values.put(ContactsContract.Data.MIMETYPE,
				"vnd.android.cursor.item/email_v2");
		values.put("data2", "2"); // 单位
		values.put("data1", "xzdong@xzdong.com");
		resolver.insert(uri, values);
	}

	private void contactProviderTest_bulkInsert(ContentResolver resolver) {
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		ContentProviderOperation operation1 = ContentProviderOperation
				.newInsert(uri).withValue("account_name", null).build();
		operations.add(operation1);
		// 向data添加数据
		uri = Uri.parse("content://com.android.contacts/data");
		// 添加姓名
		ContentProviderOperation operation2 = ContentProviderOperation
				.newInsert(uri).withValueBackReference("raw_contact_id", 0)
				.withValue("mimetype", "vnd.android.cursor.item/name")
				.withValue("data2", "xzdong").build();
		operations.add(operation2);
		// 添加手机数据
		ContentProviderOperation operation3 = ContentProviderOperation
				.newInsert(uri).withValueBackReference("raw_contact_id", 0)
				.withValue("mimetype", "vnd.android.cursor.item/phone_v2")
				.withValue("data2", "2").withValue("data1", "0000000").build();
		operations.add(operation3);
		try {
			resolver.applyBatch("com.android.contacts", operations);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
			
		}
	}

	private void contactProviderTest_delete(ContentResolver resolver) {
		String name = "xzdong";
		// 根据姓名求id
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Cursor cursor = resolver.query(uri,
				new String[] { ContactsContract.Data._ID }, "display_name=?",
				new String[] { name }, null);
		if (cursor.moveToFirst()) {
			int id = cursor.getInt(0);
			// 根据id删除data中的相应数据
			resolver.delete(uri, "display_name=?", new String[] { name });
			uri = Uri.parse("content://com.android.contacts/data");
			resolver.delete(uri, "raw_contact_id=?", new String[] { id + "" });
		}
	}

	private void contactProviderTest_update(ContentResolver resolver) {
		int id = 1;
		String phone = "999999";
		Uri uri = Uri.parse("content://com.android.contacts/data");
		ContentValues values = new ContentValues();
		values.put("data1", phone);
		resolver.update(uri, values, "mimetype=? and raw_contact_id=?",
				new String[] { "vnd.android.cursor.item/phone_v2", id + "" });
	}

	private void smsProviderTest() {
		try {
			ContentResolver cr = getContentResolver();
			// query
			smsProviderTest_query(cr);
			// send
			smsProviderTest_send();
			Toast.makeText(getApplicationContext(), "sms", Toast.LENGTH_SHORT)
			.show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
		}
		
	}

	private void smsProviderTest_query(ContentResolver cr) {
		Uri uri = Uri.parse("content://sms");
		String[] projection = new String[] { "_id", "address", "person",
				"body", "date", "type" };
		Cursor cur = cr.query(uri, projection, null, null, "date desc");
		cur.close();
	}

	private void smsProviderTest_send() {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage("13812911010", null, "test message", null, null);
	}

	// private void smsProviderTest_receive() {
	//
	// }

	private void mediaStoreProviderTest() {

		try {
			ContentResolver cr = getContentResolver();
			mediaStoreProviderTest_queryAlbum(cr);
			Toast.makeText(getApplicationContext(), "media", Toast.LENGTH_SHORT)
			.show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_SHORT).show();
		}
		
	}

	private void mediaStoreProviderTest_queryAlbum(ContentResolver cr) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor query = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				proj, null, null, null);
	}
}
