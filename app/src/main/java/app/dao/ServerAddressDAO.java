package app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import app.dao.exception.DuplicateServerAddressNameException;
import app.dao.exception.InvalidServerAddressNameException;
import app.dao.model.ServerAddress;
import app.util.LogUtil;

import static android.provider.BaseColumns._ID;

/**
 *
 * @author jinbing
 */
public class ServerAddressDAO extends MainDAOHelper {

    private static final String LOG_TAG = ServerAddressDAO.class.getSimpleName();

	public ServerAddressDAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
    public ServerAddress save(ServerAddress address) {
    	if (address.getId() != null) {
            return updateExistingServerAddress(address);
        } else {
            return createNewServerAddress(address);
        }
        
    }

    public ServerAddress findById(Long id) {
        Cursor cursor = null;
        ServerAddress address = null;

        SQLiteDatabase db = null;
        try {
            db = getReadableOrWritableDatabase();
            cursor = db.query(SERVER_ADDRESS_TABLE_NAME, SERVER_ADDRESS_ALL_COLUMS, _ID + " = ?",
            		new String[]{id.toString()}, null, null, null);
            if (cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                    address = cursorToServerAddress(cursor);
                }
            }
        } catch (SQLiteException e) {
        	e.printStackTrace();
        } finally {
            closeCursor(cursor);
            closeDb(db);
        }

        return address;
    }
    
    public ServerAddress findByName(String name) {
        Cursor cursor = null;
        ServerAddress address = null;
        name = name.trim();

        SQLiteDatabase db = null;
        try {
            db = getReadableOrWritableDatabase();
            cursor = db.query(SERVER_ADDRESS_TABLE_NAME, SERVER_ADDRESS_ALL_COLUMS, SERVER_ADDRESS_NAME + " = ?",
            		new String[]{name}, null, null, null);
            if (cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                	address = cursorToServerAddress(cursor);
                }
            }
        } catch (SQLiteException e) {
        	e.printStackTrace();
        } finally {
            closeCursor(cursor);
            closeDb(db);
        }

        LogUtil.d(LOG_TAG, (address == null ? "Unsuccessfully" : "Successfully") +
                " found address with a name of '" + name + "'");
        return address;
    }

    public ServerAddress findDefault() {
        Cursor cursor = null;
        ServerAddress address = null;

        SQLiteDatabase db = null;
        try {
            db = getReadableOrWritableDatabase();
            cursor = db.query(SERVER_ADDRESS_TABLE_NAME, SERVER_ADDRESS_ALL_COLUMS, SERVER_ADDRESS_IS_DEFAULT + " = ?",
                    new String[]{"1"}, null, null, null);
            if (cursor.getCount() == 1) {
                if (cursor.moveToFirst()) {
                    address = cursorToServerAddress(cursor);
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
            closeDb(db);
        }

        return address;
    }
    
    public List<ServerAddress> findAllServerAddress() {
        List<ServerAddress> categories = new ArrayList<ServerAddress>();
        Cursor cursor = null;

        SQLiteDatabase db = null;
        try {
            db = getReadableOrWritableDatabase();
            cursor = db.query(SERVER_ADDRESS_TABLE_NAME, SERVER_ADDRESS_ALL_COLUMS,
            		null, null, null, null, null);
            while (cursor.moveToNext()) {
            	categories.add(cursorToServerAddress(cursor));
            }
        } catch (SQLiteException e) {
        	e.printStackTrace();
        } finally {
            closeCursor(cursor);
            closeDb(db);
        }

        LogUtil.d(LOG_TAG, "Found " + categories.size() + " address");
        return categories;
    }

    private ServerAddress cursorToServerAddress(Cursor cursor) {
    	Long id = cursor.getLong(cursor.getColumnIndex(_ID));
        String name = cursor.getString(cursor.getColumnIndex(SERVER_ADDRESS_NAME));
        String apiHost = cursor.getString(cursor.getColumnIndex(SERVER_ADDRESS_API_HOST));
        Integer apiPort = cursor.getInt(cursor.getColumnIndex(SERVER_ADDRESS_API_PORT));
        boolean isDefault = cursor.getInt(cursor.getColumnIndex(SERVER_ADDRESS_IS_DEFAULT)) > 0;

        ServerAddress address = new ServerAddress();
        address.setId(id);
        address.setName(name);
        address.setApiHost(apiHost);
        address.setApiPort(apiPort);
        address.setDefault(isDefault);

        return address;
    }

    private ContentValues addressToContentValues(ServerAddress address) {
        ContentValues values = new ContentValues();
        values.put(SERVER_ADDRESS_NAME, address.getName());
        values.put(SERVER_ADDRESS_API_HOST, address.getApiHost());
        values.put(SERVER_ADDRESS_API_PORT, address.getApiPort());
        values.put(SERVER_ADDRESS_IS_DEFAULT, address.isDefault() ? 1 : 0);

        return values;
    }
    

    public void delete(ServerAddress address) {
        LogUtil.d("ser", "Deleting address with the name of '" + address.getName() + "'");
        if (address.getId() != null) {
        	SQLiteDatabase db = null;
            try {
            	db = getWritableDatabase();
            	db.delete(SERVER_ADDRESS_TABLE_NAME, _ID + " = ?", new String[]{address.getId().toString()});
            } catch (SQLiteException e) {
            	e.printStackTrace();
            } finally {
                closeDb(db);
            }
        }
    }
    
    private boolean attemptingToCreateDuplicateServerAddress(ServerAddress address) {
        return address.getId() == null && findByName(address.getName()) != null;
    }

    private ServerAddress createNewServerAddress(ServerAddress address) {
        if (address.getName() == null || address.getName().trim().length() == 0) {
            String msg = "Attempting to create a address with an empty name";
            LogUtil.d(LOG_TAG, msg);
            throw new InvalidServerAddressNameException(msg);
        }

        if (attemptingToCreateDuplicateServerAddress(address)) {
            String msg = "Attempting to create duplicate address with the name " + address.getName();
            LogUtil.d(LOG_TAG, msg);
            throw new DuplicateServerAddressNameException(msg);
        }

        LogUtil.d(LOG_TAG, "Creating new address with a name of '" + address.getName() + "'");
        ContentValues values = addressToContentValues(address);
        SQLiteDatabase db = null;
        try {
        	db = getWritableDatabase();
	        long id = db.insertOrThrow(SERVER_ADDRESS_TABLE_NAME, null, values);

            ServerAddress newAddress = new ServerAddress();
            newAddress.setId(id);
            newAddress.setName(address.getName());
            newAddress.setApiHost(address.getApiHost());
            newAddress.setApiPort(address.getApiPort());
            newAddress.setDefault(address.isDefault());
	        
	        return newAddress;
        } catch (SQLiteException e) {
        	e.printStackTrace();
        	return null;
        } finally {
            closeDb(db);
        }
    }

    private ServerAddress updateExistingServerAddress(ServerAddress address) {
    	if (address.getName() == null || address.getName().trim().length() == 0) {
            String msg = "Attempting to create a address with an empty name";
            LogUtil.d(LOG_TAG, msg);
            throw new InvalidServerAddressNameException(msg);
        }

        if (attemptingToCreateDuplicateServerAddress(address)) {
            String msg = "Attempting to create duplicate address with the name " + address.getName();
            LogUtil.d(LOG_TAG, msg);
            throw new DuplicateServerAddressNameException(msg);
        }

        LogUtil.d(LOG_TAG, "Updating address with the name of '" + address.getName() + "'");
        ContentValues values = addressToContentValues(address);
    	SQLiteDatabase db = null;
        try {
        	db = getWritableDatabase();
            long id = db.update(SERVER_ADDRESS_TABLE_NAME, values, _ID + " = ?",
            		new String[]{address.getId().toString()});

            ServerAddress newAddress = new ServerAddress();
            newAddress.setId(id);
            newAddress.setName(address.getName());
            newAddress.setApiHost(address.getApiHost());
            newAddress.setApiPort(address.getApiPort());
            newAddress.setDefault(address.isDefault());

            return newAddress;
        } catch (SQLiteException e) {
        	e.printStackTrace();
        	return null;
        } finally {
            closeDb(db);
        }
    }
}
